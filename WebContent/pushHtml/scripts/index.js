
var userid="test01";
var start=0;
var limit=4;
var max=0;
var newOpenUrl="http://localhost:8080/TestDemo/rest/NewOpen";
var indexUrl="http://localhost:8080/TestDemo/rest/index";
$(function(){
	showNewOpen(start,limit);
	showSmartDeal();
	$("#nextPage").click(function(){
		start+=limit;
		if(start>=max){
			start=max-limit;
			if(start<=0){
				start=0;
			}
		}
		showNewOpen(start,limit);
	});
	$("#lastPage").click(function(){
		start-=limit;
		if(start<=0){
			start=0;
		}
		showNewOpen(start,limit);
		});
});
//显示新开盘的
function showNewOpen(start,limit){
			$.ajax({
				url:newOpenUrl+'/NewOpenList',
				type:"get",
				data:{
					start:start,
					limit:limit
				},
				dataType:'json',
				success:function(data){
					var d=data.entity.dataList;
					var text="";
					$.each(d,function(i,item){	
						$("#newOpentbody").html(text+="<tr><td>"+item.name+"</td><td>"+item.kaiPrice+"</td><td>"+item.nowPrice+"</td><td>"+item.platform+"</td><td>"+item.time+"</td></tr>");
						max=data.entity.totalCount;
			});
		}
	});
}
//显示智能买卖
function showSmartDeal(){
	$.ajax({
		url:indexUrl+"/smartdeal",
		type:"post",
		data:{
			userid:userid
		},
		dataType:"json",
		success:function(data){
			var d=data.entity.dataList;
			var text="";
			var s1="";
			var s2="";
			$.each(d,function(i,item){
				if(item.askOrBid=='ask'){
					s1="卖";
					s2="天最高价";
					$("#stmartDeal").html(text+="<tr><td><span class='text-danger'>"+s1+"</span>CRB</td><td>"+item.askPrice+"</td><td>"+item.bidPrice+"</td><td>"+item.priceRule+"</td><td>"+item.price4Days+s2+"</td><td><a class='btn btn-xs btn-danger btn-outline text-danger' onclick='delsmart(\""+item.id+"\")' >取消</td></tr>");	
				}
				else{
					s1="买";
					s2="天最低价";
					$("#stmartDeal").html(text+="<tr><td><span class='text-success'>"+s1+"</span>CRB</td><td>"+item.askPrice+"</td><td>"+item.bidPrice+"</td><td>"+item.priceRule+"</td><td>"+item.price4Days+s2+"</td><td><a class='btn btn-xs btn-danger btn-outline text-danger' onclick='delsmart(\""+item.id+"\")'>取消</td></tr>");
				}
				});
		}
	});
}

function delsmart(id){
	$.ajax({
		url:indexUrl+"/delsmart",
		type:"post",
		data:{
			id:id
		},
		dataType:"json",
		success:function(data){
			var f=data.entity.flag;
			if(f==1){
			showSmartDeal();
			}
		}
	});
}
