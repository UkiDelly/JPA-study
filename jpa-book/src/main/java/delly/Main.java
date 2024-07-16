package delly;

import delly.domain.practice.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import static jakarta.persistence.Persistence.createEntityManagerFactory;

public class Main {
  public static void main(String[] args) {

    try (EntityManagerFactory emf = createEntityManagerFactory("hello")) {
      try (EntityManager em = emf.createEntityManager()) {
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        try {

          Book book = new Book();
          book.setName("JPA");
          book.setAuthor("김영한");

          em.persist(book);

          tx.commit();
        } catch (Exception e) {
          tx.rollback();
          e.printStackTrace();
        }
      }
    }
  }
}