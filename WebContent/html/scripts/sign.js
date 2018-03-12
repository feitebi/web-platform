
var signUpUrl="/rest/sign";


$(function(){
	createCode();
});
function showCheck(a){
	var c = document.getElementById("myCanvas");
	var ctx = c.getContext("2d");
	ctx.clearRect(0,0,1000,1000);
	ctx.font = "80px 'Microsoft Yahei'";
	ctx.fillText(a,0,100);
	ctx.fillStyle = "white";
}
var code ;   
function createCode(){
	$("#signUpVilcode").val("");
    code = "";      
    var codeLength = 4;
    var selectChar = new Array(1,2,3,4,5,6,7,8,9,'a','b','c','d','e','f','g','h','j','k','l','m','n','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','J','K','L','M','N','P','Q','R','S','T','U','V','W','X','Y','Z');      
    for(var i=0;i<codeLength;i++) {
       var charIndex = Math.floor(Math.random()*60);      
      code +=selectChar[charIndex];
    }      
    if(code.length != codeLength){      
      createCode();      
    }
    showCheck(code);
}
var checkaccountflag=false;
var checkPhoneflag=false;
var checkCodeflag=false;
var checkPwdflag=false;
var agreeCheckboxflag=false;
var nameOrPhoneflag=false;
var signinpwdflag=false;

function checkbox() {
if($("#signUpCheckbox").is(':checked')){
	agreeCheckboxflag=true;
	$("#signupbtn").removeAttr('disabled');
	}
	else{
	agreeCheckboxflag=false;
	$("#signupbtn").attr('disabled','disabled');
	}
}
/*//注册
function signupbtn(){
	if(checkaccountflag==true&&checkPhoneflag==true&&checkCodeflag==true&&checkPwdflag==true&&agreeCheckboxflag==true){
		$.ajax({
			url:signUpUrl+"/signUP",
			type:'post',
			data:{
				accountName:$("#signUpAccount").val(),
				phone:$("#signUpPhone").val(),
				code:$("#signUpVilcode").val(),
				pwd:$("#signUpPwd").val()
			},
			dataType:"json",
			success:function(data){
				if(data.entity.flag=="1"){
					$("#signUpAccount").val("");
					$("#signUpPhone").val("");
					$("#signUpVilcode").val("");
					$("#signUpPwd").val("");
					localStorage.setItem("userid", data.entity.otherCode);
					window.location.href="index.html";
				}
				else{
					alert(data.entity.errorMsg);
					$("#signUpAccount").val("");
					$("#signUpPhone").val("");
					$("#signUpVilcode").val("");
					$("#signUpPwd").val("");
					$("#account_i").removeAttr('class');
					$("#phone_i").removeAttr('class');
					$("#code_i").removeAttr('class');
					$("#pwd_i").removeAttr('class');
					createCode();
				}
			}
		});	
	}
	else{
		$("#modalMsg").html("请输入正确的信息!");
		$("#signModal").modal('show');
	}
}*/
//登陆
function signin(){
	if(nameOrPhoneflag==true&&signinpwdflag==true){
		$.ajax({
			url:signUpUrl+"/signIN",
			type:'post',
			data:{
				nameOrphone:$("#nameOrphone").val(),
				signinpwd:$("#signPwd").val(),
			},
			dataType:"json",
			success:function(data){
				if(data.entity.flag=="1"){
					$("#nameOrphone").val("");
					$("#signPwd").val("");
					localStorage.setItem("userid", data.entity.otherCode);
					window.location.href="index.html";
				}
				else{
					$("#modalMsg").html(data.entity.errorMsg);
					$("#signModal").modal('show');
					$("#nameOrphone").val("");
					$("#signPwd").val("");
					$("#nameOrphone_i").removeAttr('class');
					$("#signPwd_i").removeAttr('class');
				}
			}
		});	
	}
}

function checkAccount(account){
	if(account!=""){
		if((/^[a-zA-Z]\w{5,9}$/.test(account))){
			$("#account_i").attr('class','fa fa-check text-success');
			$("#accountMsg").html("");
			checkaccountflag=true;
		}
		else{
			$("#accountMsg").css('color','red');
			$("#accountMsg").html("由字母开头，6~10位字母和数字组成!");
			$("#account_i").attr('class','fa fa-close text-danger');
		}
	}
	else{
	$("#accountMsg").css('color','red');
	$("#accountMsg").html("账户不能为空!")
	$("#account_i").attr('class','fa fa-close text-danger');
	}
}
function checkPhone(phone){
	if(phone!=""){
		if((/^1[3|4|5|8][0-9]\d{8}$/.test(phone))){
			$("#phone_i").attr('class','fa fa-check text-success');
			$("#phoneMsg").html("");
			checkPhoneflag=true;
		}
		else{
			$("#phoneMsg").css('color','red');
			$("#phoneMsg").html("请输入正确的手机号!");
			$("#phone_i").attr('class','fa fa-close text-danger');
		}
	}
	else{
	$("#phoneMsg").css('color','red');
	$("#phoneMsg").html("手机号不能为空!")
	$("#phone_i").attr('class','fa fa-close text-danger');
	}
}
function checkCode(incode){
	if(incode==code){
		$("#code_i").attr('class','fa fa-check text-success');
		checkCodeflag=true;
	}
	else{
		$("#code_i").attr('class','fa fa-close text-danger');
		}
}
function checkPwd(pwd){
	
	if(pwd!=""){
		if((/\w{6}$/.test(pwd))){
		$("#pwd_i").attr('class','fa fa-check text-success');
		$("#pwdMsg").html("");
		checkPwdflag=true;
		}
		else{
			$("#pwdMsg").css('color','red');
			$("#pwdMsg").html("密码不能小于六位!")
			$("#pwd_i").attr('class','fa fa-close text-danger');
		}
	}
	else{
	$("#pwdMsg").css('color','red');
	$("#pwdMsg").html("密码不能为空!")
	$("#pwd_i").attr('class','fa fa-close text-danger');
	}
}
function checknameOrphone(nameOrphone){
	if(nameOrphone!=""){
		$("#nameOrphone_i").attr('class','fa fa-check text-success');
		$("#nameOrphoneMsg").html("");
		nameOrPhoneflag=true;
	}
	else{
	$("#nameOrphoneMsg").css('color','red');
	$("#nameOrphoneMsg").html("用户名不能为空!")
	$("#nameOrphone_i").attr('class','fa fa-close text-danger');
	}
}
function checksignPwd(signPwd) {
	if(signPwd!=""){
		$("#signPwd_i").attr('class','fa fa-check text-success');
		$("#signPwdMsg").html("");
		signinpwdflag=true;
	}
	else{
	$("#signPwdMsg").css('color','red');
	$("#signPwdMsg").html("密码不能为空!")
	$("#signPwd_i").attr('class','fa fa-close text-danger');
	}	
} 