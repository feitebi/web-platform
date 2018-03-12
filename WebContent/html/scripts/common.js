var userid=localStorage.getItem("userid");
if(userid==""||userid==null){
	alert("您还没有登录，请先登录!");
	window.location.href="signin.html";
	
}
function logOut(){
	localStorage.clear();
	window.location.href="signin.html";
}