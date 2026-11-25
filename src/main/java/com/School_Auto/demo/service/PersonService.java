package com.School_Auto.demo.service;

import com.School_Auto.demo.entity.Person;

import java.util.List;

public interface PersonService {
    List<Person> findAll();

    Person findById(int theId);
    List<Person> findAllBut(int theId);
    Person loginPerson(Person thePerson);
    
    Person save(Person thePerson);

    void deleteById(int theId);

    List<Person> findAllByOrderByLastNameAsc();

    List<Person> findAllByFirstName(String firstName);

    List<Person> findAllByLastName(String lastName);
}
