package kafka_style_nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by xinszhou on 02/12/2016.
 */
public class PlainTextTransportLayer implements TransportLayer {
    SelectionKey key;
    SocketChannel socketChannel;

    public PlainTextTransportLayer(SelectionKey key) {
        this.key = key;
        socketChannel = (SocketChannel) key.channel();
    }


    @Override
    public SocketChannel socketChannel() {
        return this.socketChannel;
    }

    @Override
    public void handshake() throws IOException {
        // do nothing
    }

    /**
     * always returns false as there will be not be any
     * pending writes since we directly write to socketChannel.
     */
    @Override
    public boolean hasPendingWrites() {
        return false;
    }

    @Override
    public long transferFrom(FileChannel fileChannel, long position, long count) throws IOException {
        return fileChannel.transferTo(position, count, fileChannel);
    }

    // 容易出错，这里是 add 不是 change
    @Override
    public void addInterestOps(int ops) {
        key.interestOps(key.interestOps() | ops);
    }

    // 有意思的做法
    @Override
    public void removeInterestOps(int ops) {
        key.interestOps(key.interestOps() & ~ops);
    }

    // 等待服务器端的返回
    // 客户端是什么时候回等待服务器端的消息呢？
    @Override
    public void finishConnect() throws IOException {
        socketChannel.finishConnect();
        key.interestOps(key.interestOps() & ~SelectionKey.OP_CONNECT | SelectionKey.OP_READ);
    }

    @Override
    public void disconnect() {
        key.cancel();
    }

    // 什么情况下 socketChannel 会觉得自己没有被 connected?
    @Override
    public boolean isConnected() {
        return socketChannel.isConnected();
    }

    @Override
    public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        return socketChannel.write(srcs, offset, length);
    }

    @Override
    public long write(ByteBuffer[] srcs) throws IOException {
        return socketChannel.write(srcs);
    }

    @Override
    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        return socketChannel.read(dsts, offset, length);
    }

    @Override
    public long read(ByteBuffer[] dsts) throws IOException {
        return socketChannel.read(dsts);
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        return socketChannel.read(dst);
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        return socketChannel.write(src);
    }

    @Override
    public boolean isOpen() {
        return socketChannel.isOpen();
    }

    @Override
    public void close() throws IOException {
        socketChannel.socket().close();
        socketChannel.close();
        key.attach(null);
        key.cancel();
    }
}

