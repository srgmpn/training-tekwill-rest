package dvl.srg.training.tekwill.springboot;

import dvl.srg.training.tekwill.springboot.domain.model.CustomErrorType;
import dvl.srg.training.tekwill.springboot.domain.model.DuplicateEmployeeException;
import dvl.srg.training.tekwill.springboot.domain.model.Employee;
import dvl.srg.training.tekwill.springboot.domain.model.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class RestApiController {


    public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

    private final EmployeeService employeeService;

    @Autowired
    public RestApiController(final EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @RequestMapping(value = "/employee/", method = RequestMethod.GET)
    public ResponseEntity<List<Employee>> listAllEmployees() {
        List<Employee> employees = employeeService.findAllEmployee();
        if (employees.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @RequestMapping(value = "/employee/{name}", method = RequestMethod.GET)
    public ResponseEntity<?> findEmployeeByName(@PathVariable("name") String name) {
        logger.info("Fetching Employee with name {}", name);
        Optional<Employee> employee = employeeService.findByName(name);
        if (!employee.isPresent()) {
            logger.error("Employee with name {} not found.", name);
            return new ResponseEntity(new CustomErrorType("Employee with name " + name
                    + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(employee.get(), HttpStatus.OK);
    }

    @RequestMapping(value = "/employee/", method = RequestMethod.POST)
    public ResponseEntity<?> createEmployee(@RequestBody Employee employee, UriComponentsBuilder ucBuilder) {
        logger.info("Creating Employee : {}", employee);

        try {
            employeeService.saveEmployee(employee);
        } catch (DuplicateEmployeeException e) {
            logger.error("Unable to create. A Employee with name {} already exist", employee.getName());
            return new ResponseEntity(new CustomErrorType("Unable to create. A Employee with name " +
                    employee.getName() + " already exist."), HttpStatus.CONFLICT);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/employees/employee/{id}").buildAndExpand(employee.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/employee/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateEmployee(@PathVariable("id") long id, @RequestBody Employee employee) {
        logger.info("Updating Employee with id {}", id);

        final Optional<Employee> currentEmployeeOptional = employeeService.findById(id);

        if (!currentEmployeeOptional.isPresent()) {
            logger.error("Unable to update. Employee with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to upate. Employee with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }

        final Employee currentEmployee = currentEmployeeOptional.get();
        currentEmployee.updatePersonalInfo(employee.getName(), employee.getAddress(), employee.getSalary());

        employeeService.updateEmployee(currentEmployee);
        return new ResponseEntity<>(currentEmployee, HttpStatus.OK);
    }

    @RequestMapping(value = "/employee/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") long id) {
        logger.info("Fetching & Deleting Employee with id {}", id);

        Optional<Employee> employeeOptional = employeeService.findById(id);
        if (!employeeOptional.isPresent()) {
            logger.error("Unable to delete. Employee with id {} not found.", id);
            return new ResponseEntity(new CustomErrorType("Unable to delete. Employee with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        employeeService.deleteEmployee(employeeOptional.get());
        return new ResponseEntity<Employee>(HttpStatus.NO_CONTENT);
    }
}
