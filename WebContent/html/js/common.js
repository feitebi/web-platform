var userid=localStorage.getItem("userid");
var chartApisUrl="/rest/ClactData";
var bookmarkURL="/rest/Bookmark";
var pariName="BTC";
/*if(userid==""||userid==null){
	alert("您还没有登录，请先登录!");
	window.location.href="signin.html";
	
}*/
function logOut(){
	localStorage.clear();
	window.location.href="signin.html";
}
var USER_TAG = "USER_DATA";
var LOGIN_TAG = "LOGIN_TAG";

var MY_COMPLETE_BALANCES = 'MY_COMPLETE_BALANCES';

var MY_AVAILABLE_BTC ='MY_AVAILABLE_BTC';

var PLATFORM = "PLATFORM";

var server_uri = "http://poloniex.unionchain.org/rest/xchange";

function exitSys() {
     exitLogin();
     window.location.href = 'signin.html';
}

function getUserId() {
  var user = getStorageJson(USER_TAG);
   if (typeof user != 'undefined' && user != null) {
      return user.userId;
   }
   return "";
}

function getPlatform() {
  var pf = getStorageJson(PLATFORM);
   if (typeof pf != 'undefined' && pf != null) {
      return pf
   }
   return "Poloniex";
}

function login() {
     $.ajax({
         type: 'POST',
         url: server_uri+'/user/login',
         data: {
             phone: $('#phone').val(),
             pwd: $('#pwd').val(),
             userId: getUserId()
         },
         dataType: 'json',
         success: function(data) {
             var json = data.entity;
             if (json.id != '0') {
                 storeLogin();
                 json.rememberMe = $('#rememberMe').is(':checked');
                 storeJsonObject(USER_TAG, json);
                 window.location.href = 'index.html';
             } else {
                 $('#dlgTips').html('登录失败，请检查输入！');
                 $('#tipsDlg').modal('show');
                 return;
             }
         }
     });
 }

 function register() {
     $.ajax({
         type: 'POST',
         url: server_uri+'/user/register',
         data: {
             phone: $('#phone').val(),
             pwd: $('#pwd').val(),
             userId: getUserId(),
             phoneValidCode: $('#phoneValidCode').val()
         },
         dataType: 'json',
         success: function(data) {
             var json = data.entity;
             if (json.id != '0') {
                 json.rememberMe = false;
                 storeLogin();
                 storeJsonObject(USER_TAG, json);
                 window.location.href = 'index.html';
             } else {
                 $('#dlgTips').html('注册失败，请检查输入！');
                 $('#tipsDlg').modal('show');
                 return;
             }
         }
     });
 }

 function sendPassword() {
     $.ajax({
         type: 'POST',
         url: server_uri+'/user/send_pwd',
         data: {
             phone: $('#phone').val()
         },
         dataType: 'json',
         success: function(data) {
             var state = data;
             if (state.flag == '1') {
                 showModalDlg("发送成功，请查看短信！", 'signin.html');
             } else {
                 $('#dlgTips').html('发送失败，请检查输入！');
                 $('#tipsDlg').modal('show');
                 return;
             }
         }
     });
 }

 function setPassword() {
     $.ajax({
         type: 'POST',
         url: server_uri+'/user/change_password',
         data: {
             userId: getUserId(),
             pwd: $('#newPwd').val(),
             oldPwd: $('#oldPwd').val()
         },
         dataType: 'json',
         success: function(data) {
             var state = data.entity;
             if (state.userId != '0') {
                 $('#dlgTips').html('修改成功！');
                 $('#tipsDlg').modal('show');
             } else {
                 $('#dlgTips').html('发送失败，请检查输入！');
                 $('#tipsDlg').modal('show');
                 return;
             }
         }
     });
 }

 var timer_minute = 60000;
 var timer;

 function sendCode() {
     $.ajax({
         type: 'POST',
         url: server_uri+'/user/send_verify_code',
         data: {
             userId: getUserId(),
             phone: $('#phone').val()
         },
         dataType: 'json',
         success: function(data) {
             var state = data;
             if (state.flag == '1') {
                 $('#codeBtn').addClass('disabled');
                 timer = setInterval(countdown, 1000);
             } else {
                 $('#dlgTips').html('发送失败，请检查输入！');
                 $('#tipsDlg').modal('show');
                 return;
             }
         }
     });
 }

 function countdown() {
     timer_minute = timer_minute - 1000;
     if (timer_minute > 0) {
         $('#codeBtn').html(timer_minute / 1000 + "秒后重发");
     } else {
         clearInterval(timer);
         timer_minute = 60000;
         $('#codeBtn').removeClass('disabled');
         $('#codeBtn').html("发送验证码");
     }
 }

function storeLogin() {
   var login = new Object();
   login.login = "1";
   login.time = new Date().toDateString();
   sessionStorage.setItem(LOGIN_TAG, JSON.stringify(login));
}

function exitLogin() {
   localStorage.clear();
}

function isLogin() {
  /*var json = sessionStorage.getItem(LOGIN_TAG);
  if (typeof json != 'undefined' && json != null) {*/
     return true;
/*  }

  json = getStorageJson(USER_TAG);
  if (typeof json != 'undefined' && json != null && json.rememberMe && json.userId !='') {
       return true;
  }
 return false;*/
}


function getStorageJson(key) {
 if (window.localStorage) {
   return JSON.parse(localStorage.getItem(key));
 } else {
   alert('失败');
   return "";
 }
}

function storeJsonObject(key, val) {
 if (window.localStorage) {
   localStorage.setItem(key, JSON.stringify(val));
 } else {
   alert('失败');
 }
}

var isMobile = {
    Android: function() {
        return navigator.userAgent.match(/Android/i);
    },
    BlackBerry: function() {
        return navigator.userAgent.match(/BlackBerry/i);
    },
    iOS: function() {
        return navigator.userAgent.match(/iPhone|iPad|iPod/i);
    },
    Opera: function() {
        return navigator.userAgent.match(/Opera Mini/i);
    },
    Windows: function() {
        return navigator.userAgent.match(/IEMobile/i);
    },
    any: function() {
        return (isMobile.Android() || isMobile.BlackBerry() || isMobile.iOS() || isMobile.Opera() || isMobile.Windows());
    }
};

Date.prototype.format = function(format) {
 var o = {
   "M+": this.getMonth() + 1,
           // month
           "d+": this.getDate(),
           // day
           "h+": this.getHours(),
           // hour
           "m+": this.getMinutes(),
           // minute
           "s+": this.getSeconds(),
           // second
           "q+": Math.floor((this.getMonth() + 3) / 3),
           // quarter
           "S": this.getMilliseconds()
           // millisecond
         };
         if (/(y+)/.test(format) || /(Y+)/.test(format)) {
           format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
         }
         for (var k in o) {
           if (new RegExp("(" + k + ")").test(format)) {
             format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
           }
         }
         return format;
       };

       function GetRandomNum(Min, Max) {
         var Range = Max - Min;
         var Rand = Math.random();
         return (Min + Math.round(Rand * Range));
       }

       function timestampformat(timestamp) {
         return (new Date(timestamp)).format("yyyy-MM-dd hh:mm");
       }
       
 function addApi() {
  
  var pf = $('#platform').text();
  var key = $('#key').val();
  var secret = $('#secret').val();
  if(!isLogin() || pf=='' || key=='' || secret==''){
    alert('请填写API参数');
    return;
  }


    $.ajax({
       type: 'post',
       url: server_uri+'/apis/add',
       data: {
          userId: getUserId(),
          platform: pf,
          key: key,
          secret: secret
       },
       dataType: 'json',
       success: function(data) {
          var id = data.entity.id;
          if(id!='-1'){
            alert('success');
          }else{
            alert('failed');
          }
      }
    });
  }

   
   function returnCompleteBalances() {
    if(getStorageJson(USER_TAG).apiList.length<=0){
      $('#platform').text('Poloniex');
      $('#apiSetDlg').modal('show');
      return;
    }

    storeJsonObject(MY_AVAILABLE_BTC,0);

     $.ajax({
       type: 'post',
       url: server_uri+'/account/summary',
       data: {
         userId: getUserId(),
         platform: getPlatform()
       },
       dataType: 'json',
       success: function(data) {
        
        var arr = data.entity;

         var myCoinQty=0;
         var btcVal = 0;

         for(var i=0;i<arr.length;i++) {
          var value = arr[i];

           if(parseFloat(value.total)>0) {
             
             myCoinQty++;
             $("#myCoinTotalCount").html(myCoinQty);
             
             //btcVal += parseFloat(value.btcValue);

             //$("#myBTCTotalAmount").html(btcVal.toFixed(8));

             if(value.currency=='BTC'){
                storeJsonObject(MY_AVAILABLE_BTC,value.available);
             }
             
           } else {
             // $('.tr_BTC_'+name).hide();
           }
         };

         storeJsonObject(MY_COMPLETE_BALANCES,data);
         
       }
     });
   }
   
  function returnExchanges() {
    $.ajax({
     type: 'post',
     url: 'http://api.huobi.com/staticmarket/depth_btc_1.js',
     data: {
        userId: getUserId(),
        platform: getPlatform()
     },
     dataType: 'json',
     success: function(data) {
      var ticker = data;
      var btccny = ((ticker.asks[0][0]+ticker.bids[0][0])/2);
      
      var btcQty = parseFloat($("#myBTCTotalAmount").text());
      var usdrmb = btccny*btcQty;
      $("#myRMBTotalAmount").html(usdrmb.toFixed(2));
    }
  });
  }
  
 
 function returnMyTradeHistory(allHis) {
   $.ajax({
     type: 'post',
     url: server_uri+'/trade/list',
     data: {
       userId: getUserId(),
       platform: getPlatform()
     },
     dataType: 'json',
     success: function(data) {
       if (typeof allHis=='undefined' || allHis==null || allHis==false) {
         $("#balancesList tbody").empty();
       }
       ordersList = new Array();

       $.each(data,function(name,value) {
         if ($.isArray(value)) {
          var amt = 0;
          var qty = 0;
          var sellAmt = 0;
          var order = new Object();
          order.name = name;
          for (var i=0;i<value.length;i++) {
           var sAmt = parseFloat(value[i].amount)-parseFloat(value[i].amount)*parseFloat(value[i].fee);
           var sTotal = parseFloat(value[i].total);
           qty = value[i].type=='sell'?(qty-sAmt):(qty+sAmt);
           amt = value[i].type=='sell'?(amt-sTotal):(amt+sTotal);
           
           order.date = value[0].date;
           if (typeof allHis !='undefined' && allHis==true) {
              addOrderHistoryItem(name,value[i]);
          }
        }

        if (typeof allHis=='undefined' || allHis==null || allHis==false) {
          var avgPrice = amt/qty;
          order.avgPrice = avgPrice.toFixed(8);
          order.amount = qty.toFixed(8);
          order.total = amt.toFixed(8);

          //buyAvgRate +=',"'+name+'":'+'"'+order.avgPrice +'"';

          var boughtCoins = getStorageJson(MY_COMPLETE_BALANCES);
          if (boughtCoins!=null) {
            boughtCoins = JSON.stringify(boughtCoins);
            if(boughtCoins.indexOf(name)>0) {
              addTradeItem(order);
            }
          }else{
            addTradeItem(order);
         }
       }
     }
   });
       
     }
   });
 }
 

 function returnTicker() {
   $.ajax({
     type: 'post',
     url: chartApisUrl+'/allTicker',
     data: {
      /* userId: userid,*/
       platform: getPlatform()
     },
     dataType: 'json',
     success: function(data) {
       var btcVal=0;
       var tikers = new Array();
       $.each(data.entity,function(name,value) {
         var tiker = new Object();
         tiker.name = name;
         tiker.last = value.last;
         tiker.percentChange = value.percentChange;
         tiker.baseVolume = value.baseVolume;
         tiker.lowestAsk = value.lowestAsk;
         tiker.highestBid = value.highestBid;
         tikers.push(tiker);

         //update buy or sell form realtime.
         var lastSelectedCoin = $("#buyCoinName").text();
         if(lastSelectedCoin!='' && tiker.name=='BTC_'+lastSelectedCoin) {
              $(".buyPriceRate").html(tiker.lowestAsk);
              var sr = tiker.highestBid;
              $(".sellPriceRate").html(sr==''?'-':sr);
         }

       });

       var aTab = $('.realTimeNav .active').text();

       if(aTab=='' || aTab==null || aTab==undefined){
          aTab="BTC";
       }
       var tbList = 'tikerList'+ aTab.trim();

      if($('#'+tbList).length>0){
       var sortedCol = $('#'+tbList).dataTable().fnSettings().aaSorting[0][0];
       var sortedDir = $('#'+tbList).dataTable().fnSettings().aaSorting[0][1];
       
       tikers.sort(function(a,b){
          if(sortedCol==0 && sortedDir=='asc') {
            return a.name.localeCompare(b.name);
          }else if(sortedCol==0 && sortedDir=='desc') {
            return b.name.localeCompare(a.name);
          }else if(sortedCol==1 && sortedDir=='asc') {
           return a.last-b.last;
          }else if(sortedCol==1 && sortedDir=='desc') {
           return b.last-a.last;
          }else if(sortedCol==2 && sortedDir=='asc') {
           return a.percentChange-b.percentChange;
          }else if(sortedCol==2 && sortedDir=='desc') {
           return b.percentChange-a.percentChange;
          }else if(sortedCol==3 && sortedDir=='asc') {
           return a.baseVolume-b.baseVolume;
          }else if(sortedCol==3 && sortedDir=='desc') {
           return b.baseVolume-a.baseVolume;
          }
       });
       
      $("#realtimeDataTable table tbody").empty();
    }

      for(var j=0;j<tikers.length;j++){
         var value = tikers[j];
         var name = value.name;

         var db = getStorageJson(MY_COMPLETE_BALANCES);
         if(db!=null){
            var arr = db.entity;
           
            for (var k=0;k<arr.length;k++) {
                if(name==pariName+"_"+arr[k].currency || name==pariName+"-"+arr[k].currency) {

                  btcVal += parseFloat(arr[k].total)*parseFloat(value.last);
                  var test = btcVal*800;
                  $("#myBTCTotalAmount").html(test.toFixed(6));
                }
            }
         }


        if($('#chartCoinName').text().trim()=='' && !isMobile.any()){
          var x=tikers.length,y=1;
          var rand = parseInt(Math.random() * (x - y + 1) + y);
          if(rand<x){
            returnChartData(name);
          }
        }
    
      addTiker(name,value.last,value.baseVolume,value.percentChange,value.lowestAsk,value.highestBid);
   }

   returnExchanges();
 }
});
 }
 
 function addTiker(name,price,vol,change,lowestAsk,highestBid) {
  var f='0';
  try {
    var boughtCoins = getStorageJson(MY_COMPLETE_BALANCES);
    if(boughtCoins!=null){
    boughtCoins = JSON.stringify(boughtCoins);
    if(boughtCoins.indexOf(name.substring(4))>0) {
     f='1';
   }
 }
}catch(err) {
}


var bg =$('#chartCoinName').text()==name?'warn text-black':'';
var sName=name;

sName = sName.toUpperCase();
sName = sName.replace('/','');
sName = sName.replace('_','');
sName = sName.replace('-','');

var tList = "tikerListBTC";
if(name.indexOf("BTC")>=0){
  sName = sName.replace('BTC','');
} else if(name.indexOf("ETH")>=0){
  sName = sName.replace('ETH','');
  tList ="tikerListETH";
} else if(name.indexOf("USDT")>=0 || name.indexOf("USD")>=0){
  sName = sName.replace('USDT','');
  sName = sName.replace('USD','');
  tList ="tikerListUSDT";
}else if(name.indexOf("CNY")>=0){
  sName = sName.replace('CNY','');
  tList ="tikerListCNY";
}else if(name.indexOf("XMR")>=0 && name.length>=6){
  return;
}
var tag ='<i class="fa fa-heart fa-xs text-dark inline" onclick="boolmark(\''+sName+'\',\''+getPlatform()+'\')"></i>';

if (f && f=='1') {
 f='0';
 tag='<i class="fa fa-heart fa-xs text-danger inline"></i>';
}else{
 f='1';
}
var pc = parseFloat(change*100).toFixed(2);
var pctag = pc>0?'text-success':'text-danger';
var tip =pc>0?'<i class="fa fa-level-up"> ':'<i class="fa fa-level-down"> ';
var item ='<tr class="'+bg+' ticker_tr_bg text-xs" id="ticker_tr_'+sName+'" onclick="javascript:tikerClick(\''+sName+'\',\''+lowestAsk+'\');">';
item += '<td><span ui-toggle-class class="inline">'+tag+'</span> ';
item +='<span class="hide" id="tiker_highestBid_'+sName+'">'+highestBid+'</span>'+sName+'</td>';
item +='</td><td>';
item +=  price;
item +='</td><td class="'+pctag+'">';
item += tip + Math.abs(pc);
item +='</td><td>';
item += parseFloat(vol).toFixed(2);
item +='</td></tr>';

$('#'+tList+' tbody').append(item);

}


function addTradeItem(order) {
  var ev= '';
  if(isMobile.any()){
    ev= 'onclick="javascript:showSellDlg(\''+order.name.substring(4)+'\',\''+order.total+'\');"';
  }
  var item ='';
  item =' <tr '+ev+' class="tr_'+order.name+'"><td>'+order.name.substring(4)+'</td><td id="'+order.name+'">';
  item += '-';
  item +='</td><td id="'+order.name+'_vrate">';
  item += order.avgPrice;
  item +='</td><td id="'+order.name+'_change">';
  item += '-';
  item +='</td><td id="'+order.name.substring(4)+'_available_qty">';
  item += order.amount;
  item +='</td><td id="'+order.name.substring(4)+'_bought_qty">';
  item += order.total;
  item +='</td><td id="'+order.name+'_tips">';
  item += '-';
  item +='</td></tr>';

  $("#balancesList tbody").append(item);
  
}

function returnOpenOrders() {
 $.ajax({
   type: 'post',
   url: server_uri+'/order/list',
   data: {
     userId: getUserId(),
     platform: getPlatform()
   },
   dataType: 'json',
   success: function(data) {
     $.each(data,function(name,value) {
       if ($.isArray(value) && value.length>0) {
        for (var i=0;i<value.length;i++) {
         addOpenOrderItem(name,value[i]);
       }
     }
   });
   }
 });
}

function addOpenOrderItem(name,order) {
 var item ='';
 item =' <tr id="open_order_tr_'+order.orderNumber+'"><td>';
 item += order.type=='sell'?'<span class="label warning">卖</span>':'<span class="label success">买</span>';
 item += '</td><td>'+name.substring(4)+'</td><td>';
 item += order.rate;
 item +='</td><td>';
 item += order.amount;
 item +='</td><td>';
 item += order.total;
 item +='</td><td>';
 item += order.date;
 item +='</td><td>';
 item += '<a href="javascript:cancelOrder(\''+order.orderNumber+'\',\'open_order_tr_'+order.orderNumber+'\');" class="btn default btn-xs">取消</a>';
 item +='</td></tr>';

 $("#openOrderList tbody").append(item);
}

function addOrderHistoryItem(name,order) {
 var item ='';
 item =' <tr><td>';
 item += order.type=='sell'?'<span class="label warning">卖</span>':'<span class="label success">买</span>';
 item += '</td><td>'+name.substring(4)+'</td><td>';
 item += order.rate;
 item +='</td><td>';
 item += order.amount;
 item +='</td><td>';
 item += order.total;
 item +='</td><td>';
 item += order.date;
 item +='</td></tr>';
 
 $("#orderList tbody").append(item);
}

function updateBuyOrSellFormAfterTicker(name,price) {
  if(!isMobile.any()) {
      
      $('.ticker_tr_bg').removeClass('warn');
      $('#ticker_tr_'+name).addClass('warn');

      $('.chartCoinName').html(name);
      $(".buyCoinName").html(name);
      $(".buyPriceRate").html(price);
      $("#buyRate").val(price);
      
      
      $('#buyAmt').val('');
      $('#buyTotalQty').val('');

     var sr = $('#tiker_highestBid_'+name).text();
     var qty = '-';
     var db = getStorageJson(MY_COMPLETE_BALANCES);
     if(db!=null) {
      var data =db.entity;
       for(var i=0;i<data.length;i++){

          if(data[i].currency==name) {
              qty = data[i].available.toFixed(8);
          }
          if(data[i].currency=='BTC'){
            $('.myBuyBtcQty').html(data[i].available.toFixed(8));
          }
       };
     }

     $(".sellCoinName").html(name);
     $(".sellPriceRate").html(sr==''?'-':sr);
     $("#sellRate").val(sr==''?'-':sr);
     $('.mySellCoinQty').html(((qty=='-'||qty=='')?'0.00000000':qty));

     $('#sellAmt').val('');
     $('#sellTotalQty').val('');
     $('#sellProfit').html('0.00000000');

     if(parseFloat($('#mySellCoinQty').text())>0){
        $('#sellBtn').removeClass('disabled');
     }
     if(parseFloat($('#myBuyBtcQty').text())>0){
       $('#buyBtn').removeClass('disabled');
     }
   }
}


function changePariName(pariname){
	pariName=pariname;
}

function tikerClick(name,price) {
  if(!isMobile.any()) {
      
     updateBuyOrSellFormAfterTicker(name, price);

     returnChartData(pariName,name);

  }else{
    showBuyDlg(name, price);
  }
}

function showBuyDlg(name,price) {
  var availableBtc = getStorageJson(MY_AVAILABLE_BTC);
  $(".buyCoinName").html(name);
  $(".buyPriceRate").html(price);
  $("#buyRate").val(price);
  $('.myBuyBtcQty').html(availableBtc.toFixed(8));
  
  $('#buyAmt').val('');
  $('#buyTotalQty').val('');
  
  $('#buyDlg').modal('show');
  if (parseFloat(availableBtc)<=0) {
   $('#buyBtn').addClass('disabled');
  }
}

function showSellDlg(name,qty) {
  
 var sr = $('#tiker_highestBid_'+name).text();
 $(".sellCoinName").html(name);
 $(".sellPriceRate").html(sr==''?'-':sr);
 $("#sellRate").val(sr==''?'-':sr);
 $('.mySellCoinQty').html(qty);
 
 if (parseFloat(qty)<=0) {
   $('#confirmDlg').modal('show');
   return;
 }
 
 $('#sellAmt').val('');
 $('#sellTotalQty').val('');
 $('#sellProfit').html('0.00000000');
 
 $('#sellDlg').modal('show');
 if (name=='BTC') {
   $('#sellBtn').addClass('disabled');
 }
}

function cancelOrder(orderNumber,tagId) {
 $.ajax({
   type: 'post',
   url: 'http://poloniex.unionchain.org/api/poloniex/cancelOrder',
   data: {
     key: getStorageJson('key'),
     secret: getStorageJson('secret'),
     orderNumber:orderNumber
   },
   dataType: 'json',
   success: function(data) {
     if (data.success=='1') {
       $('#'+tagId).hide();
     }else{
       $('#msgTips').html('删除失败，请稍后重试！');
       $('#msgDlg').modal('show');
     }
   }
 });
}


function buyNow() {
  var availableBtc = parseFloat($('#myBuyBtcQty').text());
  var totalQty = parseFloat($('#buyTotalQty').val());
  if (availableBtc-totalQty<0) {
   $('#msgTips').html('BTC不足，请到Poloniex冲值！');
   $('#msgDlg').modal('show');
   return;
 }
 
 var currencyPair= 'BTC_'+$('#buyCoinName').text();
 var rate= $("#buyRate").val();
 var amount= $("#buyAmt").val();
 var fillOrKill= '1';
 var immediateOrCancel= '0';
 var postOnly= '0';
 
 if (rate=='' || rate=='0' || amount=='' || amount=='0') {
   $('#msgTips').html('请输入价格和数量！');
   $('#msgDlg').modal('show');
   return;
 }
 $('#buyBtn').addClass('disabled').html('提交中...');
 
 $.ajax({
   type: 'post',
   url: 'http://poloniex.unionchain.org/api/poloniex/buy',
   data: {
     key: getStorageJson('key'),
     secret: getStorageJson('secret'),
     currencyPair:currencyPair,
     rate:rate,
     amount:amount,
     postOnly:postOnly,
     immediateOrCancel:immediateOrCancel,
     fillOrKill:fillOrKill
   },
   dataType: 'json',
   success: function(data) {
     $('#buyBtn').removeClass('disabled').html('立即购买');
     
     if (typeof data =='object' && data.orderNumber && data.orderNumber !=='') {
       $('#msgTips').html('订单购买成功！');
       setTimeout(returnCompleteBalances, 200);
     }else{
       $('#msgTips').html('购买失败，请检查输入参数！');
     }
     $('#buyDlg').modal('hide');
     $('#msgDlg').modal('show');
   }
 });
}


function sellNow() {
  var currencyPair= 'BTC_'+$('#sellCoinName').text();
  var data = getStorageJson(MY_COMPLETE_BALANCES);
  var availableQty= '0';
  var onOrders = '0';

  $.each(data,function(name,value) {
    if(name==$('#sellCoinName').text()){
        availableQty= value.available;
        onOrders = value.onOrders;
    }
  });
  
  availableQty = parseFloat(availableQty).toFixed(8);
  var sellAmt = parseFloat($('#sellAmt').val());
  if (availableQty-sellAmt<0) {
   $('#msgTips').html('您可出售的数量为：'+availableQty+', 智能合约订单冻结为：'+onOrders);
   $('#msgDlg').modal('show');
   return;
 }
 
 var currencyPair= 'BTC_'+$('#sellCoinName').text();
 var rate= $("#sellRate").val();
 var amount= $("#sellAmt").val();
 var fillOrKill= '0';
 var immediateOrCancel= '0';
 var postOnly= '0';
 if (rate=='' || rate=='0' || amount=='' || amount=='0') {
   $('#msgTips').html('请输入价格和数量！');
   $('#msgDlg').modal('show');
   return;
 }
 $('#sellBtn').addClass('disabled').html('提交中...');

 $.ajax({
   type: 'post',
   url: 'http://poloniex.unionchain.org/api/poloniex/sell',
   data: {
     key: getStorageJson('key'),
     secret: getStorageJson('secret'),
     currencyPair:currencyPair,
     rate:rate,
     amount:amount,
     postOnly:postOnly,
     immediateOrCancel:immediateOrCancel,
     fillOrKill:fillOrKill
   },
   dataType: 'json',
   success: function(data) {
     $('#sellBtn').removeClass('disabled').html('立即卖出');
     if (typeof data =='object' && data.orderNumber && data.orderNumber !=='') {
       $('#msgTips').html('卖出订单成功！');
       setTimeout(returnCompleteBalances, 200);
     }else{
       $('#msgTips').html('卖出失败，请检查输入参数！');
     }
     $('#sellDlg').modal('hide');
     $('#msgDlg').modal('show');
   }
 });
}

function loadAllTicketsDataHistory(timeLimit) {
  var tikers = getStorageJson(ALL_TICKETS_COINS);
  var blackHourse = new Array();
  $.each(tikers,function(name,value) {

  });
}

function getTicketHistory(currecyPair,timeLimit){
  var lastDay = new Date(new Date().getTime() - parseInt(timeLimit)*24*60*60*1000);
  var start = Math.round(lastDay.getTime()/1000);
  var end = Math.round(new Date().getTime()/1000);
   var period = 300;
  if(timeLimit<=5){
     period = 300;
  }else if(timeLimit<=30){
     period = 900;
  }else if(timeLimit<=60){
     period = 1800;
  }else if(timeLimit<=90){
    period =7200;
  }else if(timeLimit<=180){
    period = 14400;
  }else if(timeLimit<=360){
    period = 86400;
  }
 

  $.ajax({
   type: 'post',
   url: server_uri+'/chart_data',
   data: {
     userId: getUserId(),
     platform: getPlatform(),
     currencyPair:currencyPair,
     period: period,
     start: start,
     end:'9999999999'
   },
   dataType: 'json',
   success: function(data) {
    var timeArr = new Array();

    var kDataArr = new Array();
    for (var i = 0; i < data.length; i++) {
      var dataRow = new Array();
          // 开盘，收盘，最低，最高
          dataRow[0]=data[i].open;
          dataRow[1]=data[i].close;
          dataRow[2]=data[i].low;
          dataRow[3]=data[i].high;
          
          kDataArr[i]=dataRow;
        }
      }

  });

}


function refreshChart(timeLimit){
  if (typeof timeLimit=='undefined' || timeLimit=='') {
    timeLimit = 10;
  }
 
  var pairToName=$('#chartCoinName').text();
  var arr = pairToName.split('_');
 
  returnChartData(pariName,arr[1],timeLimit);
}

function returnChartData(currencyPair,coinName,timeLimit) {
  /*if (!timeLimit) {
    timeLimit = 10;
  }*/
	
  /*var lastDay = new Date(new Date().getTime() - parseInt(timeLimit)*24*60*60*1000);
  var start = Math.round(lastDay.getTime()/1000);
  var end = Math.round(new Date().getTime()/1000);*/
   /*var period = 300;*/
  if(timeLimit==1){
	  var period = 60;
  }else if(timeLimit==5){
	  var period = 300;
  }else if(timeLimit==15){
	  var period = 900;
  }else if(timeLimit==30){
	  var period = 1800;
  }else if(timeLimit==60){
	  var  period = 3600;
  }else if(timeLimit==120){
	  var period =7200;
  }else if(timeLimit==180){
	  var period =10800;
  }else  if(timeLimit==240){
	  var period = 14400;
  }else  if(timeLimit==720){
	  var period = 43200;
  }else if(timeLimit==1440){
	  var period = 86400;
  }
  if(coinName==null){
	  var currency=currencyPair.substring("_").substring(0,3);
	  var coin=currencyPair.substring("_").substring(4);
	   $('#chartCoinName').html(currency+"_"+coin);
	   $('#kECharts').html('<p class="text-center p-t-lg text-lg">加载中...<p>');
	   $.ajax({
		   type: 'get',
		   url:chartApisUrl+"/ChartData_Kine", /*'http://poloniex.unionchain.org/api/poloniex/returnChartData',*/
		   data: {
			  /* userId:userid,*/
			   exchangeName:getPlatform(),
			   currency:currency,
			   Pair:coin,
			   period:period
		     /*key: getStorageJson('key'),
		     secret: getStorageJson('secret'),
		     currencyPair:currencyPair,
		     period: period,
		     start: start,
		     end:'9999999999'*/
		   },
		   dataType: 'json',
		   success: function(data) {
		    var timeArr = new Array();

		    var kDataArr = new Array();
		    for (var i = 0; i < data.entity.length; i++) {
		    	
		      var dataRow = new Array();
		          // 开盘，收盘，最低，最高
		          dataRow[0]=data.entity[i].open;
		          dataRow[1]=data.entity[i].close;
		          dataRow[2]=data.entity[i].low;
		          dataRow[3]=data.entity[i].high;
		          
		          kDataArr[i]=dataRow;
		          timeArr[i] = timestampformat(parseInt(data.entity[i].date)*1000).substring(5);
		        }

		        var myChart = echarts.init(document.getElementById('kECharts')); 

		        myChart.setOption({
		          dataZoom : {
		            show : true,
		            realtime: true,
		            start : 90,
		            end : 100,
		            handleColor:'#2196f3',
		            handleStyle: {
		                    borderColor: "#2196f3",
		                    borderWidth: "2",
		                    shadowBlur: 2,
		                    background: "#999",
		                    shadowColor: "#aaa",
		            }
		          },
		          grid: {
		            x: 55,
		            y: 5,
		            x2:10
		          },
		          
		          xAxis : [
		          {
		            type : 'category',
		            boundaryGap : true,
		            axisTick: {onGap:false},
		            splitLine: {show:false},
		            data : timeArr,
		            axisLine:{
		                lineStyle:{
		                    color:'#2196f3',
		                    width:2,
		                }
		            }
		          }
		          ],
		          yAxis : [
		          {
		            type : 'value',
		            scale: true,
		            boundaryGap: [0.01, 0.01],
		            axisLine:{
		                lineStyle:{
		                    color:'#2196f3',
		                    width:2,
		                }
		            }
		          }
		          ],
		          series : [
		          {
		            name:currencyPair,
		            type:'k',
		            data: kDataArr
		          }
		          ]
		        });
		      }
		    });
	  }else{
		  $('#chartCoinName').html(currencyPair+"_"+coinName);
		  $('#kECharts').html('<p class="text-center p-t-lg text-lg">加载中...<p>');
		  $.ajax({
			   type: 'get',
			   url:chartApisUrl+"/ChartData_Kine", /*'http://poloniex.unionchain.org/api/poloniex/returnChartData',*/
			   data: {
				  /* userId:userid,*/
				   exchangeName:getPlatform(),
				   currency:currencyPair,
				   Pair:coinName,
				   period:period
			     /*key: getStorageJson('key'),
			     secret: getStorageJson('secret'),
			     currencyPair:currencyPair,
			     period: period,
			     start: start,
			     end:'9999999999'*/
			   },
			   dataType: 'json',
			   success: function(data) {
			    var timeArr = new Array();

			    var kDataArr = new Array();
			    for (var i = 0; i < data.entity.length; i++) {
			    	
			      var dataRow = new Array();
			          // 开盘，收盘，最低，最高
			          dataRow[0]=data.entity[i].open;
			          dataRow[1]=data.entity[i].close;
			          dataRow[2]=data.entity[i].low;
			          dataRow[3]=data.entity[i].high;
			          
			          kDataArr[i]=dataRow;
			          timeArr[i] = timestampformat(parseInt(data.entity[i].date)*1000).substring(5);
			        }

			        var myChart = echarts.init(document.getElementById('kECharts')); 

			        myChart.setOption({
			          dataZoom : {
			            show : true,
			            realtime: true,
			            start : 90,
			            end : 100,
			            handleColor:'#2196f3',
			            handleStyle: {
			                    borderColor: "#2196f3",
			                    borderWidth: "2",
			                    shadowBlur: 2,
			                    background: "#999",
			                    shadowColor: "#aaa",
			            }
			          },
			          grid: {
			            x: 55,
			            y: 5,
			            x2:10
			          },
			          
			          xAxis : [
			          {
			            type : 'category',
			            boundaryGap : true,
			            axisTick: {onGap:false},
			            splitLine: {show:false},
			            data : timeArr,
			            axisLine:{
			                lineStyle:{
			                    color:'#2196f3',
			                    width:2,
			                }
			            }
			          }
			          ],
			          yAxis : [
			          {
			            type : 'value',
			            scale: true,
			            boundaryGap: [0.01, 0.01],
			            axisLine:{
			                lineStyle:{
			                    color:'#2196f3',
			                    width:2,
			                }
			            }
			          }
			          ],
			          series : [
			          {
			            name:currencyPair,
			            type:'k',
			            data: kDataArr
			          }
			          ]
			        });
			      }
			    });
	  }



  
}

function changeEx(name) {
  storeJsonObject(PLATFORM,name);
  $('#platformTag').html(name);
  window.location.href = 'trade.html';
}


function createSocketServer(clientId) {
       var ws = null;

       // 设定WebSocket,注意协议是ws，请求是指向对应的WebSocketServlet的
       var url = "wss://114.55.234.81:8081/socket.io?clientId=" + clientId;

       // 创建WebSocket实例，下面那个MozWebSocket是Firefox的实现
       if ('WebSocket' in window) {
           ws = new WebSocket(url);
       } else if ('MozWebSocket' in window) {
           ws = new MozWebSocket(url);
       } else {
           return null;
       }
       return ws;
}

function a(){
   var ws = createSocketServer(clientId);
       if (ws != null) {
           // 收到服务器发送的文本消息, event.data表示文本内容
           ws.onmessage = function(event) {
               if (event.data == (clientId + '_1')) {
                   $('.alert').removeClass('hide');
                   $('.alert').fadeOut(6000);
               }
           };
       }
}
//关注币种
function boolmark(name,platform){
	console.log(userid,name,platform);
	if(name!=""&&platform!=""){
		$.ajax({
			url:bookmarkURL+"/AddBookMark",
			type:"post",
			data:{
				userid:userid,
				platform:platform,
				coin:name
			},
			dataType:"json",
			success:function(data){
				/*console.log(data);*/
			}
		});
	}
}