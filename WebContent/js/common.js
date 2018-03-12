




var DOMAIN ='http://139.196.36.168:6060';
 
 var getUrlParameter = function getUrlParameter(sParam) {
	 alert(111);
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

 function loadPinDanPage(p) {
      $.ajax({
           type: 'POST',
           url: DOMAIN+'/Home/pindansuos',
           data: {
                page: p,
                longitude: '120.182321',
                latitude: '30.223051',
                sort: '价格'
           },
           dataType: 'json',
           success: function(json) {
                var arr = $.parseJSON(json.message);
                $(arr).each(function(i, val) {
                     addRow(val);
                });
           }
      });
 }

 function addRow(json) {

      var items = "<div class='item'>";
      items += "<div class='icon'>";
      items += '<a href="ping_join.html?pindanId=' + json.pindanId + '"><img src="../../up/' + json.pindanImag + '" width="100" height="100" class="img-responsive"/></a>';
      items += "</div>";
      items += "<div class='detail'>";
      items += "<ul>";
      items += "<li>";
      items += '<div class="dl ht"><a href="ping_join.html?pindanId=' + json.pindanId + '">' + json.heaDline + '</a></div>';
      items += "<div class='dr top5'>" + json.distance + "km</div>";
      items += "</li>";
      items += "<li>";
      items += "<div class='dl'>" + json.synopsis + "</div>";
      items += "<div class='dr'><img src='images/star_blank.png' width='16'/></div>";
      items += "</li>";
      items += "<li>";
      items += "<div><img src='images/minus.png' width='16'/> 满20元发拼减5，参拼减2</div>";
      items += "</li>";
      items += "<li>";
      items += "<div class='dl price green'>¥人均<b>" + json.meanExpense + "</b>元</div>";
      items += "<div class='dr green top5'>已拼 " + json.pindanNumber + " 人</div>";
      items += "</li>";
      items += "<li>";
      items += " </ul>";
      items += "</div>";
      items += "</div>";
      $('.pingList').append(items);
 }
 
 
  function loadPinDanDetail() {
     var pindanId = getUrlParameter('pindanId');
     
      $.ajax({
           type: 'POST',
           url: DOMAIN+'/Home/pindanxq',
           data: {
                pindanId: pindanId
           },
           dataType: 'json',
           success: function(json) {
               console.log(json);
           }
      });
 }

 