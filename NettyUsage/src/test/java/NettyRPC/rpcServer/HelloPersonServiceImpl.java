package NettyRPC.rpcServer;

import NettyRPC.protocol.HelloPersonService;
import NettyRPC.protocol.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinszhou on 05/12/2016.
 */
public class HelloPersonServiceImpl implements HelloPersonService {

    @Override
    public List<Person> GetTestPerson(String name, int num) {
        List<Person> persons = new ArrayList<>(num);

        for (int i = 0; i < num; ++i) {
            persons.add(new Person(Integer.toString(i), name));
        }
        return persons;
    }
}
