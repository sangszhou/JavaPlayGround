package kvstore.core.Table;

import javafx.scene.control.Tab;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xinszhou on 18/01/2017.
 */
public class InMemoryTable implements Table {

    Map<String, String> store = new HashMap<>();
    String name;

    public InMemoryTable(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void put(String key, String value) {
        store.put(key, value);
    }

    @Override
    public String get(String key) {
        return store.get(key);
    }

    @Override
    public String delete(String key) {
        return store.remove(key);
    }

}
