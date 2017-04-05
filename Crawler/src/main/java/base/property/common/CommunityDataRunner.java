package base.property.common;

import base.property.api.CommunityCrawler;
import base.property.api.CommunityDataStore;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

/**
 * Created by xinszhou on 04/04/2017.
 */
public class CommunityDataRunner {

    CommunityCrawler communityCrawler;
    CommunityDataStore dataStore;

    public CommunityDataRunner(CommunityCrawler communityCrawler, CommunityDataStore ds) {
        this.communityCrawler = communityCrawler;
        dataStore = ds;
    }

    //@todo end page number
    public void run(int startPageNum) {
        int currentPageNum = startPageNum;

        while (true) {
            String nextOverviewPageLink = communityCrawler.nextOverviewPage(currentPageNum+"");

            List<String> detailCommunityPageLinks = communityCrawler.communityLinksInCurrentPage(nextOverviewPageLink);
            Scheduler.tick();
            if (detailCommunityPageLinks != null) {
                System.out.println("crawling page link: " + nextOverviewPageLink);
                System.out.println("page num: " + currentPageNum);

                // go to each community detail page
                for(String communityDetailUrl: detailCommunityPageLinks) {
                    JsonNode communityInfo = communityCrawler.retrieveCommunityDetails(communityDetailUrl);
                    dataStore.save(communityInfo);
                    Scheduler.tick();
                }

            } else {
                System.out.println("no more page available, exit program!");
                break;
            }
            currentPageNum++;

        }

        dataStore.close();
    }

}
