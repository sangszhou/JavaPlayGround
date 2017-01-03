package employee;

/**
 * Created by xinszhou on 20/12/2016.
 */
public class MainApp {
    public static void main(String args[]) {
        EmployeeManager mgr = new EmployeeManagerImpl();
        mgr.getAllEmployee();
    }
}
