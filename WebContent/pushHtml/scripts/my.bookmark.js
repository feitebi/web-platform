var userid="2021";
var showurl="http://localhost:8080/TestDemo/rest/Bookmark/Bookmarklist";
var updateurl="http://localhost:8080/TestDemo/rest/Bookmark/isabel";
var start=0;
var limit=3;
var max=0;
$(function(){
	show(userid,start,limit);
	$("#refresh").click(function(){
		show(userid,start,limit);
	});	
	$("#nextPage").click(function(){
		start+=limit;
		if(start>=max){
			start=max-limit;
		}
		show(userid,start,limit);
	});
	$("#lastPage").click(function(){
		start-=limit;
		if(start<=0){
			start=0;
		}
		show(userid,start,limit);
	});
})
//取消收藏
function notlook(id){
	if(id==null||id==""){}
	$.ajax({
		url:updateurl,
		type:'post',
		data:{
			id:id,
			userId:userid
		},
		dataType:'json',
		success:function(data){
			if(data.entity.flag==1){
				show(userid,start,limit);
			}
		}
	});
}
//显示收藏的币
function show(userid,start,limit){
	$.ajax({
		url:showurl,
		type:"POST",
		data:{
			userId:userid,
			start:start,
			limit:limit
		},
		dataType:'json',
		success:function(data){
			
			var d=data.entity.dataList;
			var text="";
			$.each(d,function(i,item){		
				$("#tbody").html(text+="<tr><td>"+item.platform+"</td><td><span class='text-success'></span>"+item.currency+"</td><td>0.00055</td><td>0.00035</td><td>"+item.createTime+"</td><td><a name='notlook"+item.id+"' onclick='notlook(\""+item.id+"\")' class='btn btn-xs btn-danger btn-outline text-danger'>取消</td></tr>");
				max=data.entity.totalCount;
			});
		}
	});
}
