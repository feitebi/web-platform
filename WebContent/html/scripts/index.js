
var userid=localStorage.getItem("userid");
//分页参数
//1.新开盘
var openstart=0;
var openlimit=5;
var openmax=0;
//2.智能策略
var smartstart=0;
var smartlimit=5;
var smartmax=0;
//3.关注的币
var bookstart=0;
var booklimit=5;
var bookmax=0;
//3.关注的大师
var marststart=0;
var marstlimit=5;
var marstmax=0;

var newOpenUrl="/rest/NewOpen";
var indexUrl="/rest/index";
var StrategyUrl="/rest/strategy";
var btctoCny=0;
var exchangeData=new Array();
/*var time;*/
var btccount=0;
var btcfrozen=0;
/*var cnycount=0;
var cnyfrozen=0;*/
var assatsCount=0;
var isfresh=false;
/*function pagefresh(){
	clearTimeout(time);
}*/
$(function(){
	
	/*huobi();*/
	showNewOpen(openstart,openlimit);
	showAllApis();
	showSmartDeal(smartstart,smartlimit);
	showmaster(marststart,marstlimit);
	Mybookmark(bookstart,booklimit);	
	
	$("#openOrdercount").html(btcfrozen);
	$("#btcAount").html(btccount);
	/*$("#totelCNY").html(0);	*/
	$("#bdhAmount").html("0");		
	/*$("#availableAmount").html(0);*/
	//新开盘的分页
	$("#openNext").click(function(){
		openstart+=openlimit;
		if(openstart>=openmax){
			openstart=openmax-openlimit;
			if(openstart<=0){
				openstart=0;
			}
		}
		showNewOpen(openstart,openlimit);
	});
	$("#openLast").click(function(){
		openstart-=openlimit;
		if(openstart<=0){
			openstart=0;
		}
		showNewOpen(openstart,openlimit);
		});
	//智能策略分页
	$("#smartNext").click(function(){
		smartstart+=smartlimit;
		if(smartstart>=smartmax){
			smartstart=smartmax-smartlimit;
			if(smartstart<=0){
				smartstart=0;
			}
		}
		showSmartDeal(smartstart,smartlimit);
	});
	$("#smartLast").click(function(){
		smartstart-=smartlimit;
		if(smartstart<=0){
			smartstart=0;
		}
		showSmartDeal(smartstart,smartlimit);
		});
	//关注的币分页
	$("#bookNext").click(function(){
		bookstart+=booklimit;
		if(bookstart>=bookmax){
			bookstart=bookmax-booklimit;
			if(bookstart<=0){
				bookstart=0;
			}
		}
		Mybookmark(bookstart,booklimit);
	});
	$("#bookLast").click(function(){
		bookstart-=booklimit;
		if(bookstart<=0){
			bookstart=0;
		}
		Mybookmark(bookstart,booklimit);
		});
	//关注的大师分页
	$("#marstNext").click(function(){
		marststart+=marstlimit;
		if(marststart>=marstmax){
			marststart=marstmax-marstlimit;
			if(marststart<=0){
				marststart=0;
			}
		}
		showmaster(marststart,marstlimit);
	});
	$("#marstLast").click(function(){
		marststart-=marstlimit;
		if(marststart<=0){
			marststart=0;
		}
		showmaster(marststart,marstlimit);
		});
});
//显示新开盘的
function reloadNewOpen(){
	showNewOpen(openstart,openlimit);
	$("#newOpentbody").html("<tr><td colspan='7'><br><br><br>" +
			"<p class='h1 text-center p-t-md text-info'>" +
			"<img  src='./logo/loging.gif'></p><br><br></td></tr>");
	
}
function showNewOpen(openstart,openlimit){
			$.ajax({
				url:newOpenUrl+'/NewOpenList',
				type:"post",
				data:{
					start:openstart,
					limit:openlimit
				},
				dataType:'json',
				success:function(data){
					var d=data.entity.dataList;
					var text="";
					var rowcount=0;
					if(d.length>0){
					$.each(d,function(i,item){	
						text+="<tr style='height:35px;'><td>"+item.name 
						+"</td><td>"+smallToBig(item.platform)+"</td><td>"+item.kaiPrice+"</td><td id='"+smallToBig(item.platform)+"_"+item.name 
						+"_price'>加载中...</td><td id='"+smallToBig(item.platform)+"_"+item.name 
						+"_volem'>加载中...</td><td>"+item.time+"</td></tr>";
						rowcount++;
						getnewopenTicker(smallToBig(item.platform),item.name);	
						
			});
					if(rowcount<5){
						for(var i=0;i<5-rowcount;i++){
							text+="<tr style='height:35px;'><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>";
						}
					}	
			$("#newOpentbody").html(text);
			openmax=data.entity.totalCount;
			}
			else{
				$("#newopenContent").html("<div style='height:220px'><br><br><br><p class='h6 text-center p-t-md ' style='color: #C0C0C0'>近期还没有新开盘的代币!</p>" +
				"</div>");
			}
		}
	});
}
//获取新开盘当前价
function getnewopenTicker(platform,name){
	if(platform!=""&&name!=""){
		$.ajax({
			url:indexUrl+"/newopenLast",
			type:"get",
			data:{
				exchangName:platform,
				userid:userid,
				coin:name
			},
			dataType:"json",
			success:function(data){
				var d=data.entity[0];
				if(d!=null){
					$("#"+platform+"_"+name+"_price").html(d.nowPrice);
					$("#"+platform+"_"+name+"_volem").html(d.volume);
				}
			}
		});
	}
}
//显示智能买卖
function showSmartDeal(smartstart,smartlimit){
	$.ajax({
		url:StrategyUrl+"/getsmartorders",
		type:"post",
		data:{
			userid:userid,
			start:smartstart,
			limit:smartlimit
		},
		dataType:"json",
		success:function(data){
			var d=data.entity.dataList;
			var text="";
			var s1="";
			var s2="";
			var rowccount=0;			
			if(d.length>0){
			$.each(d,function(i,item){
				if(item.askOrBid=='Sell'){
					s1="卖";
					text+="<tr style='height:35px;'><td><span class='text-danger'>"+s1+"</span> "+item.coinName+"</td>";
				}
				else{
					s1="买";
					text+="<tr style='height:35px;'><td><span class='text-success'>"+s1+"</span> "+item.coinName+"</td>";
					
				}
				text+="<td id='btcNow_"+smallToBig(item.platform)+"_"+item.coinName+"_"+item.id+"'>加载中...</td>";
				if(item.priceRule==""){
					text+="<td>"+item.bidPrice+"</td><td>"+smallToBig(item.platform)+"</td>" +
					"<td>指定价格</td><td>";
				}
				else{
					text+="<td>"+item.topOrlow+"</td><td>"+smallToBig(item.platform)+"</td>" +
					"<td>"+item.priceRule+"</td><td>";
				}	
				text+="<a class='btn btn-xs btn-danger btn-outline text-danger' onclick='delsmart(\""+item.id+"\")' >取消</td></tr>";
				rowccount++;
				
				getTicker(smallToBig(item.platform),item.coinName,item.id);
				});
			
			if(rowccount<5){
				for(var i=0;i<5-rowccount;i++){
					text+="<tr style='height:35px;'><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>";
					}
			}
			$("#stmartDeal").html(text);
			smartmax=data.entity.totalCount;
			}
			else{
				$("#smartDealContent").html("<div style='height:220px'><br><br><br><p class='h6 text-center p-t-md' style='color: #C0C0C0'>您尚未在本平台制定智能策略!</p>" +
						"</div>");
			}
		}
	});
	
}
//获取当前价
function getTicker(platform,coinName,id){
	if(platform!=""&&coinName!=""){
		$.ajax({
			url:StrategyUrl+"/getticker",
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

//取消智能策略
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
			showSmartDeal(smartstart,smartlimit);
			}
		}
	});
}
//账户信息
function showblance(allBTC,guadanBTC,allCNY,frozenCNY) {
	var pageAllBtc=$("#btcAount").html();
	var pageGuaBtc=$("#openOrdercount").html();
	/*var AllCNY=parseFloat($("#totelCNY").html());*/
	btccount=parseFloat(pageAllBtc)+parseFloat(allBTC);
	$("#btcAount").html(parseFloat(btccount).toFixed(8));
	btcfrozen=parseFloat(pageGuaBtc)+parseFloat(guadanBTC);
	$("#openOrdercount").html(parseFloat(btcfrozen).toFixed(8));
   /* cnycount+=parseFloat(allCNY);
    cnyfrozen+=parseFloat(frozenCNY);*/
   /* $("#totelCNY").html(parseFloat((btccount*btctoCny)+cnycount).toFixed(8));
    $("#availableAmount").html(parseFloat(((btccount-btcfrozen)*btctoCny)+(cnycount-cnyfrozen)).toFixed(8));*/
}
//显示交易所信息
var firstFlag=true;
function showAllApis(){
	$.ajax({
		url:indexUrl+"/platform_api",
		type:"get",
		data:{userId:userid,
			selectAll:'no'
			},
		dataType:"json",
		success:function(data){
			var d=data.entity.dataList;
			var count=0;
			var text="";
			if(d.length>0){		
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
					count++;
					Mybalance(userid,item.logo,item.apiKey,item.apiSecret,firstFlag,isfresh);
				});
				if(count<4){
				text+="<div class='col-md-3 col-xs-6'>"+
				"<a href='my.api.html' ><div class='box grey-50' style='height:280px;'>"+
	            "<br><br><br><br><p class='h1 text-center p-t-md text-info'>+</p></div></a></div>";
				}
				else{
					$("#more_button").html("<a href='allApisBalance.html' class='btn btn-sm rounded btn-outline b-info text-xs text-info pull-right'>更多>></a>");
				}
				$("#mapBlance").html(text);
			}
			else{
				$("#assatsCount").html(0);
				text+="<div class='col-md-3 col-xs-6'>"+
				"<a href='my.api.html' ><div class='box grey-50' style='height:280px;'>"+
	            "<br><br><br><br><p class='h1 text-center p-t-md text-info'>+</p></div></a></div>";
				}
				$("#mapBlance").html(text);
			
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
function Mybalance(userid,platform,key,Secret,flag,isfresh){
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
				var d=data.entity.dataList[0].list;
				
				var rowccount=0;
				if(d!=null){
				$.each(d,function(i,item){
					var json=item;
					if(json.length<1){
						$("div ."+platform+"").html("<br><p class='h6 text-center p-t-md ' style='color: #C0C0C0'>请核对您的<a class='text-info' href='my.api.html'>API信息</a></p>" +
								"<p class='h6 text-center p-t-md ' style='color: #C0C0C0'>或者检查您的账户是否存在虚拟币!</p>" +
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
				var b=data.entity.dataList[0];
				if(isfresh==false){
				showblance(b.btcAount,b.openOrdercount,b.totlecny,b.frozencny);
				assatsCount+=json.length;
				}
				if(isfresh==true&&flag==true){
						showblance(b.btcAount,b.openOrdercount,b.totlecny,b.frozencny);
						assatsCount+=json.length;
				}
				isfresh=false;
				});
				$("#assatsCount").html(assatsCount);
				}
				
			}
		});
	}
}
//我关注的币
function reloadMybookmark(){
	Mybookmark(bookstart,booklimit);
	$("#bookList").html("<tr><td colspan='7'><br><br><br>" +
			"<p class='h1 text-center p-t-md text-info'>" +
			"<img  src='./logo/loging.gif'></p><br><br></td></tr>");
}
function Mybookmark(bookstart,booklimit){
	$.ajax({
		url:indexUrl+"/MyBookmark",
		type:"post",
		data:{
			userid:userid,
			start:bookstart,
			limit:booklimit
		},
		dataType:"json",
		success:function(data){
			var bookmark=data.entity.dataList;
			var bookmarkText="";
			var bookmarkcount=0;
			if(bookmark.length>0){
			$.each(bookmark,function(b,item){
				bookmarkText+="<tr style='height:35px;'><td>"+item.currency+"</td>" +
						"<td id='bm_"+item.platform+"_"+item.currency+"_price'>加载中...</td>" +
						"<td id='bm_"+item.platform+"_"+item.currency+"_high'>加载中...</td>" +
						"<td id='bm_"+item.platform+"_"+item.currency+"_low'>加载中...</td>" +
						"<td id='bm_"+item.platform+"_"+item.currency+"_volem'>加载中...</td>" +
						"<td>"+smallToBig(item.platform)+"</td></tr>";	
				getBookMarkInfo(item.currency,item.platform);
				bookmarkcount++;
				
			});
			if(bookmarkcount<5){
				for(var i=0;i<5-bookmarkcount;i++)
				bookmarkText+="<tr style='height:35px;'><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>";	
			}
			$("#bookList").html(bookmarkText);	
			bookmax=data.entity.totalCount;
			}else{
				$("#booklistContent").html("<div style='height:220px'><br><br><br><p class='h6 text-center p-t-md' style='color: #C0C0C0'>您尚未在本平台关注过代币!</p>" +
				"</div>");
			}
		}
	});
}
//关注的币详细信息
function getBookMarkInfo(coinName,platform){
	if(coinName!=""&&platform!=""){
		$.ajax({
			url:indexUrl+"/getBookMarkCoin",
			type:"post",
			data:{
				userid:userid,
				platfrom:platform,
				coin:coinName
			},
			dataType:"json",
			success:function(data){
				var d=data.entity.dataList[0];
				if(d!=null){
					$("#bm_"+platform+"_"+coinName+"_price").html(d.lastPrice);
					$("#bm_"+platform+"_"+coinName+"_high").html(d.hightPrive);
					$("#bm_"+platform+"_"+coinName+"_low").html(d.lowPrice);
					$("#bm_"+platform+"_"+coinName+"_volem").html(d.volumeCount);
				}
			}
		});
	}
}
//关注的大师
function showmaster(marststart,marstlimit) {
	$.ajax({
		url:indexUrl+"/getMymaster",
		type:'get',
		data:{
			userid:userid,
			start:marststart,
			limit:marstlimit
			},
		dataType:'json',
		success:function(data){
		
			var master=data.entity.dataList;
			var masterText="";
			var rowccount=0;
			if(master.length>0){
			$.each(master,function(k,item){
			
				masterText+="<tr style='height:35px;'><td>"+item.walletAddr+"</td><td>暂无数据</td><td>"+item.fansnumber+"</td><td>"+item.strategynum+"</td><td>暂无数据</td></tr>";
				rowccount++;
				
			});
			if(rowccount<5){
				for(var i=0;i<5-rowccount;i++)
				masterText+="<tr style='height:35px;'><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>";
			}
			$("#masterList").html(masterText);	
			marstmax=data.entity.totalCount;
		}else{
			$("#masterContent").html("<div style='height:220px'><br><br><br><p class='h6 text-center p-t-md' style='color: #C0C0C0'>您尚未在本平台关注过大师!</p>" +
			"</div>");
		}
		}
	});
	
}
/*//火币参照
function huobi() {
	$.ajax({
		url:"http://api.huobi.com/staticmarket/detail_btc_json.js",
		dataType:'json',
		success:function(data){
			btctoCny=data.p_new;
		}
	});
	
}*/

/*//计时器
function timedCount()
 {
	huobi();
	
	$("#availableAmount").html(parseFloat(((btccount-btcfrozen)*btctoCny)+(cnycount-cnyfrozen)).toFixed(8));
	$("#totelCNY").html(parseFloat((btccount*btctoCny)+cnycount).toFixed(8));
	time=setTimeout("timedCount()",3000);
 }*/


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
				assatsCount++;
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
				assatsCount++;
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
