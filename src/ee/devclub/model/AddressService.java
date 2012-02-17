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
public class AddressService {
    @Autowired
    HibernateOperations hibernate;

    public List<Address> getAllAddresses() {
        return hibernate.loadAll(Address.class);
    }

    public Address persist(Address address) {
        hibernate.saveOrUpdate(address);
        return address;
    }

    public Address getOneById(Long id) {
        return hibernate.get(Address.class, id);
    }

}

