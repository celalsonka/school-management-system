package com.School_Auto.demo.service;

import com.School_Auto.demo.dao.PersonDao;
import com.School_Auto.demo.entity.Person;
import com.School_Auto.demo.security.Passwords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    private final PersonDao personDao;

    @Autowired
    public PersonServiceImpl(PersonDao personDao) {
        this.personDao = personDao;
    }

    @Override
    public List<Person> findAll() {
        return personDao.findAll();
    }

    @Override
    public Person findById(int theId) {
        Person thePerson = personDao.findById(theId);
        if (thePerson == null) {
            throw new RuntimeException("Person not found with id - " + theId);
        }
        return thePerson;
    }

    @Override
    public List<Person> findAllBut(int theId) {
        return personDao.findAllBut(theId);
    }

    /**
     * Finds the person with this first name whose password matches. Passwords are compared with
     * BCrypt in the application (never in a SQL WHERE clause); legacy plain-text passwords are
     * upgraded to a hash on the first successful login.
     */
    @Override
    public Person loginPerson(Person credentials) {
        if (credentials.getFirstName() == null || credentials.getPassword() == null) {
            return null;
        }

        for (Person candidate : personDao.findAllByFirstName(credentials.getFirstName())) {
            if (Passwords.matches(credentials.getPassword(), candidate.getPassword())) {
                if (Passwords.needsUpgrade(candidate.getPassword())) {
                    candidate.setPassword(Passwords.hash(credentials.getPassword()));
                    return personDao.save(candidate);
                }
                return candidate;
            }
        }
        return null;
    }

    @Override
    public Person save(Person thePerson) {
        return personDao.save(thePerson);
    }

    @Override
    public void deleteById(int theId) {
        personDao.deleteById(theId);
    }

    @Override
    public List<Person> findAllByOrderByLastNameAsc() {
        return personDao.findAllByOrderByLastNameAsc();
    }

    @Override
    public List<Person> findAllByFirstName(String firstName) {
        return personDao.findAllByFirstName(firstName);
    }

    @Override
    public List<Person> findAllByLastName(String lastName) {
        return personDao.findAllByLastName(lastName);
    }
}
