package fr.salut.test2.explication;

public class Animal {

  private String name;
  private AnimalType type;

  public Animal(String name, AnimalType type) {
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public AnimalType getType() {
    return type;
  }
}
