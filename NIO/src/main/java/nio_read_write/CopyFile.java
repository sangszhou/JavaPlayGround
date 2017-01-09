package nio_read_write;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by xinszhou on 02/12/2016.
 */
public class CopyFile {
    public static void main(String args[]) throws Exception {
//        if(args.length < 2) {
//            System.err.println("Usage: Java copy file infile outfile");
//            System.exit(1);
//        }

        String infile = "abc.txt";
        String outfile = "bcd.txt";

        FileInputStream inputStream = new FileInputStream(infile);
        FileOutputStream outputStream = new FileOutputStream(outfile);


        FileChannel fcin = inputStream.getChannel();
        FileChannel fout = outputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (true) {
            buffer.clear();

            int r = fcin.read(buffer);

            if(r == -1) {
                break;
            }

            // ready for write
            // limit = position, position = 0
            buffer.flip();

            fout.write(buffer);
        }

        inputStream.close();
        outputStream.close();
        fcin.close();
        fout.close();
    }
}
