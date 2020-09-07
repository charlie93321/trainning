<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: DXM_0093
  Date: 2020/9/2
  Time: 19:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>分页插件</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/statics/css/bootstrap.min.css"/>
    <script type="application/javascript" src="${pageContext.request.contextPath}/statics/js/jquery.min.js"></script>
    <script type="application/javascript" src="${pageContext.request.contextPath}/statics/js/bootstrap.min.js"></script>

    <script type="application/javascript"
            src="${pageContext.request.contextPath}/statics/js/bootstrap-paginator.min.js"></script>

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.4/css/bootstrap-select.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.4/js/bootstrap-select.min.js"></script>
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">

    <style>
        .table-scroll {
            white-space: nowrap;
        }

        .table-scroll table {
            table-layout: fixed;
        }

        .table-scroll thead {
            display: table-row;
        }

        .table-scroll tbody {
            overflow-y: auto;
            display: block;
            height: 520px;
        }

        .table-scroll th, td {
            width: 160px;
            overflow: hidden;
            text-overflow: ellipsis;
            min-width: 160px;
            border: 1px solid #808080;
        }

    </style>
</head>
<body>
<div class="row" style="margin-top: 20px">

    <div class="col-md-8 col-md-offset-1">
        <input class="input form-control " id="keys" placeholder="请输入关键字"/>
    </div>
    <a class="btn btn-success col-md-offset-1 col-md-1" id="btn-search">搜索</a>


</div>
<%--<div class="row" style="margin-top: 20px;height: 500px">
    <div class="col-md-offset-1 col-md-10">
        <table id="dataTable" class="table table-hover table-bordered">
        </table>
    </div>
</div>--%>

<div class="row" style="height:520px;">
    <div class="col-md-offset-1 col-md-10">
        <div id="select" style="height:520px;">
            <!-- table table-bordered 带边框的样式 -->
            <table class="table  table-striped table-bordered table-hover table-scroll " id="userTable">
            </table>
            <div style="text-align:center;" class="row">


                <div class="col-md-offset-1 col-md-3 row" style="margin-top: 16px;">
                    <div class="col-md-3" style="margin-top:10px;text-align: right">
                        pageSize:
                    </div>
                    <div class="col-md-8">
                        <select class="selectpicker">
                            <option>10</option>
                            <option>20</option>
                            <option>30</option>
                        </select>
                    </div>
                </div>
                <div class=" col-md-5">
                    <ul id="useroption"></ul>
                </div>
            </div>
        </div>
    </div>
</div>


<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <%--<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>--%>
                <h4 class="modal-title" id="myModalLabel">添加用户</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-2 required" style="text-align: right;margin-top: 7px">
                        <label>姓名:<font color="red">*</font></label>
                        <input type="hidden" id="data-id"/>
                    </div>
                    <div class="col-md-9">
                        <input class="form-control required " type="text" id="data-name">
                    </div>
                </div>
                <div class="row" style="margin-top: 8px">
                    <div class="col-md-2" style="text-align: right;margin-top: 7px">
                        <label>年龄:<font color="red">*</font></label>
                    </div>
                    <div class="col-md-9">
                        <select class="ageSelect " id="data-age">

                        </select>
                    </div>
                </div>
                <div class="row" style="margin-top: 8px">
                    <div class="col-md-2" style="text-align: right;margin-top: 7px">
                        <label>手机:<font color="red">*</font></label>
                    </div>
                    <div class="col-md-9">
                        <input class="form-control required " id="data-phone" type="text">
                    </div>
                </div>
                <div class="row" style="margin-top: 8px">
                    <div class="col-md-2" style="text-align: right;margin-top: 7px">
                        <label>描述:</label>
                    </div>
                    <div class="col-md-9">
                        <textarea style="width: 100%;height: 200px" id="data-remark"></textarea>
                    </div>
                </div>

            </div>
            <div class="modal-footer">
                <a class="btn btn-default" id="addDataClsBtn">关闭</a>
                <a class="btn btn-primary" id="data-addDataBtn" onclick="addDataBtnFun()">提交</a>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>


</body>
<script>

    function addDataBtnFun() {
        debugger;
        var result = validData();
        if (result.code == 500) {
            alert(result.msg);
        } else {
            if (result.data.id) {




               /* $.post('/train/test/test5/edit',
                    JSON.stringify(result.data),
                    function (result) {

                        if (result.code == 200) {

                            $('#myModal').modal('hide');
                            queryUser(null);
                            setTimeout(function () {
                                alert("数据修改成功！！！");
                            }, 500)
                        }
                    }
                );*/

                $.ajax({
                    url:'/train/test/test5/edit',
                    dataType:'json',
                    type:'post',
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(result.data),
                    success:(result)=>{
                        if (result.code == 200) {

                            $('#myModal').modal('hide');
                            queryUser(null);
                            setTimeout(function () {
                                alert("数据修改成功2！！！");
                            }, 500)
                        }
                    }
                });



            } else {
                $.post('/train/test/test5/add',
                    result.data,
                    function (result) {

                        if (result.code == 200) {

                            $('#myModal').modal('hide');
                            queryUser(null);
                            setTimeout(function () {
                                alert("数据添加成功！！！");
                            }, 500)
                        }
                    });

            }
        }
    }

    function validData() {

        debugger;
        var name = $("#data-name").val();
        var age = $("#data-age").val();
        var phone = $("#data-phone").val();
        var remark = $("#data-remark").val();
        var id = $("#data-id").val();

        if (!name) {
            return {code: 500, msg: '姓名不能为空！！！'}
        }
        if (!age) {
            return {code: 500, msg: '年龄不能为空！！！'}
        }

        if (!phone) {
            return {code: 500, msg: '手机号码不能为空！！！'}
        }
        $("#data-name").val('');
        $("#data-phone").val('');
        $("#data-remark").val('');
        $("#data-id").val('');
        if (id) {

            return {
                code: 200,
                msg: 'ok',
                data: {id: id, name: name, age: age, phone: phone, remark: remark}
            };

        } else {
            return {
                code: 200,
                msg: 'ok',
                data: {name: name, age: age, phone: phone, remark: remark}
            };

        }


    }

    function bindEvent() {
        $(".delbtn").bind("click", function () {
            const id = $(this.parentElement.parentElement).find("td")[0].textContent;
            $.get('/train/test/test5/del?id=' + id, function (result) {
                if (result.code == 200) {
                    alert("数据删除成功!!!");
                    queryUser(null);
                }
            });

        });

        $(".addbtn").bind("click", function () {
            $("#myModalLabel").text("添加用户");
            var cds = $(".ageSelect").children();
            if (!cds || cds.length == 0) {
                for (let i = 0; i < 120; i++) {
                    $(".ageSelect").append("<option>" + i + "</option>");
                }
                $('.ageSelect').selectpicker({});
            }


        });

        $(".setbtn").bind("click", function () {
            $("#myModalLabel").text("编辑用户");
            // get Data
            const id = $(this.parentElement.parentElement).find("td")[0].textContent;
            var cds = $(".ageSelect").children();
            if (!cds || cds.length == 0) {
                for (let i = 0; i < 120; i++) {
                    $(".ageSelect").append("<option>" + i + "</option>");
                }
                $('.ageSelect').selectpicker({});
            }
            $.get('/train/test/test5/query?id=' + id, function (result) {
                if (result.code == 200) {
                    var stu = result.data;
                    $("#data-name").val(stu.name);
                    $("#data-id").val(stu.id);
                    debugger;
                    $("#data-age").val(stu.age);
                    $('#data-age').selectpicker('render');
                    $("#data-phone").val(stu.phone);
                    $("#data-remark").val(stu.remark);
                    $("#myModal").modal("show");
                }
            });

        });


        $("#addDataClsBtn").bind("click", function () {
            $("#data-name").val('');
            $("#data-phone").val('');
            $("#data-remark").val('');
            $("#data-id").val('')
            $('#myModal').modal('hide');
        })
    }

    function queryUser(page) {

        if(!page){
           var nodes = $("li.active");
            for (let i = 0; i < nodes.length; i++) {
                var node = nodes[i];
                var text = $(node).text();
                if(text && /[0-9]{1,5}/.test(text.trim())){
                    page=parseInt(text.trim());
                }
            }
        }

        $.ajax({
            async: true,
            type: "get",
            url: "/train/test/test4/data",//向后台发送请
            dataType: "json",
            data: {page: page, pageSize: $('.selectpicker').val(), keys: $("#keys").val()},
            cache: false,
            success: function (data) {
                var result = data;   //data.json_data为后台返回的JSON字符串，这里需要将其转换为JSON对象

                if (!result.rows || result.rows.length == 0) {
                    $("#userTable").css("height", "20px")
                } else {
                    $("#userTable").css("height", $("#select").css("height"))
                }

                var tbody = "<tr style='background:#fff;'><th >用户名</th><th>姓名</th>" +
                    "<th >角色</th><th>职务</th><th>联系方式</th><th>操作</th></tr>";
                for (var i = 0; i < result.rows.length; i++) {//拼接对应<th>需要的值
                    var trs = "";
                    trs += '<tr ><td >' + result.rows[i].id
                        + '</td><td >' + result.rows[i].name
                        + '</td><td >' + result.rows[i].age
                        + '</td><td>' + result.rows[i].remark
                        + '</td><td>' + result.rows[i].phone
                        + '</td><td>' +

                        '<a class="btn btn-danger delbtn" href="javascript:void(0)" aria-label="Delete"  style="margin-right: 2px"><i class="fa fa-trash-o" aria-hidden="true"></i></a>'
                        + '<a class="btn btn-success addbtn" href="javascript:void(0)" aria-label="Add" data-toggle="modal" data-target="#myModal" style="margin-right: 2px"><i class="fa fa-plus" aria-hidden="true"></i></a>'
                        + '<a class="btn btn-default setbtn" href="javascript:void(0)" aria-label="Settings" data-toggle="modal" ><i class="fa fa-cog" aria-hidden="true"></i></a>'
                        + '</td></tr>';
                    tbody += trs;
                }
                ;
                $("#userTable").html(tbody);

               bindEvent();

                var currentPage = result.page; //当前页数
                var pageCount = result.totalPages; //总页数
                var options = {
                    bootstrapMajorVersion: 3, //版本

                    currentPage: currentPage, //当前页数

                    totalPages: pageCount, //总页数

                    numberOfPages: 5,
                    shouldShowPage: true,//是否显示该按钮

                    itemTexts: function (type, page, current) {

                        switch (type) {

                            case "first":

                                return "首页";

                            case "prev":

                                return "上一页";

                            case "next":

                                return "下一页";

                            case "last":

                                return "末页";

                            case "page":

                                return page;

                        }

                    },//点击事件，用于通过Ajax来刷新整个list列表
                    onPageClicked: function (event, originalEvent, type, page) {
                        $.ajax({
                            async: true,
                            url: "/train/test/test4/data",
                            type: "get",
                            dataType: "json",
                            data: {page: page, pageSize: $('.selectpicker').val(), keys: $("#keys").val()},
                            cache: false,
                            success: function (data) {
                                var result = data;
                                if (!result.rows || result.rows.length == 0) {
                                    $("#userTable").css("height", "20px")
                                } else {
                                    $("#userTable").css("height", $("#select").css("height"))
                                }
                                var tbody = "<tr style='background:#fff;'><th >用户名</th>                                <th>姓名</th>" +
                                    "<th >角色</th><th>职务</th><th>联系方式</th><th>操作</th></tr>";
                                for (var i = 0; i < result.rows.length; i++) {

                                    var trs = "";
                                    trs += '<tr ><td >' + result.rows[i].id
                                        + '</td><td >' + result.rows[i].name
                                        + '</td><td >' + result.rows[i].age
                                        + '</td><td>' + result.rows[i].remark
                                        + '</td><td>' + result.rows[i].phone
                                        + '</td><td>' +
                                        '<a class="btn btn-danger delbtn" href="javascript:void(0)" aria-label="Delete"  style="margin-right: 2px"><i class="fa fa-trash-o" aria-hidden="true"></i></a>'
                                        + '<a class="btn btn-success addbtn" href="javascript:void(0)" data-toggle="modal" data-target="#myModal" aria-label="Add" style="margin-right: 2px"><i class="fa fa-plus" aria-hidden="true"></i></a>'
                                        + '<a class="btn btn-default setbtn" href="javascript:void(0)" data-toggle="modal" aria-label="Settings"><i class="fa fa-cog" aria-hidden="true"></i></a>'
                                        + '</td></tr>';
                                    +'</td></tr>';
                                    tbody += trs;

                                }
                                ;
                                $("#userTable").html(tbody);

                                bindEvent();

                            }/*success*/
                        });

                    }

                };
                $('#useroption').bootstrapPaginator(options);
            }/*success*/

        });
    }

    $(function () {


        $('.selectpicker').selectpicker({});
        $(".selectpicker").change(function () {
            queryUser(null);
        });

        queryUser(1);


    });

</script>
</html>
