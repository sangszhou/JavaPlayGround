package NettyRPC.protocol;


import java.util.List;

/**
 * Created by xinszhou on 05/12/2016.
 */
public interface HelloPersonService {
    List<Person> GetTestPerson(String name, int num);

}
