var userid=localStorage.getItem("userid");
var exchangeData=new Array();
var indexUrl="/rest/index";
//显示交易所信息
var firstFlag=true;
$(function(){
	showAllApis();
})
function showAllApis(){
	$.ajax({
		url:indexUrl+"/platform_api",
		type:"get",
		data:{userId:userid,
			selectAll:'yes'
			},
		dataType:"json",
		success:function(data){
			console.log(data);
			var d=data.entity.dataList;
			if(d.length>0){		
				var text="";
				$.each(d,function(i,item){
					text+="<div class='col-md-3 col-xs-6'>";
					text+="<div class='box' style='height:280px;'>"+
	                "<div class='box-header b-b'>"+
	                "<a onclick='reloadBalance(\""+userid+"\",\""+item.logo+"\",\""+item.apiKey+"\",\""+item.apiSecret+"\")' class='btn btn-xs white pull-right'><i class='fa fa-fw fa-refresh'></i></a>"+
	                "<a id='next_"+item.logo+"' onclick='NextPage(\""+item.logo+"\")' class='btn btn-xs white pull-right hide' ><i class='fa fa-chevron-right'></i></a>"+
	                "<a id='last_"+item.logo+"' onclick='LastPage(\""+item.logo+"\")' class='btn btn-xs white pull-right hide' ><i class='fa fa-chevron-left'></i></a>" +  
	                "<span class='h6 text-info'>"+smallToBig(item.logo)+"</span></div>"+
	                "<input type='hidden' value=1 id='p_"+item.logo+"'>" +
	                "<div class='table-responsive "+item.logo+"'>" +
	                "<br><br><p class='h1 text-center p-t-md text-info'>" +
	                "<img  src='./logo/loging.gif'></p>"+
	                "</div></div></div>";
					Mybalance(userid,item.logo,item.apiKey,item.apiSecret,firstFlag);
					
				});
				
				$("#allApis").html(text);
			}
		}
		
	});
}


//balance刷新
function reloadBalance(userid,platform,key,Secret){
	var flag=$("#"+platform+"_balance_flag").val();
	var isfresh=true;
	Mybalance(userid,platform,key,Secret,flag,isfresh);
	$("div ."+platform+"").html("<br><br><p class='h1 text-center p-t-md text-info'>" +
	                "<img  src='./logo/loging.gif'></p>");
	
}
//显示我的balance信息
function Mybalance(userid,platform,key,Secret,flag){
	if(userid!=""&&platform!=""&&key!=""&&Secret!=""){
		$.ajax({
			url:indexUrl+"/platform_getPlatformSummary",
			type:"post",
			data:{userId:userid,
				  platformindex:platform,
				  apikey:key,
				  apisecret:Secret
				},
			dataType:"json",
			success:function(data){
				console.log(data);
				var d=data.entity.dataList[0].list;
				
				var rowccount=0;
				if(d!=null){
				$.each(d,function(i,item){
					var json=item;
					if(json.length<1){
						$("div ."+platform+"").html("<p class='h5 text-center p-t-md '>请核对您的<a class='text-info' href='my.api.html'>API信息</a></p>" +
								"<p class='h5 text-center p-t-md '>或者检查您的账户是否存在虚拟币!</p>" +
						"<input id='"+platform+"_balance_flag' type='hidden' value='true'>");
					}
					else{
					if(json.length>5){
						$("#last_"+i+"").removeClass('hide');
						$("#next_"+i+"").removeClass('hide');
					}
					var text="<table class='table table-sm text-sm m-b-xs' ><thead>" +
					"<input id='"+platform+"_balance_flag' type='hidden' value='false'>"+
	                "<tr style='white-space:nowrap'>"+
	                "<th>币种</th>"+
	                "<th>总数量</th>"+
	                "<th>当前价</th>"+
	                "<th>可用量</th></tr></thead>"+
	                "<tbody class='text-xs m-b-xs'>"; 
					$.each(json,function(j,iten){
						text+="<tr style='height:30px;'><td>"+iten.currency+"</td><td>"+parseFloat(iten.total).toFixed(8)+"</td><td>"+parseFloat(iten.last).toFixed(8)+"</td><td>"+parseFloat(iten.available).toFixed(8)+"</td></tr>";
						rowccount++;
						if(rowccount>4){
							return false;
						}
					});
					if(rowccount<5){
						for(var k=0;k<5-rowccount;k++){
							text+="<tr style='height:30px;'><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>";
						}
					}
					text+="</tbody></table></div></div></div>";
					}
				$("div ."+i+"").html(text);
				//交易所二维数组赋值
				exchangeData[i]=new Array(json);
				});
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
//页面分页
var pageNum=5;
pageNum=parseInt(pageNum,0);
function LastPage (a){
	var nowPage = $("#p_"+a+"").val();
	nowPage=parseInt(nowPage,0);
	if(exchangeData!=null){
		if(nowPage>1){	
		$("#p_"+a+"").val(nowPage-1);
		var d=exchangeData[a][0];
		var text="<table class='table table-sm text-sm m-b-xs'><thead>"+
      "<tr style='white-space:nowrap'>"+
      "<th>币种</th>"+
      "<th>总数量</th>"+
      "<th>当前价</th>"+
      "<th>可用量</th></tr></thead>"+
      "<tbody class='text-xs'>"; 
		var rowccount=0;
		$.each(d,function(i,item){
			if(i>=((nowPage-2)*pageNum)){
				text+="<tr style='height:30px;'><td>"+item.currency+"</td><td>"+parseFloat(item.total).toFixed(8)+"</td><td>"+parseFloat(item.last).toFixed(8)+"</td><td>"+parseFloat(item.available).toFixed(8)+"</td></tr>";
				rowccount++;
				if(rowccount>(pageNum-1)){
					return false;
				}
			}
		});
		if(rowccount<pageNum){
			for(var k=0;k<pageNum-rowccount;k++){
				text+="<tr style='height:30px;'><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>";
			}
		}
		text+="</tbody></table></div></div></div>";
		$("div ."+a+"").html(text);
		}
		
	}
	
}
function NextPage (b){
	console.log(b);
	var max=0;
	var nowPage = $("#p_"+b+"").val();
	nowPage=parseInt(nowPage,0);	
	if(exchangeData!=null){		
		var d=exchangeData[b][0];
		max=(d.length/pageNum);
		if(nowPage<max){
		$("#p_"+b+"").val(nowPage+1);
		var text="<table class='table table-sm text-sm m-b-xs'><thead>"+
      "<tr style='white-space:nowrap'>"+
      "<th>币种</th>"+
      "<th>总数量</th>"+
      "<th>当前价</th>"+
      "<th>可用量</th></tr></thead>"+
      "<tbody class='text-xs'>"; 
		var rowccount=0;
		$.each(d,function(i,item){
			if(i>=((nowPage)*pageNum)){
				text+="<tr style='height:30px;'><td>"+item.currency+"</td><td>"+parseFloat(item.total).toFixed(8)+"</td><td>"+parseFloat(item.last).toFixed(8)+"</td><td>"+parseFloat(item.available).toFixed(8)+"</td></tr>";
				rowccount++;
				if(rowccount>(pageNum-1)){
					return false;
				}
			}
		});
		if(rowccount<pageNum){
			for(var k=0;k<pageNum-rowccount;k++){
				text+="<tr style='height:30px;'><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>";
			}
		}
		text+="</tbody></table></div></div></div>";
		$("div ."+b+"").html(text);
		}
		}
	
}