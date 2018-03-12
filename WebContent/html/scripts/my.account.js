var userid=localStorage.getItem("userid");
var accUrl="/rest/Account";
var useraccUrl="/UserAccountRest";


//显示手机号
function showPhone(){
	$.ajax({
		url:useraccUrl+'/userlist',
		type:"get",
		data:{
			userid:userid
		},
		dataType:"json",
		success:function(data){
			var d=data.entity.dataList;
			$.each(d,function(i,item){
				$("#phoneNumber").text(item.phone.substring(0,3)+"****"+item.phone.substring(7,11));
				$("#nowPhone").text(item.phone.substring(0,3)+"****"+item.phone.substring(7,11));
				$("#hidePhone").val(item.phone);
			})
		}
	});
}
function updatePhone(){
	$("#phoneUP").modal("show");
}
function updatePwd(){
	$("#upPassword").modal("show");
}
function sendCode(){
	var nowPhone = $("#hidePhone").val();
	if(nowPhone!=""){
		$.ajax({
			url:useraccUrl+"/sendCode",
			type:"post",
			data:{
				userid:userid,
				phone:nowPhone
			},
			dataType:"json",
			success:function(data){
				var d=data.entity;
				if(d.flag==1){
					$("#hideCode").val(d.otherCode);
					
				}
			}
		});
	}
	$('.codeBtn').attr('disabled','true');
	timer = setInterval(countdown, 1000);
}
var timer_minute = 60000;
var timer;
function countdown() {
    timer_minute = timer_minute - 1000;
    if (timer_minute > 0) {
        $('.codeBtn').html(timer_minute / 1000 + "秒后重发");
    } else {
        clearInterval(timer);
        timer_minute = 60000;
        $('.codeBtn').removeAttr('disabled');
        $('.codeBtn').html("发送验证码");
    }
}
//更改手机号
function upPhone(){
	var newPhone = $("#phoneChangeTo").val();
	var hideCode = $("#hideCode").val();
	var code=$("#upPhoneCode").val();
	if(newPhone!=""&&code!=""){
		if(code==hideCode){
		$.ajax({
			url:useraccUrl+"/updatePhone",
			type:"post",
			data:{
				userid:userid,
				phone:newPhone,
				code:hideCode
			},
			dataType:"json",
			success:function(data){
				var f=data.entity.flag;
				if(f=="1"){
					$("#phoneUP").modal("hide");
					$("#phoneNumber").text(newPhone.substring(0,3)+"****"+newPhone.substring(7,11));
					$("#hidePhone").val(newPhone);
					$("#upPhoneCode").val("");
					$("#phoneChangeTo").val("");
					clearInterval(timer);
			        timer_minute = 60000;
			        $('.codeBtn').removeAttr('disabled');
			        $('.codeBtn').html("发送验证码");
				}
				else{
					alert("请检查输入!");
				}
			}
		});
		}else{
			alert("请输入正确的验证码!");
		}
	}
	else{
		alert("验证码或手机号不能为空!");
	}
}
//更改密码
function upPwd(){
	var newPwd = $("#pwdChangeTo").val();
	var hideCode = $("#hideCode").val();
	var code=$("#upPwdCode").val();
	if(newPwd!=""&&code!=""){
		if(code==hideCode){
		$.ajax({
			url:useraccUrl+"/updatePwd",
			type:"post",
			data:{
				userid:userid,
				code:hideCode,
				newPwd:newPwd
			},
			dataType:"json",
			success:function(data){
				var f=data.entity.flag;
				if(f=="1"){
					$("#upPassword").modal("hide");
					$("#upPwdCode").val("");
					$("#pwdChangeTo").val("");
					clearInterval(timer);
			        timer_minute = 60000;
			        $('.codeBtn').removeAttr('disabled');
			        $('.codeBtn').html("发送验证码");
				}
				else{
					alert("请检查输入!");
				}
			}
		});
		}else{
			alert("请输入正确的验证码!");
		}
	}
	else{
		alert("验证码或密码不能为空!");
	}
}





/*//手机提示CheckBox
function phoneMsg(id){
	var Fieldvalue=0;
	var ischecked = $("#"+id+"").is(':checked');
	if(ischecked){
		Fieldvalue=1;
	}
	else
	{
		Fieldvalue=0;	
	}
 		$.ajax({
		url:accUrl+'/notifyMessage',
		type:'post',
		data:{
			fieldValue:Fieldvalue,
			fieldName:id,
			userId:userid
		},
		dataType:'json',
		success:function(data){
			console.log(data.entity.flag);
		}
		
	});
	
}*/


//窗体加载
$(function(){
	showPhone();
	pageon();
	$("#googleAuthBnt").click(function(){
	var ss="";
 	$.ajax({
		url:accUrl+"/google?userid="+userid,
		type:'GET',	
		dataType:'json',
		success:function(data){
			var d=eval(data);
			$.each(d,function(i,item){
			    ss=item.goolgleSecretKey;
			});			
			$("#code1").empty();
			$("#code1").qrcode({ 
			    render: "table",
			    width: 170, //宽度 
			    height:170, //高度 
			    text:ss //任意内容 
			});
			$("#codemodal").modal("show");
		}	
	}); 	
}); 
	$("#codesure").click(function(){
	var s1 = $("#googlecode").val();
	var dd="";
	$.ajax({
		url:accUrl+'/isgoogle',
		type:'post',
		data:{
			userId:userid,
			code:s1
		},
		dataType:"json",
		success:function(data){
			var d=eval(data);
			$.each(d,function(i,item){
			    dd=item.isEnablegoolegleAutor;
			});
			if(dd==1){
				$("#googleAuthBnt").html("已启用");
				$("#googleAuthBnt").attr("disabled","disabled");
				$("#stopGoole").css("display","");
				$("#codemodal").modal("hide");
			}
			else{
				$("#Googlemsg").text("您的输入可能有误，请确认后重新输入！")
				$("#Googlemsg").css("color","red");
			}
		}		
	});
});
	$("#stopGoole").click(function(){
	var dd="";
	$.ajax({
		url:accUrl+'/stopgoogle',
		type:'post',
		data:{
			userId:userid
		},
		dataType:"json",
		success:function(data){
			var d=eval(data);
			$.each(d,function(i,item){
			    dd=item.isEnablegoolegleAutor;
			});
			if(dd==0){
				$("#googleAuthBnt").html("启用");
				$("#googleAuthBnt").removeAttr("disabled");
				$("#stopGoole").css("display","none");
			}
		}
	});
});
});
//页面加载时按钮的状态
function pageon(){
	var dd="";
	$.ajax({
		url:accUrl+'/redegoogle',
		type:'post',
		data:{
			userId:userid
		},
		dataType:"json",
		success:function(data){
			var d=eval(data);
			$.each(d,function(i,item){
			    dd=item.isEnablegoolegleAutor;
			});
			if(dd==1){
				$("#googleAuthBnt").html("已启用");
				$("#googleAuthBnt").attr("disabled","disabled");
				$("#stopGoole").css("display","");
			}
			else{
				$("#googleAuthBnt").html("启用");
				$("#googleAuthBnt").removeAttr("disabled");
				$("#stopGoole").css("display","none");
			}
		}
	});
	}
/*
	/*checked();*/
/*	$("#updatePhone").click(function(){
		var phoneNumber=$("#phoneNumber").text();
		$("#Pnumber").text(phoneNumber);
		$.ajax({
			url:useraccUrl+'/sendCode',
			type:"post",
			data:{
				userid:userid,
				phone:phoneNumber
			},
			dataType:"json",
			success:function(data){
				var f=data.entity.flag;
				if(f=='1'){
					$("#phoneUP").modal('show');
				}
			}
		});
	});*/
	
	
/*//CheckBox被选中的事件
function checked(){
	$.ajax({
		url:accUrl+'/checke',
		type:'post',
		data:{
			userId:userid
		},
		dataType:"json",
		success:function(data){
			var d=data.entity.dataList;
			$.each(d,function(i,item){
				if(item.notify4followed==1){
					$("#notify4followed").attr("checked","checked");
				}
				if(item.notify4Viewed==1){
					$("#notify4Viewed").attr("checked","checked");
				}
				if(item.notify4Received==1){
					$("#notify4Received").attr("checked","checked");
				}
				if(item.notify4Traded==1){
					$("#notify4Traded").attr("checked","checked");
				}
			});
		}
	});*/
