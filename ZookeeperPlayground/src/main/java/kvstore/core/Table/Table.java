package kvstore.core.Table;

/**
 * Created by xinszhou on 18/01/2017.
 */
public interface Table {

    String getName();

    void setName(String name);

    void put(String key, String value);

    String get(String key);

    String delete(String key);
}
