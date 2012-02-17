package ee.devclub.model;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 10/29/11
 * Time: 10:26 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="Trade")
@Access(AccessType.FIELD)
public class Trade {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="product_id")
    private Product product;

    private Integer quantity;

    @Column(name = "total_price")
    private Double totalPrice;

    public Trade() {

    }
    public Trade(Product product,
                 Integer quantity) {
        this.quantity = quantity;
        this.product = product;
        calcTotalPrice();
    }


    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
        if(this.quantity != null && this.product != null) {
            calcTotalPrice();
        }
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        if(this.product != null) {
            calcTotalPrice();
        }
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Map<String,Object> getTradeMap() {
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("id", this.id);
        result.put("product_code", this.product.getCode());
        result.put("product_name", this.product.getName());
        result.put("product_unitPrice", this.product.getUnitPrice());
        result.put("quantity", this.quantity);
        if(totalPrice == null) {
            calcTotalPrice();
        }
        result.put("totalPrice", this.totalPrice);
        return result;
    }

    private void calcTotalPrice() {
         this.totalPrice = this.quantity * this.product.getUnitPrice();
    }


//    working result
//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name="product_id")
//    private Product product;
//

//    @OneToOne(fetch = FetchType.EAGER, mappedBy = "product", cascade = CascadeType.ALL)
//    @OneToOne(fetch = FetchType.EAGER, mappedBy = "PERSON", cascade = CascadeType.ALL)
//@ManyToOne(fetch = FetchType.LAZY, targetEntity = Product.class)
//    @JoinColumn(name="trade_id", insertable = true, updatable = true, nullable=true)
//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Address2.class)
//@JoinColumn(name = "id", referencedColumnName = "person_id", insertable = false, updatable = false)
//private Address2 address;
//  @ManyToOne(targetEntity = Organization.class, fetch = FetchType.LAZY)
//    @JoinColumn(name = "id", unique = true, insertable = false, updatable = false)

}
