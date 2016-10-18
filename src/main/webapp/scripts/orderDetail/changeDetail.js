$(document).ready(function(){
	
	// 判断订单状态
	$.decideInputActive = function(order_status){
		console.log(order_status);
		switch(order_status){
			case "已发货":{
				// 发票可修改
				$("#need_invoice").removeAttr("disabled").addClass("table-row-input-active");
				$("#invoice_category").removeAttr("disabled").addClass("table-row-input-active");
				$("#invoice_title").removeAttr("disabled").addClass("table-row-input-active");
				$("#invoice_express").removeAttr("disabled").addClass("table-row-input-active");
				$("#invoice_express_id").removeAttr("disabled").addClass("table-row-input-active");
				// 修改订单状态限制
				$(".order-status").text("改变订单状态：特殊").addClass("order-status-change special-status");
			}
				break;
			case "已付款":{
				// 发票可修改
				$("#need_invoice").removeAttr("disabled").addClass("table-row-input-active");
				$("#invoice_category").removeAttr("disabled").addClass("table-row-input-active");
				$("#invoice_title").removeAttr("disabled").addClass("table-row-input-active");
				$("#invoice_express").removeAttr("disabled").addClass("table-row-input-active");
				$("#invoice_express_id").removeAttr("disabled").addClass("table-row-input-active");
				// 商品颜色可修改
				$("#product_color").removeAttr("disabled").addClass("table-row-input-active");
				// 交货信息模块可修改
				$("#delivery_date").removeAttr("disabled").addClass("table-row-input-active");
				$("#address_recipient").removeAttr("disabled").addClass("table-row-input-active");
				$("#address").removeAttr("disabled").addClass("table-row-input-active");
				$("#address_phonenum").removeAttr("disabled").addClass("table-row-input-active");
				// 发货信息可修改
				$("#ship_from").removeAttr("disabled").addClass("table-row-input-active");
				$("#ship_date").removeAttr("disabled").addClass("table-row-input-active");
				$("#express_id").removeAttr("disabled").addClass("table-row-input-active");
				// 修改订单状态限制
				$(".order-status").text("改变订单状态：已发货").addClass("order-status-change already-ship");
				$(".order-status").after("<button class='order-status order-status-change order-close'>改变订单状态：已关闭</button>");
			}
				break;
			default:break;
		}
	};
	$.changeOrderStatus = function(){
		$(".order-status").click(function(){
			if($(".order-change-explain").text() != "修改订单信息"){
				if($(this).hasClass("special")){
					$("#order_status").text("特殊");
				}
				if($(this).hasClass("already-ship")){
					$("#order_status").text("已发货");
				}
				if($(this).hasClass("order-close")){
					$("#order_status").text("已关闭");
				}
			}
		})
	}
});