import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Employee> employees = DummyData.getEmployees();

        System.out.println("Employees loaded: " + employees.size());

        // ==========================================================
        // PRACTICE HERE
        // Read README.md and solve the exercises one by one.
        // ==========================================================

        // Example only:
         employees.stream()
                .filter(Employee::active)
                .forEach(System.out::println);
    }
}
