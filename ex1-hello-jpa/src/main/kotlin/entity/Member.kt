package entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "member")
@SequenceGenerator(
  name = "MEMBER_SEQ_GENERATOR",
  sequenceName = "MEMBER_SEQ",
  initialValue = 1,
)
class Member(
  name: String,
  age: Int,
  roleType: RoleType = RoleType.USER,
) {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
  @Column(name = "id", nullable = false)
  val id: Long? = null
  
  @Column(name = "name", nullable = false)
  var name: String = name
  
  @Column
  var age: Int = age
  
  @Enumerated(EnumType.STRING)
  @Column(name = "role", length = 10)
  var roleType: RoleType = roleType
  
  @Column(name = "created_at", updatable = false)
  val createdAt: LocalDateTime = LocalDateTime.now()
  
  
  @Column(name = "updated_at")
  var updatedAt: LocalDateTime = LocalDateTime.now()
  
  @Lob
  var description: String? = null
  
  
  enum class RoleType {
    ADMIN, USER
  }
  
  override fun toString() = "Member(id=$id, name='$name')"
}