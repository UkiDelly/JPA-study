import entity.Member
import jakarta.persistence.Persistence

fun main() {
  val emf = Persistence.createEntityManagerFactory("hello")
  val em = emf.createEntityManager()
  val tx = em.transaction
  
  try {
    tx.begin()
    
    val member1 = Member("A")
    val member2 = Member("B")
    
    
    em.persist(member1)
    em.persist(member2)
    println("====================")
    
    tx.commit()
  } catch (e: Exception) {
    tx.rollback()
  } finally {
    em.close()
    emf.close()
  }
}