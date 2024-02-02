import entity.Member
import jakarta.persistence.Persistence

fun main() {
  val emf = Persistence.createEntityManagerFactory("hello")
  val em = emf.createEntityManager()
  val tx = em.transaction
  
  try {
    tx.begin()
    
    //? 생성
    // val Member = Member("Hello")
    
    //? JPA가 관리하는 영속성 컨텍스트에 저장
    // em.persist(member)
    
    //? Id로 조회
    // val member = em.find(Member::class.java, 1L)
    
    // println("member.id = ${member.id}")
    // println("member.name = ${member.name}")
    
    //? 수정하면 업데이트 쿼리가 생성
    // member.name = "HelloJPA"
    
    //? JPQL 사용
    val result = em.createQuery("select m from Member m ", Member::class.java)
      .setFirstResult(0)
      .setMaxResults(10)
      .resultList
    for (m in result) {
      println("member.name = ${m.name}")
    }
    
    //? 커밋 시점에 모든 쿼리가 실행
    tx.commit()
  } catch (e: Exception) {
    tx.rollback()
  } finally {
    em.close()
    emf.close()
  }
}