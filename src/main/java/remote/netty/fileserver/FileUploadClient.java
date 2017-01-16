package remote.netty.fileserver;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by xinszhou on 16/01/2017.
 */
public class FileUploadClient {
    public static final int CLIENT_COUNT = 1;

    public static void main(String args[]) {
        for(int i = 0; i < CLIENT_COUNT; i++){
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        uploadFile();
                    } catch (Exception ex) {
                        Logger.getLogger(FileUploadClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }).start();

        }

    }

    private static void uploadFile() throws Exception{
        File file = new File("/ws/github/JavaPlayGround/src/main/java/remote/netty/fileserver/small.jpg");

        HttpEntity httpEntity = MultipartEntityBuilder.create()
                .addBinaryBody("file", file, ContentType.create("image/jpeg"), file.getName())
                .build();

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("http://localhost:8111");

        httppost.setEntity(httpEntity);
        System.out.println("executing request " + httppost.getRequestLine());

        CloseableHttpResponse response = httpclient.execute(httppost);

        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
            System.out.println("Response content length: " + resEntity.getContentLength());
        }

        EntityUtils.consume(resEntity);

        response.close();
    }

}
