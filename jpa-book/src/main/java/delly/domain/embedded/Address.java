package delly.domain.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Address {

  private String city;

  private String street;

  @Column(name = "zip_code")
  private String zipCode;

  protected Address() {
  }

  public Address(String city, String street, String zipCode) {
    this.city = city;
    this.street = street;
    this.zipCode = zipCode;
  }


  public Address copy() {
    return new Address(this.city, this.street, this.zipCode);
  }

  public Address copyWith(String city) {
    return copyWith(city, null, null);
  }

  public Address copyWith(String city, String street) {
    return copyWith(city, street, null);
  }

  public Address copyWith(String city, String street, String zipCode) {
    return new Address(city, street, zipCode);
  }

  public String getCity() {
    return city;
  }


  public String getStreet() {
    return street;
  }


  public String getZipCode() {
    return zipCode;
  }

  @Override
  public int hashCode() {
    return Objects.hash(city, street, zipCode);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Address address = (Address) o;
    return Objects.equals(city, address.city) && Objects.equals(street, address.street) && Objects.equals(zipCode, address.zipCode);
  }
}
