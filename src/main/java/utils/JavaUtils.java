package utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JavaUtils {
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("aluno");

    public static EntityManager getEntityManager(){
        return entityManagerFactory.createEntityManager();
    }
}
