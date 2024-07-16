package delly.domain.cascade;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Parent {


  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<Child> children = new ArrayList<>();

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  // Constructor
  protected Parent() {
  }

  public Parent(String name) {
    this.name = name;
  }

  // Getter and Setter

  public List<Child> getChildren() {
    return children;
  }

  public void addChild(Child child) {
    children.add(child);
    child.setParent(this);
  }

  public Long getId() {
    return id;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
