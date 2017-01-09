package nio_read_write;

import java.nio.FloatBuffer;

/**
 * Created by xinszhou on 02/12/2016.
 */
public class FloatBufferDemo {
    public static void main(String args[]) {
        FloatBuffer buffer = FloatBuffer.allocate(10);

        for(int i = 0; i < buffer.capacity(); i ++) {
            float f = (float) (i*1.0);
            buffer.put(f);
        }

        buffer.flip();

        while (buffer.hasRemaining()) {
            float f = buffer.get();
            System.out.println(f);
        }



    }
}
