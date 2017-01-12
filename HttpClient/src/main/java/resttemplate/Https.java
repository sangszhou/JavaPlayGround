package resttemplate;

import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by xinszhou on 11/01/2017.
 */
public class Https {
    public static void main(String args[]) {
        String url = "https://edsartrule-dev.cloudapps.cisco.com/services/v1/rule/simulate";

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create()
                        .disableContentCompression()
                        .setMaxConnPerRoute(20)
                        .setMaxConnPerRoute(30).build());

        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);

        String body = "{\"name\":\"SCAppGrp:UOVTest\",\"baseType\":\"RESOURCE\",\"source\":\"CEPM\"}";


        String crendential = "YXJ0YXBpLWNlcG0uZ2VuOkdCNXZlc0J0";
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Basic " + crendential);
        headers.add("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        System.out.println(response.getBody());
    }

}
