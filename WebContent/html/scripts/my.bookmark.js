var userid=localStorage.getItem("userid");
var showurl="/rest/Bookmark/Bookmarklist";
var updateurl="/rest/Bookmark/isabel";
var smartUrl="/rest/strategy";
var start=0;
var limit=20;
var max=0;
$(function(){
	show(userid,start,limit);
	$("#nextPage").click(function(){
		start+=limit;
		if(start>=max){
			start=max-limit;
			if(start<0){
				start=0;
			}
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
			console.log(data);
			var d=data.entity.dataList;
			var text="";
			$.each(d,function(i,item){		
				$("#tbody").html(text+="<tr><td>"+smallToBig(item.platform)+"</td><td>"+item.currency+"</td>" +
						"<td id='btcNow_"+smallToBig(item.platform)+"_"+item.currency+"_"+item.id+"'>加载中...</td>" +
						"<td>---</td><td>"+datetimeFormat_1(item.createTime)+"</td>" +
						"<td><a name='notlook"+item.id+"' onclick='notlook(\""+item.id+"\")' class='btn btn-xs btn-danger btn-outline text-danger'>取消</td></tr>");
				max=data.entity.totalCount;
				getTicker(smallToBig(item.platform),item.currency,item.id);
			});
		}
	});
}
//获取当前价
function getTicker(platform,coinName,id){
	if(platform!=""&&coinName!=""){
		$.ajax({
			url:smartUrl+"/getticker",
			type:"post",
			data:{
				platform:platform,
				userid:userid,
				coinname:coinName
			},
			dataType:"json",
			success:function(data){
				var d=data.entity.dataList[0];
				if(d!=null){
					$("#btcNow_"+platform+"_"+coinName+"_"+id+"").html(d);
				}
				
			}
		});
	}
}
//交易所名称转换为首字母大写
function smallToBig(s){
	 var i, ss = s.toLowerCase().split(/\s+/);  
	    for (i = 0; i < ss.length; i++) {  
	        ss[i] = ss[i].slice(0, 1).toUpperCase() + ss[i].slice(1);  
	    }  
	    return ss.join(' ');  
}
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