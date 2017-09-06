package dvl.srg.training.tekwill.springboot.domain.model;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Optional<Employee> findById(final long id);

    Optional<Employee> findByName(final String name);

    void saveEmployee(final Employee employee) throws DuplicateEmployeeException;

    void updateEmployee(final Employee employee);

    void deleteEmployee(final Employee employee);

    List<Employee> findAllEmployee();

}
