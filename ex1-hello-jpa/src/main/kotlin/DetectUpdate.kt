import entity.Member
import jakarta.persistence.Persistence

fun main() {
  val emf = Persistence.createEntityManagerFactory("hello")
  val em = emf.createEntityManager()
  val tx = em.transaction
  
  try {
    tx.begin()
    
    val member1 = Member("A", 10)
    em.persist(member1)
    
    var findMember = em.find(Member::class.java, 1L)
    println("========== Before ==========")
    println(findMember)
    
    //? 값을 바꾸고 em.persist()를 호출하지 않아도 업데이트가 된다.
    // JPA가 관리하는 영속성 컨텍스트에서 관리하고 있는 객체의 값을 바꾸면 자동으로 업데이트가 된다.
    // JPA가 변화를 감지하고 update 쿼리를 생성한다.
    findMember.name = "B"
    println("========== After ==========")
    findMember = em.find(Member::class.java, 1L)
    println(findMember)
    
    // commit 시점에 update 쿼리가 나간다.
    tx.commit()
  } catch (e: Exception) {
    tx.rollback()
  } finally {
    em.close()
    emf.close()
  }
}