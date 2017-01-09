package nio_read_write;

import java.nio.ByteBuffer;

/**
 * Created by xinszhou on 02/12/2016.
 */
public class ByteBufferFromArray {
    public static void main(String args[]) {
        byte[] array = new byte[1024];

        ByteBuffer buffer = ByteBuffer.wrap(array);

        buffer.put((byte) 'a');
        buffer.put((byte) 'b');
        buffer.put((byte) 'c');

        buffer.flip();

        // print 97, 98, 99 if
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
    }
}
