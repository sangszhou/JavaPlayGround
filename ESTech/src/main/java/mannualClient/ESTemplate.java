package mannualClient;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by xinszhou on 13/02/2017.
 */
public class ESTemplate {

    private ArrayList<ESNode> nodes;
    private int nextNodeIndex;

    private int bulkSize = 5; // bulk size is default to 5
    private ArrayList<ESTemplate.DocInfo> bulkBuffer;
    private long maxDocSize = 4194304; // max document size is default to 4Mb

    private RestTemplate restClient;

    public ESTemplate(String[] instances, int maxDocSize) {
        nodes = new ArrayList<>();
        for (String instance : instances) {
            ESTemplate.ESNode node = new ESTemplate.ESNode("http://" + instance);
            // mark node as fail-over mode
            node.status = ESTemplate.ESNodeStatus.FAIL_OVER;
            nodes.add(node);
        }
        // nodes should not be changed after initialization
        nextNodeIndex = 0;
        bulkBuffer = new ArrayList<>();
        if (maxDocSize > 0) {
            this.maxDocSize = maxDocSize;
        } else {
            throw new IllegalArgumentException("es max doc size should be a positive number");
        }

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setDefaultMaxPerRoute(8);
        connManager.setMaxTotal(8 * nodes.size());
        HttpComponentsClientHttpRequestFactory httpFactory = new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create().setConnectionManager(connManager).build());
        restClient = new RestTemplate(httpFactory);
        // recover all nodes during initialization
        recover();

        Runnable clusterProbe = this::recover;
        // Probe the whole ES cluster once every 60 seconds
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(clusterProbe,
                60, 60, TimeUnit.SECONDS);
    }

    private Optional<ESNode> getAvailableNode() {
        // number of nodes should be at least 1 if this function is reached
        assert nodes.size() > 0;
        for (int attempt = 0; attempt != nodes.size(); attempt++) {
            int pivot = (nextNodeIndex + attempt) % nodes.size();
            if (nodes.get(pivot).status == ESTemplate.ESNodeStatus.AVAILABLE) {
                nextNodeIndex = (pivot + 1) % nodes.size();
                return Optional.of(nodes.get(pivot));
            }
        }
        return Optional.empty();
    }

    private void recover() {
        for (ESTemplate.ESNode node : nodes) {
            if (node.status == ESTemplate.ESNodeStatus.FAIL_OVER) {
                try {
                    ResponseEntity<JsonNode> response = restClient.getForEntity(node.url, JsonNode.class);
                    if (response.getStatusCodeValue() == 200) {
                        node.status = ESTemplate.ESNodeStatus.AVAILABLE;
                    }
                } catch (Exception e) {
                    System.out.println("Failed to recover " + node.url + ", keep this node in fail-over mode");
                    failOver(node);
                }
            }
        }
    }

    public void setBulkSize(int bulkSize) {
        this.bulkSize = bulkSize;
    }

    public int getBulkSize() {
        return this.bulkSize;
    }

    /**
     * Index a document into a specific index and type.
     * The function will return document id if index request is acknowledged successfully,
     * otherwise exception will be thrown.
     *
     * @param index name of the index
     * @param type  name of the type
     * @param id    id of the document, use automatically generated id if this argument is null
     * @param doc   document to be indexed represented by JsonNode
     * @return id of the document
     */
    public String indexDoc(String index, String type, String id, JsonNode doc) {
        if (!checkDocSize(doc))
            throw new RuntimeException("Size of the document is larger than " + maxDocSize + ", doc: " + doc);
        if (id != null) {
            // use PUT to index or update document with specific id
            String uri = "/" + index + "/" + type + "/" + id;
            ResponseEntity<JsonNode> response = put(uri, doc.toString());
            int statusCode = response.getStatusCodeValue();
            if (statusCode == 200 || statusCode == 201) {
                // OK or CREATED, return silently
                return id;
            } else {
                JsonNode body = response.getBody();
                throw new RuntimeException("Unexpected status code while indexing document into " + uri + ": " + statusCode
                        + ", body: " + body.toString());
            }
        } else {
            // use POST
            String uri = "/" + index + "/" + type;
            ResponseEntity<JsonNode> response = post(uri, doc.toString());
            int statusCode = response.getStatusCodeValue();
            JsonNode body = response.getBody();
            if (statusCode == 200 || statusCode == 201) {
                // OK or CREATED
                return body.get("_id").asText();
            } else {
                throw new RuntimeException("Unexpected status code while indexing document into " + uri + ": " + statusCode
                        + ", body: " + body.toString());
            }
        }
    }

    /**
     * Index a document into a specific index and type using bulk API.
     * Beware that the document may not be available immediately until the bulk request is executed successfully.
     * Exception will be thrown if some documents cannot be indexed after the bulk request is executed.
     *
     * @param index name of the index
     * @param type  name of the type
     * @param id    id of the document, use automatically generated id if this argument is null
     * @param doc   document to be indexed represented by JsonNode
     */
    public void bulkIndexDoc(String index, String type, String id, JsonNode doc) {
        if (!checkDocSize(doc))
            throw new RuntimeException("Size of the document is larger than " + maxDocSize + ", doc: " + doc);
        synchronized (bulkBuffer) {
            bulkBuffer.add(new ESTemplate.DocInfo(index, type, id, doc));
        }
        if (bulkBuffer.size() >= bulkSize) {
            commitBulkRequest();
        }
    }

    public void commitBulkRequest() {
        // construct payload
        String payload = "";
        synchronized (bulkBuffer) {
            for (ESTemplate.DocInfo data : bulkBuffer) {
                payload += "{\"index\": {\"_index\": \"" + data.index + "\", \"_type\": \"" + data.type + "\"";
                if (data.id != null)
                    payload += ", \"_id\": \"" + data.id + "\"";
                payload += "}}\n";
                payload += data.doc.toString() + "\n";
            }
            bulkBuffer.clear();
        }
        if (payload.length() == 0) // corner case under multi-threading
            return;
        ResponseEntity<JsonNode> response = post("/_bulk", payload);
        JsonNode body = response.getBody();
        if (body.get("errors").asBoolean()) {
            // errors occurred in bulk request
            throw new RuntimeException("Errors occurred in bulk index request: " + body.toString());
        }
    }

    /**
     * Get a document from a specific index, type and id
     *
     * @param index name of the index
     * @param type  name of the type
     * @param id    id of the document
     * @return JsonNode representing the document if the document is found with specific id,
     * otherwise returns Optional.empty()
     */
    public Optional<JsonNode> getDoc(String index, String type, String id) {
        String uri = "/" + index + "/" + type + "/" + id;
        ResponseEntity<JsonNode> response = get(uri);
        int statusCode = response.getStatusCodeValue();
        JsonNode body = response.getBody();
        if (statusCode == 200) {
            return Optional.of(body.get("_source"));
        }
        if (statusCode == 404) {
            return Optional.empty();
        }
        throw new RuntimeException("Unexpected status code while deleting document from " + uri + ": " + statusCode + ", body: " + body.toString());
    }

    /**
     * Delete document with specific index, type and id
     *
     * @param index name of the index
     * @param type  name of the type
     * @param id    id of the document
     * @return true if the document existed and is deleted successfully, false if the document is not found
     */
    public boolean deleteDoc(String index, String type, String id) {
        String uri = "/" + index + "/" + type + "/" + id;
        ResponseEntity<JsonNode> response = delete(uri);
        int statusCode = response.getStatusCodeValue();
        JsonNode body = response.getBody();
        if (statusCode == 200) {
            // everything is fine
            return true;
        }
        if (statusCode == 404) {
            // document with specific id is not found
            return false;
        }
        // something is wrong
        throw new RuntimeException("Unexpected status code while deleting document from " + uri + ": " + statusCode + ", body: " + body.toString());
    }

    private void failOver(ESTemplate.ESNode node) {
        node.status = ESTemplate.ESNodeStatus.FAIL_OVER;
    }

    private ResponseEntity<JsonNode> put(String uri, String payload) {
        if (!uri.startsWith("/"))
            // make sure that uri starts with '/'
            uri = "/" + uri;
        Optional<ESTemplate.ESNode> nodeOpt = getAvailableNode();
        while (nodeOpt.isPresent()) {
            ESTemplate.ESNode node = nodeOpt.get();
            String url = node.url + uri;
            try {
                HttpEntity<String> requestEntity = new HttpEntity<>(payload);
                return restClient.exchange(url, HttpMethod.PUT, requestEntity, JsonNode.class);
            } catch (Exception e) {
                if (e instanceof HttpServerErrorException) {
                    // set this node to fail-over mode only if it is a server error
                    System.out.println("Failed to send PUT request to " + url + ", set this node to fail-over mode");
                    failOver(node);
                    nodeOpt = getAvailableNode();
                } else throw new RuntimeException("Failed to send PUT request to " + url, e);
            }
        }
        throw new RuntimeException("No available Elasticsearch instance");
    }

    private ResponseEntity<JsonNode> get(String uri) {
        if (!uri.startsWith("/"))
            // make sure that uri starts with '/'
            uri = "/" + uri;
        Optional<ESTemplate.ESNode> nodeOpt = getAvailableNode();
        while (nodeOpt.isPresent()) {
            ESTemplate.ESNode node = nodeOpt.get();
            String url = node.url + uri;
            try {
                return restClient.getForEntity(url, JsonNode.class);
            } catch (Exception e) {
                // set this node to fail-over mode only if it is a server error
                if (e instanceof HttpServerErrorException) {
                    System.out.println("Failed to send GET request to " + url + ", set this node to fail-over mode");
                    failOver(node);
                    nodeOpt = getAvailableNode();
                } else throw new RuntimeException("Failed to send GET request to " + url, e);
            }
        }
        throw new RuntimeException("No available Elasticsearch instance");
    }

    private ResponseEntity<JsonNode> post(String uri, String payload) {
        if (!uri.startsWith("/"))
            // make sure that uri starts with '/'
            uri = "/" + uri;
        Optional<ESTemplate.ESNode> nodeOpt = getAvailableNode();
        while (nodeOpt.isPresent()) {
            ESTemplate.ESNode node = nodeOpt.get();
            String url = node.url + uri;
            try {
                return restClient.postForEntity(url, payload, JsonNode.class);
            } catch (Exception e) {
                if (e instanceof HttpServerErrorException) {
                    // set this node to fail-over mode only if it is a server error
                    System.out.println("Failed to send POST request to " + url + ", set this node to fail-over mode");
                    failOver(node);
                    nodeOpt = getAvailableNode();
                } else throw new RuntimeException("Failed to send POST request to " + url, e);
            }
        }
        throw new RuntimeException("No available Elasticsearch instance");
    }

    private ResponseEntity<JsonNode> delete(String uri) {
        if (!uri.startsWith("/"))
            // make sure that uri starts with '/'
            uri = "/" + uri;
        Optional<ESTemplate.ESNode> nodeOpt = getAvailableNode();
        while (nodeOpt.isPresent()) {
            ESTemplate.ESNode node = nodeOpt.get();
            String url = node.url + uri;
            try {
                HttpEntity requestEntity = HttpEntity.EMPTY;
                return restClient.exchange(url, HttpMethod.DELETE, requestEntity, JsonNode.class);
            } catch (Exception e) {
                if (e instanceof HttpServerErrorException) {
                    // set this node to fail-over mode only if it is a server error
                    System.out.println("Failed to send DELETE request to " + url + ", set this node to fail-over mode");
                    failOver(node);
                    nodeOpt = getAvailableNode();
                } else throw new RuntimeException("Failed to send DELETE request to " + url, e);
            }
        }
        throw new RuntimeException("No available Elasticsearch instance");
    }

    private boolean checkDocSize(JsonNode doc) {
        return doc.toString().length() <= maxDocSize;
    }

    class DocInfo {
        String index;
        String type;
        String id;
        JsonNode doc;

        public DocInfo(String index, String type, String id, JsonNode doc) {
            this.index = index;
            this.type = type;
            this.id = id;
            this.doc = doc;
        }
    }

    class ESNode {
        String url;
        ESTemplate.ESNodeStatus status;

        public ESNode(String url) {
            this.url = url;
        }
    }

    enum ESNodeStatus {
        AVAILABLE,
        FAIL_OVER
    }

}

