<%--<%@ page language="java" contentType="text/html; charset=UTF-8"--%>
<%--	pageEncoding="UTF-8"%>--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
<%--<meta name="viewport" content="width=device-width, initial-scale=1">--%>
<title>会员注册</title>

	<link rel="stylesheet" href="css/bootstrap.min.css" type="text/css" />
	<link rel="stylesheet" href="css/style.css" type="text/css" />
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/jquery-1.11.1.js" ></script>
	<!--validate.js是建立在jquery之上的，所以得先导入jquery的类库-->
	<script type="text/javascript" src="js/jquery.validate.js" ></script>
	<!--引入国际化js文件-->
	<script type="text/javascript" src="js/messages_zh.js" ></script>
<style>
body {
	margin-top: 20px;
	margin: 0 auto;
}

.carousel-inner .item img {
	width: 100%;
	height: 300px;
}

font {
	color: #3164af;
	font-size: 18px;
	font-weight: normal;
	padding: 0 10px;
}
.error {
	color:red;
}
</style>

<script type="text/javascript">


	//自定义校验规则
	$.validator.addMethod(
			//规则名称
			"checkUsername",
			function (value, element, parms) {
				//定义一个标志
				var flag = false;
				//value:输入的内容
				//element:被校验的元素对象
				//parms:规则对应的参数值
				//目的：对输入的username惊醒ajax校验
				$.ajax({
					"async": false,
					"url": "${pageContext.request.contextPath}/checkUsername",
					"data": {"username": value},
					"type": "post",
					"dataType": "json",
					"success": function (data) {
						flag = data.isExist;
					}

				});
				//返回false代表该校验器不通过
				return !flag;
			}
	)
	$(function () {
		$("#myform").validate({
			// onfocusout: function (element) {
			//     $(element).valid();
			// },
			rules: {
				username: {
					required: true,
					checkUsername: true
				},
				password: {
					required: true,
					rangelength: [6, 12]
				},
				repassword: {
					required: true,
					rangelength: [6, 12],
					equalTo: "[name='password']"
				},
				email: {
					required: true,
					email: true
				},
				name:{
					required: true,
				},
				sex: {
					required: true
				}
			},
			messages: {
				username: {
					required: "用户名不能为空",
					checkUsername: "用户名已存在"
				},
				password: {
					required: "密码不能为空",
					rangelength: "密码长度6~12位"
				},
				repassword: {
					required: "密码不能为空",
					rangelength: "密码长度6~12位",
					equalTo: "两次密码不一致"
				},
				email: {
					required: "邮箱不能为空",
					email: "邮箱格式不正确"
				},
				name:{
					required:"这是必填字段"
				}

			}
		});
	})

	// $.validator.setDefaults({
	//     ignore: ':hidden',
	// });
	// $("#button").click(function () {
	//     //registered为表单id。所有数据符合校验要求时，isValid 返回true。
	//     var isValid = $("#myform").form('validate');
	//     if (isValid) {
	//         $("#button").attr("type", "submit");
	//     }
	// });
	// function submitForm() {
	// 	$("#myform").submit();
	// }
</script>
</head>
<body>

	<!-- 引入header.jsp -->
	<jsp:include page="./header.jsp"></jsp:include>

	<div class="container"
		style="width: 100%; background: url('image/regist_bg.jpg');">
		<div class="row">
			<div class="col-md-2"></div>
			<div class="col-md-8"
				style="background: #fff; padding: 40px 80px; margin: 30px; border: 7px solid #ccc;">
				<font>会员注册</font>USER REGISTER
				<div>&nbsp;
					<div>
						<span style="color: red">${registerInfo }</span>
					</div>
				</div>
				<form id="myform" class="form-horizontal" action="${pageContext.request.contextPath}/register" method="post" style="margin-top: 5px;">
					<div class="form-group">
						<label for="username" class="col-sm-2 control-label">用户名</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="username" name="username"
								placeholder="请输入用户名">
						</div>
					</div>
					<div class="form-group">
						<label for="password" class="col-sm-2 control-label">密码</label>
						<div class="col-sm-6">
							<input type="password" class="form-control" id="password" name="password"
								placeholder="请输入密码">
						</div>
					</div>
					<div class="form-group">
						<label for="confirmpwd" class="col-sm-2 control-label">确认密码</label>
						<div class="col-sm-6">
							<input type="password" class="form-control" id="confirmpwd" name="repassword"
								placeholder="请输入确认密码">
						</div>
					</div>
					<div class="form-group">
						<label for="inputEmail3" class="col-sm-2 control-label">Email</label>
						<div class="col-sm-6">
							<input type="email" class="form-control" id="inputEmail3" name="email"
								placeholder="Email">
						</div>
					</div>
					<div class="form-group">
						<label for="usercaption" class="col-sm-2 control-label">姓名</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="usercaption" name="name"
								placeholder="请输入姓名">
						</div>
					</div>
					<div class="form-group opt">
						<label for="sex" class="col-sm-2 control-label">性别</label>
						<div class="col-sm-6" id="sex" >
							<label class="radio-inline" >
								<input type="radio" name="sex" id="sex1" value="male">男
							</label> <label class="radio-inline">
							<input type="radio" name="sex" id="sex2" value="female">女
							</label>
						</div>
					</div>
					<div class="form-group">
						<label for="date" class="col-sm-2 control-label">出生日期</label>
						<div class="col-sm-6">
							<input type="date" id="date" class="form-control" name="birthday">
						</div>
					</div>

					<div class="form-group">
						<label for="form-control" class="col-sm-2 control-label">验证码</label>
						<div class="col-sm-3">
							<input type="text" id="form-control" class="form-control " name="checkCode"
							placeholder="请输入验证码">
						</div>
						<div class="col-sm-2">
							<img src="${pageContext.request.contextPath}/verificationCode" />
						</div>

					</div>

					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<input id="button" type="submit" width="100" value="注册" name="submit"
								style="background: url('./images/register.gif') no-repeat scroll 0 0 rgba(0, 0, 0, 0); height: 35px; width: 100px; color: white;">
						</div>
					</div>
				</form>
			</div>

			<div class="col-md-2"></div>

		</div>
	</div>
	<!-- 引入footer.jsp -->
	<jsp:include page="./footer.jsp"></jsp:include>

</body>
</html>




