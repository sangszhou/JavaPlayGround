package leaderelection;

import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.CloseableUtils;
import org.apache.log4j.net.SyslogAppender;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by xinszhou on 30/12/2016.
 */
public class LeaderSelectorExample {
    private static final int CLIENT_QTY = 10;
    private static final String PATH = "/examples/leader";

    public static void main(String args[]) throws Exception{
        List<CuratorFramework> clients = Lists.newArrayList();
        List<ExampleClient> examples = Lists.newArrayList();
        TestingServer server = new TestingServer();

        try {
            for(int i = 0; i < CLIENT_QTY; i ++) {
                CuratorFramework client = CuratorFrameworkFactory.newClient(server.getConnectString(),
                        new ExponentialBackoffRetry(1000, 3));

                clients.add(client);
                ExampleClient exampleClient = new ExampleClient(client, PATH, "Client #" + i);
                examples.add(exampleClient);
                client.start();
                exampleClient.start();
            }

            System.out.println("Press enter/return to quite");
            new BufferedReader(new InputStreamReader(System.in)).readLine();

        } catch (Exception e) {

        } finally {
            System.out.println("Shutting down");
            for (ExampleClient exampleClient : examples) {
                CloseableUtils.closeQuietly(exampleClient);
            }

            for (CuratorFramework client : clients) {
                CloseableUtils.closeQuietly(client);
            }

            CloseableUtils.closeQuietly(server);

        }

    }

}
