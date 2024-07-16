package delly.domain.practice;

import jakarta.persistence.*;

@Entity(name = "Delivery")
public class Delivery extends BaseEntity {

  @Id
  @GeneratedValue
  private Long id;

  private String city;

  private String street;

  @Column(name = "zip_code")
  private String zipCode;

  @Enumerated(EnumType.STRING)
  private DeliveryStatus status;

}
