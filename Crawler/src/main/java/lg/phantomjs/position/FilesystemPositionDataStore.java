package lg.phantomjs.position;

import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by xinszhou on 14/01/2017.
 */

@Service
public class FilesystemPositionDataStore implements PositionStore {

    String dirPath = "/ws/github/JavaPlayGround/Crawler/src/main/resources/positionOutput/";

    @Override
    public void save(String companyId, String pageNn, String data) {
        String fileName = dirPath + companyId + "-" + pageNn + ".json";
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);
            fileWriter.write(data);
            fileWriter.flush();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }
}
