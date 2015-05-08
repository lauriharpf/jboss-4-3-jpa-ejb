package com.foo.bar.example;

import org.jboss.annotation.ejb.LocalBinding;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.SynchronizationType;

@Stateful
@LocalBinding(jndiBinding = "MyEJB/EntityManagerBean")
public class EntityManagerHolderBean implements EntityManagerHolderLocal {
    @PersistenceContext(type = PersistenceContextType.EXTENDED, synchronization = SynchronizationType.UNSYNCHRONIZED)
    protected EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}

