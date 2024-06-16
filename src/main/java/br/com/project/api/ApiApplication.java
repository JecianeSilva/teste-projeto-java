package br.com.project.api;

import br.com.project.api.model.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
public class ApiApplication implements CommandLineRunner {

  @PersistenceContext
  private EntityManager entityManager;

  public static void main(String[] args) {
    SpringApplication.run(ApiApplication.class, args);
  }

  @Override
  @Transactional
  public void run(String... args) throws Exception {
    // Removendo Teste
    deleteEmployeeByName("Example");

    System.out.println("3.1 - Inserir todos os funcionários");
    insertEmployees();

    System.out.println("\n\n3.2 - Remover o funcionário: João");
    deleteEmployeeByName("João");

    System.out.println("3.3 - Imprimir todos os funcionários com todas suas informações");
    printAllEmployees();

    System.out.println("\n\n3.4 - Os funcionários receberam 10% de aumento de salário");
    increaseSalaries(new BigDecimal("0.10"));
    printAllEmployees();

    System.out.println("\n\n3.5 - Agrupar os funcionários por função em um MAP");
    Map<String, List<Employee>> employeesByRole = groupByFunction();
    printGroupedByFunction(employeesByRole);

    System.out.println("\n\n3.8 - Imprimir os funcionários que fazem aniversário no mês 10 e 12");
    System.out.println("Mês 10");
    printEmployeesByBirthMonth(10);
    System.out.println("Mês 12");
    printEmployeesByBirthMonth(12);

    System.out.println("\n\n3.9 - Imprimir o funcionário com a maior idade2");
    Employee oldestEmployee = getOldestEmployee();
    printOldestEmployee(oldestEmployee);

    System.out.println("\n\n3.10 - Imprimir a lista de funcionários por ordem alfabética");
    List<Employee> employeesSortedByName = getEmployeesSortedByName();
    printEmployeesSortedByName(employeesSortedByName);

    System.out.println("\n\n3.11 - Imprimir o total dos salários dos funcionários");
    BigDecimal totalSalaries = getTotalSalaries();
    printTotalSalaries(totalSalaries);

    System.out.println("\n\n3.12 - Imprimir quantos salários mínimos ganha cada funcionário");
    BigDecimal minimum = new BigDecimal("1412.00");
    Map<String, BigDecimal> salaryInMinimum = getSalaryInMinimum(minimum);
    printSalaryInMinimum(salaryInMinimum);
  }

  private void insertEmployees() {
    entityManager.persist(new Employee("Maria", LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"));
    entityManager.persist(new Employee("João", LocalDate.of(1990, 5, 12), new BigDecimal("2284.38"), "Operador"));
    entityManager.persist(new Employee("Caio", LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"), "Coordenador"));
    entityManager.persist(new Employee("Miguel", LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"));
    entityManager.persist(new Employee("Alice", LocalDate.of(1995, 1, 5), new BigDecimal("2234.68"), "Recepcionista"));
    entityManager.persist(new Employee("Heitor", LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"));
    entityManager.persist(new Employee("Arthur", LocalDate.of(1993, 3, 31), new BigDecimal("4071.84"), "Contador"));
    entityManager.persist(new Employee("Laura", LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"), "Gerente"));
    entityManager.persist(new Employee("Heloisa", LocalDate.of(2003, 5, 24), new BigDecimal("1606.85"), "Eletricista"));
    entityManager.persist(new Employee("Helena", LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"), "Gerente"));
    printAllEmployees();
    System.out.println("Finalizado");
  }

  private void deleteEmployeeByName(String name) {
    List<Employee> employees = entityManager.createQuery("FROM Employee WHERE name = :name", Employee.class)
        .setParameter("name", name)
        .getResultList();
    employees.forEach(entityManager::remove);

    System.out.println(name + "foi removido com sucesso!\n\n");
  }

  private void printAllEmployees() {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    List<Employee> employees = entityManager.createQuery("FROM Employee", Employee.class).getResultList();
    employees.forEach(employee -> {
      String formattedDate = employee.getDateBirth().format(dateFormatter);
      String formattedSalary = String.format("%,.2f", employee.getSalary());
      System.out.println("Nome: " + employee.getName() + ", Data de Nascimento: " + formattedDate +
          ", Salário: " + formattedSalary + ", Função: " + employee.getRole());
    });
  }

  private void increaseSalaries(BigDecimal percentage) {
    List<Employee> employees = entityManager.createQuery("FROM Employee", Employee.class).getResultList();
    employees.forEach(employee -> {
      BigDecimal newSalary = employee.getSalary().multiply(BigDecimal.ONE.add(percentage));
      employee.setSalary(newSalary);
      entityManager.merge(employee);
    });
  }

  private Map<String, List<Employee>> groupByFunction() {
    List<Employee> employees = entityManager.createQuery("FROM Employee", Employee.class).getResultList();
    return employees.stream().collect(Collectors.groupingBy(Employee::getRole));
  }

  private void printGroupedByFunction(Map<String, List<Employee>> employeesByRole) {
    employeesByRole.forEach((role, employees) -> {
      System.out.println("Função: " + role);
      employees.forEach(employee -> System.out.println(" - " + employee.getName()));
    });
  }

  private void printEmployeesByBirthMonth(int month) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    List<Employee> employees = entityManager
        .createQuery("FROM Employee WHERE MONTH(dateBirth) = :month", Employee.class)
        .setParameter("month", month)
        .getResultList();
    employees.forEach(employee -> {
      String formattedDate = employee.getDateBirth().format(dateFormatter);
      String formattedSalary = String.format("%,.2f", employee.getSalary());
      System.out.println("Nome: " + employee.getName() + ", Data de Nascimento: " + formattedDate +
          ", Salário: " + formattedSalary + ", Função: " + employee.getRole());
    });
  }

  private Employee getOldestEmployee() {
    List<Employee> employees = entityManager.createQuery("FROM Employee", Employee.class).getResultList();
    return employees.stream().min(Comparator.comparing(Employee::getDateBirth)).orElseThrow();
  }

  private void printOldestEmployee(Employee employee) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String formattedDate = employee.getDateBirth().format(dateFormatter);
    String formattedSalary = String.format("%,.2f", employee.getSalary());
    System.out.println("Funcionário mais velho: " + employee.getName() + ", Data de Nascimento: " + formattedDate +
        ", Salário: " + formattedSalary + ", Função: " + employee.getRole());
  }

  private List<Employee> getEmployeesSortedByName() {
    List<Employee> employees = entityManager.createQuery("FROM Employee", Employee.class).getResultList();
    return employees.stream().sorted(Comparator.comparing(Employee::getName)).collect(Collectors.toList());
  }

  private void printEmployeesSortedByName(List<Employee> employees) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    employees.forEach(employee -> {
      String formattedDate = employee.getDateBirth().format(dateFormatter);
      String formattedSalary = String.format("%,.2f", employee.getSalary());
      System.out.println("Nome: " + employee.getName() + ", Data de Nascimento: " + formattedDate +
          ", Salário: " + formattedSalary + ", Função: " + employee.getRole());
    });
  }

  private BigDecimal getTotalSalaries() {
    List<Employee> employees = entityManager.createQuery("FROM Employee", Employee.class).getResultList();
    return employees.stream().map(Employee::getSalary).reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private void printTotalSalaries(BigDecimal totalSalaries) {
    System.out.println("Total dos salários: " + String.format("%,.2f", totalSalaries));
  }

  private Map<String, BigDecimal> getSalaryInMinimum(BigDecimal minimum) {
    List<Employee> employees = entityManager.createQuery("FROM Employee", Employee.class).getResultList();

    return employees.stream()
        .collect(Collectors.toMap(Employee::getName,
            employee -> employee.getSalary().divide(minimum, RoundingMode.HALF_DOWN)));
  }

  private void printSalaryInMinimum(Map<String, BigDecimal> salaryInMinimum) {
    salaryInMinimum.forEach((name, salary) -> {
      System.out.println(name + " ganha " + salary.intValue() + " salários mínimos.");
    });
  }
}
