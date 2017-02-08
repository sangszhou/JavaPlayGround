package inheritance;

/**
 * Created by xinszhou on 01/02/2017.
 */
public class Example1 {
    static class Father {
        static void print() {
            System.out.println(" in father   method ");
        }
    }

    static class Child extends Father {
        static void print() {
            System.out.println(" in child method ");
        }
    }

    public static void main(String args[]) {
        Father f = new Child();

        f.print();


//        new String('a');
//        new String()
    }



}
