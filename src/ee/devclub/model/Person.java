package ee.devclub.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 11/10/11
 * Time: 8:47 AM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name="PERSON")
@Access(AccessType.FIELD)
public class Person {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="FIRST_NAME")
    private String firstName;
    @Column(name="LAST_NAME")
    private String lastName;

    @OneToOne(fetch = FetchType.EAGER)
//    @OneToOne(fetch = FetchType.EAGER, mappedBy = "PERSON", cascade = CascadeType.ALL)
    @JoinColumn(name="ADDRESS_ID")
    private Address address;


    /**
     * Gets id (primary key).
     */

    public Long getId() {
        return id;
    }

    /**
     * Sets id (primary key).
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets first name.
     */

    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets last name.
     */

    public String getLastName() {
        return lastName;
    }

    /**
     * Sets last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets list of <code>Address</code>es.
     */

    public Address getAddress() {
        return address;
    }

    /**
     * Sets list of <code>Address</code>es.
     */
    public void setAddress(Address address) {
        this.address = address;
    }



    public Person() {}

}