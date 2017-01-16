package disJobFramework.cluster.worker;

import disJobFramework.core.client.Resource;

/**
 * Created by xinszhou on 16/01/2017.
 */
public class DummyResource implements Resource {
    int n;

    public DummyResource(int n) {
        this.n = n;
    }

    @Override
    public void empty() {

    }
}
