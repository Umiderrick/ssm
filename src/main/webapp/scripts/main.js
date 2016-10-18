$(document).ready(function(){
	var filterPost = {
		order_status: ["已付款","制作中","已发货","维修中","已关闭","未付款"],
		from_create_date: "2015-01-01",
		to_create_date: "2016-01-01",
		product_id: [1,2,3,4,5,6],
		special_need: ["特殊加工","无特殊加工"],
		delivery_date: null,
		status: ["普通","加急"],
		ship_from: ["北京","深圳","未定"],
		ship_date: null,
		invoice_category: [0,1,2],
		express_status: ["已发货","未发货"],
		payment_method: ["支付宝","微信"]
	};
	console.log(filterPost);
});
$(document).ready(function(){
	// slidebar 点击后样式
	$(".slide-button").click(function(){
		$(".slide-button").removeClass("on");
		$(this).addClass("on");
	});
	// filter 面板中点击确认和取消按钮
	$(".confirm-button").click(function(){
		$(this).parent().parent().slideUp();
		console.log(filterPost);
		// ajax
	});
	$(".cancel-button").click(function(){
		$(this).parent().parent().slideUp();
	});
	// order-status-filter 点击下拉
	$(".filter-order-status").click(function(){
		$(".filter-order-status-board").slideToggle();
	});
	// order-create-board 点击下拉
	$(".filter-order-create").click(function(){
		$(".filter-order-create-board").slideToggle();
	});
	// color-board 点击下拉
	$(".filter-color").click(function(){
		$(".filter-color-board").slideToggle();
	});
	// special-need-board 点击下拉 选择
	$(".filter-special-need").click(function(){
		$(".filter-special-need-board").slideToggle();
	});
	$(".special-need-options").click(function(){
		optionsClick($(this));
	});
	// delivery-date-board 点击下拉
	$(".filter-delivery-date").click(function(){
		$(".filter-delivery-date-board").slideToggle();
	});
	// status-board 点击下拉 选择
	$(".filter-status").click(function(){
		$(".filter-status-board").slideToggle();
	});
	$(".status-options").click(function(){
		optionsClick($(this));
	});
	// ship-from-board 点击下拉
	$(".filter-ship-from").click(function(){
		$(".filter-ship-from-board").slideToggle();
	});
	$(".ship-from-options").click(function(){
		optionsClick($(this));
	});
	// ship-date-board 点击下拉
	$(".filter-ship-date").click(function(){
		$(".filter-ship-date-board").slideToggle();
	});
	// invoice-board 点击下拉
	$(".filter-invoice").click(function(){
		$(".filter-invoice-board").slideToggle();
	});
	$(".invoice-options").click(function(){
		optionsClick($(this));
	});
	// express-status-board 点击下拉
	$(".filter-express-status").click(function(){
		$(".filter-express-status-board").slideToggle();
	});
	$(".express-status-options").click(function(){
		optionsClick($(this));
	});
	// payment-method-board 点击下拉
	$(".filter-payment-method").click(function(){
		$(".filter-payment-method-board").slideToggle();
	});
	$(".payment-method-options").click(function(){
		optionsClick($(this));
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