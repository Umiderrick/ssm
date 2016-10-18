$(document).ready(function(){
	
	// filter 面板中点击确认和取消按钮
	$(".confirm-button").click(function(){
		//console.log(filterPost);
		// ajax
	});
	$(".cancel-button").click(function(){
		$(this).parent().parent().slideUp();
	});
	// order-status-filter 点击下拉
	$(".filter-order-status").click(function(){
		if(!$("#batch_change_start").hasClass("batch-change-color")){
			$(".filter-order-status-board").slideToggle();
		}
	});
	// order-create-board 点击下拉
	$(".filter-order-create").click(function(){
		if(!$("#batch_change_start").hasClass("batch-change-color")){
			$(".filter-order-create-board").slideToggle();
		}
	});
	// color-board 点击下拉
	$(".filter-color").click(function(){
		if(!$("#batch_change_start").hasClass("batch-change-color")){
			$(".filter-color-board").slideToggle();
		}
	});
	// special-need-board 点击下拉 选择
	$(".filter-special-need").click(function(){
		if(!$("#batch_change_start").hasClass("batch-change-color")){
			$(".filter-special-need-board").slideToggle();
		}
	});
	$(".special-need-options").click(function(){
		optionsClick($(this));
	});
	// delivery-date-board 点击下拉
	$(".filter-delivery-date").click(function(){
		if(!$("#batch_change_start").hasClass("batch-change-color")){
			$(".filter-delivery-date-board").slideToggle();
		}
	});
	// status-board 点击下拉 选择
	$(".filter-status").click(function(){
		if(!$("#batch_change_start").hasClass("batch-change-color")){
			$(".filter-status-board").slideToggle();
		}
	});
	$(".status-options").click(function(){
		optionsClick($(this));
	});
	// ship-from-board 点击下拉
	$(".filter-ship-from").click(function(){
		if(!$("#batch_change_start").hasClass("batch-change-color")){
			$(".filter-ship-from-board").slideToggle();
		}
	});
	$(".ship-from-options").click(function(){
		optionsClick($(this));
	});
	// ship-date-board 点击下拉
	$(".filter-ship-date").click(function(){
		if(!$("#batch_change_start").hasClass("batch-change-color")){
			$(".filter-ship-date-board").slideToggle();
		}
	});
	// invoice-board 点击下拉
	$(".filter-invoice").click(function(){
		if(!$("#batch_change_start").hasClass("batch-change-color")){
			$(".filter-invoice-board").slideToggle();
		}
	});
	$(".invoice-options").click(function(){
		optionsClick($(this));
	});
	// express-status-board 点击下拉
	$(".filter-express-status").click(function(){
		if(!$("#batch_change_start").hasClass("batch-change-color")){
			$(".filter-express-status-board").slideToggle();
		}
	});
	$(".express-status-options").click(function(){
		optionsClick($(this));
	});
	// payment-method-board 点击下拉
	$(".filter-payment-method").click(function(){
		if(!$("#batch_change_start").hasClass("batch-change-color")){
			$(".filter-payment-method-board").slideToggle();
		}
	});
	$(".payment-method-options").click(function(){
		optionsClick($(this));
	});
	// batch-order-status-board 点击下拉
	$(".batch-order-status").click(function(){
		$(".batch-change-order-status-board").slideToggle();
	});
	// batch-delivery-date-board 点击下拉
	$(".batch-delivery-date").click(function(){
		$(".batch-change-delivery-date-board").slideToggle();
	});
	// batch-ship-from-board 点击下拉
	$(".batch-ship-from").click(function(){
		$(".batch-change-ship-from-board").slideToggle();
	});
	// batch-ship-date-board 点击下拉
	$(".batch-ship-date").click(function(){
		$(".batch-change-ship-date-board").slideToggle();
	});
	// batch-express-status-board 点击下拉
	$(".batch-express-status").click(function(){
		$(".batch-change-express-status-board").slideToggle();
	});
	// 查看详细订单信息，跳转至orderdetail页面
	$.changeOrder = function(){
		$(".order-detail-row").click(function(){
			if(!$("#batch_change_start").hasClass("batch-change-color")){
				console.log(1);
				var version = $(this).attr("version");
				var order_id = $(this).attr("order");
				window.location = "orderDetail.html?version=" + version + "&orderId=" + order_id;
			}else{
				if($(this).find(".batch-change-tag-hide")[0] === undefined){
					$(this).find(".batch-change-tag-show").addClass("batch-change-tag-hide").removeClass("batch-change-tag-show");
					$(this).removeClass("white-row");
				}else{
					$(this).find(".batch-change-tag-hide").addClass("batch-change-tag-show").removeClass("batch-change-tag-hide");
					$(this).addClass("white-row");
				}
			}
		})
	};
	// 群发按钮，get 方式跳转到 groupSending 页面，对点击事件限定
	$("#group_sending").click(function(){
		if($(".order-detail-table").children()[0] !== undefined && !$("#batch_change_start").hasClass("batch-change-color")){
			// console.log("群发条件满足");
			console.log(filterPost);
			var url = "groupSending.html?delivery_date=" + filterPost.delivery_date
			&express_status=" + filterPost.express_status + "
			&from_create_date=" + filterPost.from_create_date + "
			&invoice_category=" + filterPost.invoice_category + "
			&order_status=" + filterPost.order_status + "
			&payment_method=" + filterPost.payment_method + "
			&product_id=" + filterPost.product_id + "
			&ship_date=" + filterPost.ship_date + "
			&ship_from=" + filterPost.ship_from + "
			&special_need=" + filterPost.special_need + "
			&status=" + filterPost.status + "
			&to_create_date=" + filterPost.to_create_date：;
			window.location = url;
		}
	});

	// 通用方法
	// 筛选板 选项点击事件
	function optionsClick(a){
		if(a.hasClass("on")){
			a.removeClass("on");
		}else{
			a.addClass("on");
		}
	}
});