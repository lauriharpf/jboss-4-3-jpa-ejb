# JBoss 4.3.0.GA_CP06, EJB3 and extended PersistenceContext

**Note: This project demonstrates a problem. It is NOT functional and should NOT be used.**

Test project for using JBoss 4.3.0.GA_CP06 together with JPA 2.0 (Hibernate 4.3.9.Final). com.foo.bar.example.EntityManagerHolderBean is intended to be injected with a @PersistenceContext, but that does not happen.

Deploying the EAR built from this project causes an exception:

```
2015-05-08 13:53:49,441 WARN  [org.jboss.system.ServiceController] Problem starting service persistence.units:ear=earmodule-1.0-SNAPSHOT.ear,jar=ejb-1.0-SNAPSHOT.jar,unitName=examplePersistenceUnit
java.lang.ClassCastException: org.hibernate.jpa.HibernatePersistenceProvider cannot be cast to javax.persistence.spi.PersistenceProvider
	at org.jboss.ejb3.entity.PersistenceUnitDeployment.start(PersistenceUnitDeployment.java:245)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
```

## Reproducing the problem

1. Install JBoss 4.3.0.GA_CP06
2. Install Maven 3.2.1
3. Clone this project.
4. Build the project via e.g. command line with `mvn clean package` in the project directory.
5. Move the generated `earmodule-1.0-SNAPSHOT.ear` to the JBoss deploy directory (e.g. default/deploy).
6. Start JBoss, look at `server.log`.

## Notes
Different error messages can be obtained by either

1. Removing the `jboss-app.xml` from the META-INF directory of the EAR, which results in the following (due to a mismatch between the JBoss JARs and the EAR JARs):

```
2015-05-08 14:03:20,519 WARN  [org.jboss.system.ServiceController] Problem starting service persistence.units:ear=earmodule-1.0-SNAPSHOT.ear,jar=ejb-1.0-SNAPSHOT.jar,unitName=examplePersistenceUnit
java.lang.RuntimeException: java.lang.NoSuchMethodError: org.jboss.logging.Logger.tracef(Ljava/lang/String;Ljava/lang/Object;)V
	at org.jboss.ejb3.ServiceDelegateWrapper.startService(ServiceDelegateWrapper.java:109)
	at org.jboss.system.ServiceMBeanSupport.jbossInternalStart(ServiceMBeanSupport.java:289)
	at org.jboss.system.ServiceMBeanSupport.jbossInternalLifecycle(ServiceMBeanSupport.java:245)
...
Caused by: java.lang.NoSuchMethodError: org.jboss.logging.Logger.tracef(Ljava/lang/String;Ljava/lang/Object;)V
	at org.hibernate.jpa.HibernatePersistenceProvider.createContainerEntityManagerFactory(HibernatePersistenceProvider.java:150)
	at org.jboss.ejb3.entity.PersistenceUnitDeployment.start(PersistenceUnitDeployment.java:246)
...
```

2. Removing `ejb3-persistence.jar` from the JBoss lib directory (e.g. default/lib):

```
2015-05-08 14:08:04,955 WARN  [org.jboss.system.ServiceController] Problem creating service jboss.j2ee:service=EJB3,module=ejb-1.0-SNAPSHOT.jar
java.lang.NoClassDefFoundError: javax/persistence/spi/PersistenceUnitTransactionType
	at org.jboss.ejb3.Ejb3Deployment.initializePersistenceUnits(Ejb3Deployment.java:536)
	at org.jboss.ejb3.Ejb3Deployment.create(Ejb3Deployment.java:330)
	at org.jboss.ejb3.Ejb3Module.createService(Ejb3Module.java:74)
```

ejb3-persistence.jar contains `javax.persistence.spi.PersistenceProvider`.