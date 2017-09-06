package dvl.srg.training.tekwill.springboot;

import dvl.srg.training.tekwill.springboot.domain.model.DuplicateEmployeeException;
import dvl.srg.training.tekwill.springboot.domain.model.Employee;
import dvl.srg.training.tekwill.springboot.domain.model.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class DefaultEmployeeService implements EmployeeService {

    private static final AtomicLong counter = new AtomicLong();

    private static List<Employee> employees;

    static {
        employees = populateDummyEmployees();
    }

    @Override
    public Optional<Employee> findById(final long id) {
        if (id < 0) {
            return Optional.empty();
        }

        return employees.stream()
                .filter(employee -> id == employee.getId())
                .findAny();
    }

    @Override
    public Optional<Employee> findByName(final String name) {
        if (Objects.isNull(name) || name.isEmpty()) {
            return Optional.empty();
        }

        return employees.stream()
                .filter(employee -> name.equals(employee.getName()))
                .findAny();
    }

    @Override
    public void saveEmployee(final Employee employee) throws DuplicateEmployeeException {
        if (Objects.isNull(employee)) {
            throw new IllegalArgumentException("Employee entity is null!");
        }

        boolean exists = employees.contains(employee);
        if (exists) {
            throw new DuplicateEmployeeException("Employee entity already exists!");
        }

        employees.add(employee);
    }

    @Override
    public void updateEmployee(final Employee employee) {
    }

    @Override
    public void deleteEmployee(final Employee employee) {
        if (Objects.isNull(employee)) {
            throw new IllegalArgumentException("Employee entity is null!");
        }
        employees.remove(employee);
    }

    @Override
    public List<Employee> findAllEmployee() {
        return employees;
    }

    private static List<Employee> populateDummyEmployees() {
        final List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(counter.incrementAndGet(), "Sam", "Stefan cel Mare", 30.5));
        employees.add(new Employee(counter.incrementAndGet(), "Tom", "Bd. Moscova", 50.5));
        employees.add(new Employee(counter.incrementAndGet(), "Jerome", "Db.Dacia", 45));
        employees.add(new Employee(counter.incrementAndGet(), "Silvia", "Mircea cel Batrin", 30));
        return employees;

    }
}
