$(function() {
    $.ajaxSetup({
       error: function(req) {
           if (req.status == 0) return;
           console.log(req);
           alert('Failed: ' + req.status + ' ' + req.statusText + (req.responseText && req.responseText.length < 200 ? ': ' + req.responseText : ''));
       }
    });
});

function detectLocation(callback) {
    navigator.geolocation.getCurrentPosition(
        callback,
        function(error) {
            alert('geolocation failed: ' + error.code);
        }
    );
}

$(document).ready(
    function() {
        jQuery("#list2").jqGrid('GridUnload', "#list2");
        jQuery("#list3").jqGrid('GridUnload', "#list3");
        loadProductTable();
    }
);


function loadTradeTable() {

    jQuery("#list2").jqGrid('GridUnload', "#list2");
    jQuery("#list3").jqGrid('GridUnload', "#list3");


    var lastsel;

    jQuery("#list2").jqGrid({
        url:'/rest/trades',
        editurl: "/rest/trades",
        datatype: "json",
        colNames:['Trade Id','Product Code', 'Product Name', 'Unit Price', 'Trade Quantity', 'Total Price'],
        colModel:[
                    {   name:'id',
                        index:'id',
                        width:55,
                        sorttype:'int',
                        editable: false,
                        edittype: 'text',
                        editrules: {edithidden:true,integer:true, required:true},
                        search:true,
                        searchrules: {number:true}
                    },
                    {   name:'product_code',
                        index:'product_code',
                        width:90,
                        sorttype: 'text',
                        sortable: true,
                        editable: true,
                        edittype: 'select',
                        //formatter: 'select',
                        editoptions: {

                            dataUrl:'/rest/goods?getProductCodes=true',
                            buildSelect: function(response) {

                                 var prodObj = $.evalJSON(response);
                                 var s = '<select>';

                                 for(var key in prodObj) {
                                 // display == value here
                                    s += '<option value="'+prodObj[key]+'">'+prodObj[key]+'</option>';
                                 }
                                 s+= "</select>";
                                 return s;
                            }
                        }
                    },
                    {   name:'product_name',
                        index:'product_name',
                        width:100,
                        sorttype: 'text',
                        editable: false,
                        edittype: 'text'
                    },
                    {   name:'product_unitPrice',
                        index:'product_unitPrice',
                        width:180,
                        align:"right",
                        sorttype: 'float',
                        editable: false,
                        edittype: 'float',
                        editrules:{number:true, required:true}
                    },
                    {   name:'quantity',
                        index:'quantity',
                        width:180,
                        align:"right",
                        sorttype: 'int',
                        editable: true,
                        edittype: 'text',
                        editrules:{integer:true, required:true}
                    },
                    {   name:'totalPrice',
                        index:'totalPrice',
                        width:180,
                        align:"right",
                        sorttype: 'float',
                        editable: false,
                        edittype: 'text',
                        editrules:{number:true, required:true}
                    },
                 ],
        rowNum:6,
        //rowList:[3,6,9],
        pager: '#pager2',
        sortname: 'Id',
        viewrecords: true,
        sortorder: 'asc',
        caption:"Minu tehingud",
        jsonReader: {
            repeatitems: false,
            id: 'id',
            root: function (obj) { return obj.data; },
            page: function (obj) {
                    return obj.page;
            },
            total: function (obj) {
                   return obj.totalPages;
                   },
            records: function (obj) { return obj.totalQuantity; },

        },
        onSelectRow: function(id){
            if(id && id!==lastsel){
                jQuery('#list3').jqGrid('restoreRow',lastsel);
                jQuery('#list3').jqGrid('editRow',id,true);
                lastsel=id;
            }
        }

    }).navGrid("#pager2",
                {edit:true,add:true,del:true},
                {},
                {},
                {},
                {multipleSearch:true, multipleGroup:false}
    );





    //jQuery("#list2").jqGrid('navGrid','#pager2', {edit:false,add:false,del:false});
//    var myfirstrow = {id:"1", code:"2007-10-01", name:"note", unitPrice:"200.00"};
//    var mysecondrow = {id:"2", code:"2008-10-01", name:"imechKo", unitPrice:"500.00"};
    //jQuery("#list2").addRowData("1", myfirstrow);
    //jQuery("#list2").addRowData("2", mysecondrow);
    //jQuery("#list2").setRowData("2", {unitPrice:"666.00"});

//    $.get('/rest/goods', function(goods) {
//        for (var i in goods) {
//            var row = {id: goods[i].id, name: goods[i].name, code: goods[i].code, unitPrice: goods[i].unitPrice};
//            jQuery("#list2").addRowData(i, row);
//        }
//
//    });
//    $.get('/rest/goods', function(goods) {
//        for (var i in goods) {
//            str = "<tr><td>" + goods[i].code + "</td><td> " + goods[i].name + " </td><td>" + goods[i].unitPrice + "</td></tr>"
//            $('#myTable').append(str);
//        }
//
//    });


//    $('#list2').empty();
//    //$('#pager2').empty();
//    jQuery("#list2").jqGrid({
//        datatype: 'clientSide',
//        colNames:['Inv No','Date', 'Amount','Tax','Total','Notes'],
//        colModel :[
//        {name:'invid',index:'invid', width:55, sorttype:'int'},
//        {name:'invdate',index:'invdate', width:90, sorttype:'date', datefmt:'Y-m-d'},
//        {name:'amount',index:'amount', width:80, align:'right',sorttype:'float'},
//        {name:'tax',index:'tax', width:80, align:'right',sorttype:'float'},
//        {name:'total',index:'total', width:80,align:'right',sorttype:'float'},
//        {name:'note',index:'note', width:150, sortable:false} ],
//        pager: '#pager2',
//        rowNum:10,
//        viewrecords: true,
//        caption: 'My first grid'
//   });
//   var myfirstrow = {invid:"1", invdate:"2007-10-01", note:"note", amount:"200.00", tax:"10.00", total:"210.00"};
//   var mysecondrow = {invid:"2", invdate:"2008-10-01", note:"param", amount:"244200.00", tax:"20.00", total:"310.00"};
//   jQuery("#list2").addRowData("1", myfirstrow);
//   jQuery("#list2").addRowData("2", mysecondrow);
//   jQuery("#list").addRowData("3", {amount:"666.00"});
};

function loadProductTable() {
    //$('#list2').jqGrid("destroy");
    jQuery("#list2").jqGrid('GridUnload', "#list2");
    jQuery("#list3").jqGrid('GridUnload', "#list3");

    var lastsel;

    jQuery("#list3").jqGrid({
        url:'/rest/goods',
        editurl: "/rest/goods",
        datatype: "json",
        colNames:['Id','Code', 'Name', 'Price'],
        colModel:[
                    {   name:'id',
                        index:'id',
                        width:55,
                        sorttype:'int',
                        editable: false,
                        edittype: 'text',
                        editrules: {edithidden:true,integer:true, required:true},
                        search:true,
                        searchrules: {number:true}
                    },
                    {   name:'code',
                        index:'code',
                        width:90,
                        sorttype: 'text',
                        sortable: true,
                        editable: true,
                        edittype: 'text'
                    },
                    {   name:'name',
                        index:'name',
                        width:100,
                        sorttype: 'text',
                        editable: true,
                        edittype: 'text'
                    },
                    {   name:'unitPrice',
                        index:'unitPrice',
                        width:180,
                        align:"right",
                        sorttype: 'float',
                        editable: true,
                        edittype: 'text',
                        editrules:{number:true, required:true}
                    },
                 ],
        rowNum:6,
        //rowList:[3,6,9],
        pager: '#pager3',
        sortname: 'Id',
        viewrecords: true,
        sortorder: 'asc',
        caption:"Minu kaup",
        jsonReader: {
            repeatitems: false,
            id: 'id',
            root: function (obj) { return obj.data; },
            page: function (obj) {
                    //var currentPage = jQuery("#list3").jqGrid('getGridParam','page');
                    //var perPage = jQuery("#list3").jqGrid('getGridParam','rowNum');
                    return obj.page;//currentPage;
            },
            total: function (obj) {
                   var perPage = jQuery("#list3").jqGrid('getGridParam','rowNum');
                   return obj.totalPages;// Math.ceil(obj.length/perPage);
                   //alert(perPage);
                   //return perPage;
                   },
            records: function (obj) { return obj.totalQuantity; },
            //rowNum: function (obj) { return obj.length; }

        },
        onSelectRow: function(id){
            if(id && id!==lastsel){
                jQuery('#list3').jqGrid('restoreRow',lastsel);
                jQuery('#list3').jqGrid('editRow',id,true);
                lastsel=id;
            }
        }




//        jsonReader : {
//             root: "rows",
//             page: "page",
//             total: "total",
//             records: "records",
//             repeatitems: true,
//             cell: "cell",
//             id: "id",
//             userdata: "userdata",
//             subgrid: {root:"rows",
//                repeatitems: true,
//               cell:"cells"
//             }
//        }

    }).navGrid("#pager3",
                {edit:true,add:true,del:true},
                {},
                {},
                {},
                {multipleSearch:true, multipleGroup:false}
    );

//    $.get('/rest/goods', function(goods) {
//        for (var i in goods) {
//            var row = {id: goods[i].id, name: goods[i].name, code: goods[i].code, unitPrice: goods[i].unitPrice};
//            jQuery("#list3").addRowData(i, row);
//        }
//
//    });


//    jQuery("#badata").click(function(){&nbsp;
//     jQuery("#<%= dom_id(@live_community_builder) %>").editGridRow("new",
//     {mtype:'POST',
//     closeAfterAdd:true,
//     height:280, width:600,
//     reloadAfterSubmit:true,
//     url:'<%= widget_context_mappings_path(@live_community_builder.widget) %>'});
//     });
//    jQuery("#bedata").click(function(){
//     var gr = jQuery("#<%= dom_id(@live_community_builder) %>").getGridParam('selrow');
//     if( gr != null )
//     jQuery("#<%= dom_id(@live_community_builder) %>").editGridRow(gr,
//     {mtype:'PUT',
//     closeAfterEdit:true,
//     height:280, width:600,
//     reloadAfterSubmit:true,
//     url:'<%= widget_context_mappings_path(@live_community_builder.widget) %>/' + gr});
//     else
//     alert("Please Select Row");
//     });
//    jQuery("#bddata").click(function(){
//     var gr = jQuery("#<%= dom_id(@live_community_builder) %>").getGridParam('selrow');
//     if( gr != null )
//     jQuery("#<%= dom_id(@live_community_builder) %>").delGridRow(gr,
//     {mtype:'DELETE',
//     closeAfterEdit:true,
//     reloadAfterSubmit:true,
//     url:'<%= widget_context_mappings_path(@live_community_builder.widget) %>/' + gr});
//     else
//     alert("Please Select Row");
//    });

}
//jQuery("#ed1").click( function() {
//    jQuery("#list2").jqGrid('editRow',"new",
//     {   mtype:'POST',
//         closeAfterAdd:true,
//         height:280, width:600,
//         reloadAfterSubmit:true,
//         url:'/rest/goods'
//     }
//     );
//     this.disabled = 'true';
//     jQuery("#sved1,#cned1").attr("disabled",false);
//});

//jQuery("#sved1").click( function() {
//    jQuery("#rowed1").jqGrid('saveRow',"13");
//    jQuery("#sved1,#cned1").attr("disabled",true);
//    jQuery("#ed1").attr("disabled",false); });
//jQuery("#cned1").click( function() {
//    jQuery("#rowed1").jqGrid('restoreRow',"13");
//    jQuery("#sved1,#cned1").attr("disabled",true);
//    jQuery("#ed1").attr("disabled",false); });

    //jQuery("#<%= dom_id(@live_community_builder) %>").editGridRow("new",
//     {mtype:'POST',
//     closeAfterAdd:true,
//     height:280, width:600,
//     reloadAfterSubmit:true,
//     url:'<%= widget_context_mappings_path(@live_community_builder.widget) %>'});
//     });