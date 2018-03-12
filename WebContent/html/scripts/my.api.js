var apiUrl="/rest/apis";
var userid=localStorage.getItem("userid");
$(function(){
	showApi();
	$("button[name='addapi']").click(function(){
		var pindanId =$("#exchangid").val(); 
	    var key=$("#key").val();
	    var secret=$("#secret").val();
		var bntval=$("button[name='sssapi"+pindanId+"']").text();
	       if(key=="" || key==null || secret=="" || secret==null){
	          alert("您好，请填写完整");
	    	 }else{
	    		 if(bntval=='添加API'){
	    			 addApis(userid,pindanId,key,secret);}
	    		 else{
	    			 updateApis(userid,pindanId,key,secret);
	    		 }
     		} 	
	});
});
//修改API
function updateApis(userid,pindanId,key,secret){
	$.ajax({
        type: 'POST',
        url: apiUrl+"/update",
        data: {
           userId:userid,
           exchangid:pindanId,
           apikey:key,
           apisecret:secret 
        },
        dataType: 'json',
        success: function(json){
         	  $('#apisetdlg').modal('hide');
         	 $("#key").val("");
			 $("#secret").val("");  
        }
   	});
}
//添加API
function addApis(userid,pindanId,key,secret){
	
	$.ajax({
        type: 'POST',
        url: apiUrl+"/addapis",
        data: {
     	   userid:userid,
     	   exchangid:pindanId,
           apikey:key,
     	   apisecret:secret 
        },
        dataType: 'json',
        success: function(json){
        		 $('#apisetdlg').modal('hide');
                 $("#aaaaa_"+pindanId).removeClass("text-muted");
                 $("button[name='sssapi"+pindanId+"']").text("修改API"); 
                 $("#key").val("");
				 $("#secret").val("");
 				 $("i[name='icon_"+pindanId+"']").attr('class','material-icons md-24 text-info m-v-sm inline');
 				 $("i[name='icon_"+pindanId+"']").attr('class','material-icons md-24 text-info m-v-sm inline');
 				 $("i[name='icon_"+pindanId+"']").attr('class','material-icons md-24 text-info m-v-sm inline');
        }
   	});
}
//添加按钮点击事件
function clicka(id){
	$("#key").val("");
	 $("#secret").val("");  
	$("#exchangid").val(id);
	var bntval=$("button[name='sssapi"+id+"']").text();
	if(bntval=='修改API'){
		$.ajax({
			url:apiUrl+"/apislist",
			data:{
				userid:userid
			},
			dataType:"json",
			success:function(data){
				var d=data.entity.dataList;
				$.each(d,function(i,item){
					if(item.exchangid==id){
						$("#key").val(item.apiKey);
						$("#secret").val(item.apiSecret);
						$('#apisetdlg').modal('show');
					};
				});
			}
		});
	}
	else {
		$('#apisetdlg').modal('show');
		}	
	}
//删除按钮
function delapi(exchangeid){
	$.ajax({
		url:apiUrl+'/upadteIsEnable',
		type:'post',
		data:{
			userId:userid,
			exchangid:exchangeid
		},
		dataType:"json",
		success:function(data){
			var d=data.entity.flag;
			if(d==1){
				$("button[name='sssapi"+exchangeid+"']").text("添加API");
				$("#aaaaa_"+exchangeid).addClass("text-muted");
				$("i[name='icon_"+exchangeid+"']").attr('class','material-icons md-24 text-muted m-v-sm inline');
				$("i[name='icon_"+exchangeid+"']").attr('class','material-icons md-24 text-muted m-v-sm inline');
				$("i[name='icon_"+exchangeid+"']").attr('class','material-icons md-24 text-muted m-v-sm inline');
			}
		}
	});
}	
//暂停or开启访问
function openinter(exid){
	var s = $("#openinter"+exid+" span").text();
	if(s=='暂停访问'){
		$.ajax({
			url:apiUrl+'/isflag',
			type:'post',
			data:{
				userId:userid,
				exchangid:exid
			},
			dataType:'json',
			success:function(data){
				$("#del"+exid).removeAttr('onclick');
				$("#openinter"+exid+" i").html("&#xe037;");
				$("#openinter"+exid+" span").text("开启访问");
			}
		});
	}
	else{
		$.ajax({
			url:apiUrl+'/isflag',
			type:'post',
			data:{
				userId:userid,
				exchangid:exid
			},
			dataType:'json',
			success:function(data){
				$("#del"+exid).attr("onclick","delapi(\""+exid+"\")");
				$("#openinter"+exid+" i").html("&#xe034;");
				$("#openinter"+exid+" span").text("暂停访问");
			}
		});
		
	}
}
//显示API
function showApi(){
	$.ajax({
		url:apiUrl+'/exchanglist',
		type:'GET',
		data:{userid:userid},
		dataType:'json', 
		success:function(data){
			var d=data.entity.dataList;
			var text="";
			var s1="text-muted";
			console.log(data);
			$.each(d,function(i,item){
				if(item.exchangSign==null||item.exchangSign==""){
					item.exchangSign="--";
				}
				 text+="<div id='aaaaa_"+item.exchangid+"' class='col-xs-12 col-sm-6 col-md-4 "+s1+"'><div class='box'>"+
						 "<div class='box-tool'><a href='"+item.uri+"' target='_blank'><img src='logo/"+item.logo+".png' /></a></div><div class='p-a-md text-center'><a href='"+item.uri+"' target='_blank' class='text-md block'>"+item.name+"</a>"+
						 "<p class='text-muted text-sm p-t-sm'>"+item.exchangSign+"</p><button name='sssapi"+item.exchangid+"' class='btn btn-sm b-info text-info btn-outline'  onclick='clicka(\""+item.exchangid+"\")'>添加API</button>"
						 +"</div><div class='row no-gutter b-t text-center text-xs'><div  class='col-xs-4 b-r'>"+
						 "<a id='del"+item.exchangid+"' onclick='delapi(\""+item.exchangid+"\")' target='_blank' class='p-a block text-center' ui-toggle-class>";
				 text+="<i name='icon_"+item.exchangid+"' class='material-icons md-24 ";
				 		if(item.status!="1"){
				 		text+="text-muted m-v-sm inline'>&#xe872;</i>";
						}
						else{
						text+="text-info m-v-sm inline'>&#xe872;</i>";
						}
				 		text+="<span class='block'>删除</span></a></div>"+
						 "<div class='col-xs-4 b-r'><a href='"+item.uri+"' target='_blank' class='p-a block text-center' ui-toggle-class>";
				 		text+="<i name='icon_"+item.exchangid+"' class='material-icons md-24 ";
				 		if(item.status!="1"){
				 			text+="text-muted m-v-sm inline'>&#xe80b;</i>";
						}
						else{
							text+="text-info m-v-sm inline'>&#xe80b;</i>";
						}
						 text+="<span class='block'>官网</span></a></div>"+
						 "<div class='col-xs-4' onclick='openinter(\""+item.exchangid+"\")' id='openinter"+item.exchangid+"'>"+
					     "<a class='p-a block text-center' ui-toggle-class>";
						 text+="<i name='icon_"+item.exchangid+"' class='material-icons md-24 ";
					     if(item.status!="1"){
					    	 text+="text-muted m-v-sm inline'>&#xe037;</i>";
						}
						else{
							text+="text-info m-v-sm inline'>&#xe037;</i>";
						}
					     text+="<span class='block'>开启访问</span></a></div>"+
					     "</div></div></div>";
				 		$("#APIjs").html(text);
				 		$.each(d,function(i,item){
							if(item.status=='1'){
								$("button[name='sssapi"+item.exchangid+"']").text("修改API");
								$("#aaaaa_"+item.exchangid).removeClass("text-muted");
								$("#openinter"+item.exchangid+" i").html("&#xe034;");
								$("#del"+item.exchangid).attr("onclick","delapi(\""+item.exchangid+"\")");
								$("#openinter"+item.exchangid+" i").html("&#xe034;");
								$("#openinter"+item.exchangid+" span").text("暂停访问");
							}
							else{
								$("#del"+item.exchangid).removeAttr('onclick');
								$("#openinter"+item.exchangid+" i").html("&#xe037;");
								$("#openinter"+item.exchangid+" span").text("开启访问");
							}
					});
			});
	
		}
	});
}
