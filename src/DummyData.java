import java.util.List;

public final class DummyData {
    private DummyData() {}

    public static List<Employee> getEmployees() {
        return List.of(
            new Employee(1, "John Silva", "IT", 120000, 28, true, "Colombo", List.of("Java", "Spring Boot", "SQL")),
            new Employee(2, "Anna Perera", "HR", 90000, 32, true, "Negombo", List.of("Recruitment", "Excel")),
            new Employee(3, "Mike Fernando", "IT", 150000, 35, true, "Colombo", List.of("Java", "AWS", "Docker")),
            new Employee(4, "Sara Jayasinghe", "Finance", 110000, 26, true, "Kandy", List.of("Excel", "SAP")),
            new Employee(5, "David Dias", "IT", 95000, 24, false, "Negombo", List.of("Java", "React")),
            new Employee(6, "Emma Rodrigo", "HR", 105000, 29, true, "Colombo", List.of("Recruitment", "Communication")),
            new Employee(7, "Chris De Silva", "Finance", 130000, 38, true, "Galle", List.of("SAP", "SQL")),
            new Employee(8, "Sophia Fernando", "IT", 175000, 31, true, "Negombo", List.of("Java", "Spring Boot", "AWS", "Kubernetes")),
            new Employee(9, "Daniel Perera", "Operations", 85000, 27, true, "Colombo", List.of("Logistics", "Excel")),
            new Employee(10, "Olivia Silva", "Finance", 125000, 33, false, "Kandy", List.of("Excel", "SQL")),
            new Employee(11, "James Rodrigo", "IT", 140000, 30, true, "Colombo", List.of("Java", "Spring Boot", "PostgreSQL")),
            new Employee(12, "Mia Dias", "HR", 88000, 25, true, "Negombo", List.of("Recruitment", "Excel")),
            new Employee(13, "Robert Fernando", "Operations", 92000, 36, true, "Galle", List.of("Logistics", "Leadership")),
            new Employee(14, "Emily Silva", "IT", 160000, 34, true, "Kandy", List.of("Java", "Angular", "AWS")),
            new Employee(15, "William Perera", "Finance", 115000, 29, true, "Colombo", List.of("SAP", "Excel")),
            new Employee(16, "Charlotte Dias", "HR", 98000, 31, false, "Negombo", List.of("Recruitment", "Communication")),
            new Employee(17, "Thomas Rodrigo", "Operations", 102000, 40, true, "Colombo", List.of("Logistics", "Leadership", "Excel")),
            new Employee(18, "Amelia Fernando", "IT", 135000, 27, true, "Galle", List.of("Java", "React", "Docker")),
            new Employee(19, "George Silva", "Finance", 145000, 37, true, "Colombo", List.of("SAP", "SQL", "Power BI")),
            new Employee(20, "Isabella Perera", "HR", 108000, 28, true, "Kandy", List.of("Recruitment", "Excel", "Power BI")),
            new Employee(21, "Jack Dias", "IT", 155000, 33, false, "Negombo", List.of("Java", "Spring Boot", "Docker")),
            new Employee(22, "Grace Rodrigo", "Operations", 97000, 26, true, "Galle", List.of("Logistics", "Excel")),
            new Employee(23, "Henry Fernando", "Finance", 118000, 32, true, "Colombo", List.of("SAP", "SQL")),
            new Employee(24, "Lily Silva", "IT", 125000, 29, true, "Negombo", List.of("Java", "Spring Boot")),
            new Employee(25, "Edward Perera", "HR", 93000, 35, true, "Colombo", List.of("Recruitment", "Leadership")),
            new Employee(26, "Ella Dias", "Operations", 112000, 30, true, "Kandy", List.of("Logistics", "SQL", "Excel")),
            new Employee(27, "Lucas Rodrigo", "IT", 145000, 36, true, "Colombo", List.of("Java", "Spring Boot", "Kubernetes")),
            new Employee(28, "Chloe Fernando", "Finance", 128000, 28, false, "Galle", List.of("Excel", "Power BI")),
            new Employee(29, "Oscar Silva", "HR", 100000, 34, true, "Negombo", List.of("Recruitment", "Communication")),
            new Employee(30, "Lucy Perera", "IT", 170000, 32, true, "Colombo", List.of("Java", "Spring Boot", "AWS", "Docker"))
        );
    }
}
