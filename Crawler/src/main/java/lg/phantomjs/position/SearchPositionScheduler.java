package lg.phantomjs.position;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.JsonUtil;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by xinszhou on 14/01/2017.
 */
@Service
public class SearchPositionScheduler {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    PositionCrawler positionCrawler;

    String cityDir = "/ws/github/JavaPlayGround/Crawler/js/phantomjs/RestfulAPI/lg/output/hangzhou/";

    public void searchPosition() {

        for (int i = 2; i < 70; i++) {
            String fileName = cityDir + i + ".json";

            try {
                JsonNode companyInfo = JsonUtil.inputStream2Json(new FileInputStream(fileName));
                ArrayNode companies = (ArrayNode) companyInfo.path("result");

                for (int j = 0; j < companies.size(); j++) {
                    JsonNode company = companies.get(j);
                    String companyId = company.path("companyId").asText();
                    int maxPageNo = positionCrawler.getPositionPageNo(companyId);

                    logger.info("get position info for companyId " + companyId +
                            ", position page number is " + maxPageNo +
                            ", company name is " + company.path("companyShortName").asText());

                    tick();

                    for (int k = 1; k <= maxPageNo; k++) {
                        positionCrawler.getData(companyId, k);
                        tick();
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void tick() {
        try {
            Thread.sleep(6600);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
