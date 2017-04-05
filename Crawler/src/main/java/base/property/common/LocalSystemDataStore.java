package base.property.common;

import base.property.api.CommunityDataStore;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by xinszhou on 04/04/2017.
 */
public class LocalSystemDataStore implements CommunityDataStore {

    Logger log = LoggerFactory.getLogger(getClass());

    String dirPath = null;
    FileWriter fileWriter = null;

    public LocalSystemDataStore(String dirPath) {
        this.dirPath = dirPath;
    }

    @Override
    public void save(JsonNode data) {
        String content = data.toString();

        log.info("save content to file: " + content);

        try {
            fileWriter = new FileWriter(dirPath, true);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void close() {
    }

}
