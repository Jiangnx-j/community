function like(btn,entityType,entityId,userId) {
    $.post(
        CONTEXT_PATH+"/like",
        {"entityType":entityType,"entityId":entityId,"authorId":userId},
        function (data) {
            data = $.parseJSON(data);
            if (data.code == 0){
                $(btn).children("b").text(data.status==1?"已赞":"赞");
                $(btn).children("i").text(data.counts);
            } else {
                alert(data.msg);
            }
        }
    )
}