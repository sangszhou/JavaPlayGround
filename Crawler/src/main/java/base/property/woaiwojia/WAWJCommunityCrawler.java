package base.property.woaiwojia;

import base.property.api.CommunityCrawler;
import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by xinszhou on 04/04/2017.
 */
public class WAWJCommunityCrawler {
    Logger logger = LoggerFactory.getLogger(getClass());

    RestTemplate restTemplate = new RestTemplate();

    List<String> areas = new LinkedList<>();
    List<String> years = new LinkedList<>();
    final String baseUrl = "http://hz.5i5j.com/community/";

    public WAWJCommunityCrawler() {
        areas.add("gongshu");
        areas.add("xiacheng");
        areas.add("shangcheng");
        areas.add("binjiang");
        areas.add("yuhang");
        areas.add("xiaoshan");
        areas.add("xihu");
        areas.add("jianggan");
        areas.add("fuyang");

        years.add("y1");
        years.add("y2");
        years.add("y3");
        years.add("y4");
        years.add("y5");
    }


    // this input is not enough to compute the next overview page
    // but it's rather easy to compute the next page if argument
    // @todo too complex
    public String nextOverviewPage(String area, int year, String type, int pageNum) {
        StringBuilder sb = new StringBuilder();


        //next area
//        if (!year.startsWith("y")) {
//            int areaIndex = areas.indexOf(area);
//
//            if (areaIndex+1 == areas.size()) {
//                return null;
//            }
//
//            // first page of next area
//            String nextArea = areas.get(areaIndex + 1);
//            return nextArea + "y1n1";
//        } else {
//
//        }

        return null;
    }

    public List<String> communityLinksInCurrentPage(String currentPage) {
        String firstPart = "#commList";
        String secondPart = " > div.list-info-comm > h2 > a";

        ResponseEntity<String> wholePage = restTemplate.getForEntity(currentPage, String.class);
        if (!wholePage.getStatusCode().is2xxSuccessful()) {
            logger.error("failed to get content for url " + currentPage);
            return null;
        }

        Document doc = Jsoup.parse(wholePage.getBody());
        List<String> links = new LinkedList<>();

        for (int i = 1; i <= 12; i++) {
            String finalPart = firstPart + i + secondPart;
            Elements communityPageLink = doc.select(finalPart);
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

    public JsonNode retrieveCommunityDetails(String url) {
        return null;
    }
}
