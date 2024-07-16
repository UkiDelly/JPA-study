package delly.domain.cascade;

import jakarta.persistence.*;

@Entity
public class Child {
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private Long id;

  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Parent parent;

  // Constructor

  protected Child() {
  }

  public Child(String name) {
    this.name = name;
  }


  // Getter and Setter

  public Parent getParent() {
    return parent;
  }

  public void setParent(Parent parent) {
    this.parent = parent;
  }

  public Long getId() {
    return id;
  }


}