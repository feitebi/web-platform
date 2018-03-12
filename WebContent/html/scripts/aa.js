   var WALLET_KEY_TAG = "BDH_WALLET_KEY";
   var WALLET_ADDR_TAG = "BDH_WALLET_ADDR";
   var WALLET_SEED_TAG = "BDH_WALLET_SEED";
   var WALLET_BALANCE_TAG = "BDH_WALLET_BALANCE";

   var CERTS_LIST = 'BDH_CERTS_LIST';
   var ISSUED_ASSETS = 'BDH_ISSUED_ASSETS';

   var HD_WALLET = 'BDH_HD_WALLET';
   var INIVTE_CODE = 'INIVTE_CODE';

   var BIRD_TOKEN = 'BIRD_TOKEN';

   var postApiUrl = "/api"

   var unfs = 'http://dfs.unionchain.org:8385/';

   var USER_TAG = "USER_DATA";
   var USER_ID_FACTOR2 = "USER_ID_FACTOR2";
   var LOGIN_TAG = "LOGIN_TAG";

   var BIRD_FINISHED = "BIRD_FINISHED";

   var FEE_BALANCE = "FEE_BALANCE";

   var INIVTE_FROM_ADDR = "INIVTE_FROM_ADDR";

   var server_uri = "http://localhost:8080/XChange/rest/xchange";

   var REALNAME_VALID='REALNAME_VALID';

   var ETH_BALANCE_URL = "https://api.etherscan.io/api?module=account&action=balance&tag=";
      ETH_BALANCE_URL += "latest&apikey=BMTKE1ANJN5YN7I8E67FNGM7Z6CQN777WY&address=";

   var radomNum=""; 
   var getUrlParameter = function getUrlParameter(sParam) {
       var sPageURL = decodeURIComponent(window.location.search.substring(1)),
           sURLVariables = sPageURL.split('&'),
           sParameterName,
           i;

       for (i = 0; i < sURLVariables.length; i++) {
           sParameterName = sURLVariables[i].split('=');

           if (sParameterName[0] === sParam) {
               return sParameterName[1] === undefined ? true : sParameterName[1];
           }
       }
   };

   function getUserId() {
      var user = getStorageJson(USER_TAG);
       if (typeof user != 'undefined' && user != null) {
          return user.userId;
       }
       return "";
   }

  function login() {
      if($('#phone').val() =='' || $('#pwd').val()==''){
          $('#dlgTips').html('请检查输入！');
          $('#tipsDlg').modal('show');
          return;
      }
      $('#loginBtn').attr('disabled','true');
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
//               if (json.id != '0') {
//                   var factor2Valid = json.twoFactorKey;
//                   if(factor2Valid!='' && factor2Valid.length>0){
//                       storeJsonObject(USER_ID_FACTOR2, json.userId);
//                       window.location.href = 'loginByFactor2.html';
//                   }else {
//                     storeLogin();
//                     storeJsonObject(USER_TAG, json);
//                     storeJsonObject(REALNAME_VALID,json.nameCertFlag);
//                     //json.rememberMe = $('#rememberMe').is(':checked');
//                     if(json.walletAddr=='' || json.walletKey==''){
//                          //createNewAddress();
//                          $('#dlgTips').html('钱包地址获取失败，请与管理员联系！');
//                          $('#tipsDlg').modal('show');
//                          return;
//                     } else {
//                          storeJsonObject(WALLET_KEY_TAG, json.walletKey);
//                          storeJsonObject(WALLET_ADDR_TAG, json.walletAddr);
//                          if(json.birdToken && json.birdToken>0){
//                            storeJsonObject(BIRD_TOKEN, json.birdToken);
//                          }
//                          window.location.href = 'index.html';
//                     }
//                 }
               if(json.errorMsg==""){
            	   console.log(json);
            	localStorage.setItem("userid",json.userId);
            	window.location.href = 'index.html';
               }
               else {
                   $('#dlgTips').html('登录失败，请检查输入！');
                   $('#tipsDlg').modal('show');
                   $('#loginBtn').remoeAttr('disabled');
                   return;
               }
           }
       });
   }

   function register() {
      if($('#phone').val() =='' || $('#pwd').val()=='' || $('#phoneValidCode').val()==''){
          $('#dlgTips').html('请检查输入！');
          $('#tipsDlg').modal('show');
          return;
      }

      $('#registerBtn').attr('disabled','true');
      var refAddr = getStorageJson(INIVTE_FROM_ADDR);

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
                   //json.rememberMe = false;
                   storeLogin();
                   storeJsonObject(USER_TAG, json);

                   storeJsonObject(REALNAME_VALID,json.nameCertFlag);

                   if(json.walletAddr=='' || json.walletKey==''){
                        /*createNewAddress();*/
                	   window.location.href="login.html";
                   } else {
                        storeJsonObject(WALLET_KEY_TAG, json.walletKey);
                        storeJsonObject(WALLET_ADDR_TAG, json.walletAddr);
                   }
               } else {
                   $('#dlgTips').html('注册失败，请检查输入！');
                   $('#tipsDlg').modal('show');
                   $('#registerBtn').removeAttr('disabled');
                   return;
               }
             
           }
       });
   }

   //重置密码
   function resetPwd() {
      var phone=$("#phone").val();
      var code=$("#phoneValidCode").val();
      var newpwd=$("#pwd").val();
      if(newpwd=="" || code=="" || phone==""){
        $("#dlgTips").text("验证码和密码不能为空！");
        $("#tipsDlg").modal('show');
        return;

      } else {
        $.ajax({
          url: server_uri+'/user/reset_password',
          type:'post',
          data:{
            phone:phone,
            code:code,
            pwd:newpwd
          },
          dataType:'json',
          success:function(data){
        	  console.log(data);
            var f=data.entity;
            if(f!=null){
              $("#dlgTips").text("修改成功，请重新登录！");
              $("#tipsDlg").modal('show');
              setTimeout(function() {
                    localStorage.clear();
                    sessionStorage.clear();
                    window.location.href='login.html';
              }, 1000);
            }
            else{
              $("#dlgTips").text("请检查验证码是否输入正确！");
              $("#tipsDlg").modal('show');
            }
          }
        });
      }
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
                   $('#dlgTips').html('请查看手机信息短信！');
                   $('#tipsDlg').modal('show');
                   window.location.href='signin.html';
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

   function sendCode(v) {
      var phone=$("#phone").val();
      if(phone==null||phone==""){
        $("#dlgTips").text("手机不能为空！");
        $("#tipsDlg").modal('show');
        return;
      }
      $('#codeBtn').attr('disabled','true');
      $('#codeBtn').addClass('disabled');
      $.ajax({
    	   url: server_uri+'/user/sendverifycode',
           type: 'POST',
           data: {
               phone: $('#phone').val(),
               value:v,
               code:radomNum
           },
           dataType: 'json',
           success: function(data) {
               var state = data;
               if (state.flag == '1') {
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
           $('#codeBtn').removeAttr('disabled');
           $('#codeBtn').html("发送验证码");
       }
   }

  function storeLogin() {
     var login = new Object();
     login.login = "1";
     login.time = new Date().toDateString();
     sessionStorage.setItem(LOGIN_TAG, JSON.stringify(login));
  }

  function isLogin() {
      var json = sessionStorage.getItem(LOGIN_TAG);
      if (typeof json != 'undefined' && json != null) {
         return true;
      }

      json = getStorageJson(USER_TAG);
      if (typeof json != 'undefined' && json != null && json.userId !='') {
           return true;
      }
      
     return false;
  }

   function isRealNameValid() {
      var nv = getStorageJson(REALNAME_VALID);

      if (typeof nv != 'undefined' && nv == '1') {
         return true;
      }
      
      return false;
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

   function getStorageJson(key) {
       if (window.localStorage) {
           return JSON.parse(localStorage.getItem(key));
       } else {
           return "";
       }
   }

   function storeJsonObject(key, val) {
       if (window.localStorage) {
           localStorage.setItem(key, JSON.stringify(val));
       } else {
         alert('请使用支持H5的浏览器。');
       }
   }

   function generateUUID() {
       var d = new Date().getTime();
       var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
           var r = (d + Math.random() * 16) % 16 | 0;
           d = Math.floor(d / 16);
           return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
       });
       return uuid;
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
       return (new Date(timestamp * 1)).format("yyyy-MM-dd hh:mm");
   }

   function checkIfInitWallet() {

       var key = getStorageJson(WALLET_KEY_TAG);
       var addr = getStorageJson(WALLET_ADDR_TAG);
       if (!key || key === '') {
           logout();
       } 
   }

   function logout() {

     var bt = getStorageJson(BIRD_TOKEN);

     localStorage.clear();
     sessionStorage.clear();

     storeJsonObject(BIRD_TOKEN, bt);

     window.location.href='signin.html';

   }

   function restoreWalletByKey() {
      var privateKey = $('#sPrivateKey').val();
      if(privateKey!='' && privateKey.length>50){
        restoreWalletWith('',privateKey);
      }
   }
   function restoreWalletBySeed() {
      var seed = $('#sSeed').val();
      if(seed!='' && seed.length>12){
        restoreWalletWith(seed,'');
      }
   }

   function createNewAddress() {
      restoreWalletWith('','');
   }

   function restoreWalletWith(seed,privateKey) {
       var bt = getStorageJson(BIRD_TOKEN);
       if(bt==undefined || bt==null || bt=='null' || bt==''){
          bt='0';
       }else{
          bt='1';
       }
       var invitedFromAddr = getStorageJson(INIVTE_FROM_ADDR);
       if(invitedFromAddr==undefined || invitedFromAddr==null || invitedFromAddr=='null') {
          invitedFromAddr="";
       }
       if(!isLogin()){
          return;
       }

       $.ajax({
           type: 'POST',
           url: postApiUrl + '/wallet/init',
           data: {
              userId: getUserId(),
              seed: seed,
              privateKey: privateKey,
              isAlreadyBird: bt,
              invitedFromAddr:invitedFromAddr
           },
           dataType: 'json',
           success: function(data) {
               if (!data.address || data.address === 'undefined') {
                   $('#dlgTips').html('创建钱包失败，请刷新再试！');
                   $('#tipsDlg').modal('show');
               } else {
                   storeJsonObject(WALLET_KEY_TAG, data.privateKey);
                   storeJsonObject(WALLET_ADDR_TAG, data.address);
                   storeJsonObject(WALLET_SEED_TAG, data.seed);
                   if(data.birdToken && data.birdToken>0){
                      storeJsonObject(BIRD_TOKEN, data.birdToken);
                   }

                   addAddress2Store(data);

                   updateUserWalletInfo(data.privateKey,data.address,data.birdToken);

                   if(seed=='' && privateKey==''){
                      window.location.href='backup.html';
                   } else {
                      window.location.href='index.html';
                   }
               }
           }
       })
   }

  

  function checkIfRefLink() {
       var invitedFromAddr = getUrlParameter('ref');
       if(invitedFromAddr && invitedFromAddr!='' && invitedFromAddr!=null && invitedFromAddr!=undefined){
          storeJsonObject(INIVTE_FROM_ADDR, invitedFromAddr);
       }
   }

   


 function loginByFactor2() {
      var twoFactorCode =$('#twoFactorCode').val();
      var userId = getStorageJson(USER_ID_FACTOR2);
      if(twoFactorCode.length!=6 || userId==null || userId=='') {
          $("#dlgTips").text("登录失败，请重新登录！");
          $("#tipsDlg").modal('show');
          setTimeout(function() {
                localStorage.clear();
                sessionStorage.clear();
                window.location.href='login.html';
          }, 2000);
          return;
      }

       $.ajax({
           type: 'POST',
           url: server_uri+'/user/login_factor2',
           data: {
               userId: userId,
               twoFactorCode: twoFactorCode
           },
           dataType: 'json',
           success: function(data) {
              var json = data.entity;
              if (json.id != '0') {
                   storeLogin();

                   storeJsonObject(USER_TAG, json);

                   storeJsonObject(REALNAME_VALID,json.nameCertFlag);

                   if(json.walletAddr=='' || json.walletKey==''){
                        $('#dlgTips').html('钱包地址获取失败，请与管理员联系！');
                        $('#tipsDlg').modal('show');
                        return;
                   } else {
                        storeJsonObject(WALLET_KEY_TAG, json.walletKey);
                        storeJsonObject(WALLET_ADDR_TAG, json.walletAddr);
                        if(json.birdToken && json.birdToken>0){
                          storeJsonObject(BIRD_TOKEN, json.birdToken);
                        }
                        window.location.href = 'index.html';
                   }
              } else {
                  $('#dlgTips').html('双重验证登录失败，请输入正确的Google验证码');
                  $('#tipsDlg').modal('show');
              }
           }
         });
     }

  function getFactor2Qr() {
       $.ajax({
           type: 'POST',
           url: server_uri+'/user/two_factor_qr',
           data: {
               userId: getUserId()
           },
           dataType: 'json',
           success: function(data) {
              var barCode = data.otherCode;
              var twoFactorKey = data.otherCode1;
              $('#twoFactorKey').val(twoFactorKey);
              $('#tfk').text(twoFactorKey);
              $("#factor2CodeQr").empty();
              $("#factor2CodeQr").qrcode({ 
                  width: 200,
                  height: 200, 
                  text:barCode
              });
              if(data.targetId>0 && data.targetId!='-1') {
                  $('#enableFactor2Btn').attr('disabled','true');
                  $('#enableFactor2Btn').addClass('btn-grey').removeClass('danger');
                  $('#enableFactor2Btn').text('取消双重验证');
                  $('#enableFactor2Btn').attr('onclick','cancelFactor2();');
              }
           }
         });
    }

  function enableFactor2() {
      var twoFactorCode =$('#twoFactorCode').val();
      var twoFactorKey =$('#twoFactorKey').val()
      if(twoFactorCode.length!=6 || twoFactorKey==''){
        return;
      }

      $('#enableFactor2Btn').attr('disabled','true');
      $('#enableFactor2Btn').addClass('btn-grey').removeClass('danger');
      $('#enableFactor2Btn').text('正在启用中...');

       $.ajax({
           type: 'POST',
           url: server_uri+'/user/enable_factor2',
           data: {
               userId: getUserId(),
               twoFactorCode: twoFactorCode,
               twoFactorKey: twoFactorKey
           },
           dataType: 'json',
           success: function(data) {
                $('#twoFactorCode').val('');
              if(data.flag=='1'){
                  $('#enableFactor2Btn').text('取消双重验证');
                  $('#enableFactor2Btn').attr('onclick','cancelFactor2();');
              }else{
                  $('#dlgTips').text('启用双重验证失败，请输入正确的Google验证码！');
                  $('#tipsDlg').modal('show');
                  $('#enableFactor2Btn').text('启用双重验证');
              }
           }
         });
     }

  function cancelFactor2() {
      var twoFactorCode =$('#twoFactorCode').val()
      if(twoFactorCode.length!=6){
        return;
      }

      $('#enableFactor2Btn').attr('disabled','true');
      $('#enableFactor2Btn').addClass('btn-grey').removeClass('danger');
      $('#enableFactor2Btn').text('正在取消中...');

       $.ajax({
           type: 'POST',
           url: server_uri+'/user/cancel_factor2',
           data: {
               userId: getUserId(),
               twoFactorCode: twoFactorCode
           },
           dataType: 'json',
           success: function(data) {
               $('#twoFactorCode').val('');

              if(data.flag=='1'){
                 $('#enableFactor2Btn').text('启用双重验证');
                 $('#enableFactor2Btn').attr('onclick','enableFactor2();');
              }else{
                  $('#dlgTips').text('取消双重验证失败，请输入正确的Google验证码！');
                  $('#tipsDlg').modal('show');
                  $('#enableFactor2Btn').text('取消双重验证');
              }
           }
         });
  }
//滑块验证
  
  $(function () {	
	 
	  MathRand();
      var slider = new SliderUnlock("#slider",{
				successLabelTip : "验证成功"	
			},function(){
			$.ajax({
				url:server_uri+'/user/silder',
				type:'post',
				data:{
					code:radomNum
				},
				dataType:'json',
				success:function(data){
					console.log(data);
				}
			});    	
      	});
      slider.init();
      function MathRand() 
      { 
      for(var i=0;i<6;i++) 
      { 
    	  radomNum+=Math.floor(Math.random()*8+1); 
      } 
      } 
      
  });
  