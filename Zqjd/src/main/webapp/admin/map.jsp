<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'map.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/2.1.1/jquery.js"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=uwHtznWOUP0hL0QjBqqARPdF"></script>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript">
	$(function(){
    						var longitude = $("input[name='longitude']").attr("value");
    						var latitude = $("input[name='latitude']").attr("value");
    						$("#allmap").css("display","block");
    				    	// 百度地图API功能
    				    	var map = new BMap.Map("allmap");            // 创建Map实例
    				    	var point = new BMap.Point(112.487391, 23.062751);    // 创建点坐标
    				    	map.centerAndZoom(point,13);  // 初始化地图,设置中心点坐标和地图级别。
    				    	var geoc = new BMap.Geocoder();
    				    	map.enableScrollWheelZoom();                            //启用滚轮放大缩小
    				    	map.addControl(new BMap.NavigationControl()); //地图控件
    				    	//map.addOverlay(new BMap.Marker(new BMap.Point(longitude, latitude)));	//标注
    				    	map.addEventListener("click", function(e){      //事件
    				    	       	//alert(e.point.lng + ", " + e.point.lat);      
    				    				$("input[name='longitude']").attr("value",e.point.lng);
    				    				$("input[name='latitude']").attr("value",e.point.lat);
    				    				$("#allmap").css("display","block");
    				    				map.clearOverlays();
    				    				map.addOverlay(new BMap.Marker(new BMap.Point(e.point.lng, e.point.lat)));
    				    				var pt = e.point;
										geoc.getLocation(pt, function(rs){
											var addComp = rs.addressComponents;
											$("input[name='province']").attr("value",addComp.province);
											$("input[name='city']").attr("value",addComp.city);
											$("input[name='distric']").attr("value",addComp.district);
											$("input[name='street']").attr("value",addComp.street);
											$("input[name='streetNum']").attr("value",addComp.streetNumber);
										});
    				    		});
    					})
	</script>
  </head>
  
  <body>
    <div id="allmap" style="width: 100%;height: 90%;"></div> 
	 纬度 <input id="lat" type="text"  name="latitude" size="14" value="" />
   	 经度 <input id="lag" type="text"  name="longitude" size="14" value="" /><br>
   	 省份<input id="province" type="text" name="province" size="8" value=""/> 
   	 城市<input id="city" type="text" name="city" size="8" value=""/>
   	 县区<input id="distric" type="text" name="distric" size="8"　value=""/>
   	 街道<input id="street" type="text" name="street" size="8" value="" />
   	 门号<input id="streetNum" type="text" name="streetNum" size="8" value="" />　
  </body>
</html>
