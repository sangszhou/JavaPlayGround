package NettyRPC.protocol;

/**
 * Created by xinszhou on 05/12/2016.
 */
public class Person {

    private String firstName;
    private String lastName;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString(){
        return firstName + " " + lastName;
    }

    @Override
    public int hashCode() {
        return this.firstName.hashCode()^this.lastName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Person) ) return false;
        Person p = (Person)obj;
        return this.firstName.equals(p.firstName) && this.lastName.equals(p.lastName);
    }


}
