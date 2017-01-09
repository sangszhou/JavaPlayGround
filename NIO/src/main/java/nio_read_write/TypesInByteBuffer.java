package nio_read_write;

import java.nio.ByteBuffer;

/**
 * Created by xinszhou on 02/12/2016.
 */
public class TypesInByteBuffer {
    public static void main(String args[]) {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        buffer.putInt(30);
        buffer.putLong(7000000L);
        buffer.putDouble(Math.PI);

        buffer.flip();
        System.out.println(buffer.getInt());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getDouble());

    }
}
