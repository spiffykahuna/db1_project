package ee.devclub.rest;

import ee.devclub.model.Product;
import ee.devclub.model.ProductService;
import ee.devclub.model.Trade;
import ee.devclub.model.TradeService;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 11/17/11
 * Time: 12:24 AM
 * To change this template use File | Settings | File Templates.
 */
@Path("trades")
@Produces("application/json")
public class TradeResource extends SpringAwareResource  {
    @Autowired
    ProductService productService;
    @Autowired
    TradeService tradeService;

    private Logger logger = LoggerFactory.getLogger("requestLogger");

    @GET
    public Map<String, Object> getAllTrades(@Context HttpServletRequest req) {

        List<Trade> items = tradeService.getAllTrades();

        int page = new Integer(req.getParameter("page"));
        int rows = new Integer(req.getParameter("rows"));
        // count total pages according to  jqGrid rowNum parameter
        int totalPages = (items.size()/rows);

        // if for some reasons the requested page is greater than the total
        // set the requested page to total page
        if (page > totalPages) {
            page = totalPages + 1;

        }
        //calculate sublist indexes
        int startIndex = rows*page - rows;
        if(startIndex < 0) { startIndex = 0; };
        int stopIndex = rows*page;

        logger.info("========== Request Parameters =============");
        logger.info("StartIndex: " + startIndex );
        logger.info("StopIndex: " + stopIndex );
        logger.info("TotalPages: " + totalPages );

        logger.info("========== Request Parameters END =============");
        if( stopIndex > items.size()) {
            stopIndex = items.size();
        }
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("data", getTradeMapList(items, startIndex, stopIndex));
        result.put("page", page);
        result.put("totalPages", totalPages+1);
        result.put("totalQuantity", items.size());
        result.put("startIndex", startIndex);
        result.put("stopIndex", stopIndex);
        result.put("rows", rows);
        return result;
     }

    private List<Map<String, Object>> getTradeMapList(List<Trade> items, int startIndex, int stopIndex) {
        List<Map<String, Object>> result = new LinkedList<Map<String, Object>>();

        for(Trade item: items.subList(startIndex,stopIndex)) {
            result.add(item.getTradeMap());
        }
        return result;
    }

    @POST
    public Trade newTrade(@Context HttpServletRequest req) {

//                        @FormParam("name") String name,
//                        @FormParam("code") String code,
//                        @FormParam("unitPrice") double price,
//                        @FormParam("id") long id,
//                        @FormParam("oper") String param) {

        Trade item = tradeService.getOneById(new Long(1));

        Map<String, String[]> requestFields = req.getParameterMap();
        try {
            if(requestFields.containsKey("oper")) {
                String param =  requestFields.get("oper")[0];
                Map<String, String> objectParams;
                if(param.equals("add")) {

                    try{
                        Integer quantity = new Integer(requestFields.get("quantity")[0]);
                        String productCode = requestFields.get("product_code")[0];

                        Product prod = productService.searchOneByCode(productCode);
                        if(prod == null) {
                            throw new NotFoundException("This product does not exist: " + productCode);
                        };

                        Trade newTrade = new Trade(prod,quantity);

                        tradeService.persist(newTrade);
                        //prod.setTrade(newTrade);
                        //productService.persist(prod);

                        return  newTrade;


                    } catch (Exception e) {
                        logger.info(this.getClass().getName()+ "  New trade creation failed: " +  e.getMessage());
                        return null;
                    }


                }else if(param.equals("edit")) {

                    try {
                        objectParams = parseRequest(requestFields);
                        Long tradeId = new Long(objectParams.get("id"));
                        String productCode = objectParams.get("product_code");
                        Integer quantity = new Integer(objectParams.get("quantity"));

                        Product prod = null;
                        Trade editableTrade = tradeService.getOneById(tradeId);

                        if(!editableTrade.getProduct().getCode().equals(productCode)){
                            //assign new product if codes are not equal

                            prod = productService.searchOneByCode(productCode);
                            if(prod == null) {
                                throw new NotFoundException("This product does not exist: " + productCode);
                            };
                            editableTrade.setProduct(prod);
                        }

                        editableTrade.setQuantity(quantity);
                        //prod.setTrade(editableTrade);
                        //productService.persist(prod);

                        return tradeService.persist(editableTrade);

                    } catch (Exception e) {
                        logger.info(this.getClass().getName()+ "  New trade creation failed: " +  e.getMessage());
                        return null;
                    }

                }else if(param.equals("del")){
                    Long id = new Long(requestFields.get("id")[0]);
                    return tradeService.deleteOneById(id);
                }

            }

        }catch (Exception e) {
            logger.info("REQUEST PARSING ERROR: " + e.getMessage());
            System.out.print(e.getStackTrace());
        }

//        Set<Map.Entry<String,String[] >> requestFields = req.getParameterMap().entrySet();
//        for(Map.Entry<String,String[]> param: requestFields) {
//            logger.info("PARAM: " + param.getKey() + "=" + param.getValue().toString());
//        }




        return tradeService.persist(item);
        //return productService.persist(new Product(code, name , price));
    }

    private Map<String, String> parseRequest(Map<String, String[]> requestFields) {
        String id = requestFields.get("id")[0];
        String productCode = requestFields.get("product_code")[0];
        String quantity = requestFields.get("quantity")[0];

        Map<String,String> result = new HashMap<String, String>();
        result.put("id", id);
        result.put("product_code",productCode);
        result.put("quantity", quantity);

        return result;
    }
}
