package delly.domain.practice;

import jakarta.persistence.*;

@Entity(name = "OrderItems")
public class OrderItem extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  private Item item;

  @Column(name = "order_price")
  private int orderPrice;

  private int count;

  public Long getId() {
    return id;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public Item getItem() {
    return item;
  }

  public void setItem(Item item) {
    this.item = item;
    item.addOrderItem(this);
  }

  public int getOrderPrice() {
    return orderPrice;
  }

  public void setOrderPrice(int orderPrice) {
    this.orderPrice = orderPrice;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }
}
