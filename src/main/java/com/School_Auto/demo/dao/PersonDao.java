package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.Person;

import java.util.List;

public interface PersonDao {

    List<Person> findAll();
    List<Person> findAllBut(int theId);

    Person findById(int theId);

    Person save(Person thePerson);

    void deleteById(int theId);

    List<Person> findAllByOrderByLastNameAsc();

    List<Person> findAllByFirstName(String firstName);

    List<Person> findAllByLastName(String lastName);
}
