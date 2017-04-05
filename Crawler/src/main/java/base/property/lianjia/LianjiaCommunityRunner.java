package base.property.lianjia;

import base.property.api.CommunityDataStore;
import base.property.api.CommunityCrawler;
import base.property.common.CommunityDataRunner;
import base.property.common.LocalSystemDataStore;

/**
 * Created by xinszhou on 04/04/2017.
 */
public class LianjiaCommunityRunner {

    public static void main(String args[]) {
        CommunityCrawler crawler = new LianjiaCommunityCrawler();
        // @todo
        String dirPath = "/ws/github/JavaPlayGround/Crawler/src/main/resources/lianjiaCommunityOut/communities.log";
        CommunityDataStore ds = new LocalSystemDataStore(dirPath);

        CommunityDataRunner runner = new CommunityDataRunner(crawler, ds);

        int startPageNum = 1-1;
        runner.run(startPageNum);
    }
}
