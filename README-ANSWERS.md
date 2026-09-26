# Java Collections & Streams Practice Lab --- Optimized Answers

This answer guide matches **Q1--Q57** from the practice README.

The goal is not just to make the code work. The examples prefer: -
readable and maintainable Java; - method references where they improve
clarity; - primitive streams (`mapToDouble`, `mapToInt`) for numeric
aggregation; - `max()` / `min()` instead of sorting an entire list when
only one extreme is needed; - safe `Optional` handling; - explicit merge
functions when `toMap()` can receive duplicate keys; - filtering before
mapping/sorting when it reduces the amount of work; - a normal loop when
it is clearer or avoids repeatedly traversing the same collection.

Assume this is available in `main()`:

``` java
List<Employee> employees = DummyData.getEmployees();
```

Useful imports:

``` java
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
```

------------------------------------------------------------------------

# Level 1 --- `filter()`

## Q1 --- IT employees

``` java
List<Employee> result = employees.stream()
    .filter(e -> "IT".equals(e.department())) // Keep only IT employees.
    .toList();                                // Java 16+: returns an unmodifiable list.

result.forEach(e -> System.out.println(e.name()));
```

**Why:** `filter()` is the correct operation when the output still
contains the original `Employee` objects but only some records should
remain. Writing the constant first (`"IT".equals(...)`) is null-safe if
department could later become nullable.

------------------------------------------------------------------------

## Q2 --- Salary greater than 120,000

``` java
List<Employee> result = employees.stream()
    .filter(e -> e.salary() > 120_000) // Remove employees outside the salary rule.
    .toList();

result.forEach(e ->
    System.out.println(e.name() + " - " + e.salary())
);
```

**Why:** Numeric separators such as `120_000` improve readability and do
not change the value.

------------------------------------------------------------------------

## Q3 --- Employees younger than 30

``` java
List<Employee> result = employees.stream()
    .filter(e -> e.age() < 30) // Keep only employees satisfying the age condition.
    .toList();
```

**Why:** Keep the predicate close to the business rule; avoid extra
temporary lists.

------------------------------------------------------------------------

## Q4 --- Active employees

``` java
List<Employee> result = employees.stream()
    .filter(Employee::active) // Method reference is cleaner for a boolean accessor.
    .toList();
```

**Why:** `Employee::active` is equivalent to `e -> e.active()` but more
concise.

------------------------------------------------------------------------

## Q5 --- Active IT employees earning at least 130,000

``` java
List<Employee> result = employees.stream()
    .filter(Employee::active)                 // Cheap/simple filters first.
    .filter(e -> "IT".equals(e.department()))
    .filter(e -> e.salary() >= 130_000)
    .toList();
```

**Why:** Separate filters are often easier to debug and modify than one
large boolean expression. Streams are lazy, so rejected records do not
continue through later stages.

------------------------------------------------------------------------

## Q6 --- Employees from Negombo

``` java
List<Employee> result = employees.stream()
    .filter(e -> "Negombo".equals(e.city()))
    .toList();
```

**Why:** This is null-safe and directly represents the business
condition.

------------------------------------------------------------------------

## Q7 --- IT or Finance

``` java
Set<String> allowedDepartments = Set.of("IT", "Finance");

List<Employee> result = employees.stream()
    .filter(e -> allowedDepartments.contains(e.department()))
    .toList();
```

**Why:** For multiple accepted values, a `Set` is cleaner and scales
better than a long chain of `||` checks.

------------------------------------------------------------------------

# Level 2 --- `map()`

## Q8 --- Employee names only

``` java
List<String> names = employees.stream()
    .map(Employee::name) // Transform Employee -> String.
    .toList();
```

**Why:** `map()` changes the representation of each element.

------------------------------------------------------------------------

## Q9 --- Names in uppercase

``` java
List<String> names = employees.stream()
    .map(Employee::name)
    .map(String::toUpperCase) // Transform each extracted name.
    .toList();
```

**Why:** Keeping transformations as small stages makes the pipeline easy
to read.

------------------------------------------------------------------------

## Q10 --- API-style string transformation

``` java
List<String> result = employees.stream()
    .map(e -> "%d - %s - %s".formatted(
        e.id(), e.name(), e.department()
    ))
    .toList();
```

**Why:** `formatted()` is clearer than repeated string concatenation for
structured output.

------------------------------------------------------------------------

## Q11 --- IT employee names

``` java
List<String> names = employees.stream()
    .filter(e -> "IT".equals(e.department())) // Reduce records first.
    .map(Employee::name)                       // Then transform only matching records.
    .toList();
```

**Why:** Filter before mapping when the mapping is only required for
matching elements.

------------------------------------------------------------------------

# Level 3 --- Sorting, limiting and pagination

## Q12 --- Salary ascending

``` java
List<Employee> result = employees.stream()
    .sorted(Comparator.comparingDouble(Employee::salary))
    .toList();
```

**Why:** `comparingDouble` avoids boxing `double` values into `Double`.

------------------------------------------------------------------------

## Q13 --- Salary descending

``` java
List<Employee> result = employees.stream()
    .sorted(Comparator.comparingDouble(Employee::salary).reversed())
    .toList();
```

**Why:** Build the comparator once and reverse it rather than writing
manual comparison logic.

------------------------------------------------------------------------

## Q14 --- Five highest-paid employees

``` java
List<Employee> topFive = employees.stream()
    .sorted(Comparator.comparingDouble(Employee::salary).reversed())
    .limit(5) // Stop the output after five sorted records.
    .toList();
```

**Why:** This is clean for normal in-memory collections. For very large
datasets, ask the database for the top five instead of loading
everything into Java.

------------------------------------------------------------------------

## Q15 --- Youngest first

``` java
List<Employee> result = employees.stream()
    .sorted(Comparator.comparingInt(Employee::age))
    .toList();
```

**Why:** `comparingInt` avoids unnecessary boxing.

------------------------------------------------------------------------

## Q16 --- Department ascending, salary descending

``` java
Comparator<Employee> comparator =
    Comparator.comparing(Employee::department)
        .thenComparing(
            Comparator.comparingDouble(Employee::salary).reversed()
        );

List<Employee> result = employees.stream()
    .sorted(comparator)
    .toList();
```

**Why:** A named comparator is easier to understand and reuse than a
deeply nested expression.

------------------------------------------------------------------------

## Q17 --- Page 2, page size 5

``` java
int page = 2;
int pageSize = 5;

List<Employee> result = employees.stream()
    .skip((long) (page - 1) * pageSize) // Skip all records belonging to earlier pages.
    .limit(pageSize)
    .toList();
```

**Why:** Casting to `long` matches `skip(long)`. In real database-backed
APIs, prefer database pagination (`Pageable`) rather than loading all
rows first.

------------------------------------------------------------------------

# Level 4 --- Find, match and count

## Q18 --- Find employee ID 15

``` java
Optional<Employee> employee = employees.stream()
    .filter(e -> e.id() == 15)
    .findFirst();

employee.ifPresentOrElse(
    e -> System.out.println(e.name()),
    () -> System.out.println("Employee not found")
);
```

**Why:** `Optional` explicitly represents that a matching employee may
not exist.

------------------------------------------------------------------------

## Q19 --- Missing employee ID 999

``` java
String name = employees.stream()
    .filter(e -> e.id() == 999)
    .findFirst()
    .map(Employee::name)
    .orElse("Employee not found");

System.out.println(name);
```

**Why:** `map()` transforms the value only when present; `orElse()`
safely handles absence.

------------------------------------------------------------------------

## Q20 --- Any salary over 170,000

``` java
boolean exists = employees.stream()
    .anyMatch(e -> e.salary() > 170_000);
```

**Why:** `anyMatch()` short-circuits as soon as the first match is
found.

------------------------------------------------------------------------

## Q21 --- Are all employees active?

``` java
boolean allActive = employees.stream()
    .allMatch(Employee::active);
```

**Why:** `allMatch()` communicates the requirement directly and
short-circuits on the first `false`.

------------------------------------------------------------------------

## Q22 --- Nobody younger than 18

``` java
boolean nobodyUnder18 = employees.stream()
    .noneMatch(e -> e.age() < 18);
```

**Why:** `noneMatch()` is clearer than negating `anyMatch()`.

------------------------------------------------------------------------

## Q23 --- Count IT employees

``` java
long count = employees.stream()
    .filter(e -> "IT".equals(e.department()))
    .count();
```

**Why:** `count()` avoids constructing an unnecessary intermediate list.

------------------------------------------------------------------------

# Level 5 --- Min, max, average and reduce

## Q24 --- Highest-paid employee

``` java
Optional<Employee> highestPaid = employees.stream()
    .max(Comparator.comparingDouble(Employee::salary));
```

**Why:** `max()` is O(n) and does not sort the whole collection just to
obtain one employee.

------------------------------------------------------------------------

## Q25 --- Lowest-paid employee

``` java
Optional<Employee> lowestPaid = employees.stream()
    .min(Comparator.comparingDouble(Employee::salary));
```

**Why:** Same principle as Q24: use the operation that directly matches
the requirement.

------------------------------------------------------------------------

## Q26 --- Average salary

``` java
double averageSalary = employees.stream()
    .mapToDouble(Employee::salary) // Primitive stream avoids Double boxing.
    .average()
    .orElse(0.0);                  // Safe value when the list is empty.
```

**Why:** `mapToDouble()` provides numeric aggregation operations
directly.

------------------------------------------------------------------------

## Q27 --- Total IT salary

Preferred:

``` java
double total = employees.stream()
    .filter(e -> "IT".equals(e.department()))
    .mapToDouble(Employee::salary)
    .sum();
```

Using `reduce()` for practice:

``` java
double totalWithReduce = employees.stream()
    .filter(e -> "IT".equals(e.department()))
    .map(Employee::salary)
    .reduce(0.0, Double::sum);
```

**Why:** `sum()` is the clearer production choice for primitive numeric
addition. `reduce()` is useful for learning general accumulation.

------------------------------------------------------------------------

## Q28 --- Oldest employee

``` java
Optional<Employee> oldest = employees.stream()
    .max(Comparator.comparingInt(Employee::age));
```

**Why:** No full sort is required to find one maximum.

------------------------------------------------------------------------

# Level 6 --- `groupingBy()`

## Q29 --- Employees by department

``` java
Map<String, List<Employee>> byDepartment = employees.stream()
    .collect(Collectors.groupingBy(Employee::department));
```

**Why:** `groupingBy()` is designed for one-to-many classification.

------------------------------------------------------------------------

## Q30 --- Employee count by department

``` java
Map<String, Long> counts = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::department,
        Collectors.counting() // Downstream collector counts each group.
    ));
```

**Why:** Count directly during grouping instead of first creating
grouped lists and then traversing them again.

------------------------------------------------------------------------

## Q31 --- Average salary by department

``` java
Map<String, Double> averages = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::department,
        Collectors.averagingDouble(Employee::salary)
    ));
```

**Why:** The downstream collector performs aggregation per group in one
collection operation.

------------------------------------------------------------------------

## Q32 --- Highest-paid employee per department

``` java
Map<String, Optional<Employee>> highestByDepartment = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::department,
        Collectors.maxBy(
            Comparator.comparingDouble(Employee::salary)
        )
    ));
```

Cleaner result without `Optional` values:

``` java
Map<String, Employee> highestByDepartment = employees.stream()
    .collect(Collectors.toMap(
        Employee::department,
        Function.identity(),
        (a, b) -> a.salary() >= b.salary() ? a : b
    ));
```

**Why:** The second version keeps the better employee whenever the same
department key appears, so the resulting map contains `Employee`
directly.

------------------------------------------------------------------------

## Q33 --- Employee names grouped by city

``` java
Map<String, List<String>> namesByCity = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::city,
        Collectors.mapping(Employee::name, Collectors.toList())
    ));
```

**Why:** `mapping()` transforms each employee inside its group without
requiring a second pass.

------------------------------------------------------------------------

# Level 7 --- `toMap()` and `partitioningBy()`

## Q34 --- Employee lookup by ID

``` java
Map<Integer, Employee> byId = employees.stream()
    .collect(Collectors.toMap(
        Employee::id,
        Function.identity()
    ));
```

**Why:** `Function.identity()` means "use the original employee as the
map value."

------------------------------------------------------------------------

## Q35 --- Name to salary

If names are guaranteed unique:

``` java
Map<String, Double> salaryByName = employees.stream()
    .collect(Collectors.toMap(
        Employee::name,
        Employee::salary
    ));
```

Safer production version:

``` java
Map<String, Double> salaryByName = employees.stream()
    .collect(Collectors.toMap(
        Employee::name,
        Employee::salary,
        (existing, replacement) -> existing // Explicit duplicate policy.
    ));
```

**Why:** `toMap()` throws on duplicate keys unless a merge function is
supplied.

------------------------------------------------------------------------

## Q36 --- Active vs inactive employees

``` java
Map<Boolean, List<Employee>> partitioned = employees.stream()
    .collect(Collectors.partitioningBy(Employee::active));
```

**Why:** `partitioningBy()` is ideal when the classifier is strictly
boolean.

------------------------------------------------------------------------

# Level 8 --- `flatMap()` and `distinct()`

## Q37 --- Flatten all skills

``` java
List<String> skills = employees.stream()
    .flatMap(e -> e.skills().stream()) // List<List<String>> conceptually becomes Stream<String>.
    .toList();
```

**Why:** `flatMap()` is used when each source object contains another
collection that should become one stream.

------------------------------------------------------------------------

## Q38 --- Unique skills

``` java
List<String> uniqueSkills = employees.stream()
    .flatMap(e -> e.skills().stream())
    .distinct() // Remove duplicate skill strings.
    .sorted()   // Stable/readable output; optional for the requirement.
    .toList();
```

**Why:** `distinct()` uses equality to remove duplicates. Sorting is
added only for predictable display.

------------------------------------------------------------------------

## Q39 --- Java developers

``` java
List<Employee> javaDevelopers = employees.stream()
    .filter(e -> e.skills().contains("Java"))
    .toList();
```

**Why:** We need employees, not individual skills, so filter the
employee by whether its skill collection contains Java.

------------------------------------------------------------------------

## Q40 --- Skill frequency

``` java
Map<String, Long> skillFrequency = employees.stream()
    .flatMap(e -> e.skills().stream())
    .collect(Collectors.groupingBy(
        Function.identity(),
        Collectors.counting()
    ));
```

**Why:** Flatten first, then group identical skill strings and count
them.

------------------------------------------------------------------------

## Q41 --- Most common skill

``` java
Optional<Map.Entry<String, Long>> mostCommonSkill =
    employees.stream()
        .flatMap(e -> e.skills().stream())
        .collect(Collectors.groupingBy(
            Function.identity(),
            Collectors.counting()
        ))
        .entrySet()
        .stream()
        .max(Map.Entry.comparingByValue());
```

**Why:** First build frequencies, then find the largest count.
`Optional` handles an empty employee/skill dataset safely.

------------------------------------------------------------------------

# Level 9 --- Spring Boot-style requirements

## Q42 --- Search endpoint

``` java
List<String> result = employees.stream()
    .filter(Employee::active)
    .filter(e -> "IT".equals(e.department()))
    .filter(e -> e.salary() >= 130_000)
    .sorted(Comparator.comparingDouble(Employee::salary).reversed())
    .map(Employee::name)
    .toList();
```

**Why:** Filter before sorting so fewer elements need to be sorted; map
after sorting because the comparator needs employee salary.

> In a real database-backed API, push these filters and sorting into
> JPA/SQL when possible.

------------------------------------------------------------------------

## Q43 --- Dashboard statistics

For only 30 employees, several stream operations are perfectly
acceptable and very readable:

``` java
long totalEmployees = employees.size();

long activeEmployees = employees.stream()
    .filter(Employee::active)
    .count();

long inactiveEmployees = totalEmployees - activeEmployees;

DoubleSummaryStatistics salaryStats = employees.stream()
    .mapToDouble(Employee::salary)
    .summaryStatistics(); // Calculates count/sum/min/max/average together.

System.out.println("Total employees: " + totalEmployees);
System.out.println("Active employees: " + activeEmployees);
System.out.println("Inactive employees: " + inactiveEmployees);
System.out.println("Average salary: " + salaryStats.getAverage());
System.out.println("Highest salary: " + salaryStats.getMax());
System.out.println("Lowest salary: " + salaryStats.getMin());
```

**Why:** `summaryStatistics()` obtains several salary metrics in one
traversal instead of separate `average()`, `max()`, and `min()` streams.

------------------------------------------------------------------------

## Q44 --- Department API response

Create a DTO/record rather than returning loosely related maps:

``` java
record DepartmentSummary(
    long employeeCount,
    long activeCount,
    double averageSalary
) {}
```

Readable solution:

``` java
Map<String, DepartmentSummary> result = employees.stream()
    .collect(Collectors.groupingBy(Employee::department))
    .entrySet()
    .stream()
    .collect(Collectors.toMap(
        Map.Entry::getKey,
        entry -> {
            List<Employee> group = entry.getValue();

            long activeCount = group.stream()
                .filter(Employee::active)
                .count();

            double averageSalary = group.stream()
                .mapToDouble(Employee::salary)
                .average()
                .orElse(0.0);

            return new DepartmentSummary(
                group.size(),
                activeCount,
                averageSalary
            );
        }
    ));
```

**Why:** A DTO gives the API response a clear contract. For a huge
dataset, aggregate this in SQL or use a custom collector rather than
retaining every grouped employee in memory.

------------------------------------------------------------------------

## Q45 --- Case-insensitive partial name search

``` java
String search = "silva";
String normalizedSearch = search.toLowerCase(Locale.ROOT);

List<Employee> result = employees.stream()
    .filter(e -> e.name() != null) // Defensive if production data may contain null names.
    .filter(e -> e.name()
        .toLowerCase(Locale.ROOT)
        .contains(normalizedSearch))
    .toList();
```

**Why:** Normalize the search term once outside the stream instead of
recalculating it for every employee. `Locale.ROOT` avoids
locale-specific case surprises.

------------------------------------------------------------------------

## Q46 --- Active employees in salary range

``` java
double minSalary = 100_000;
double maxSalary = 150_000;

List<Employee> result = employees.stream()
    .filter(Employee::active)
    .filter(e -> e.salary() >= minSalary && e.salary() <= maxSalary)
    .sorted(Comparator.comparingDouble(Employee::salary).reversed())
    .toList();
```

**Why:** Filter first to reduce the number of records that need sorting.

------------------------------------------------------------------------

## Q47 --- Dynamic optional filters

``` java
String department = "IT";
String city = null;
Double minSalary = 120_000.0;
Boolean active = true;

List<Employee> result = employees.stream()
    .filter(e -> department == null || department.equals(e.department()))
    .filter(e -> city == null || city.equals(e.city()))
    .filter(e -> minSalary == null || e.salary() >= minSalary)
    .filter(e -> active == null || e.active() == active)
    .toList();
```

**Why:** Each predicate becomes automatically "disabled" when its
parameter is null. This is easy to understand for a small in-memory
filter.

> For a real Spring Data API with many optional database filters,
> consider Specifications, QueryDSL, or a purpose-built repository query
> so filtering happens in the database.

------------------------------------------------------------------------

# Level 10 --- Senior / interview challenges

## Q48 --- Second-highest distinct salary

``` java
OptionalDouble secondHighest = employees.stream()
    .mapToDouble(Employee::salary)
    .distinct()     // Important: second-highest distinct salary.
    .boxed()
    .sorted(Comparator.reverseOrder())
    .skip(1)
    .mapToDouble(Double::doubleValue)
    .findFirst();
```

Simpler to read:

``` java
Optional<Double> secondHighest = employees.stream()
    .map(Employee::salary)
    .distinct()
    .sorted(Comparator.reverseOrder())
    .skip(1)
    .findFirst();
```

**Why:** `distinct()` prevents two employees with the same top salary
from incorrectly making that salary the "second highest." For very large
data, SQL is usually the better place to calculate this.

------------------------------------------------------------------------

## Q49 --- Highest-paid employee per city

``` java
Map<String, Employee> topByCity = employees.stream()
    .collect(Collectors.toMap(
        Employee::city,
        Function.identity(),
        (a, b) -> a.salary() >= b.salary() ? a : b
    ));
```

**Why:** `toMap()` with a merge function can keep the better employee as
duplicate city keys arrive.

------------------------------------------------------------------------

## Q50 --- Department salary bill sorted descending

``` java
Map<String, Double> totals = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::department,
        Collectors.summingDouble(Employee::salary)
    ));

LinkedHashMap<String, Double> sortedTotals = totals.entrySet()
    .stream()
    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
    .collect(Collectors.toMap(
        Map.Entry::getKey,
        Map.Entry::getValue,
        (a, b) -> a,
        LinkedHashMap::new // Preserve the stream's sorted encounter order.
    ));
```

**Why:** A normal `HashMap` does not promise iteration order;
`LinkedHashMap` preserves the sorted order.

------------------------------------------------------------------------

## Q51 --- Skill to employees

``` java
Map<String, List<Employee>> employeesBySkill = employees.stream()
    .flatMap(employee ->
        employee.skills().stream()
            .map(skill -> Map.entry(skill, employee))
    )
    .collect(Collectors.groupingBy(
        Map.Entry::getKey,
        Collectors.mapping(
            Map.Entry::getValue,
            Collectors.toList()
        )
    ));
```

**Why:** Each employee-skill relationship is temporarily represented as
an entry, making it straightforward to regroup by skill.

------------------------------------------------------------------------

## Q52 --- Employees with Java AND Spring Boot

``` java
List<Employee> result = employees.stream()
    .filter(e -> e.skills().containsAll(
        Set.of("Java", "Spring Boot")
    ))
    .toList();
```

**Why:** `containsAll()` clearly expresses that both required skills
must be present.

------------------------------------------------------------------------

## Q53 --- Salary bands

``` java
enum SalaryBand {
    LOW, MEDIUM, HIGH
}
```

``` java
Map<SalaryBand, List<Employee>> bySalaryBand = employees.stream()
    .collect(Collectors.groupingBy(e -> {
        if (e.salary() < 100_000) {
            return SalaryBand.LOW;
        }
        if (e.salary() < 150_000) {
            return SalaryBand.MEDIUM;
        }
        return SalaryBand.HIGH;
    }));
```

**Why:** Prefer an enum over magic strings such as `"LOW"` and `"HIGH"`
when the categories are part of your domain model.

------------------------------------------------------------------------

## Q54 --- Duplicate-safe map by name

Keep the **latest encountered employee**:

``` java
Map<String, Employee> byName = employees.stream()
    .collect(Collectors.toMap(
        Employee::name,
        Function.identity(),
        (existing, replacement) -> replacement
    ));
```

Or keep the **first**:

``` java
Map<String, Employee> byName = employees.stream()
    .collect(Collectors.toMap(
        Employee::name,
        Function.identity(),
        (existing, replacement) -> existing
    ));
```

**Why:** The important production decision is not just avoiding the
exception; the merge policy must match the business rule and should be
documented.

------------------------------------------------------------------------

## Q55 --- Top 3 active employees from each department

``` java
Map<String, List<Employee>> topThreeByDepartment = employees.stream()
    .filter(Employee::active) // Remove inactive employees before grouping.
    .collect(Collectors.groupingBy(
        Employee::department,
        Collectors.collectingAndThen(
            Collectors.toList(),
            group -> group.stream()
                .sorted(
                    Comparator.comparingDouble(Employee::salary)
                        .reversed()
                )
                .limit(3)
                .toList()
        )
    ));
```

**Why:** Group first, then sort only within each department. This
directly represents "top three per group."

> For a very large database table, use a database/window-function
> solution instead of loading all employees into memory.

------------------------------------------------------------------------

## Q56 --- Employee DTO transformation

``` java
record EmployeeSummary(
    int id,
    String name,
    String department,
    double salary
) {}
```

``` java
List<EmployeeSummary> result = employees.stream()
    .map(e -> new EmployeeSummary(
        e.id(),
        e.name(),
        e.department(),
        e.salary()
    ))
    .toList();
```

**Why:** Do not expose your persistence/domain object automatically when
an API only needs a subset of fields. A DTO creates a stable response
contract.

------------------------------------------------------------------------

## Q57 --- Combined department report

Define a clear report model:

``` java
record DepartmentReport(
    String department,
    long employeeCount,
    long activeCount,
    double averageAge,
    double averageSalary,
    String highestPaidEmployee,
    Set<String> skills
) {}
```

Readable solution:

``` java
List<DepartmentReport> reports = employees.stream()
    .collect(Collectors.groupingBy(Employee::department))
    .entrySet()
    .stream()
    .map(entry -> {
        String department = entry.getKey();
        List<Employee> group = entry.getValue();

        long activeCount = group.stream()
            .filter(Employee::active)
            .count();

        IntSummaryStatistics ageStats = group.stream()
            .mapToInt(Employee::age)
            .summaryStatistics();

        DoubleSummaryStatistics salaryStats = group.stream()
            .mapToDouble(Employee::salary)
            .summaryStatistics();

        String highestPaid = group.stream()
            .max(Comparator.comparingDouble(Employee::salary))
            .map(Employee::name)
            .orElse("N/A");

        Set<String> skills = group.stream()
            .flatMap(e -> e.skills().stream())
            .collect(Collectors.toCollection(TreeSet::new));
            // TreeSet removes duplicates and keeps output sorted.

        return new DepartmentReport(
            department,
            group.size(),
            activeCount,
            ageStats.getAverage(),
            salaryStats.getAverage(),
            highestPaid,
            skills
        );
    })
    .sorted(Comparator.comparing(DepartmentReport::department))
    .toList();
```

**Why:** The report has several independent metrics, so forcing
everything into one enormous collector would hurt readability.
`summaryStatistics()` is useful when several numeric statistics are
required. For production reporting over a large database, perform
aggregation in SQL where practical.

------------------------------------------------------------------------

# Best-practice patterns to remember

### 1. Need one maximum/minimum? Do not sort everything.

Prefer:

``` java
employees.stream()
    .max(Comparator.comparingDouble(Employee::salary));
```

over:

``` java
employees.stream()
    .sorted(Comparator.comparingDouble(Employee::salary).reversed())
    .findFirst();
```

`max()` communicates the requirement better and avoids a full O(n log n)
sort.

### 2. Need only a count? Do not create a list first.

Prefer:

``` java
long count = employees.stream()
    .filter(Employee::active)
    .count();
```

instead of collecting and then calling `.size()`.

### 3. Filter before expensive work.

Prefer:

``` java
employees.stream()
    .filter(Employee::active)
    .sorted(...)
```

rather than sorting records you will discard afterward.

### 4. Use primitive streams for numeric calculations.

``` java
employees.stream()
    .mapToDouble(Employee::salary)
    .average();
```

This avoids unnecessary boxing and provides `sum`, `average`,
`summaryStatistics`, etc.

### 5. Be careful with `toMap()`.

This can throw when keys duplicate:

``` java
Collectors.toMap(Employee::name, Function.identity())
```

If duplicates are possible, define the business rule:

``` java
Collectors.toMap(
    Employee::name,
    Function.identity(),
    (existing, replacement) -> existing
)
```

### 6. Do not call `Optional.get()` blindly.

Prefer:

``` java
optional.orElse(...)
optional.orElseThrow(...)
optional.ifPresent(...)
optional.map(...)
```

depending on the business requirement.

### 7. `Stream.toList()` is unmodifiable.

If you need to add/remove elements afterward:

``` java
List<Employee> mutable = employees.stream()
    .filter(Employee::active)
    .collect(Collectors.toCollection(ArrayList::new));
```

### 8. Streams are not automatically better than loops.

A stream is excellent for filtering, mapping, grouping and aggregation.
A normal loop can be better when: - several unrelated values must be
updated in one pass; - the stream pipeline becomes difficult to
understand; - you need complex stateful logic.

Readable code beats clever code.

### 9. Do not use Java Streams to replace the database.

Avoid:

``` java
repository.findAll()
    .stream()
    .filter(...)
```

for huge tables when SQL/JPA can perform the filter.

Prefer repository/database filtering, pagination, sorting and
aggregation whenever appropriate.

### 10. Spring Boot service-layer mental model

A common pipeline is:

``` text
Repository / external API
        ↓
List<Entity>
        ↓
filter business rules
        ↓
sort / group / aggregate
        ↓
map Entity -> DTO
        ↓
Controller response
```

But when filtering can happen efficiently in the database:

``` text
Controller
    ↓
Service
    ↓
Repository query / Specification / QueryDSL
    ↓
only required rows
    ↓
DTO mapping / small business transformations
    ↓
Response
```

That second approach usually scales much better.

------------------------------------------------------------------------

# Suggested practice method

First solve a question yourself. Then compare your solution with this
guide using three checks:

1.  **Correctness** --- does it return the required result?
2.  **Readability** --- can another developer understand the business
    rule quickly?
3.  **Efficiency** --- are you doing unnecessary sorting, collecting,
    boxing, or repeated database fetching?

Do not worry if your answer is different from this file. Java often has
several valid implementations. The best implementation depends on data
size, source (database vs in-memory), mutability requirements,
nullability, and the surrounding business rules.
