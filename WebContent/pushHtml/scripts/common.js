function getUserId() {
 return "2021";
}
var userid=getUserId();
//*************************************
/**url**/
//手续费
var commssionUrl="http://localhost:8080/XChange/rest/Commssionratio";
//挂单
var PushUrl="http://localhost:8080/XChange/rest/Push";
//提现
var CashUrl="http://localhost:8080/XChange/rest/adminPush";
//公共挂单
var Public_PutUrl="http://localhost:8080/XChange/rest/Push";
//充值
var PayCodeUrl="http://localhost:8080/XChange/rest/topup";
//冻结列表-管理员
var FizzonUrl="http://localhost:8080/XChange/rest/adminPush";
//购买
var BuyUrl="http://localhost:8080/XChange/rest/Push";
//提现申请--管理员
var AdminCashUrl="http://localhost:8080/XChange/rest/adminPush";

//**************************************
//显示余额
function showLastBlance(){
	$.ajax({
		url:CashUrl+"/getUserBalance",
		type:"get",
		data:{userid:userid},
		dataType:'json',
		success:function(data){
			var d=data.entity;
			$("#username").text(d.username);
			$("#getuserblance").text(parseFloat(d.money-d.fundMoney).toFixed(4));
			$("#fundBlance").text(parseFloat(d.fundMoney).toFixed(4));
		}
	});
}
//显示可用BDH
function freedomBDH(){
	//钱包的BDH
	var walletBDH="10000"; 
	//冻结的BDH
	var freezing;
	$.ajax({
		url:CashUrl+"/getfreezing",
		type:'get',
		data:{userid:userid},
		dataType:'json',
		success:function(data){
			freezing=data.entity.fundBDH;
			$("#username").text(data.entity.username);
			$("#freedomBDH").text(parseFloat((walletBDH-freezing)).toFixed(4));
			$("#freezingbdh").text(parseFloat(freezing).toFixed(4));
		}
	});
}
//挂单
var start=0;
var limit=5;
var max=0;
//改动数量
function changePushnum(){	
	Pushnum=$("#bdhNum").val();
	Pushprice=$("#bdhPrice").val();
	$("#sum").text(parseFloat(Pushnum*Pushprice).toFixed(4));
	if(Pushnum!=""){
		$("#PushNumMsg").html("");
	if(Pushprice!=""){
		$("#surePushButton").removeAttr("disabled");
	}
	else{
		$("#surePushButton").attr("disabled","disabled");
	}
	}
	else{
		$("#PushNumMsg").css('color','red');
		$("#PushNumMsg").html("数量不能为空");
		$("#surePushButton").attr("disabled","disabled");
	}
}
//改动价格
function changePushPrice() {
	Pushnum=$("#bdhNum").val();
	Pushprice=$("#bdhPrice").val();
	$("#sum").text(parseFloat(Pushnum*Pushprice).toFixed(4));
	if(Pushprice!=""){
		$("#PushPriceMsg").html("");
		if(Pushnum!=""){
			$("#surePushButton").removeAttr("disabled");
		}
		else{
			$("#surePushButton").attr("disabled","disabled");
		}
	}
	else{
		$("#surePushButton").attr("disabled","disabled");
		$("#PushPriceMsg").css('color','red');
		$("#PushPriceMsg").html("单价不能为空");
	}
}
//显示我的挂单
function showMydata(){	
	$.ajax({
		url:PushUrl+'/PushList',
		type:'get',
		data:{	
			start:start,
			limit:limit,
			userid:userid
		},
		dataType:'json',
		success:function(data){
			var d=data.entity.dataList;
			var text="";
			$.each(d,function(i,item){
				$("#myPushtbody").html(text+="<tr id='tr"+item.pid+"'><td>BDH</td>"+
						"<td>"+item.leftqty+"</td>"+
						"<td>"+item.askPrice+"</td>"+
						"<td>"+(item.leftqty*item.askPrice)+"￥</td>"+
						"<td><a class='btn btn-xs btn-danger btn-outline text-danger' onclick='delPush(\""+item.pid+"\"  )'>取消</a></td></tr>");
						max=data.entity.totalCount;
			});
		}
	});
}
//确认挂单
function guadan(){
	Pushnum=$("#bdhNum").val();
	Pushprice=$("#bdhPrice").val();
	if(Pushnum==""||Pushprice==""){
		$("#pushh5").html('挂单数量或单价不能为空');
		$("#pushModal").modal('show');
	}
	else{
		$.ajax({
			url:PushUrl+'/PushPut',
			type:'post',
			data:{
				userid:userid,
				pid:id,
				formAddr:formAddr,
				formKey:formKey,		
				askPrice:Pushprice,
				leftqty:Pushnum
			},
			dataType:'json',
			success:function(data){
				$("#pushh5").html('挂单成功');
				freedomBDH();
				if(data.entity.flag!=1){
					$("#bdhNum").val("");
					$("#bdhPrice").val("");
					$("#sum").text("0");
					showMydata();
					$("#pushh5").html('修改成功');
					$("#pushModal").modal('show');
					
				}
				if(data.entity.flag!=2){
					$("#bdhNum").val("");
					$("#bdhPrice").val("");
					$("#sum").text("0");
					showMydata();	
					$("#pushModal").modal('show');
				}
			}
		});
	}
	}

//取消挂单
function delPush(id){
	$.ajax({
		url:PushUrl+'/upPushOff',
		type:'post',
		data:{
			pid:id
		},
		dataType:'json',
		success:function(data){
			if(data.entity.flag==1){
				showMydata();
				freedomBDH();
			}
		}
	});
}
//修改挂单
/*function updatePush(sid,price,num){
	$("#bdhNum").val(num);
	$("#bdhPrice").val(price);
	$("#sum").text(num*price);
	id=sid;
}*/


//提现
//改动账号
function changePay() {
	var payAccount=$("#payAccount").val();
	var goden=$("#goden").val();
	if(payAccount!=""){
		$("#payMsg").html("");
		$("#cashbutton").removeAttr("disabled");
		if(goden!=""){
			$("#cashbutton").removeAttr("disabled");
		}
		else{
			$("#cashbutton").attr("disabled","disabled");
		}
	}
	else{
		$("#cashbutton").attr("disabled","disabled");
		$("#payMsg").css('color','red');
		$("#payMsg").html("支付宝账号不能为空");
	}
}

//改动数量
function changenum(){	
	var goden=$("#goden").val();
	var payAccount=$("#payAccount").val();
	var userblance=$("#getuserblance").text();
	$.ajax({
		url:commssionUrl+"/Commssionratio",
		type:"get",
		dataType:'json',
		success:function(data){
			var commssion=data.entity.dataList[0].withdrawRatio;	
			$("#poundage").text((goden*commssion).toFixed(4));
		}
	});
	var shouxu=$("#poundage").text();
	if(goden!=""){
	var add=(parseFloat(goden)+parseInt(shouxu*10));
	if(add.toFixed(4)<=parseFloat(userblance)){
		$("#cashbutton").removeAttr("disabled");
		$("#cashMsg").html("");
	}
	else{
		$("#cashbutton").attr("disabled","disabled");
		$("#cashMsg").css('color','red');
		$("#cashMsg").html("输入超额");
	}
	if(payAccount!=""){
		$("#cashbutton").removeAttr("disabled");
	}
	else{
		$("#cashbutton").attr("disabled","disabled");
	}
	}
	else{
		$("#cashMsg").css('color','red');
		$("#cashMsg").html("数量不能为空");
		$("#cashbutton").attr("disabled","disabled");
	}
}

//提现操作
function tixian(){	
	var payAccount=$("#payAccount").val();//去往账号
	var goden=$("#goden").val();//提现金额
	var shouxu=$("#poundage").text();
			$.ajax({
			url:CashUrl+"/addWithdraw",
			type:'post',
			data:{
				userid:userid,
				amount:goden,
				toAccount:payAccount,
				bankName:'支付宝',
				commission:shouxu
			},
			dataType:'json',
			success:function(data){
				var f=data.entity.flag;
				if(f==1){
					$("#cashh5").html('您的提现申请已成功提交');	
					$("#cashModal").modal('show');
					$("#payAccount").val("");
					$("#goden").val("");
					$("#poundage").text("");
					showcashing();
					showLastBlance();
					$("#cashbutton").attr("disabled","disabled");
					}
				}
			});
}
//显示申请中列表
function showcashing(){
	
	$.ajax({
		url:AdminCashUrl+"/applying",
		type:'GET',
		data:{
			  start:start, 
			  limit:limit,
			  userid:userid
			  },
		dataType:'json',
		success:function(data){
			var d=data.entity.dataList;
			 var text="";
			 console.log(data);
			$.each(d,function(i,item){
				$("#cashingList").html(text+="<tr>" +
						"<td>"+item.toAccount+"</td>" +
						"<td>"+(item.amount-item.commission)+"</td>" +
						"<td>"+datetimeFormat_1(item.createTime)+"</td>" +
						"<td>申请中</td>" +
						"<td><a onclick='deloutcash(\""+item.wid+"\")' class='btn btn-xs btn-danger btn-outline text-danger'>取消申请</a></td>" +
						"</tr>");
					max=data.entity.totalCount;
			})
		}
	});	 
}

//取消申请提现
function deloutcash(wid){
	$.ajax({
		url:AdminCashUrl+"/isApply",
		type:'post',
		data:{
			wid:wid
		},
		dataType:"json",
		success:function(data){
			var f=data.entity.flag;
			if(f==1){
				showcashing();
				showLastBlance();
			}
		}
	});
}

//显示提现完成
function showcashed(){
	$.ajax({
		url:AdminCashUrl+"/okapply",
		type:'GET',
		data:{
			  start:start, 
			  limit:limit,
			  userid:userid
			  },
		dataType:'json',
		success:function(data){
			var d=data.entity.dataList;
			 var text="";
			$.each(d,function(i,item){
				$("#cashedList").html(text+="<tr>" +
						"<td>"+item.toAccount+"</td>" +
						"<td>"+(item.amount-item.commission)+"</td>" +
						"<td>"+datetimeFormat_1(item.createTime)+"</td>" +
						"<td>已完成</td>" +
						"</tr>");
				max=data.entity.totalCount;
			})
		}
	});	 
	
}

//公共挂单

//显示公共挂单
function showPublic_Putdata(){
	$.ajax({
			url:Public_PutUrl+'/PushAllList',
			type:'get',
			data:{
				start:start,
				limit:limit
			},
			dataType:'json',
			success:function(data){
				var d=data.entity.dataList;
				var text="";
				
				$.each(d,function(i,item){
					var b=item.qty+item.leftqty;
					var a=item.leftqty/b;
					if(item.leftqty>0){
						text+="<tr><td>"+item.name+"</td>"+
						"<td>BDH</td>"+
						"<td>"+
						"  <div class='progress'>"+item.leftqty+" <div class='progress-bar primary' style='width: "+a*100+"%;'>"+item.qty+"</div> </div>"+
						"</td>"+
						"<td>"+item.askPrice+"</td>"+
						"<td>"+parseFloat(item.leftqty*item.askPrice).toFixed(4)+"￥</td>"+
						"<td>";
						if(item.userId!=userid){
						text+="<a  href='buy.html?pid="+item.pid+"' class='btn rounded b-primary text-primary'>购买</a>";
						}
						text+="</td></tr>";
					}
						$("#allPushtbody").html(text);
					max=data.entity.totalCount;
				});
			}
		});
}


//充值


//充值操作
function chong(){
var code=$("#code").val();
var amount=$("#cashnum").val();
var twoNum=parseInt($("#twocode").text())/100;
var trueamount=parseInt(amount)+twoNum;
$.ajax({
		url:PayCodeUrl+'/Voucher',
		type:'post',
		data:{
			userid:userid,
			amount:trueamount,
			rechargeCode:code
		},
		dataType:'json',
		success:function(data){
			if(data.entity.flag){
				$("#cashnum").val("");
				$("#fillAmount").val(trueamount);
				$("#SixCode").val(code);
				$("#cashdiv").css("display","none");
				$("#codediv").css("display","block");
			}
		}	
	});
}
//6位随机数
function suiji() 
{ 
var Num=""; 
for(var i=0;i<6;i++) 
{ 
Num+=Math.floor(Math.random()*9); 
} 
$("#code").val(Num+""); 
}; 
//充值2位随机数
function suijitwo() 
{ 
var Num=""; 
for(var i=0;i<2;i++) 
{ 
Num+=Math.floor(Math.random()*8)+1; 
} 
$("#twocode").text(Num+""); 
}; 

//提现申请--管理员

var adminid="1001";

//显示所有用户的申请提现信息
function showwithdrawList(start,limit){
$.ajax({
	url:AdminCashUrl+"/getWithdrawAll",
	type:'GET',
	data:{
		  start:start, 
		  limit:limit
		  },
	dataType:'json',
	success:function(data){
		
		var d=data.entity.dataList;
		 var text="";
		$.each(d,function(i,item){
			
			if(item.applyFlag=="0"){
				 $("#AllList").html(text+=" <tr> <td id='wid' style='display: none;'>"+item.wid+"</td> <td>"+item.name+"</td> <td>"+item.toAccount+"</td> <td>"+(item.amount-item.commission)+"</td>"+ 
		                    "<td>"+datetimeFormat_1(item.createTime)+"</td> <td><span class='label yellow primary'>申请中</span></td> <td><button onclick='showModal("+item.wid+","+(item.amount-item.commission)+")' class='btn btn-outline rounded b-primary text-primary b-2x'>打款</button></td></tr>");
	 
          }else if(item.applyFlag=="1"){
          	 $("#AllList").html(text+=" <tr> <td>"+item.name+"</td> <td>"+item.toAccount+"</td> <td>"+(item.amount-item.commission)+"</td>"+ 
                       "<td>"+datetimeFormat_1(item.createTime)+"</td> <td> <span class='label green primary'>打款成功</span></td> <td><button onclick='showModal("+item.wid+","+(item.amount-item.commission)+")' disabled='' class='btn rounded  btn-fw'>已打款</button></td></tr>");
          }else{
          	 $("#AllList").html(text+=" <tr> <td>"+item.name+"</td> <td>"+item.toAccount+"</td> <td>"+(item.amount-item.commission)+"</td>"+ 
                       "<td>"+datetimeFormat_1(item.createTime)+"</td> <td> <span class='label deep-orange primary'>账户异常</span></td> <td><button onclick='showModal("+item.wid+""+(item.amount-item.commission)+")' disabled='' class='btn rounded btn-fw'>拒绝打款</button></td></tr>");
          	 
          }
			 max=item.totalCount;
		})
	}
});
}
//显示打款MODAL
function showModal(wid,cash){
	$("#wid").val(wid);
	$("#hiddenNum").val(cash);
	$("#m-a-f").modal('show');	
}
//检查打款金额
function checkcashnum(){
	var cashin=parseFloat($("#confirmAmount").val()).toFixed(4);
	var cashck=parseFloat($("#hiddenNum").val()).toFixed(4);
	if(cashin>cashck){
		$("#OutCashMsg").css('color','red');
		$("#OutCashMsg").html('输入超额');
		$("#sureCash").attr('disabled','disabled');
	}
	else{
		$("#OutCashMsg").html('');
		$("#sureCash").removeAttr("disabled");
	}
}
//确认打款
function Okmoney(){
	var id=$("#wid").val();
	var confirmAmount=$("#confirmAmount").val();
	var txId=$("#txId").val();
	var adminContext=$("#beizhu").val();
	$.ajax({
		  type: 'POST',
        url: AdminCashUrl+"/OkWithdraw",
        data: {
      	  adminid:adminid,
      	  confirmAmount:confirmAmount,
      	  txId:txId,
      	  adminContext:adminContext,
      	  wid:id
        },
        dataType: 'json',
        success: function(json){
        	$("#confirmAmount").val("");
        	$("#txId").val("");
        	$("#beizhu").val("");
         	  $('#m-a-f').modal('hide');
         	 showwithdrawList(start,limit);
        }
	})
} 
//拒绝打款
function Offmoney(){
		var id=$("#wid").val();
		var adminContext=$("#beizhu").val();
		$.ajax({
			  type: 'POST',
	          url: AdminCashUrl+"/OffWithdraw",
	          data: {
	        	  adminid:adminid,
	        	  adminContext:adminContext,
	        	  wid:id
	          },
	          dataType: 'json',
	          success: function(json){
	        	  	$("#confirmAmount").val("");
	          		$("#txId").val("");
	          		$("#beizhu").val("");
	          		$('#m-a-f').modal('hide');
	          		showwithdrawList(start,limit);
	          }
		})
	}
//购买


//跳转购买页面
function toBuy(id) {
	if(id==""||id==null){
		$("#buysubmit").attr("disabled","disabled");
		console.log("id为空");
	}
	else{
		$("#buysubmit").removeAttr("disabled");
	$.ajax({
		url:Public_PutUrl+'/getIdList',
		type:'GET',
		data:{
			pid:id
		},
		dataType:'json',
		success:function(data)
		{console.log(data);
			var d=data.entity.dataList;
			$.each(d,function(i,item){
				
				$("#master").val(item.name);
				$("#bdhNum").val(item.leftqty);
				$("#price").val(item.askPrice);
				num=item.leftqty;
				$("#qqq").val(num);
				askprice=item.askPrice;
				fromname=item.name;
				fromid=item.formUserId;
				pid=item.pid;
				qty=item.qty;
				bdhNum();
			});
		 }
		});
	}
}
//改动购买数量
function bdhNum(){
	var commssion=0;
	$.ajax({
		url:commssionUrl+"/Commssionratio",
		type:"get",
		dataType:'json',
		success:function(data){
			commssion=data.entity.dataList[0].pushOrderRatio;
			$("#commssion").text(parseFloat(commssion*(Nums*askprice)).toFixed(4));
		}
	});	
	var Nums=$("#bdhNum").val();
	var askprice=$("#price").val();
	$("#sum").text(parseFloat(Nums*askprice).toFixed(4));
	var userblance=$("#getuserblance").text();
	var sum=$("#sum").text();
	var shouxu=$("#commssion").text();
	var add=(parseFloat(sum)+parseFloat(shouxu*10));
	if(Nums!=""){
	if(add.toFixed(4)<=parseFloat(userblance)){
		$("#buyMsg").html("");
		$("#buysubmit").removeAttr("disabled");
	}
	else{
		$("#buyMsg").css('color','red');
		$("#buyMsg").html("已超额");
		$("#buysubmit").attr("disabled","disabled");
	}
	}
	else{
		$("#buysubmit").attr("disabled","disabled");
	}
	
	
}
//购买操作	
function buy(){
    var shu=$("#bdhNum").val();
    var nnn=$("#qqq").val();
	var comm=$("#commssion").text();
	var qtys=parseInt(shu)+parseInt(qty);
	var leftqtys=num-shu;
	var Nums=$("#bdhNum").val();
    if(shu==""){
    	$("#buyModal").modal('show');
    }
    else{
    	  $.ajax({
    			url:BuyUrl+'/BuyFill',
    			type:'post',
    			data:{
    				userid:userid,
    				price:askprice,
    				num:Nums,
    				commission:comm,
    				leftqty:leftqtys,
    				qty:qtys,
    				pid:pid,
    				formUserId:fromid		
    			},
    			dataType:'json',
    			success:function(data){
    				 if(data.entity.flag==2){
    					$("#master").val("");
    					$("#bdhNum").val("");
    					$("#price").val("");
    					$("#sum").text("0");
    				} 
    				 window.location.href='public_put.html';
    				 window.close();
    			}
    		}); 
    }
}
function getQueryString(name) { 
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
	var r = window.location.search.substr(1).match(reg); 
	if (r != null) return unescape(r[2]); return null; 
} 
//冻结列表-管理员

//管理员查充值
function showRechargeList(){
	var name=$("#rechargeName").val();
	var code=$("#rechargeCode").val();
	var amount=$("#rechargeAmount").val();
	$.ajax({
		url:PayCodeUrl+"/AllFillList",
		type:'get',
		data:{
			name:name,
			rechargeCode:code,
			amount:amount
		},
		dataType:"json",
		success:function(data){
			console.log(data);
			var d=data.entity.dataList;
			var text="";
			
			$.each(d,function(i,item){
				if(item.rechargeCode==code&&item.amount==amount){
					text+="<tr><td>"+item.name+"</td><td>"+item.phone+"</td><td>"+item.rechargeCode+"</td><td>"+datetimeFormat_1(item.createTime)+"</td><td>" +
							"<button onclick='OkRecharge("+item.id+","+item.userId+","+item.amount+")' class='btn btn-outline rounded b-primary text-primary b-2x'>充值</button></td></tr>";
				}
				else{	
					text+="<tr><td>"+item.name+"</td><td>"+item.phone+"</td><td>"+item.rechargeCode+"</td><td>"+datetimeFormat_1(item.createTime)+"</td><td> </td></tr>";
				}
			});
			$("#rechargeList").html(text);
		}
	});
}
//管理员确认充值
function OkRecharge(id,userid,amount){
	$("#OkFillModal").modal('show');
	$("#fillid").val(id);
	$("#filluserid").val(userid);
	$("#fillamount").val(amount);	 
}
function checkFill(){
	var amount = $("#fillamount").val();
	var FillAmount=$("#FillAmount").val();
	var serial=$("#serial").val();
	if(FillAmount!=""){
		$("#FillAmountMsg").html('');
		if(serial!=""){
			$("#FillSerialMsg").html('');
			if(FillAmount==amount){
				$("#surefillbutton").removeAttr('disabled');
				$("#FillAmountMsg").html('');
			}
			else{
				$("#surefillbutton").attr('disabled','disabled');
				$("#FillAmountMsg").css('color','red');
				$("#FillAmountMsg").html('输入金额不匹配，请重新输入');
			}
		}
		else{
			$("#surefillbutton").attr('disabled','disabled');
			$("#FillSerialMsg").css('color','red');
			$("#FillSerialMsg").html('流水号不能为空');
		}
	}
	else{
		$("#surefillbutton").attr('disabled','disabled');
		$("#FillAmountMsg").css('color','red');
		$("#FillAmountMsg").html('金额不能为空');
	}
}
function OkFill() {
	var id =$("#fillid").val();
	var userid =$("#filluserid").val();
	var FillAmount=$("#FillAmount").val();
	var serial=$("#serial").val();
	var adminId="1001";
	var admin="admin";
	$.ajax({
		url:PayCodeUrl+"/OKVoucher",
		type:"post",
		data:{
			id:id,
			userid:userid,
			adminId:adminId,
			admin:admin,
   		 	amount:FillAmount,
   		 	serial:serial
		},
		dataType:'json',
		success:function(data){
			var f=data.entity.flag;
			if(f==1){
				$("#rechargeName").val("");
				$("#rechargeCode").val("");
				$("#rechargeAmount").val("");
				$("#OkFillModal").modal('hide');
				showRechargeList();
			}
		}
	});
	
}
/*//显示所有用户的冻结信息
function showfizzon(start,limit){
	$.ajax({
		url:FizzonUrl+'/PushForst',
		type:'GET',
		data:{
			  start:start, 
			  limit:limit
			  },
		dataType:'json',
		success:function(data){
			var d=data.entity.dataList;
			 var text="";
			$.each(d,function(i,item){
				 $("#fizzon").html(text+="<tr> <td>"+item.name+"</td> <td>"+item.qty+"</td> <td><button class='btn rounded'>冻结</button></td></tr>");
				 max=item.totalCount;
			})
		}
	});	  
}*/


//格式化时间
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
