package employee;

import java.util.List;

/**
 * Created by xinszhou on 15/12/2016.
 */
public interface EmployeeManager {

    public EmployeeDTO getEmployeeById(Integer employeeId);

    public List<EmployeeDTO> getAllEmployee();

    public void createEmployee(EmployeeDTO employee);

    public void deleteEmployee(Integer employeeId);

    public void updateEmployee(EmployeeDTO employee);

}
