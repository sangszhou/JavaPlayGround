package firstTry.core.data;

/**
 * Created by xinszhou on 06/01/2017.
 */
public interface DataStore<K, V> {

    void put(K key, V value);

    V delete(K key);

    V get(K key);


}
