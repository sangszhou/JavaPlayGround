package resttemplate;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Created by xinszhou on 01/01/2017.
 */
public class WeavedRestTemplate extends RestTemplate {


    public WeavedRestTemplate(ClientHttpRequestFactory requestFactory) {
        setRequestFactory(requestFactory);
    }


    protected <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback,
                              ResponseExtractor<T> responseExtractor) throws RestClientException {


        Assert.notNull(url, "'url' must not be null");
        Assert.notNull(method, "'method' must not be null");
        ClientHttpResponse response = null;
        try {
            ClientHttpRequest request = createRequest(url, method);
            if (requestCallback != null) {
                requestCallback.doWithRequest(request);
            }

            try {
                response = request.execute();
            } catch (Exception e) {
                System.out.println("exception: " + e.getMessage());
                throw e;
            }

            if (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR ||
                    response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {

                System.out.println("status code is : " + response.getStatusText());
            } else {
                System.out.println("status code is " + response.getStatusText());
            }

            handleResponse(url, method, response);

            if (responseExtractor != null) {
                return responseExtractor.extractData(response);
            } else {
                return null;
            }
        } catch (IOException ex) {


            String resource = url.toString();
            String query = url.getRawQuery();
            resource = (query != null ? resource.substring(0, resource.indexOf(query) - 1) : resource);
            throw new ResourceAccessException("I/O error on " + method.name() +
                    " request for \"" + resource + "\": " + ex.getMessage(), ex);
        } finally {
            if (response != null) {
                response.close();
            }

        }
    }

    private byte[] getResponseBody(ClientHttpResponse response) {
        try {
            InputStream responseBody = response.getBody();
            if (responseBody != null) {
                return FileCopyUtils.copyToByteArray(responseBody);
            }
        } catch (IOException ex) {
            // ignore
        }
        return new byte[0];
    }

}
