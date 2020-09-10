<html>
<head>
    <link href="bootstrap.min.css">
</head>
<body>
<h2>Hello World!2xxxxxxxxxxxxxy</h2>











</body>
<script>
    $(function () {
        $(".modKu").bind("click", function () {

            var url = 'http://localhost:8080/inventory/batchUpdateSafeInventory.json'
            /*   $.ajax({
                   url: url,
                   dataType: 'json',
                   type: 'post',
                   contentType: "application/json; charset=utf-8",
                   data: JSON.stringify(result.data),
                   success: (result) => {
                       if (result.code == 200) {

                           $('#myModal').modal('hide');
                           queryUser(null);
                           setTimeout(function () {
                               alert("数据修改成功2！！！");
                           }, 500)
                       }
                   }
               });*/

            /*$.post(url,{skuIds: 12408,threshold: 25},function (data){
                console.log(data);
                alert("hahaha....!!!");
            })*/


            $.ajax({
                type: 'POST',
                url: url,
                crossDomain: true,
                data: {skuIds: 12408,threshold: 25},
                success: function(responseData, textStatus, jqXHR) {
                    alert(1);
                },
                error: function (responseData, textStatus, errorThrown) {
                    alert('2');
                }});
        })
    });
</script>
</html>
