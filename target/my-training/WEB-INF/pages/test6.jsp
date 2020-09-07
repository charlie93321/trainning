<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: DXM_0093
  Date: 2020/9/2
  Time: 15:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>抓取数据</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/statics/css/bootstrap.min.css"/>
    <script type="application/javascript" src="${pageContext.request.contextPath}/statics/js/jquery.min.js"></script>
    <script type="application/javascript" src="${pageContext.request.contextPath}/statics/js/bootstrap.min.js"></script>


    <style>
    </style>
</head>
<body>
<div class="row" style="margin-top: 50px">
    <div class="col-md-offset-2 col-md-6">
        <input class="input-group form-control" id="keyUrl" type="text"/>
    </div>
    <div class="col-md-1">
        <a class="btn btn-primary" id="catchContent">开始抓取</a>
    </div>
</div>
<div class=" content-data">

</div>

<div class="row" style="margin-top: 200px">
    <div class="col-md-5 col-md-offset-2">
        <label class="label-info form-control">
            请求url:http://123.57.17.48:8082/getProductList.htm
        </label>
    </div>
    <a class="btn btn-success col-md-2" id="saveData">请求接口并保存数据库</a>
</div>


<div class="row" style="margin-top: 200px;margin-bottom: 50px">
    <div class="col-md-2 col-md-offset-2">
        <input class="form-control input-group info" id="inputKey" type="text"/>
    </div>
    <a class="btn btn-success col-md-2" id="redisKeyBtn">后台异步运算</a>
</div>


<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <%--<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>--%>
                <h4 class="modal-title" id="myModalLabel">任务进度</h4>
            </div>
            <div class="modal-body">

                <div class="progress">
                    <div class="progress-bar" role="progressbar" aria-valuenow="60"
                         aria-valuemin="0" aria-valuemax="100" style="width: 0%;">
                        <span class="sr-only">0% 完成</span>
                    </div>
                </div>

            </div>
            <div class="modal-footer">
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

</body>
<script>
    $(function () {


        $("#redisKeyBtn").bind("click", function () {

            $.get('/train/second/test7/asynDetach', function (result) {
                const id = result.data;
                $("#myModal").modal("show");
                $(".progress-bar").css("width",  "0%");
                $("#redisKeyBtn").attr("disabled", true);
                const interval = setInterval(function () {
                    var errorCount = 0;
                    $.get('/train/second/test7/getProcess?uuid=' + id, function (result) {
                        if (result.code == 200) {
                            console.log(parseInt(result.data));
                            $(".progress-bar").css("width", result.data + "%");
                            if (100 == parseInt(result.data)) {
                                $("#redisKeyBtn").attr("disabled", false);
                                clearInterval(interval);
                                setTimeout(function (){
                                    $("#myModal").modal("hide");
                                },500);
                            }
                        } else {
                            errorCount += 1;
                            if (errorCount >= 5) {
                                alert("任务失败,详情请查看日志....")
                                $("#redisKeyBtn").attr("disabled", false);
                                clearInterval(interval);
                            }
                        }
                    });

                }, 500);
            });
        });
        $("#saveData").bind("click", function () {
            $.get('/train/second/test7/saveData', function (result) {
                if (result.code == 200) {
                    alert("数据保存成功!!!");
                } else {
                    alert("数据保存失败!!!");
                }
            });
        });


        $("#catchContent").bind("click", function () {

            let url = $("#keyUrl").val();
            if (!url || !url.trim()) {
                alert("请输入如链接!!!");
            } else {
                $.get('/train/second/test6/catchData?url=' + url, function (result) {
                    debugger;
                    if (result.code == 200) {
                        $(".content-data").html("");
                        $(".content-data").append(
                            $("<div class='row'>" +
                                "<div class='col-md-2 ' style='text-align: right'>标题</div>" +
                                "<div class='col-md-8 ' style='text-align: left'>" + result.data.title + "</div>" +
                                "</div>")
                        );
                        $(".content-data").append(
                            $("<div class='row'>" +
                                "<div class='col-md-2 ' style='text-align: right'>价格</div>" +
                                "<div class='col-md-8 ' style='text-align: left'>" + result.data.price + "</div>" +
                                "</div>")
                        );
                        $(".content-data").append(
                            $("<div class='row'>" +
                                "<div class='col-md-2 ' style='text-align: right'>样式</div>" +
                                "<div class='col-md-8 ' style='text-align: left'>" + result.data.items + "</div>" +
                                "</div>")
                        );
                    } else {
                        alert(result.msg);
                    }
                });
            }
        });
    });
</script>
</html>
