package ee.devclub.rest;


import ee.devclub.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Path("goods")
@Produces("application/json")
public class ProductResource extends SpringAwareResource {
    @Autowired
    ProductService productService;
    @Autowired
    TradeService tradeService;
    @Autowired
    PersonService personService;
    @Autowired
    AddressService addressService;

    private Logger logger = LoggerFactory.getLogger("requestLogger");




    @GET
    public Map<Object, Object> getAllProducts(@Context HttpServletRequest req) {


       // testProductAndTradeORM();

       // testPersonAddressModel();




        List<Product> items = productService.getAllGoods();



        Map<String, String[]> requestFields = req.getParameterMap();

        if(requestFields.containsKey("getProductCodes")) {
            if(requestFields.get("getProductCodes")[0].equals("true")) {
                Map<Object,Object> productCodes = new HashMap<Object, Object>();
                int i = 0;
                for(Product item: items) {
                    productCodes.put(i++,item.getCode());
                }
                return productCodes;
            }

        }


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


        if( stopIndex > items.size()) {
            stopIndex = items.size();
        }



        logger.info("========== Request Parameters =============");
        logger.info("StartIndex: " + startIndex );
        logger.info("StopIndex: " + stopIndex );
        logger.info("TotalPages: " + new Integer(totalPages+1) );
        logger.info("TotalQuantity: " + items.size() );
        logger.info("Rows Count: " + rows );
        logger.info("Current Page: " + page);
        logger.info("========== Request Parameters END =============");


        Map<Object,Object> result = new HashMap<Object, Object>();
        result.put("data", items.subList(startIndex,stopIndex));
        result.put("page", page);
        result.put("totalPages", new Integer(totalPages+1) );
        result.put("totalQuantity", items.size());
        result.put("startIndex", startIndex);
        result.put("stopIndex", stopIndex);
        result.put("rows", rows);

        return result;

    }

    private void testProductAndTradeORM() {
        logger.info("========== Product test ================");

        Long prodId = new Long(11);
        Product prod = productService.getOneById(prodId);
        logger.info("PRODUCT NAME: " + prod.getName());

        Trade myFirstTrade = new Trade();
        myFirstTrade.setProduct(prod);
        myFirstTrade.setQuantity(10);
        tradeService.persist(myFirstTrade);

        //prod.setTrade(myFirstTrade);
        productService.persist(prod);


        Trade testTrade = tradeService.getOneById(myFirstTrade.getId());
        logger.info("FIRST TRADE PRODUCT NAME: " + testTrade.getProduct().getName());

        Product testProd = productService.getOneById(prod.getId());
        //Integer quant = testProd.getTrade().getQuantity();
        //logger.info("PRODUCT " + prod.getId().toString() + " TRADE QUANTITY = " + quant.toString());

        logger.info("========== Product test  END  ================");
    }

    private void testPersonAddressModel() {
        Person denis = personService.getOneById(new Long(1));
        Person polina = personService.getOneById(new Long(2));

        Address address = new Address();
        address.setAddress("Akadeemia tee 5 430A1");
        address.setCity("Tallinn");
        address.setState("Harjumaa");
        address.setZipPostal("12611");
        address.setPerson(denis);
        addressService.persist(address);

        denis.setAddress(address);
        personService.persist(denis);


        Address address2 = new Address();
        address2.setAddress("Akadeemia tee 5 430A2");
        address2.setCity("Tallinn");
        address2.setState("Harjumaa");
        address2.setZipPostal("12611");
        address2.setPerson(polina);
        addressService.persist(address2);

        polina.setAddress(address2);
        personService.persist(polina);

        logger.info("POLINA: " + polina.getAddress().getAddress());
        logger.info("DENIS: " + denis.getAddress().getAddress());

        logger.info("ADDRESS1: " + addressService.getOneById(new Long(1)).getPerson().getFirstName());
        logger.info("ADDRESS2: "  + addressService.getOneById(new Long(2)).getPerson().getFirstName());
    }

    @POST
    public Product newProduct(@Context HttpServletRequest req) {

//            @FormParam("name") String name,
//                        @FormParam("code") String code,
//                        @FormParam("unitPrice") double price,
//                        @FormParam("id") long id,
//                        @FormParam("oper") String param) {


        Product item = productService.getOneById(new Long(1));


        Map<String, String[]> requestFields = req.getParameterMap();
        try {
            if(requestFields.containsKey("oper")) {
                String param =  requestFields.get("oper")[0];
                Map<String, String> objectParams;
                if(param.equals("add")) {
                    //id=_empty	unitPrice=3.22	code=newItem	oper=add	name=name
                    objectParams = parseRequest(requestFields);

                    Product newProd = new Product(
                            objectParams.get("code"),
                            objectParams.get("name"),
                            new Double(objectParams.get("unitPrice"))
                    );
                    return productService.persist(newProd);

                }else if(param.equals("edit")) {
                    objectParams = parseRequest(requestFields);
                    Product editableProd = productService.getOneById(new Long(objectParams.get("id")));
                    editableProd.setCode(objectParams.get("code"));
                    editableProd.setName(objectParams.get("name"));
                    editableProd.setUnitPrice(new Double(objectParams.get("unitPrice")));

                    return productService.persist(editableProd);
                }else if(param.equals("del")){
                    Long id = new Long(requestFields.get("id")[0]);
                    deleteAssosiatedTrades(productService.getOneById(id));
                    return productService.deleteOneById(id);
                }

            }

        }catch (Exception e) {
            logger.info("ERRORA: " + e.getMessage());
            System.out.print(e.getStackTrace());
        }

//        Set<Map.Entry<String,String[] >> requestFields = req.getParameterMap().entrySet();
//        for(Map.Entry<String,String[]> param: requestFields) {
//            logger.info("PARAM: " + param.getKey() + "=" + param.getValue().toString());
//        }




        return productService.persist(item);
        //return productService.persist(new Product(code, name , price));
    }

    private void deleteAssosiatedTrades(Product corpse) {
        List<Trade> trades = tradeService.getAllTrades();
        for(Trade trade: trades) {
            if(trade.getProduct().getId().equals(corpse.getId())){
                tradeService.deleteOneById(trade.getId());
            }
        }

    }

    private Map<String, String> parseRequest(Map<String, String[]> requestFields) {
        String id = requestFields.get("id")[0];
        String unitPrice = requestFields.get("unitPrice")[0];
        String code = requestFields.get("code")[0];
        String name = requestFields.get("name")[0];
        Map<String,String> result = new HashMap<String, String>();
        result.put("id", id);
        result.put("unitPrice", unitPrice);
        result.put("code",code);
        result.put("name",name);
        return result;
    }
}

       //return items.subList(startIndex,stopIndex);

         // return productService.getAllGoods();
//        logger.info("id: " + id + "\n");
//        logger.info("name: " + name + "\n");
//        logger.info("code: " + code + "\n");


        /*
        <?php
//include the information needed for the connection to MySQL data base server.
// we store here username, database and password
include("dbconfig.php");

// to the url parameter are added 4 parameters as described in colModel
// we should get these parameters to construct the needed query
// Since we specify in the options of the grid that we will use a GET method
// we should use the appropriate command to obtain the parameters.
// In our case this is $_GET. If we specify that we want to use post
// we should use $_POST. Maybe the better way is to use $_REQUEST, which
// contain both the GET and POST variables. For more information refer to php documentation.
// Get the requested page. By default grid sets this to 1.
$page = $_GET['page'];

// get how many rows we want to have into the grid - rowNum parameter in the grid
$limit = $_GET['rows'];

// get index row - i.e. user click to sort. At first time sortname parameter -
// after that the index from colModel
$sidx = $_GET['sidx'];

// sorting order - at first time sortorder
$sord = $_GET['sord'];

// if we not pass at first time index use the first column for the index or what you want
if(!$sidx) $sidx =1;

// connect to the MySQL database server
$db = mysql_connect($dbhost, $dbuser, $dbpassword) or die("Connection Error: " . mysql_error());

// select the database
mysql_select_db($database) or die("Error connecting to db.");

// calculate the number of rows for the query. We need this for paging the result
$result = mysql_query("SELECT COUNT(*) AS count FROM invheader");
$row = mysql_fetch_array($result,MYSQL_ASSOC);
$count = $row['count'];

// calculate the total pages for the query
if( $count > 0 && $limit > 0) {
              $total_pages = ceil($count/$limit);
} else {
              $total_pages = 0;
}

// if for some reasons the requested page is greater than the total
// set the requested page to total page
if ($page > $total_pages) $page=$total_pages;

// calculate the starting position of the rows
$start = $limit*$page - $limit;

// if for some reasons start position is negative set it to 0
// typical case is that the user type 0 for the requested page
if($start <0) $start = 0;

// the actual query for the grid data
$SQL = "SELECT invid, invdate, amount, tax,total, note FROM invheader ORDER BY $sidx $sord LIMIT $start , $limit";
$result = mysql_query( $SQL ) or die("Couldn't execute query.".mysql_error());

// we should set the appropriate header information. Do not forget this.
header("Content-type: text/xml;charset=utf-8");

$s = "<?xml version='1.0' encoding='utf-8'?>";
$s .=  "<rows>";
$s .= "<page>".$page."</page>";
$s .= "<total>".$total_pages."</total>";
$s .= "<records>".$count."</records>";

// be sure to put text data in CDATA
while($row = mysql_fetch_array($result,MYSQL_ASSOC)) {
    $s .= "<row id='". $row['invid']."'>";
    $s .= "<cell>". $row['invid']."</cell>";
    $s .= "<cell>". $row['invdate']."</cell>";
    $s .= "<cell>". $row['amount']."</cell>";
    $s .= "<cell>". $row['tax']."</cell>";
    $s .= "<cell>". $row['total']."</cell>";
    $s .= "<cell><![CDATA[". $row['note']."]]></cell>";
    $s .= "</row>";
}
$s .= "</rows>";

echo $s;
?>
         */

        //sord=asc	page=1	nd=1319664800331	sidx=Id	_search=false	rows=10
//        Map<Object,Object> map=new HashMap<Object, Object>();
//        map.put("page", 1);
//        map.put("records", 10);
//        map.put("total", 2);
//
//        List<Map<Object,Object>> rows = new ArrayList<Map<Object, Object>>();
//        Map<Object,Object> row = new HashMap<Object,Object>();
//
//        row.put("id", 30);
//        String[] bum = {"3","4","5","6"};
//        row.put("cells",bum );
//
//
//        rows.add(row);
//
//        row = new HashMap<Object,Object>();
//        row.put("id", 40);
//        String[] bam = {"7","8","9","10"};
//        row.put("cells",bam );
//
//        rows.add(row);
//
//        map.put("rows", rows);
//        return map;
       // return productService.getAllGoods();