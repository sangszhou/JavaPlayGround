package lg.phantomjs.position;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import util.JsonUtil;

import java.io.IOException;

/**
 * Created by xinszhou on 14/01/2017.
 */
@Service
public class PositionCrawler {

    Logger logger = LoggerFactory.getLogger(getClass());

    // cannot parse https in phantomjs, will assemly in proxy server
    static final String SearchPositionUrl = "www.lagou.com/gongsi/searchPosition.json";

    static final String proxyUrl = "http://localhost:9090";

    @Autowired
    PositionStore positionStore;

    @Autowired
    RestTemplate restTemplate;

    public String buildUrl(String companyId, int pageNo) {
        return SearchPositionUrl + "?pageNo=" + pageNo + "&companyId=" + companyId;
    }

    public JsonNode getData(String companyId, int pageNo) {
        String bodyData = "{\"url\":\"" + buildUrl(companyId, pageNo) + "\"}";

        ResponseEntity<String> result = restTemplate.postForEntity(proxyUrl, bodyData, String.class);

        positionStore.save(companyId, Integer.toString(pageNo), result.getBody());

        logger.info("persisted data to datastore for companyId " + companyId + ", pageNo " + pageNo);

        return null;
    }

    public int getPositionPageNo(String companyId) {
        String url = buildUrl(companyId, 1);
        String bodyData = "{\"url\":\"" + url + "\"}";

        ResponseEntity<String> result = restTemplate.postForEntity(proxyUrl, bodyData, String.class);
        JsonNode rst = null;

        try {
            rst = JsonUtil.string2Json(result.getBody());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (rst != null) {

            int num = rst.path("content").path("data").path("page").path("totalCount").asInt();
            int page = num / 10;
            if(num % 10 > 0) {
                page += 1;
            }

            return page;

        }

        return 0;
    }
}
