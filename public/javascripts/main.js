function want(sender, itemCode, reloadFlg) {
   var route = jsRoutes.controllers.ItemUserController.want();
   $.ajax({
       url: route.url,
       type: route.type,
       data: {
           "itemCode": itemCode
       },
       success: function (data) {
           $(sender).removeClass("btn-primary");
           $(sender).addClass("btn-success");
           $(sender).text("Want");
           $(sender).off('click');
           $(sender).on('click', function(e){ doNotWant($(sender), itemCode, reloadFlg); });
           if (reloadFlg) {
               location.reload();
           }
       },
       error: function (xhr) {
           alert("Error!: " + xhr.responseText)
       }
   });
}

function doNotWant(sender, itemCode, reloadFlg) {
   var route = jsRoutes.controllers.ItemUserController.doNotWant();
   $.ajax({
       url: route.url,
       type: route.type,
       data: {
           "itemCode": itemCode
       },
       success: function (data) {
           $(sender).removeClass("btn-success");
           $(sender).addClass("btn-primary");
           $(sender).text("Want It");
           $(sender).off('click');
           $(sender).on('click', function(e){ want($(sender), itemCode, reloadFlg); });
           if (reloadFlg) {
               location.reload();
           }
       },
       error: function (xhr) {
           alert("Error!: " + xhr.responseText)
       }
   });
}