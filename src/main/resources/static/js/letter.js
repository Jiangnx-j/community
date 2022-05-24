$(function(){
	$("#sendBtn").click(send_letter);
	$(".close").click(delete_msg);
});

function send_letter() {
	var targetName = $("#recipient-name").val();
	var content = $("#message-text").val();
	if (targetName.trim().length == 0){
		$("#hintModal").modal("show");
		$("#hintBody").text("请输入好友昵称");
		setTimeout(function(){
			$("#hintModal").modal("hide");
		}, 2000);
	} else if (content.trim().length == 0) {
		$("#hintModal").modal("show");
		$("#hintBody").text("请输入私信内容");
		setTimeout(function(){
			$("#hintModal").modal("hide");
		}, 2000);

	}else {
		$("#sendModal").modal("hide");
		$.post(
			CONTEXT_PATH +"/letter/send",
			{
				"targetName":targetName,
				"content":content
			},
			function (data) {
				data = $.parseJSON(data);
				$("#hintBody").text(data.msg);
				$("#hintModal").modal("show");
				setTimeout(function () {
					$("#hintModal").modal("hide");
					if (data.code==0) {
						location.reload();
					}
				}, 2000);
			}
		);
	}

}


function delete_msg() {
	// TODO 删除数据
	$(this).parents(".media").remove();
}