var masterurl="/rest/DaShiView";
var userid=localStorage.getItem("userid");
/*var userid=2;*/
var view="id";
var limit=4;
var	start=0;
$(function(){
	console.log(userid);
	showmaster(view,userid,start,limit);
});
//更多
function more(){
	limit+=4;
	showmaster(view,userid,start,limit);
}
//显示大师
function showmaster(view,userid,start,limit){
	$.ajax({
		url:masterurl+"/getDashiView/view",
		type:"get",
		data:{
			view:view,
			userid:userid,
			start:start,
			limit:limit
		},
		dataType:"json",
		success:function(data){
			console.log(data);
			 var d=data.entity.dataList;
			 var text="";
				$.each(d,function(i,item){
					
					text+="<div class='col-xs-6 col-sm-12 col-md-3'>"+
					"<div class='box text-center'>"+
		            "<div class='box-tool text-xs text-muted'>"+
		            "<i class='fa fa-eye'></i>"+item.balanceBDH+"BDH</div>"+
		            "<div class='p-a-md'>"+
		            "<p><img src='../assets/images/a4.jpg' class='img-circle w-56'></p>"+
		            "<a href class='text-md block'>"+item.name+"</a>"+
		            "<p><span class='label primary text-xs'>¥"+item.balanceCNY+"</span> <span class='label warn text-xs'>"+item.balanceBDH+" BDH</span></p>";
		           if(item.isfollowed==1){
		        	   text+="<button id='followed"+item.userId+"' class='btn btn-sm rounded btn-outline b-info text-xs text-info' disabled='disabled' >已关注</button></div>";   
		           }
		           else{
		        	   text+="<button id='followed"+item.userId+"' class='btn btn-sm rounded btn-outline b-info text-xs text-info' onclick='following(\""+item.userId+"\")' >关注Ta</button></div>";   
		           }
		           text+="<div class='row row-col no-gutter b-t'>"+
		            "<div class='col-xs-4 b-r'>"+
		            "<a class='p-y block text-center' ui-toggle-class>"+
		            "<strong class='block'>"+item.caincont+"</strong>"+
		            "<span class='block'>币资产</span></a></div>"+
		            "<div class='col-xs-4 b-r'>"+
		            "<a class='p-y block text-center' ui-toggle-class>"+
		           	"<strong class='block'>"+item.followcount+"</strong>"+
		            "<span class='block'>关注者</span></a></div>"+
		            "<div class='col-xs-4'>"+
		           	"<a class='p-y block text-center' ui-toggle-class>"+
		            "<strong class='block'>"+item.viewcant+"</strong>"+
		            "<span class='block'>查看</span></a></div></div></div></div>";
		          
				});
				 $("#pagecont").html(text);
		}
	});
}
//关注
function following(id){
	$.ajax({
		url:masterurl+'/saveDaShiGuanZhu',
		type:'post',
		data:{
			userId:userid,
			followedUserId:id
		},
		dataType:"json",
		success:function(data){
			if(data.entity.flag==1){
				$("#followed"+id).text("已关注");
				$("#followed"+id).attr("disabled","disabled");
			}
		}
	});
}
//排序
function sort(condition){
	showmaster(condition,userid,start,limit);
}