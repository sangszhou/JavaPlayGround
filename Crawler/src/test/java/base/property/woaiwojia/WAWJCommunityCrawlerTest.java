package base.property.woaiwojia;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;

/**
 * Created by xinszhou on 04/04/2017.
 */
public class WAWJCommunityCrawlerTest {
    RestTemplate restTemplate = new RestTemplate();


    @Test
    public void nextOverviewPage() throws Exception {

    }

    @Test
    public void communityLinksInCurrentPage() throws Exception {
        final String url = "http://hz.5i5j.com/community/gongshu/u1y1/";
        ResponseEntity<String> wholePage = restTemplate.getForEntity(url, String.class);
        Document doc = Jsoup.parse(wholePage.getBody());

        String selector1 = "#commList1 > div.list-info-comm > h2 > a";
        String selector2 = "#commList2 > div.list-info-comm > h2 > a";
        String selector12 = "#commList12 > div.list-info-comm > h2";


        Elements link12 = doc.select(selector12);
        String link12_real = link12.select("a").attr("href");
        System.out.println(link12.html());
        System.out.println(link12_real);


    }

    @Test
    public void retrieveCommunityDetails() throws Exception {

    }

}