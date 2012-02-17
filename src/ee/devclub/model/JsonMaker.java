package ee.devclub.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: deko
 * Date: 10/13/11
 * Time: 12:58 PM
 * To change this template use File | Settings | File Templates.
 */

/*
 $VAR1 = {
        'page' => '1',
        'records' => '10',
        'total' => 2,
        'rows' => [
                    {
                      'id' => 3,
                      'cell' => [
                                  3,
                                  'b;lah',
                                  '2010-09-29T19:05:32',
                                  '2010-09-29T20:15:56',
                                  'asdlkfjl23;k4fjlq;3kf4jlqkj',
                                  0
                                ]
                    },
                    {
                      'id' => 1,
                      'cell' => [
                                  1,
                                  'teaasdfasdf',
                                  '2010-09-28T21:49:21',
                                  '2010-09-28T21:49:21',
                                  'aefasdfsadf',
                                  1
                                ]
                    }
                  ]
      };
 */
class KaupNotation {
    Long id;
    Map<Long,String[]> cell;
    public KaupNotation(Long id) {
        this.id = id;
        String[] array = { "3","b;lah",
                                  "2010-09-29T19:05:32",
                                  "2010-09-29T20:15:56",
                                  "asdlkfjl23;k4fjlq;3kf4jlqkj",
                                  "0"};
        cell = new HashMap<Long, String[]>();
        cell.put(this.id, array);
    }

}
public class JsonMaker implements Serializable {
    Integer page;
    Integer records;
    Integer total;
    List<KaupNotation> rows;

    String code;
    String name;
    Double unitPrice;

    public JsonMaker () {
        page =  1;
        records = 10;
        total = 10;
        rows = new ArrayList<KaupNotation>();
        rows.add(new KaupNotation(new Long(1)));
        rows.add(new KaupNotation(new Long(2)));

    }


}