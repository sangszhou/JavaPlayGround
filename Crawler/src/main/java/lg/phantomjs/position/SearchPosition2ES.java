package lg.phantomjs.position;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lg.phantomjs.position.model.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import util.JsonUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinszhou on 14/01/2017.
 */
@Service
public class SearchPosition2ES {

    public static String inputBaseDir = "/ws/github/JavaPlayGround/Crawler/src/main/resources/positionOutput";

    @Autowired
    ESPositionManager esPositionManager;

    public List<Position> extractPositions(JsonNode positionData) {
        List<Position> positionList = new ArrayList<>();

        ArrayNode companiesInJson = (ArrayNode) positionData
                .path("content").path("data").path("page").path("result");

        for (int i = 0; i < companiesInJson.size(); i++) {
            JsonNode positionInJson = companiesInJson.get(i);
            Position position = JsonUtil.convert(positionInJson, Position.class);
            positionList.add(position);
        }

        return positionList;
    }

    public void indexToES(List<Position> positions) {
        positions.forEach(position -> {
            esPositionManager.index(position);
        });
    }

    public List<String> getFilenameInFolder(String folderName) {
        List<String> filenames = new ArrayList<>();

        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {

                filenames.add(file.getAbsolutePath());
            }
        }

        return filenames;
    }

    public static void main(String args[]) {
        ApplicationContext context = new AnnotationConfigApplicationContext("lg.phantomjs.position", "util");
        SearchPosition2ES toES = context.getBean(SearchPosition2ES.class);

        List<String> jsonFileNames = toES.getFilenameInFolder(SearchPosition2ES.inputBaseDir);

        for (int i = 100; i < jsonFileNames.size(); i++) {
            System.out.println("index the " + i + " th file to es, overall file number is " + jsonFileNames.size());

            try {
                JsonNode oneFileData = JsonUtil.inputStream2Json(new FileInputStream(jsonFileNames.get(i)));
                List<Position> positions = toES.extractPositions(oneFileData);
                positions.forEach(p -> p.wrapUp());

//                System.out.println(JsonUtil.toString(positions.get(0)));
                toES.indexToES(positions);
//                toES.indexToES(positions.subList(0, 1));

//                if(i == 0) {
//                    return;
//                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
