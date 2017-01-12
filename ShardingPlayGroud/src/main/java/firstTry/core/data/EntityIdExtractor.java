package firstTry.core.data;

/**
 * Created by xinszhou on 06/01/2017.
 */
public interface EntityIdExtractor<T> {
    String extract(T t);
}
