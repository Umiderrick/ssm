$(document).ready(function(){
	//获取url中的参数
    $.getUrlParam = function(name){
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);  //匹配目标参数
        if (r != null) return unescape(r[2]); return null; //返回参数值
    };
	var postFilter = {
		"delivrey_date": $.getUrlParam("delivrey_date"),
		"express_status": $.getUrlParam("express_status"),
		"from_create_date": $.getUrlParam("from_create_date"),
		"invoice_category": $.getUrlParam("invoice_category"),
		"order_status": $.getUrlParam("order_status"),
		"payment_method": $.getUrlParam("payment_method"),
		"product_id": $.getUrlParam("product_id"),
		"ship_date": $.getUrlParam("ship_date"),
		"ship_from": $.getUrlParam("ship_from"),
		"special_need": $.getUrlParam("special_need"),
		"status": $.getUrlParam("status"),
		"to_create_date": $.getUrlParam("to_create_date")
	};
	$.orderListAjax(postFilter);
	console.log(1);
	// 批量选择发送需要发送短信的订单
	$.changeOrder = function(){
		$(".order-detail-row").click(function(){
			if($(this).find(".batch-change-tag-hide")[0] === undefined){
				$(this).find(".batch-change-tag-show").addClass("batch-change-tag-hide").removeClass("batch-change-tag-show");
				$(this).removeClass("white-row");
			}else{
				$(this).find(".batch-change-tag-hide").addClass("batch-change-tag-show").removeClass("batch-change-tag-hide");
				$(this).addClass("white-row");
			}
			$("#receiver_num").text($(".white-row").length);
			console.log($(".white-row").length)
		})
	};
	// 点击群发按钮弹出模态框 并向后台发送群发请求
	$(".send-button").click(function(){
		$(".submit-modal").fadeIn(200);
		$(".submit-modal-cancel").click(function(){
			$(".submit-modal").fadeOut(200);
		});
		$(".submit-modal-confirm").click(function(){
			// 向后台发送群发短信请求
			$(".submit-modal").fadeOut(200);
		})
	});
	// 点击取消按钮后，返回首页
	$(".cancel-button").click(function(){
		window.location = "index.html";
	});
	// 短信模版发生变化时，更新模版内容
	$("#send_message").change(function(){
		console.log(1);
		$(".message-model").removeClass("on");
		var sendMessage = $(this).val();
		switch(sendMessage){
			case "发货通知":$("#ship_message").addClass("on");break;
			case "新品上市":$("#new_product_message").addClass("on");break;
			case "逢年过节":$("#festival_message").addClass("on");break;
			default:break;
		}
	})
});