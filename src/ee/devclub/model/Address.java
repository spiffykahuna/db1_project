package ee.devclub.model;
import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 11/10/11
 * Time: 8:48 AM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name="ADDRESS")
@Access(AccessType.FIELD)
public class Address {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id = null;

    private String address = null;
    private String city = null;
    private String state = null;
    @Column(name="ZIP_POSTAL")
    private String zipPostal = null;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    //@PrimaryKeyJoinColumn
    //@Column(name="PERSON_ID")
    private Person person;




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
     * Gets address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets address.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets city.
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets city.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets state.
     */
    public String getState() {
        return state;
    }

    /**
     * Sets state.
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Gets zip or postal code.
     */

    public String getZipPostal() {
        return zipPostal;
    }

    /**
     * Sets zip or postal code.
     */
    public void setZipPostal(String zipPostal) {
        this.zipPostal = zipPostal;
    }


    public Address() {}

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
