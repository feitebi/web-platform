var userid="test01";
var accUrl="http://localhost:8080/TestDemo/rest/Account";
var useraccUrl="http://localhost:8080/TestDemo/rest/UserAccountRest";

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
				$("#phoneNumber").text(item.phone);
			})
		}
	});
}
//手机提示CheckBox
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
	
}
//比对验证码
function phoneUpdate(){
	var code=$("#phoneCode").val();
	$.ajax({		
		url:useraccUrl+'/updatePhoneNumber',
		type:"post",
		data:{
			userid:userid,
			code:code
		},
		dataType:"json",
		success:function(data){
			var f=data.entity.flag;
			if(f=='1'){
				$("#phoneCode").val("");
				$("#phoneUP").modal('hide');
				$("#PUP").modal('show');
			}
		}
	});
	
}
//更改手机号
function updateNewP(){
	var newphone=$("#newPhone").val();
	$.ajax({		
		url:useraccUrl+'/updatePhone',
		type:"post",
		data:{
			userid:userid,
			phone:newphone
		},
		dataType:"json",
		success:function(data){
			var f=data.entity.flag;
			if(f=='1'){
				$("#newPhone").val("");
				$("#PUP").modal('hide');
				alert("修改成功");
			}
		}
	});
	
}
//窗体加载
$(function(){
	pageon();
	checked();
	showPhone();
	$("#updatePhone").click(function(){
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
	});
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
//CheckBox被选中的事件
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
	});
}