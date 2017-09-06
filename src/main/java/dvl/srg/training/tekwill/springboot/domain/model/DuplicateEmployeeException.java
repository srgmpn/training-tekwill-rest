package dvl.srg.training.tekwill.springboot.domain.model;

public class DuplicateEmployeeException extends Exception {
    public DuplicateEmployeeException(String message) {
        super(message);
    }
}
