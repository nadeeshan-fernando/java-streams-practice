# Java Collections & Streams Practice Lab

A hands-on dataset for practicing the kind of collection processing commonly used inside Java/Spring Boot service layers.

**Rules:** Try each problem before looking anything up. There are deliberately **no solutions** in this project. Sample outputs show the required result, not the implementation.

## Run

Compile:

```bash
javac src/*.java
```

Run:

```bash
java -cp src Main
```

Or copy the three Java files into IntelliJ, Replit, JDoodle, or OneCompiler.

---

# Level 1 — filter()

### Q1 — IT employees
Return all employees in `IT`.

Sample:
```text
John Silva
Mike Fernando
David Dias
...
```

### Q2 — High salary
Return employees earning more than 120,000.

Sample:
```text
Mike Fernando - 150000.0
Chris De Silva - 130000.0
...
```

### Q3 — Young employees
Return employees younger than 30.

### Q4 — Active employees
Return only `active == true` employees.

### Q5 — Multiple conditions
Return employees who are active AND work in IT AND earn at least 130,000.

Sample:
```text
Mike Fernando
Sophia Fernando
James Rodrigo
...
```

### Q6 — City filter
Return employees from Negombo.

### Q7 — OR condition
Return employees working in IT OR Finance.

---

# Level 2 — map()

### Q8 — Names only
Convert `List<Employee>` into `List<String>` containing names.

Sample:
```text
[John Silva, Anna Perera, Mike Fernando, ...]
```

### Q9 — Uppercase
Return all names in uppercase.

Sample:
```text
[JOHN SILVA, ANNA PERERA, MIKE FERNANDO, ...]
```

### Q10 — API-style transformation
Return strings in this format:

```text
1 - John Silva - IT
2 - Anna Perera - HR
```

### Q11 — IT employee names
Filter IT employees and return only their names.

---

# Level 3 — sorted(), limit(), skip()

### Q12 — Salary ascending
Sort all employees from lowest salary to highest.

### Q13 — Salary descending
Sort highest salary to lowest.

Sample beginning:
```text
Sophia Fernando - 175000.0
Lucy Perera - 170000.0
Emily Silva - 160000.0
```

### Q14 — Top 5
Return the five highest-paid employees.

### Q15 — Youngest first
Sort by age ascending.

### Q16 — Multiple-field sorting
Sort by department alphabetically, then salary descending inside each department.

### Q17 — Pagination
Simulate page 2 with page size 5 using `skip()` and `limit()`.

Expected IDs:
```text
[6, 7, 8, 9, 10]
```

---

# Level 4 — find / match / count

### Q18 — Find by ID
Find employee ID `15`.

Sample:
```text
William Perera
```

Use `Optional<Employee>` safely.

### Q19 — Missing employee
Search for ID `999`. Do not throw an exception.

Sample:
```text
Employee not found
```

### Q20 — anyMatch
Does any employee earn over 170,000?

```text
true
```

### Q21 — allMatch
Are all employees active?

```text
false
```

### Q22 — noneMatch
Verify that nobody is younger than 18.

```text
true
```

### Q23 — count
Count employees in IT.

---

# Level 5 — min(), max(), average(), reduce()

### Q24 — Highest salary
Find the highest-paid employee without sorting the entire list.

Sample:
```text
Sophia Fernando - 175000.0
```

### Q25 — Lowest salary
Find the lowest-paid employee.

### Q26 — Average salary
Calculate the average salary of all employees.

### Q27 — IT salary total
Calculate the sum of salaries for IT employees.

Try `mapToDouble()` first, then solve it again using `reduce()`.

### Q28 — Oldest employee
Find the oldest employee.

---

# Level 6 — groupingBy()

### Q29 — Employees by department
Create:

```text
IT -> [John Silva, Mike Fernando, ...]
HR -> [Anna Perera, Emma Rodrigo, ...]
Finance -> [...]
Operations -> [...]
```

Target type:
```text
Map<String, List<Employee>>
```

### Q30 — Department counts
Count employees in each department.

Example shape:
```text
IT -> 10
HR -> 7
Finance -> 7
Operations -> 6
```

### Q31 — Average salary by department
Return:

```text
IT -> <calculated average>
HR -> <calculated average>
Finance -> <calculated average>
Operations -> <calculated average>
```

### Q32 — Highest-paid employee per department
Return one employee for each department.

### Q33 — Names grouped by city
Target:
```text
Map<String, List<String>>
```

---

# Level 7 — toMap() / partitioningBy()

### Q34 — Employee lookup map
Convert employees into:

```text
Map<Integer, Employee>
```

Example:
```text
1 -> John Silva
2 -> Anna Perera
...
```

### Q35 — Name to salary
Create:

```text
Map<String, Double>
```

### Q36 — Active vs inactive
Use `partitioningBy()`.

Expected shape:
```text
true  -> [active employees...]
false -> [inactive employees...]
```

---

# Level 8 — flatMap() and distinct()

Each employee has a `skills` list.

### Q37 — All skills
Return one flattened list containing every skill entry.

### Q38 — Unique skills
Return each skill only once.

Sample:
```text
Java
Spring Boot
SQL
Recruitment
Excel
AWS
Docker
...
```

### Q39 — Java developers
Find employees whose `skills` contains `Java`.

### Q40 — Skill frequency
Count how many employees have each skill.

Desired shape:
```text
Java -> ?
AWS -> ?
Docker -> ?
SQL -> ?
...
```

### Q41 — Most common skill
Determine which skill occurs most frequently.

---

# Level 9 — Real Spring Boot-style requirements

### Q42 — Search endpoint
Imagine:

```http
GET /employees?department=IT&minSalary=130000
```

Return active IT employees earning >= 130000, sorted salary descending.

Return names only.

### Q43 — Dashboard statistics
Generate:
```text
Total employees: 30
Active employees: ?
Inactive employees: ?
Average salary: ?
Highest salary: ?
Lowest salary: ?
```

Try to avoid unnecessary repeated stream processing.

### Q44 — Department API response
Build:

```text
IT
  employeeCount = ?
  activeCount = ?
  averageSalary = ?
```

Repeat for every department.

### Q45 — Search by partial name
Search case-insensitively for `"silva"`.

Sample:
```text
John Silva
Olivia Silva
Emily Silva
...
```

### Q46 — Salary range
Return active employees with salary between 100000 and 150000 inclusive.

Sort salary descending.

### Q47 — Dynamic filters
Pretend these API parameters are optional:

```text
department = "IT"
city = null
minSalary = 120000
active = true
```

Apply only filters whose values are not null.

This is a common service-layer requirement.

---

# Level 10 — Senior / Interview Challenges

### Q48 — Second-highest salary
Find the second-highest **distinct** salary.

Do not assume salaries are unique.

### Q49 — Top earner per city
Create:
```text
Colombo -> Employee
Negombo -> Employee
Kandy -> Employee
Galle -> Employee
```

### Q50 — Department salary bill
Calculate total salary by department and sort departments by total salary descending.

### Q51 — Skill -> employees
Reverse the relationship.

Instead of:
```text
Employee -> [skills]
```

produce:
```text
Java -> [employees]
AWS -> [employees]
SQL -> [employees]
...
```

### Q52 — Employees sharing skills
Find employees who have both `Java` AND `Spring Boot`.

### Q53 — Salary bands
Group employees into:

```text
LOW    -> salary < 100000
MEDIUM -> 100000 to 149999
HIGH   -> >= 150000
```

### Q54 — Duplicate-safe toMap
Imagine employee names are not guaranteed unique.

Create a map by name without crashing if duplicate names occur. Decide and document whether the first or latest employee should win.

### Q55 — Complex business rule
Return the top 3 active employees from each department by salary.

Target:
```text
Map<String, List<Employee>>
```

### Q56 — DTO transformation
Create an `EmployeeSummary` record containing:

```text
id
name
department
salary
```

Convert employees into `List<EmployeeSummary>`.

### Q57 — Combined report
Produce:

```text
Department: IT
Employees: ?
Active: ?
Average Age: ?
Average Salary: ?
Highest Paid: <name>
Skills: [unique skills used by IT employees]
```

Generate the same report for every department.

---

# Bonus — Think Like a Backend Developer

For each problem, ask:

1. Should I return `List<Employee>`, `List<String>`, `Map`, `Optional`, or a DTO?
2. Do I need `filter()` before `map()`?
3. Am I sorting the entire collection unnecessarily?
4. Can `max()`/`min()` solve it more efficiently?
5. Could `toMap()` encounter duplicate keys?
6. Could a value be null in real production data?
7. Am I repeatedly streaming the same large collection?
8. Would this filtering be better done by SQL/JPA rather than in Java?

The last question is especially important in Spring Boot: if the database can efficiently filter 1,000,000 records, don't normally fetch all 1,000,000 just to run `stream().filter()` in memory.

## Suggested order

Do Q1–Q11 first, then Q12–Q28. Once those feel natural, move into `groupingBy`, `toMap`, and `flatMap`. Q42 onward is designed to feel more like actual service-layer work and interviews.

Good luck — and don't add solutions to this project until you've attempted each problem yourself.
