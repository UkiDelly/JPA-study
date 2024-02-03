package entity

import jakarta.persistence.Persistence

fun main() {
  val emf = Persistence.createEntityManagerFactory("hello")
  val em = emf.createEntityManager()
  val tx = em.transaction
  
  try {
    tx.begin()
    
    val member1 = Member("A", 10)
    val member2 = Member("B", 10)
    val member3 = Member("C", 10)
    
    println("===== Before =====")
    em.persist(member1)
    em.persist(member2)
    em.persist(member3)
    println("===== After =====")
    
    tx.commit()
  } catch (e: Exception) {
    tx.rollback()
  } finally {
    em.close()
    emf.close()
  }
}