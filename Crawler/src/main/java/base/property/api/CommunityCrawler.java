package base.property.api;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

/**
 * Created by xinszhou on 03/04/2017.
 */
public interface CommunityCrawler {

    String nextOverviewPage(String currentPage);

    List<String> communityLinksInCurrentPage(String currentPage);

    JsonNode retrieveCommunityDetails(String url);
}
