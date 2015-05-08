package com.foo.bar.example;

import org.jboss.annotation.ejb.Local;

import javax.persistence.EntityManager;

@Local
public interface EntityManagerHolderLocal {

    EntityManager getEntityManager();
}
