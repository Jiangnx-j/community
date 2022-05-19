$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");
	var content = $("#message-text").val();
	var title = $("#recipient-name").val();
	if (title.trim().length == 0){
		$("#hintModal").modal("show");
		$("#hintBody").text("请输入标题");
		setTimeout(function(){
			$("#hintModal").modal("hide");
		}, 2000);
	} else if (content.trim().length == 0) {
		$("#hintModal").modal("show");
		$("#hintBody").text("请输入文章内容");
		setTimeout(function(){
			$("#hintModal").modal("hide");
		}, 2000);
	}else {
		$.post(
			CONTEXT_PATH+'/discuss/add',
			{"title":title,"content":content},
			function (data) {
				data = $.parseJSON(data);
				$("#hintBody").text(data.msg);
				$("#hintModal").modal("show");
				setTimeout(function(){
					$("#hintModal").modal("hide");
					if (data.code == 0){
						window.location.reload();
					}
				}, 2000);
			}
		);
	}



}