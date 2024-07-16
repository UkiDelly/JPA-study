package delly;

import delly.domain.embedded.Address;
import delly.domain.embedded.Member;
import delly.domain.embedded.Period;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDateTime;

import static jakarta.persistence.Persistence.createEntityManagerFactory;

public class ValueMain {
  public static void main(String[] args) {
    try (EntityManagerFactory emf = createEntityManagerFactory("hello")) {
      try (EntityManager em = emf.createEntityManager()) {
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
          Member member = new Member();
          member.setName("JPA");
          member.setWorkPeriod(new Period(LocalDateTime.now(), LocalDateTime.now().plusHours(10)));
          Address address = new Address("city", "street", "10000");

          address.copyWith(null, null, null);

          em.persist(member);

          System.out.println("isWorking = " + member.getWorkPeriod().isWorking());


          tx.commit();
        } catch (Exception e) {
          tx.rollback();
          e.printStackTrace();
        }
      }
    }


  }
}
