var userId="test01";
var resetPwdUrl="http://localhost:8080/TestDemo/rest/resetPwd";
var timer =60;
//倒计时
function Countdown() {
    if (timer >= 1) {
        timer -= 1;
        $("#codeBtn").html(timer+"秒");
        $("#codeBtn").attr("disabled","disabled");
        setTimeout(function() {
            Countdown();
        }, 1000);
    }
    else{
    	$("#codeBtn").removeAttr("disabled");
        $("#codeBtn").html("发送");
    }
}
//发送验证码
function sendCode(){
	var phoneNumber=$("#phone").val();
	if(phoneNumber==null||phoneNumber==""){
		$("#msgP").text("手机号不能为空。");
		$("#message").modal('show');
		
	}
	else{
	$.ajax({
		url:resetPwdUrl+'/resetPwdCode',
		type:'post',
		data:{
			userid:userId,
			phone:phoneNumber
		},
		dataType:'json',
		success:function(data){
			var f=data.entity.flag;
			if(f==1){
				Countdown();
			}
			else{
				$("#msgP").text("验证码发送失败，请检查手机号是否输入正确！");
				$("#message").modal('show');
			}
		}
	  });
	}	
}
//重置密码
function resetPwd(){
	var code=$("#phoneValidCode").val();
	var newpwd=$("#pwd").val();
	if(newpwd==null||newpwd==""){
		$("#msgP").text("密码不能为空。");
		$("#message").modal('show');
		
	}
	else{
		$.ajax({
			url:resetPwdUrl+'/resetPwd',
			type:'post',
			data:{
				userid:userId,
				code:code,
				newPwd:newpwd
			},
			dataType:'json',
			success:function(data){
				var f=data.entity.flag;
				if(f==1){
					alert("密码修改成功。");
					window.location.href='login.html';
				}
				else{
					$("#msgP").text("请检查验证码是否输入正确！");
					$("#message").modal('show');
				}
			}
		});
	}
}

