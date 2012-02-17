package ee.devclub.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateOperations;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 10/30/11
 * Time: 1:05 AM
 * To change this template use File | Settings | File Templates.
 */


@Service
public class TradeService {
    @Autowired
    HibernateOperations hibernate;

    public List<Trade> getAllTrades() {
        return hibernate.loadAll(Trade.class);
    }

    public Trade persist(Trade trade) {
        hibernate.saveOrUpdate(trade);
        return trade;
    }

    public Trade getOneById(Long id) {
        return hibernate.get(Trade.class, id);
    }
    public Trade deleteOneById(Long id) {
       Trade  corpse = hibernate.get(Trade.class, id);
       hibernate.delete(corpse);
       return corpse;
    }


}

