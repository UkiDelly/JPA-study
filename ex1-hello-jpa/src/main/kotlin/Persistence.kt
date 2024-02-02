import entity.Member
import jakarta.persistence.Persistence

fun main() {
  val emf = Persistence.createEntityManagerFactory("hello")
  val em = emf.createEntityManager()
  val tx = em.transaction
  
  try {
    tx.begin()
    
    //? 비영속
    // val member: Member = Member("Hello JPA persistence")
    
    //? 영속
    println("===== Before =====")
    // em.persist(member)
    println("===== After =====")
    
    val findMember = em.find(Member::class.java, 3L)
    val findMember2 = em.find(Member::class.java, 3L)
    println("findMember = $findMember")
    println("findMember2 = $findMember2")
    
    //? 동일한 객체임을 보장
    println("result = ${findMember == findMember2}")
    
    
  } catch (e: Exception) {
    tx.rollback()
  } finally {
    
    tx.commit()
    em.close()
    emf.close()
  }
}