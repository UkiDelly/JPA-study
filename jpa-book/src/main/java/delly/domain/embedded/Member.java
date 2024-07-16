package delly.domain.embedded;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Member {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @Embedded
  private Period workPeriod;

  @Embedded
  private Address homeAddress;

  //@Embedded
  //@AttributeOverrides({
  //  @AttributeOverride(name = "city", column = @Column(name = "work_city")),
  //  @AttributeOverride(name = "street", column = @Column(name = "work_street")),
  //  @AttributeOverride(name = "zipCode", column = @Column(name = "work_zipcode"))
  //})
  //private Address workAddress;

  @ElementCollection
  @CollectionTable(name = "Favorite_Foods", joinColumns = @JoinColumn(name = "member_id"))
  @Column(name = "food_name")
  private Set<String> favoritesFoods = new HashSet<>();

  @ElementCollection
  @CollectionTable(name = "Address_History", joinColumns = @JoinColumn(name = "member_id"))
  private List<Address> addressHistory = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Period getWorkPeriod() {
    return workPeriod;
  }

  public void setWorkPeriod(Period workPeriod) {
    this.workPeriod = workPeriod;
  }


  public Address getHomeAddress() {
    return homeAddress;
  }

  public void setHomeAddress(Address homeAddress) {
    this.homeAddress = homeAddress;
  }

  public Set<String> getFavoritesFoods() {
    return favoritesFoods;
  }

  public List<Address> getAddressHistory() {
    return addressHistory;
  }

  //public Address getWorkAddress() {
  //  return workAddress;
  //}
  //
  //public void setWorkAddress(Address workAddress) {
  //  this.workAddress = workAddress;
  //}
}
