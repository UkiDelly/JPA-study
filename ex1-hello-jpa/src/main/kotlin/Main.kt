import entity.Member
import jakarta.persistence.Persistence

fun main() {
  val emf = Persistence.createEntityManagerFactory("hello")
  val em = emf.createEntityManager()
  val tx = em.transaction
  
  try {
    tx.begin()
    
    // Id로 조회
    val member = em.find(Member::class.java, 1L)
    
    println("member.id = ${member.id}")
    println("member.name = ${member.name}")
    
    // 수정하면 업데이트 쿼리가 생성
    member.name = "HelloJPA"
    
    // 커밋 시점에 업데이트 쿼리가 실행
    tx.commit()
  } catch (e: Exception) {
    tx.rollback()
  } finally {
    em.close()
    emf.close()
  }
}