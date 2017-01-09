package kafka_style_nio;

import java.io.IOException;
import java.nio.channels.ScatteringByteChannel;

/**
 * Created by xinszhou on 02/12/2016.
 */
public interface Receive {

    public String source();

    public boolean complete();

    public long readFrom(ScatteringByteChannel channel) throws IOException;
}
