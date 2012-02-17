package ee.devclub.model;

import javax.persistence.*;


@Entity
@Table(name="Product")
@Access(AccessType.FIELD)
public class Product {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;

    String code;
    String name;
    Double unitPrice;

//    @OneToOne(fetch = FetchType.EAGER)
//    //@JoinColumn(name = "trade_id")
//    //@PrimaryKeyJoinColumn(name = "trade_id")
//    private Trade trade;

    public Product() {}

    public Product(String code, String name, Double unitPrice) {
        this.code = code;
        this.name = name;
        this.unitPrice = unitPrice;
    }


    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

//    public Trade getTrade() {
//        return trade;
//    }
//
//    public void setTrade(Trade trade) {
//        this.trade = trade;
//    }
}