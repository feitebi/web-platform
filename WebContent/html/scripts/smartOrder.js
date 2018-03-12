
var userid=localStorage.getItem("userid");
var indexUrl="/rest/index";
var smartUrl="/rest/strategy";

var smartstart=0;
var smartlimit=4;
var smartmax=0;

$(function(){
	selectapis();
	showSmartDeal(smartstart,smartlimit);
})




function smartNext() {
	smartstart+=smartlimit;
	if(smartstart>=smartmax){
		smartstart=smartmax-smartlimit;
		if(smartstart<=0){
			smartstart=0;
		}
	}
	showSmartDeal(smartstart,smartlimit);
}
function smartLast() {
	smartstart-=smartlimit;
	if(smartstart<=0){
		smartstart=0;
	}
	showSmartDeal(smartstart,smartlimit);
}


//货币btc价格
var btctoCny=0;
//显示智能合约
function showSmartDeal(smartstart,smartlimit) {
	$.ajax({
		url:smartUrl+"/getsmartorders",
		type:'post',
		data:{userid:userid,
			start:smartstart,
			limit:smartlimit
		},
		dataType:'json',
		success:function(data){
			var d=data.entity.dataList;
			var text="";
			var rowccount=0;
			if(d.length>0){
			$.each(d,function(i,item){
				if(item.askOrBid=='Sell'){
					text+="<tr style='height:35px;'><td class='text-danger'>卖</td>";
				}
				else{
					text+="<tr style='height:35px;'><td class='text-success'>买</td>";
				}
				text+="<td>"+smallToBig(item.platform)+"</td><td>"+item.coinName+"</td>" +
						"<td id='btcNow_"+smallToBig(item.platform)+"_"+item.coinName+"_"+item.id+"'>加载中...</td>";
						
				if(item.priceRule==""){
					text+="<td>"+item.bidPrice+"</td>" +
					"<td>指定价格</td>";
				}
				else{
					text+="<td>"+item.topOrlow+"</td>" +
					"<td>"+item.priceRule+"</td>";
				}	
				text+="<td>"+datetimeFormat_1(item.createTime)+"</td><td><a class='btn btn-xs btn-danger btn-outline text-danger' " +
						"onclick='delsmart(\""+item.id+"\")' >取消</td></tr>";
				rowccount++;
				getTicker(smallToBig(item.platform),item.coinName,item.id);
				});
			if(rowccount<4){
				for(var i=0;i<4-rowccount;i++){
					text+="<tr style='height:35px;'><td>&nbsp;</td>" +
							"<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>" +
							"<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>";
					}
			}
			$("#smartdealList").html(text);
			smartmax=data.entity.totalCount;
			}else{
				$("#smartdealContent").html("<div style='height:185px'><br><br><p class='h6 text-center p-t-md' style='color: #C0C0C0'>您尚未在本平台制定智能策略!</p>" +
				"</div>");
			}
		}
	});
}
//获取当前价
function getTicker(platform,coinName,id){
	if(platform!=""&&coinName!=""){
		$.ajax({
			url:smartUrl+"/getticker",
			type:"post",
			data:{
				platform:platform,
				userid:userid,
				coinname:coinName
			},
			dataType:"json",
			success:function(data){
				var d=data.entity.dataList[0];
				if(d!=null){
					$("#btcNow_"+platform+"_"+coinName+"_"+id+"").html(d);
				}
				
			}
		});
	}
}


//取消智能合约
function cancel(id){
	if(id!=""){
	$.ajax({
		url:smartUrl+"/cancelSmartorder",
		type:'post',
		data:{
			id:id,
			userid:userid
			},
		dataType:'json',
		success:function(data){
			if(data.entity.flag=="1"){
				showSmartDeal();
			}
		}
	});
	}
}
var whichFresh="";
function hisFresh(){
	whichFresh="history";
	$("#orderhistory").html("<tr id='historyCircle'>"+
            "<td colspan='8'>"+
            "<br><br><br>"+
            "<p class='h1 text-center '>"+
	          "<img  src='./logo/loging.gif'></p> "+
	          "<br><br>"+
	          "</td>"+
	          "</tr>");
	selectapis();
	
}
function dealFresh(){
	whichFresh="deal";
	$("#dealOrderList").html("<tr id='dealCircle'>"+
            "<td colspan='8'>"+
            "<br><br><br>"+
            "<p class='h1 text-center '>"+
	          "<img  src='./logo/loging.gif'></p> "+
	          "<br><br>"+
	          "</td>"+
	          "</tr>");
	selectapis();
}

//查询API
function selectapis(){
	$.ajax({
		url:indexUrl+"/platform_api",
		type:"get",
		data:{userId:userid,
			 selectAll:'yes'
		},
		dataType:"json",
		success:function(data){
			var d=data.entity.dataList;
			$.each(d,function(i,item){
				var platform=item.logo;
				var key=item.apiKey;
				var Secret=item.apiSecret;
				if(whichFresh==""){
				showHistoryOrders(userid,platform,key,Secret);
				showDealOrder(userid,platform,key,Secret);
				
				}
				else{
					if(whichFresh=="history"){
						showHistoryOrders(userid,platform,key,Secret);
					}
					else{
						showDealOrder(userid,platform,key,Secret);
					}
				}
			});	
		}
	});
}
//显示历史订单
function showHistoryOrders(userid,platform,key,Secret){
	if(userid!=""&&platform!=""&&key!=""&&Secret!=""){
		$.ajax({
			url:smartUrl+"/getTradeHistory",
			type:"post",
			data:{
				userid:userid,
				platform:platform,
				apikey:key,
				apisecret:Secret
			},
			dataType:"json",
			success:function(data){
				var d=data.entity;
				$("#historyCircle").remove();
				var text=$("#orderhistory").html();
				if(d!=null){
				$.each(d,function(i,item){
					if(item.length>0){
						$.each(item,function(j,iten){	
							var num=parseFloat(iten.tradableAmount).toFixed(8);
							var price=parseFloat(iten.price).toFixed(8);
							var amount=parseFloat(num*price).toFixed(8);
							if(iten.type=='ASK'){
								text+="<tr><td class='text-danger'>卖</td>";
							}
							else{
								text+="<tr><td class='text-success'>买</td>";
							}
							text+="<td>"+smallToBig(platform)+"</td><td>"+iten.currencyPair+"</td><td>"+num+"</td>" +
									"<td>"+price+"</td><td>"+amount+"</td><td>"+datetimeFormat_1(iten.timestamp)+"</td>";
						});	
					}	
				});
				$("#orderhistory").html(text);
				}
				else{
					
				}
			}
			
		
		});
	}
}
//显示正在挂单
function showDealOrder(userid,platform,key,Secret){
	if(userid!=""&&platform!=""&&key!=""&&Secret!=""){
		$.ajax({
			url:smartUrl+"/getMyAllOpenOrderList",
			type:"post",
			data:{
				userid:userid,
				platform:platform,
				apikey:key,
				apisecret:Secret
			},
			dataType:"json",
			success:function(data){
				var d=data.entity;
				$("#dealCircle").remove();
				var text=$("#dealOrderList").html();
				if(d!=null){
				$.each(d,function(i,item){
					if(item.length>0){
						$.each(item,function(j,iten){	
							var num=parseFloat(iten.tradableAmount).toFixed(8);
							var price=parseFloat(iten.limitPrice).toFixed(8);
							var amount=parseFloat(num*price).toFixed(8);
							if(iten.type=='ASK'){
								text+="<tr><td class='text-danger'>卖</td>";
							}
							else{
								text+="<tr><td class='text-success'>买</td>";
							}
							text+="<td>"+smallToBig(platform)+"</td><td>"+iten.currencyPair+"</td><td>"+num+"</td>" +
									"<td>"+price+"</td><td>"+amount+"</td><td>"+fermitTime(iten.timestamp)+"</td>";
						});	
					}	
				});
				$("#dealOrderList").html(text);
				}
				else{
					
				}
			}
		});
	}
}
//格式化时间
function fermitTime(time){
	var now = new Date(time);
	var year = now.getFullYear();
	var mon = now.getMonth()+1;
	var date= now.getDate();
	if(mon<10){
	mon = '0'+mon;
	};
	if(date<10){
	date = '0'+date;
	}
	var postDate = year+'-'+mon+'-'+date;
	return postDate;
	}
function datetimeFormat_1(longTypeDate){ 
	  var datetimeType = ""; 
	  var date = new Date(); 
	  date.setTime(longTypeDate); 
	  datetimeType+= date.getFullYear();  //年 
	  datetimeType+= "-" + getMonth(date); //月  
	  datetimeType += "-" + getDay(date);  //日 
	  datetimeType+= "  " + getHours(date);  //时 
	  datetimeType+= ":" + getMinutes(date);   //分
	  datetimeType+= ":" + getSeconds(date);   //分
	  return datetimeType;
	} 
	//返回 01-12 的月份值  
	function getMonth(date){ 
	  var month = ""; 
	  month = date.getMonth() + 1; //getMonth()得到的月份是0-11 
	  if(month<10){ 
	    month = "0" + month; 
	  } 
	  return month; 
	} 
	//返回01-30的日期 
	function getDay(date){ 
	  var day = ""; 
	  day = date.getDate(); 
	  if(day<10){ 
	    day = "0" + day; 
	  } 
	  return day; 
	}
	//返回小时
	function getHours(date){
	  var hours = "";
	  hours = date.getHours();
	  if(hours<10){ 
	    hours = "0" + hours; 
	  } 
	  return hours; 
	}
	//返回分
	function getMinutes(date){
	  var minute = "";
	  minute = date.getMinutes();
	  if(minute<10){ 
	    minute = "0" + minute; 
	  } 
	  return minute; 
	}
	//返回秒
	function getSeconds(date){
	  var second = "";
	  second = date.getSeconds();
	  if(second<10){ 
	    second = "0" + second; 
	  } 
	  return second; 
	}
	//火币参照
	function huobi() {
		$.ajax({
			url:"http://api.huobi.com/staticmarket/detail_btc_json.js",
			dataType:'json',
			success:function(data){
				btctoCny=data.p_new;
			}
		});
		
	}
	//交易所名称转换为首字母大写
	function smallToBig(s){
		 var i, ss = s.toLowerCase().split(/\s+/);  
		    for (i = 0; i < ss.length; i++) {  
		        ss[i] = ss[i].slice(0, 1).toUpperCase() + ss[i].slice(1);  
		    }  
		    return ss.join(' ');  
	}
	