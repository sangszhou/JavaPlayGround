package base.property.lianjia;

import base.property.api.CommunityCrawler;
import base.util.NumberUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import util.JsonUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by xinszhou on 03/04/2017.
 */
public class LianjiaCommunityCrawler implements CommunityCrawler {

    Logger logger = LoggerFactory.getLogger(getClass());

    RestTemplate restTemplate = new RestTemplate();
    String nextPageButtonXPath = "/html/body/div[4]/div[1]/div[3]/div[2]/div/a[5]";
    final String communityBaseUrl = "http://hz.lianjia.com/xiaoqu/pg";


    @Override
    public String nextOverviewPage(String currentPageNum) {
        int pageNum = Integer.parseInt(currentPageNum);
        String pageUrl = communityBaseUrl + (pageNum+1);
        return pageUrl;
    }

    //at most 30 comminuties in one page
    @Override
    public List<String> communityLinksInCurrentPage(String currentPage) {
        String firstPart = "body > div.content > div.leftContent > ul > li:nth-child(";
        String secondPart = ") > div.info > div.title > a";

        ResponseEntity<String> wholePage = restTemplate.getForEntity(currentPage, String.class);
        if(!wholePage.getStatusCode().is2xxSuccessful()) {
            logger.error("failed to get content for url " + currentPage);
            return null;
        }

        Document doc = Jsoup.parse(wholePage.getBody());
        List<String> links = new LinkedList<>();

        for(int i = 1; i <= 30; i ++) {
            String finalPart = firstPart + i + secondPart;
            Elements communityPageLink = doc.select(finalPart);
            // if exception thrown here, then it must be blocked by the server
            String link = communityPageLink.select("a").first().attr("href");
            if (link == null) {
                logger.error("failed to parse community link: " + finalPart);
            } else {
                links.add(link);
                System.out.println(link);
            }
        }
        return links;
    }

    @Override
    public JsonNode retrieveCommunityDetails(String url) {
        ResponseEntity<String> wholePage = restTemplate.getForEntity(url, String.class);
        Document doc = Jsoup.parse(wholePage.getBody());


        String communityNamePath = "body > div.xiaoquDetailHeader > div > div.detailHeader.fl > h1";
        Elements name = doc.select(communityNamePath);


        Elements pricePath = doc.select("body > div.xiaoquOverview > div.xiaoquDescribe.fr > div.xiaoquPrice.clear > div > span.xiaoquUnitPrice");

        Elements buildYear = doc.select("body > div.xiaoquOverview > div.xiaoquDescribe.fr > div.xiaoquInfo > div:nth-child(1) > span.xiaoquInfoContent");

        Elements buildingNumPath = doc.select("body > div.xiaoquOverview > div.xiaoquDescribe.fr > div.xiaoquInfo > div:nth-child(6) > span.xiaoquInfoContent");

        Elements apartmentNum = doc.select("body > div.xiaoquOverview > div.xiaoquDescribe.fr > div.xiaoquInfo > div:nth-child(7) > span.xiaoquInfoContent");

        Elements kfs = doc.select("body > div.xiaoquOverview > div.xiaoquDescribe.fr > div.xiaoquInfo > div:nth-child(5) > span.xiaoquInfoContent");


        LianjiaCommunityInfo info = new LianjiaCommunityInfo();
        info.setApartmentNum(NumberUtil.retrieveIntFromString(apartmentNum.html()));
        info.setBuildingNum(NumberUtil.retrieveIntFromString(buildingNumPath.html()));
        info.setCommunityName(name.html());
        info.setKfs(kfs.html());
        info.setInfoDate(System.currentTimeMillis());
        info.setPricePerSquare(NumberUtil.retrieveIntFromString(pricePath.html()));
        info.setYearBuilt(NumberUtil.retrieveIntFromString(buildYear.html()));
        return JsonUtil.obj2Json(info);
    }

}
