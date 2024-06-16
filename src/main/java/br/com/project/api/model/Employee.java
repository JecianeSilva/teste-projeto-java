package br.com.project.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Employee")
public class Employee extends Person {
  private BigDecimal salary;
  private String role;

  public Employee() {
  }

  public Employee(String name, LocalDate dateBirth, BigDecimal salary, String role) {
    setName(name);
    setDateBirth(dateBirth);
    this.salary = salary;
    this.role = role;
  }

  // Getters e Setters
  public BigDecimal getSalary() {
    return salary;
  }

  public void setSalary(BigDecimal salary) {
    this.salary = salary;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
