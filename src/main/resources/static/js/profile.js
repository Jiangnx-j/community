$(function(){

});

function follower(btn,userId,followerId) {
	var follower = 'follower';
	$.post(
		CONTEXT_PATH+"/follower",
		{"userId":userId,"followerId":followerId},
		function (data) {
			data = $.parseJSON(data);
			if (data.code==0){
				if(follower == data.msg) {
					// 关注TA
					$(btn).text("已关注").removeClass("btn-info").addClass("btn-secondary");
				} else {
					// 取消关注
					$(btn).text("关注TA").removeClass("btn-secondary").addClass("btn-info");
				}
				location.reload();
			}else {
				alert("服务器异常")
			}
		}
	);
}

function isFollower(body,isFollower) {
	var followBtn = $("#follow-btn");
	if(isFollower) {
		// 关注TA
		followBtn.text("已关注").removeClass("btn-info").addClass("btn-secondary");
	} else {
		// 取消关注
		followBtn.text("关注TA").removeClass("btn-secondary").addClass("btn-info");
	}
}