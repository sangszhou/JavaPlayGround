package base.property.api;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by xinszhou on 04/04/2017.
 */
public interface CommunityDataStore {
    void save(JsonNode data);

    void close();
}
