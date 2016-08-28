<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String url = request.getParameter("url").toString();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  <script type="text/javascript" language="javascript">
    function iFrameHeight() {
        var ifm= document.getElementById("iframepage");
        var subWeb = document.frames ? document.frames["iframepage"].document :ifm.contentDocument;
        if(ifm != null && subWeb != null) {
        	ifm.height = subWeb.body.scrollHeight;
        }
    }
	</script>
  </head>
  <body style="padding:0;margin:0;">
    <iframe src="<%=url %>" marginheight="0" marginwidth="0" frameborder="0" scrolling="auto" width=100% height=100% id="iframepage" name="iframepage" onLoad="iFrameHeight()" ></iframe>
  </body>
</html>
