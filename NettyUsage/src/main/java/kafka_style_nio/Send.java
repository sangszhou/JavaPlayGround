package kafka_style_nio;

import java.io.IOException;
import java.nio.channels.GatheringByteChannel;

/**
 * Created by xinszhou on 02/12/2016.
 */
public interface Send {

    public String destination();

    public boolean completed();

    public long writeTo(GatheringByteChannel channel) throws IOException;

    public long size();
}
