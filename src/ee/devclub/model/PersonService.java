package ee.devclub.model;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 11/10/11
 * Time: 8:52 AM
 * To change this template use File | Settings | File Templates.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateOperations;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class PersonService {
    @Autowired
    HibernateOperations hibernate;

    public List<Person> getAllPeople() {
        return hibernate.loadAll(Person.class);
    }

    public Person persist(Person person) {
        hibernate.saveOrUpdate(person);
        return person;
    }

    public Person getOneById(Long id) {
        return hibernate.get(Person.class, id);
    }

}

