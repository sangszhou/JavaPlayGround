package base.property.lianjia;

import base.util.NumberUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;


/**
 * Created by xinszhou on 03/04/2017.
 */
public class LianjiaCommunityCrawlerTest {

    LianjiaCommunityCrawler cc = new LianjiaCommunityCrawler();
    RestTemplate restTemplate = new RestTemplate();

    @Test
    public void nextPageTest() throws Exception {

        for (int i = 0; i < 100; i ++) {
            String pageUrl = cc.nextOverviewPage(i + "");
            ResponseEntity<String> wholePage = restTemplate.getForEntity(pageUrl, String.class);
            Thread.sleep(1000);
            if (wholePage.getStatusCode().is2xxSuccessful()) {
                System.out.println("status code successfully");
            } else {
                System.out.println("status code failed for url : " + wholePage);
            }
        }
    }

    @Test
    public void getCommunityDetailInfo() {
        String url = "http://hz.lianjia.com/xiaoqu/pg1/";
        String pageContentSelector = "body > div.content > div.leftContent > ul > li:nth-child(29) > div.info > div.title";
        ResponseEntity<String> wholePage = restTemplate.getForEntity(url, String.class);
        Document doc = Jsoup.parse(wholePage.getBody());
        Elements body = doc.select(pageContentSelector);
        System.out.println(body.select("a").first().attr("href"));
    }

    @Test
    public void CommunitiesInCurrentPageTest() throws Exception {
        String url = "http://hz.lianjia.com/xiaoqu/pg1/";
        List<String> links = cc.communityLinksInCurrentPage(url);
        if (links != null) {
            links.forEach(community -> System.out.println(community));
        } else {
            System.out.println("links return null");
        }
    }

    @Test
    public void getInfo() {
        String url = "http://hz.lianjia.com/xiaoqu/1811063919693/";
        ResponseEntity<String> wholePage = restTemplate.getForEntity(url, String.class);
        Document doc = Jsoup.parse(wholePage.getBody());
        LianjiaCommunityInfo info = new LianjiaCommunityInfo();


        String communityNamePath = "body > div.xiaoquDetailHeader > div > div.detailHeader.fl > h1";
        Elements name = doc.select(communityNamePath);
        System.out.println(name.html());


        Elements pricePath = doc.select("body > div.xiaoquOverview > div.xiaoquDescribe.fr > div.xiaoquPrice.clear > div > span.xiaoquUnitPrice");
        System.out.println(pricePath.html());

        Elements buildYear = doc.select("body > div.xiaoquOverview > div.xiaoquDescribe.fr > div.xiaoquInfo > div:nth-child(1) > span.xiaoquInfoContent");
        System.out.println(NumberUtil.retrieveIntFromString(buildYear.html()));

        Elements buildingNumPath = doc.select("body > div.xiaoquOverview > div.xiaoquDescribe.fr > div.xiaoquInfo > div:nth-child(6) > span.xiaoquInfoContent");
        System.out.println(buildingNumPath.html());

        Elements apartmentNum = doc.select("body > div.xiaoquOverview > div.xiaoquDescribe.fr > div.xiaoquInfo > div:nth-child(7) > span.xiaoquInfoContent");

        System.out.println(apartmentNum.html());
    }

    @Test
    public void getInfoMethodTest() {
        final String url = "http://hz.lianjia.com/xiaoqu/1811063919693/";
        JsonNode result = cc.retrieveCommunityDetails(url);
        System.out.println(result.toString());
    }

}