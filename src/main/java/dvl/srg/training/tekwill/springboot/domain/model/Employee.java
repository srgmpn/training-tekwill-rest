package dvl.srg.training.tekwill.springboot.domain.model;

import java.io.Serializable;

public class Employee implements Serializable {

    private long id;
    private String name;
    private String address;
    private double salary;

    public Employee() {
    }

    public Employee(long id, String name, String address, double salary) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.salary = salary;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getSalary() {
        return salary;
    }

    public void updatePersonalInfo(final String name, final String address, final double salary) {
        this.name = name;
        this.address = address;
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Employee employee = (Employee) o;

        if (id != employee.id) {
            return false;
        }
        if (Double.compare(employee.salary, salary) != 0) {
            return false;
        }
        if (name != null ? !name.equals(employee.name) : employee.name != null) {
            return false;
        }
        return address != null ? address.equals(employee.address) : employee.address == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        temp = Double.doubleToLongBits(salary);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", salary=" + salary +
                '}';
    }
}
