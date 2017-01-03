package resttemplate;

import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * Created by xinszhou on 01/01/2017.
 */
public class RestTemplateMain {
    public static void main(String args[]) {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build());

        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);

        ResponseEntity<String> result = null;


        // 400 bad request
        try {
            result = restTemplate.postForEntity("http://localhost:9200", null, String.class);
            System.out.println(result.getBody());
        } catch (Exception e) {

            e.printStackTrace();
        }

        // my response code  200
        RestTemplate myRestTemplate = new WeavedRestTemplate(clientHttpRequestFactory);

        try {
            result = myRestTemplate.getForEntity("http://localhost:9200", String.class);
            System.out.println(result.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //  my response code 400

        try {
            result = myRestTemplate.postForEntity("http://localhost:9200", null, String.class);
            System.out.println(result.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }


        // other exception

        try {
            result = myRestTemplate.postForEntity("http://localhost:33243", null, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
