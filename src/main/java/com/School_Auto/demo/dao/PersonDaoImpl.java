package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class PersonDaoImpl implements PersonDao {

    private EntityManager entityManager;

    @Autowired
    public PersonDaoImpl(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }

    @Override
    public List<Person> findAll() {
        TypedQuery<Person> theQuery = entityManager.createQuery("FROM Person", Person.class);
        List<Person> persons = theQuery.getResultList();
        return persons;
    }

    @Override
    public List<Person> findAllBut(int theId) {
        // JPQL sorgusu: PostgreSQL'de 'id' alanı primary key olduğu varsayılıyor
        String jpql = "SELECT p FROM Person p WHERE p.id <> :id";

        TypedQuery<Person> query = entityManager.createQuery(jpql, Person.class);
        query.setParameter("id", theId); // :id parametresini theId ile eşleştiriyoruz

        return query.getResultList();
    }

    @Override
    public Person findById(int theId) {
        Person thePerson = entityManager.find(Person.class, theId);
        return thePerson;
    }

    @Transactional
    @Override
    public Person save(Person thePerson) {
        Person dbPerson = entityManager.merge(thePerson);
        return dbPerson;
    }

    @Transactional
    @Override
    public void deleteById(int theId) {
        Person thePerson = entityManager.find(Person.class, theId);
        if(findById(theId) != null)
        entityManager.remove(thePerson);
    }

    @Override
    public List<Person> findAllByOrderByLastNameAsc() {
        TypedQuery<Person> theQuery = entityManager.createQuery(
                "FROM Person ORDER BY lastName ASC", Person.class);
        return theQuery.getResultList();
    }

    @Override
    public List<Person> findAllByFirstName(String firstName) {
        TypedQuery<Person> theQuery = entityManager.createQuery(
                "FROM Person WHERE firstName = :firstName", Person.class);
        theQuery.setParameter("firstName", firstName);
        return theQuery.getResultList();
    }

    @Override
    public List<Person> findAllByLastName(String lastName) {
        TypedQuery<Person> theQuery = entityManager.createQuery(
                "FROM Person WHERE lastName = :lastName", Person.class);
        theQuery.setParameter("lastName", lastName);
        return theQuery.getResultList();
    }
}
