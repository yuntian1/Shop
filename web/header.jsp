<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<head>
    <script src="js/jquery-1.11.3.min.js" type="text/javascript"></script>
    <script type="text/javascript">

        $(function () {
            var content ="";
            $.post(
                "${pageContext.request.contextPath}/product?method=categoryList",
                function (data) {
                    for(var i=0;i<data.length;i++) {
                        <%--动态创建<li><a href="#">${category.cname}</a></li>--%>
                        content+= "<li><a href='${pageContext.request.contextPath}/product?method=productList&cid="+data[i].cid+"'>" + data[i].cname +" </a></li>";
                    }
                    // 将拼好的li放入Ul中
                    // $("#categoryUl").html(content);
                    $("#categoryUl").append(content);
                },
                "json"
            );
        });
    </script>
</head>
<body>
<!-- 登录 注册 购物车... -->

<div class="container-fluid">
	<div class="col-md-4">
		<img src="img/logo2.png" />
	</div>
	<div class="col-md-5">
		<img src="img/header.png" />
	</div>
	<div class="col-md-3" style="padding-top:20px">
		<ol class="list-inline">
			<c:if test="${empty user}">
				<li><a href="login.jsp">登录</a></li>
				<li><a href="${pageContext.request.contextPath}/register.jsp">注册</a></li>
			</c:if>
			<c:if test="${!empty user}">
				<li style="color:red">欢迎您，${user.username }</li>
			</c:if>
			<li><a href="${pageContext.request.contextPath}/user?method=logout">退出</a></li>
			<li><a href="cart.jsp">购物车</a></li>
			<li><a href="${pageContext.request.contextPath}/product?method=myOrders">我的订单</a></li>
		</ol>
	</div>
</div>

<!-- 导航条 -->
<div class="container-fluid">
	<nav class="navbar navbar-inverse">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="${pageContext.request.contextPath}/product?method=index">首页</a>
			</div>

			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav" id="categoryUl">
<%--					<c:forEach items="${categoryList}" var="category">--%>
<%--						<li><a href="#">${category.cname}</a></li>--%>
<%--					</c:forEach>--%>

				</ul>
				<form action="${pageContext.request.contextPath}/product" class="navbar-form navbar-right" role="search">
					<div class="form-group" style="position: relative;">
                        <input type="hidden" name="method" value="productSearch">
						<input id="search" name="searchname" type="text" class="form-control" placeholder="Search" onkeyup="searchWord(this)">
                        <div id="showDiv" style="display:none;position:absolute; z-index:1000; background-color:#fff;width:206px;border:1px solid #ccc;"></div>
					</div>
					<button type="submit" class="btn btn-default">Submit</button>
				</form>
<%--                完成异步搜索功能--%>
                <script type="text/javascript">
                    function overFn(obj) {
                    $(obj).css("background","#DBEAF9");
                    }
                    function outFn(obj) {
                        $(obj).css("background","#fff");

                    }
                    function clickFn(obj) {
                        // 这两种方式都行
                        $("#search").val($(obj).html());
                        //突然这种方法不好使了，奇怪，难道之前试的时候没刷新过来？
                        // $("#search").append($(obj).val());
                        $("#showDiv").css("display","none");
                    }
                    function searchWord(obj) {
                        // 1.获得输入框的输入内容
                        var word = $(obj).val();
                        var content = "";
                        $("#showDiv").html("");
                        $.post(
                            "${pageContext.request.contextPath}/searchWord",
                            {"word":word},
                            function (data) {
                                // 3.将返回的商品的名称显示在showDiv中
                                if(data.length>0){
                                    for(var i=0;i<data.length;i++){

                                        content+="<div style='padding:5px;cursor: pointer' onclick='clickFn(this)' onmouseover='overFn(this)' onmouseout='outFn(this)'>"+data[i]+"</div>";
                                    }
                                    $("#showDiv").append(content);
                                    $("#showDiv").css("display","block");
                                }

                            },
                            "json"
                        );

                        // 2.根据输入框的内容去数据库中模糊查询list<product>
                    }
                </script>

			</div>
		</div>

	</nav>
</div>
</body>
</html>