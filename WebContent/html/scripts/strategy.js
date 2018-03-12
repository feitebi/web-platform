

var userid = localStorage.getItem("userid");
var strategyUrl = "/rest/strategy";
var exurl="/rest/apis";
var strategyId =0;
var platform = "";
var coinName = "";
var askOrBid = "";
var priceRule = "";
var ProfitRatio = 0;
var askPrice = 0;
var BidPrice = 0;
var qty = 0;
var topOrlow = 0;
var strategyFlag = "";
var Price4Days = 0;


//窗体加载
$(function(){
	//读取用户的交易所
	getexchange();	
	//读取Balance
	$(".platformlist").change(function(){
		var platform=$(this).val();
		if(platform!=null){
			getBalance(platform);
		}
	});
	$(".inputnum").focus(function(){
		var coinname=$(".coinlist").val();
		getavailable(coinname);
	});
	/*$(".inputnum").blur(function(){
		$(".nummsg").html("");
	});*/
	//读取Ticker
	$(".nowprice").focus(function(){
		var coinname=$(".coinlist").val();
		var platform=$(".platformlist").val();
		getTicker(platform,coinname);
		
	});
});
//用户balance中的币
var coinlist;
//交易所的实时价格
var tickerlist;

//读取ticker
function getTicker(platform,coinname) {
	if(platform!=""&&coinname!=""){
		$.ajax({
			url:strategyUrl+"/getticker",
			type:'post',
			data:{
				platform:platform,
				userid:userid,
				coinname:coinname
				},
				dataType:'json',
				success:function(data){
					var d=data.entity.dataList;
					$.each(d,function(i,item){
						$(".nowmsg").html("当前价:"+item);
					})
				}
		});
	}
}

//清除当前价和数量提示
function clearNumAndPrice(){
	$(".nummsg").html("");
	$(".nowmsg").html("");
}


//读取balance
function getBalance(platform) {
	$.ajax({
		url:strategyUrl+"/getuserbalance",
		type:'post',
		data:{
			platform:platform,
			userid:userid
			},
		dataType:'json',
		success:function(data){
			coinlist = data.entity.dataList;
			$(".coinlist").html("");
			$.each(coinlist,function(i,item){
				$(".coinlist").append("<option value='"+item.currency+"'>"+item.currency+"</option>");
			});
		}
	});
}
//取得可用数量
function getavailable(coinname){
	$.each(coinlist,function(i,item){
		if(item.currency==coinname){
			$(".nummsg").html("可用量:"+parseFloat(item.available).toFixed(8));
		}
	});
	
}
//读取交易所信息
function getexchange() {
	console.log(userid);
	if(userid==""||userid==null){
		alert("请先登录");
	}
	else{
	$.ajax({
		url:exurl+"/exchanglist",
		type:'get',
		data:{userid:userid},
		dataType:'json',
		success:function(data){
			var d=data.entity.dataList;
			$.each(d,function(i,item){
				if(item.status=="1")
				{
				$(".platformlist").append("<option value='"+item.name+"'>"+item.name+"</option>");
				}
			});
			var platform=$(".platformlist").val();
			getBalance(platform);
		}
	});
	}
}


// 直接挂单
function directpush1() {
	var nummsg=$(".nummsg").text();
	var arr = nummsg.split(':');
	var num=parseFloat(arr[1]);
	var price= parseFloat($(".nowmsg").html());
	platform = $("#platform1").val();
	coinName = $("#coinname1").val();
	askOrBid = "Sell";
	priceRule = $("#pricerule").val() + "天最高价";
	ProfitRatio = $("#profit").val();
	topOrlow = $("#low").val();
	strategyFlag = "0";
	Price4Days = $("#pricerule").val();
	qty = $("#num1").val();
	if(qty==""||topOrlow==""){	
		$("#modalMsg").text("价格下限或数量不能为空!")
		$("#SellModal").modal("show");
	}
	else{
		
		if(qty<num){
		$.ajax({
			url:strategyUrl+"/smartSell",
			type:'post',
			data:{
				strategyId:strategyId,
				userId:userid,
				platform:platform,
				coinName:coinName,
				askOrBid:askOrBid,
				priceRule:priceRule,
				askPrice:askPrice,
				qty:qty,
				ProfitRatio:ProfitRatio,
				topOrlow:topOrlow,
				strategyFlag:strategyFlag,
				Price4Days:Price4Days
			},
			dataType:'json',
			success:function(data){
				if(data.entity.flag=='0'){
				$("#low").val("");
				$("#num1").val("");
				$("#modalMsg").text("挂单成功")
				$("#SellModal").modal("show");
				}
			}
		});
		}
		else{
			$("#modalMsg").text("可用数量不足!")
			$("#SellModal").modal("show");
			$(".nummsg").html("");
		}
		
	}
	
}
function directpush2() {
	var nummsg=$(".nummsg").text();
	var arr = nummsg.split(':');
	var num=parseFloat(arr[1]);
	var price= parseFloat($(".nowmsg").html());
	platform = $("#platform2").val();
	coinName = $("#coinname2").val();
	askOrBid = "Sell";
	askPrice=$("#absolutesell").val();
	strategyFlag = "0";
	Price4Days = $("#pricerule").val();
	qty = $("#num2").val();
	if(qty==""||askPrice==""){
		$("#modalMsg").text("价格或数量不能为空!")
		$("#SellModal").modal("show");
	}
	else{
		if(qty<num){
		$.ajax({
			url:strategyUrl+"/smartSell",
			type:'post',
			data:{
				strategyId:strategyId,
				userId:userid,
				platform:platform,
				coinName:coinName,
				askOrBid:askOrBid,
				priceRule:"",
				askPrice:askPrice,
				qty:qty,
				ProfitRatio:0,
				topOrlow:0,
				strategyFlag:strategyFlag,
				Price4Days:0
			},
			dataType:'json',
			success:function(data){
				if(data.entity.flag=='0'){
				$("#absolutesell").val("");
				$("#num2").val("");
				$("#modalMsg").text("挂单成功")
				$("#SellModal").modal("show");
				}
			}
		});
		}
		else{
			$("#modalMsg").text("可用数量不足!")
			$("#SellModal").modal("show");
			$(".nummsg").html("");
		}
	}
}
// 下一步
function nextsmart1() {
	var nummsg=$(".nummsg").text();
	var arr = nummsg.split(':');
	var num=parseFloat(arr[1]);
	var price= parseFloat($(".nowmsg").html());
	platform = $("#platform1").val();
	coinName = $("#coinname1").val();
	askOrBid = "Sell";
	priceRule = $("#pricerule").val() + "天最高价";
	ProfitRatio = $("#profit").val();
	topOrlow = $("#low").val();
	strategyFlag = "1";
	Price4Days = $("#pricerule").val();
	qty = $("#num1").val();
	if(qty==""||topOrlow==""){
		$("#modalMsg").text("价格下限或数量不能为空!")
		$("#SellModal").modal("show");
	}
	else{
		if(qty<num){
		$.ajax({
			url:strategyUrl+"/smartSell",
			type:'post',
			data:{
				strategyId:strategyId,
				userId:userid,
				platform:platform,
				coinName:coinName,
				askOrBid:askOrBid,
				priceRule:priceRule,
				askPrice:askPrice,
				qty:qty,
				ProfitRatio:ProfitRatio,
				topOrlow:topOrlow,
				strategyFlag:strategyFlag,
				Price4Days:Price4Days
			},
			dataType:'json',
			success:function(data){
				if(data.entity.flag>0){
				$("#low").val("");
				$("#num1").val("");
				localStorage.setItem("strategyId", data.entity.flag);
				window.location.href="smartBuy.html";		
				}
			}
		});
	}
	else{
		$("#modalMsg").text("可用数量不足!")
		$("#SellModal").modal("show");
		$(".nummsg").html("");
	}
	}
}
function nextsmart2() {
	var nummsg=$(".nummsg").text();
	var arr = nummsg.split(':');
	var num=parseFloat(arr[1]);
	var price= parseFloat($(".nowmsg").html());
	platform = $("#platform2").val();
	coinName = $("#coinname2").val();
	askOrBid = "Sell";
	askPrice=$("#absolutesell").val();
	strategyFlag = "1";
	Price4Days = $("#pricerule").val();
	qty = $("#num2").val();
	if(qty==""||askPrice==""){
		$("#modalMsg").text("价格或数量不能为空!")
		$("#SellModal").modal("show");
	}
	else{
		if(qty<num){
		$.ajax({
			url:strategyUrl+"/smartSell",
			type:'post',
			data:{
				strategyId:strategyId,
				userId:userid,
				platform:platform,
				coinName:coinName,
				askOrBid:askOrBid,
				priceRule:"",
				askPrice:askPrice,
				qty:qty,
				ProfitRatio:0,
				topOrlow:0,
				strategyFlag:strategyFlag,
				Price4Days:0
			},
			dataType:'json',
			success:function(data){
				if(data.entity.flag>0){
				$("#absolutesell").val("");
				$("#num2").val("");
				localStorage.setItem("strategyId", data.entity.flag);
				window.location.href="smartBuy.html";
				}
			}
		});
		}
		else{
			$("#modalMsg").text("可用数量不足!")
			$("#SellModal").modal("show");
			$(".nummsg").html("");
		}
	}
}
//买入
function buyin1(){
	var strategyID=localStorage.getItem("strategyId");
	if(strategyID!=""){
		strategyId=strategyID;
	}
	platform = $("#platform1").val();
	coinName = $("#coinname1").val();
	askOrBid = "Buy";
	priceRule = $("#pricerule").val() + "天最低价";
	topOrlow = $("#top").val();
	strategyFlag = "0";
	Price4Days = $("#pricerule").val();
	qty = $("#num1").val();
	if(qty==""||topOrlow==""){
		$("#modalMsg").text("价格上限或数量不能为空!")
		$("#sbuyModal").modal("show");
		
	}
	else{
		$.ajax({
			url:strategyUrl+"/smartBuy",
			type:'post',
			data:{
				strategyId:strategyId,
				userId:userid,
				platform:platform,
				coinName:coinName,
				askOrBid:askOrBid,
				priceRule:priceRule,
				BidPrice:0,
				qty:qty,
				topOrlow:topOrlow,
				strategyFlag:strategyFlag,
				Price4Days:Price4Days
			},
			dataType:'json',
			success:function(data){
				if(data.entity.flag=='1'){
				$("#top").val("");
				$("#num1").val("");
				$("#modalMsg").text("下单成功")
				$("#sbuyModal").modal("show");
				
				}
			}
		});
	}
	
}
function buyin2(){
	var strategyID=localStorage.getItem("strategyId");
	if(strategyID!=""){
		strategyId=strategyID;
	}
	platform = $("#platform2").val();
	coinName = $("#coinname2").val();
	askOrBid = "Buy";
	BidPrice=$("#absolutebuy").val();
	strategyFlag = "0";
	qty = $("#num2").val();
	if(qty==""||BidPrice==""){
		$("#modalMsg").text("价格上限或数量不能为空!")
		$("#sbuyModal").modal("show");
	}
	else{
		$.ajax({
			url:strategyUrl+"/smartBuy",
			type:'post',
			data:{
				strategyId:strategyId,
				userId:userid,
				platform:platform,
				coinName:coinName,
				askOrBid:askOrBid,
				priceRule:"",
				BidPrice:BidPrice,
				qty:qty,
				topOrlow:0,
				strategyFlag:strategyFlag,
				Price4Days:0
			},
			dataType:'json',
			success:function(data){
				if(data.entity.flag=='1'){
				$("#absolutebuy").val("");
				$("#num2").val("");
				$("#modalMsg").text("下单成功")
				$("#sbuyModal").modal("show");
				}
			}
		});
	}	
}

