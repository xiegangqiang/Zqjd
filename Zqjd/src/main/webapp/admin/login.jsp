<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%String path = request.getContextPath();%>
<!DOCTYPE html>
<html lang="en">

<head>
<title>微信后台管理系统</title>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" href="./login/bootstrap.min.css" />
<link rel="stylesheet" href="./login/css/camera.css" />
<link rel="stylesheet" href="./login/bootstrap-responsive.min.css" />
<link rel="stylesheet" href="./login/matrix-login.css" />
<link href="./login/font-awesome.css" rel="stylesheet" />
<script type="text/javascript" src="./login/js/jquery-1.5.1.min.js"></script>
<style type="text/css">
 	* {font-family: 微软雅黑 ;}
 	input{font-family: 微软雅黑 ;}
</style>
</head>
<body>
	<div style="width:100%;text-align: center;margin: 0 auto;position: absolute;">
		<div id="loginbox">
			<form action="${pageContext.request.contextPath}/admin/j_spring_security_check" method="post" name="loginForm" id="loginForm">
				<div class="control-group normal_text">
					<h3>
						<img src="./login/logo.png" alt="Logo" />
						微信后台管理系统
					</h3>
				</div>
				<div class="control-group">
					<div class="controls">
						<div class="main_input_box">
							<span class="add-on bg_lg">
							<i><img height="37" src="./login/user.png" /></i>
							</span><input type="text" name="j_username" id="txtUsername" value="${sessionScope['SPRING_SECURITY_LAST_USERNAME']}" placeholder="请输入用户名" />
						</div>
					</div>
				</div>
				<div class="control-group">
					<div class="controls">
						<div class="main_input_box">
							<span class="add-on bg_ly">
							<i><img height="37" src="./login/suo.png" /></i>
							</span><input type="password" name="j_password" id="txtPassword" placeholder="请输入密码" value="" />
						</div>
					</div>
				</div>
				<div style="float:right;padding-right:10%;">
					<div style="float: left;margin-top:3px;margin-right:2px;">
						<font color="white">记住密码</font>
					</div>
					<div style="float: left;">
						<input name="form-field-checkbox" id="savecookie" type="checkbox"
							onclick="delCookie();" style="padding-top:0px;" />
					</div>
				</div>
				<div class="form-actions">
					<div style="width:86%;padding-left:8%;">
						<div style="float: left;">
							<font style="color: red;font-size: 16px">
						  	<% 
						  		if (request.getAttribute("ErrorMessage") != null) { 
						  			out.println(request.getAttribute("ErrorMessage").toString()); 
						  			request.removeAttribute("ErrorMessage"); 
						  		} 
						  	%>
						  </font>
						</div>
<!-- 						<div style="float: left;">
							<i><img src="./login/yan.png" /></i>
						</div>
						<div style="float: left;" class="codediv">
							<input type="text" name="code" id="code" class="login_code"
								style="height:16px; padding-top:0px;" />
						</div>
						<div style="float: left;">
							<i><img style="height:22px;" id="codeImg" alt="点击更换"
								title="点击更换" src="" /></i>
						</div> -->

						<span class="pull-right" style="padding-right:3%;">
							<input type="button" onclick="resetForm();" class="btn btn-success" value="重置"></input>
						</span>
						<span
							class="pull-right"><input type="submit" onclick="submitLogin();" class="flip-link btn btn-info" value="登陆"></input>
						</span>

					</div>
				</div>

			</form>

			<div class="controls">
				<div class="main_input_box">
					<font color="white"><span id="nameerr">Copyright © XY 2017</span></font>
				</div>
			</div>
		</div>
	</div>
	<div id="templatemo_banner_slide" class="container_wapper">
		<div class="camera_wrap camera_emboss" id="camera_slide">
			<div data-src="./login/images/banner_slide_01.jpg"></div>
			<div data-src="./login/images/banner_slide_02.jpg"></div>
			<div data-src="./login/images/banner_slide_03.jpg"></div>
		</div>
		<!-- #camera_wrap_3 -->
	</div>

	<script src="./login/js/bootstrap.min.js"></script>
	<script src="./login/js/jquery-1.7.2.js"></script>
	<script src="./login/js/jquery.easing.1.3.js"></script>
	<script src="./login/js/jquery.mobile.customized.min.js"></script>
	<script src="./login/js/camera.min.js"></script>
	<script src="./login/js/templatemo_script.js"></script>
	<script type="text/javascript" src="./login/js/jquery.tips.js"></script>
	<script type="text/javascript" src="./login/js/jquery.cookie.js"></script>
	<script type="text/javascript">
		function submitLogin(){
			$('#loginForm').submit();
			saveCookie();
		}
		
		function resetForm() {
			$("#txtUsername").val("");
			$("#txtPassword").val("");
		}
		
		function saveCookie() {
			if ($("#savecookie").prop("checked")) {
				$.cookie('txtUsername', $("#txtUsername").val(), {
					expires : 7
				});
				$.cookie('txtPassword', $("#txtPassword").val(), {
					expires : 7
				});
			}
		}
		
		function delCookie() {
			if (!$("#savecookie").prop("checked")) {
 				$.cookie('txtUsername', '', {
					expires : -1
				});
				$.cookie('txtPassword', '', {
					expires : -1
				});
				$("#txtUsername").val('');
				$("#txtPassword").val('');
			}
		}
	</script>
</body>

</html>