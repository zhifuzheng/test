<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>优好采购-管理平台</title>
<!-- 避免IE使用兼容模式 -->
<meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
<meta name="renderer" content="webkit">
<meta name="keywords" content=''/>
<meta name="description" content=''/>

<link rel="shortcut icon" href="/zhtc/system/public/image/favicon.ico"/>
<link rel="stylesheet" href="/zhtc/static/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="/zhtc/static/third/font-awesome-4.7.0/css/font-awesome.min.css">
<style type="text/css">
    html, body {
        height: 100%;
    }

    .box {
        background: url("/zhtc/static/images/loginBg.jpg") no-repeat center center;
        background-size: cover;
        margin: 0 auto;
        position: relative;
        width: 100%;
        height: 100%;
    }

    .login-box {
        width: 100%;
        max-width: 500px;
        height: auto;
        position: absolute;
        top: 50%;
        margin-top: -200px;
        /*设置负值，为要定位子盒子的一半高度*/
    }

    @media screen and (min-width: 500px) {
        .login-box {
            left: 50%;
            /*设置负值，为要定位子盒子的一半宽度*/
            margin-left: -250px;
        }
    }

    .form {
        width: 100%;
        max-width: 500px;
        height: auto;
        margin: 2px auto 14px auto;
    }

    .login-content {
        border-bottom-left-radius: 8px;
        border-bottom-right-radius: 8px;
        height: auto;
        width: 100%;
        max-width: 500px;
        background-color: rgba(255, 250, 2550, .7);
        float: left;
    }

    .input-group {
        margin: 30px 0px 0px 0px !important;
    }

    .form-control,
    .input-group {
        height: 40px;
    }

    .form-actions {
        margin-top: 30px;
    }

    .form-group {
        margin-bottom: 0px !important;
    }

.login-title {
    border-top-left-radius: 8px;
    border-top-right-radius: 8px;
    padding: 4px 10px;
    background-color: rgba(255, 0, 0, .7);
}

    .login-title h1 {
        margin-top: 10px !important;
    }

    .login-title small {
        color: #ffa;
    }

    .link p {
        line-height: 20px;
        margin-top: 30px;
    }

    .btn-sm {
        padding: 8px 24px !important;
        font-size: 16px !important;
    }
.flag {
    /* position: absolute; */
    margin-top: 15px;
    margin-left: 44px;
    display: block;
    color: #004efd;
    font-weight: bold;
    font: 14px/normal "microsoft yahei", "Times New Roman", "宋体", Times, serif;
}
    .bgbox{
    width: 100%;
    height: 100%;
    background-color: #02ffff57;
    position: absolute;
    top: 0;
}
.login_button{
    margin-top: 32px;
    height: 40px;
    font-size: 16px;
    background-color: rgba(255, 0, 0, .7);
}
.Reset{
    border: 0;
    background-color: rgba(255, 255, 255, 0);
    font-size: 15px;
    color: #000;
    text-align: right;
    width: 78px;
    font-weight: 600;
}
</style>

</head>
<body>
<div class="box">
	<!--<div class="bgbox"></div>-->
    <div class="login-box">
        <div class="login-title text-center">  
            <h1>
                <small><font size="6">优好采购管理平台</font></small>
            </h1>
        </div>
        <div class="login-content ">
            <div class="form">
            	  <!--<span class="flag"><i class="fa fa-user"></i> 用户登录</span>-->
                <form id="modifyPassword" class="form-horizontal" action="#" method="post">
                    <input type="hidden" id="referer" name="referer" value="http://demo.ewsd.cn/system/index/index?portal=system">
                    <div class="form-group">
                        <div class="col-xs-10 col-xs-offset-1">
                            <div class="input-group">
                                <span class="input-group-addon"><span class="glyphicon glyphicon-user"></span></span>
                                <input type="text" id="username" name="username" class="form-control" placeholder="用户名" value="9527">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-xs-10 col-xs-offset-1">
                            <div class="input-group">
                                <span class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></span>
                                <input type="password" id="password" name="password" class="form-control" placeholder="密码" value="111111">
                            </div>
                        </div>
                    </div>
                     <div class="form-group">
                        <div class="col-xs-10 col-xs-offset-1">
                             <button type="button" id="login" class="btn col-xs-12 login_button">
                                <span class="fa fa-check-circle"></span> 登录
                            </button>
                        </div>
                    </div>
                    <div class="form-group form-actions">
	                    <div class="col-xs-5 col-xs-offset-1">
	                    	<input type="checkbox" id="isVip" name="isVip">使用会员账号登录
	                    </div>
	                    <button type="button" id="reset" class="Reset col-xs-offset-3">重置</button>
                    </div>
                    <div class="form-group">
                        <div class="col-xs-12 text-center">
                            
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <span class="text-danger"><i class="fa fa-warning"></i> <span id="toast">用户名或密码错误，请重试！</span></span>
            </div>
        </div>
    </div>
</div>

<!-- 引入jQuery -->
<script src="/zhtc/static/js/jquery.min.js"></script>
<script src="/zhtc/static/js/common.js"></script>
<script src="/zhtc/static/topjui/plugins/jquery/jquery.cookie.js"></script>
<script src="/zhtc/static/bootstrap/js/bootstrap.min.js"></script>
<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
<script src="/static/bootstrap/plugins/html5shiv.min.js"></script>
<script src="/static/bootstrap/plugins/respond.min.js"></script>
<![endif]-->
<script type="text/javascript">
    if (navigator.appName == "Microsoft Internet Explorer" &&
            (navigator.appVersion.split(";")[1].replace(/[ ]/g, "") == "MSIE6.0" ||
            navigator.appVersion.split(";")[1].replace(/[ ]/g, "") == "MSIE7.0" ||
            navigator.appVersion.split(";")[1].replace(/[ ]/g, "") == "MSIE8.0")
    ) {
        alert("您的浏览器版本过低，请使用360安全浏览器的极速模式或IE9.0以上版本的浏览器");
    }
</script>
<script>
    var _hmt = _hmt || [];
    (function () {
        var hm = document.createElement("script");
        hm.src = "https://hm.baidu.com/hm.js?9bbb7536a0474a4ad060a6fdc8a678b5";
        var s = document.getElementsByTagName("script")[0];
        s.parentNode.insertBefore(hm, s);
    })();
</script>
<script type="text/javascript">
    $(function () {
    	//$("#isVip").trigger("click");
        $('#password').keyup(function (event) {
            if (event.keyCode == "13") {
                $("#login").trigger("click");
                return false;
            }
        });

        $("#login").on("click", function () {
            submitForm();
        });

        function submitForm(){
            if (navigator.appName == "Microsoft Internet Explorer" &&
                    (navigator.appVersion.split(";")[1].replace(/[ ]/g, "") == "MSIE6.0" ||
                    navigator.appVersion.split(";")[1].replace(/[ ]/g, "") == "MSIE7.0" ||
                    navigator.appVersion.split(";")[1].replace(/[ ]/g, "") == "MSIE8.0")
            ){
                alert("您的浏览器版本过低，请使用360安全浏览器的极速模式或IE9.0以上版本的浏览器");
            } else {
                var formData = {
                    username: $('#username').val(),
                    password: $('#password').val(),
                    referer: $('#referer').val()
                };
                var requestUrl = '../system/user/userLogin';
                if($("#isVip").is(":checked")){
                	requestUrl = '../system/vip/vipLoginByPc';
                }
                $.ajax({
                    type: 'POST',
                    url: requestUrl,
                    //contentType: "application/json; charset=utf-8",
                    dataType:'json',
                    data: formData,
                    success: function (data) {
                        if (data.status == "1") {
                            location.href = '../admin/index.jsp';
                            //location.href = data.referer;
                        } else {
                        	$('#toast').html(data.msg);
                            $('#myModal').modal();
                            //alert("用户名或密码错误！");
                        }
                    },
                    error: function () {

                    }
                });
            }
        }
        $("#reset").on("click", function () {
            $("#username").val("");
            $("#password").val("");
            $("#isVip").removeAttr('checked');
        });
    });
</script>
</body>
</html>