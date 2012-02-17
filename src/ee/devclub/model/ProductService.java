package ee.devclub.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    HibernateOperations hibernate;

    public List<Product> getAllGoods() {
        return hibernate.loadAll(Product.class);
    }

    public Product persist(Product product) {
        hibernate.saveOrUpdate(product);
        return product;
    }

    public Product getOneById(Long id) {
        return hibernate.get(Product.class, id);
    }

    public Product deleteOneById(Long id) {
       Product  corpse = hibernate.get(Product.class, id);
       hibernate.delete(corpse);
       return corpse;
    }

    public Product searchOneByCode(String productCode) {
        List<Product> items = hibernate.loadAll(Product.class);
        for(Product item: items) {
            if (item.getCode().equals(productCode)) {
                return item;
            }
        }
        return null;
    }
}







