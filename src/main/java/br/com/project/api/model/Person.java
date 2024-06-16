package br.com.project.api.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@MappedSuperclass
public class Person {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private LocalDate dateBirth;

  // Getters e Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getDateBirth() {
    return dateBirth;
  }

  public void setDateBirth(LocalDate dateBirth) {
    this.dateBirth = dateBirth;
  }
}
