package delly.domain.practice;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "Category")
public class Category extends BaseEntity {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Category parent;

  @OneToMany(mappedBy = "parent")
  private List<Category> child = new ArrayList<>();

  // 중계 테이블을 생성하기 위해 @JoinTable 어노테이션을 추가
  // joinColumn은 현재 엔티티(Category)를 참조하는 외래 키, inverseJoinColumn은 반대편 엔티티(Item)를 참조하는 외래 키를 넣는다.
  @ManyToMany
  @JoinTable(name = "Category_Item", joinColumns = @JoinColumn(name = "category_id"), inverseJoinColumns = @JoinColumn(name = "item_id"))
  private List<Item> items = new ArrayList<>();
}
