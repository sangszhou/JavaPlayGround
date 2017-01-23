package util;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by xinszhou on 25/12/2016.
 */
@Configuration
public class HttpClient {

    @Bean(name = "httpclient")
    public RestTemplate getRestTemplate() {

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setDefaultMaxPerRoute(8);
        connManager.setMaxTotal(8);

        HttpComponentsClientHttpRequestFactory httpFactory = new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder
                        .create()
//                        .setRedirectStrategy(new LaxRedirectStrategy())
                        .setConnectionManager(connManager).build());


//        HttpClient httpClient = (HttpClient) httpFactory.getHttpClient();
//        HttpHost proxy = new HttpHost("proxtserver", 2012);


        RestTemplate restTemplate = new RestTemplate(httpFactory);
        return restTemplate;
    }




}
