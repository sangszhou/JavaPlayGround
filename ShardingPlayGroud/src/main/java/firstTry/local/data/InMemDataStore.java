package firstTry.local.data;

import firstTry.core.data.DataStore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xinszhou on 06/01/2017.
 */
public class InMemDataStore<K, V> implements DataStore<K, V> {

    Map<K, V> ds = new HashMap<K, V>();

    @Override
    public void put(K key, V value) {
        ds.put(key, value);
    }

    @Override
    public V delete(K key) {
        return ds.remove(key);
    }

    @Override
    public V get(K key) {
        return ds.get(key);
    }

}
