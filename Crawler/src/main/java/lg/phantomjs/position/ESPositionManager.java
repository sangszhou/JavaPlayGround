package lg.phantomjs.position;

import lg.phantomjs.position.model.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import util.JsonUtil;

import java.io.IOException;

/**
 * Created by xinszhou on 14/01/2017.
 */

@Service
public class ESPositionManager {

    String index = "position";
    String type = "data";

    @Autowired
    RestTemplate restTemplate;

    public void index(String index, String type, String positionId, String data) throws RestClientException {
        if(positionId.equals("1380770")) {
            System.out.println("break here");
        }

        restTemplate.postForObject("http://localhost:9200/" + index + "/" + type + "/" + positionId,
                data, String.class);
    }

    public void index(Position position) {

        try {
            index(index, type, position.getPositionId(), JsonUtil.toString(position));
        } catch (Exception e) {
            System.out.println("company id: " + position.getCompanyId() + ", position id " + position.getPositionId());
            e.printStackTrace();
        }
    }

}
