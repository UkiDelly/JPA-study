package entity

import jakarta.persistence.*

@Entity
@Table(name = "member")
class Member(
  name: String,
) {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  val id: Long? = null
  
  @Column(name = "name", nullable = false)
  var name: String = name
  
  
  override fun toString() = "Member(id=$id, name='$name')"
}