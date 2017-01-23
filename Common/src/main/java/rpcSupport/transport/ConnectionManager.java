package rpcSupport.transport;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import rpcSupport.client.RpcClientHandler;
import rpcSupport.codec.RPCDecoder;
import rpcSupport.codec.RPCEncoder;
import rpcSupport.protocol.RpcRequest;
import rpcSupport.protocol.RpcResponse;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xinszhou on 16/01/2017.
 */
public class ConnectionManager {

    private static Map<InetSocketAddress, RpcClientHandler> connectedServer = new ConcurrentHashMap<>();

    public static ConnectionManager connectionManager = new ConnectionManager();

    private static AtomicInteger roundRobin = new AtomicInteger(0);

    private static ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(16);

    EventLoopGroup group = new NioEventLoopGroup(4);

    public static ConnectionManager getInstance() {
        return connectionManager;
    }

    // host:port
    public void updateConnectedServers(List<String> allServersAddress) {
        if (allServersAddress == null || allServersAddress.size() == 0) return;

        Set<InetSocketAddress> newServerAddr = new HashSet<>();
        for (int i = 0; i < allServersAddress.size(); i++) {
            String[] array = allServersAddress.get(i).split(":");
            if (array.length == 2) {
                String host = array[0];
                int port = Integer.parseInt(array[1]);
                newServerAddr.add(new InetSocketAddress(host, port));
            }
        }

        for (InetSocketAddress addr : newServerAddr) {
            if (!connectedServer.containsKey(addr)) {
                connectServer(addr);
            }
        }

        // close and remove invalid server node
        for (Map.Entry entry : connectedServer.entrySet()) {
            if (!newServerAddr.contains(entry.getKey())) {
                ((RpcClientHandler) entry.getValue()).close();
                connectedServer.remove(entry.getKey());
            }
        }
    }

    public void connectServer(InetSocketAddress address) {
        threadPoolExecutor.submit(() -> {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new RPCEncoder(RpcRequest.class))
                                    .addLast(new RPCDecoder(RpcResponse.class))
                                    .addLast(new RpcClientHandler());
                        }
                    });
            ChannelFuture channelFuture = b.connect(address);
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    RpcClientHandler handler = channelFuture.channel().pipeline().get(RpcClientHandler.class);
                    addHandler(handler);
                }
            });
        });
    }

    public void addHandler(RpcClientHandler handler) {
        InetSocketAddress remoteServerAddress = (InetSocketAddress) handler.getChannel().remoteAddress();
        connectedServer.put(remoteServerAddress, handler);
    }

    //@todo make it optional
    public RpcClientHandler getHandler() {
        if (connectedServer.size() == 0) {
            return null;
        }

        int idx = (roundRobin.getAndAdd(1)) % connectedServer.size();
        return (RpcClientHandler) connectedServer.values().toArray()[idx];
    }


}
