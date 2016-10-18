$(document).ready(function(){
	filterPost = {
		order_status: ["已付款","制作中","已发货","维修中","已关闭","未付款"],
		from_create_date: "2015-01-01",
		to_create_date: "2016-01-01",
		product_id: [1,2,3,4,5,6],
		special_need: ["特殊加工","无特殊加工"],
		delivery_date: "",
		status: ["普通","加急"],
		ship_from: ["北京","深圳","未定"],
		ship_date: "",
		invoice_category: [0,1,2],
		express_status: ["已发货","未发货"],
		payment_method: ["支付宝","微信"],
		recipient: "",
		address_phonenum: "",
		phonenum: "",
		order_id: "",
		express_id: "",
		payment_id: "",
		invoice_express_id: "",
		page_num: "100",
		page_id: "1"
	};
	// 获取筛选条件
	$(".search-input").change(function(){
		filterPost.recipient = "";
		filterPost.address_phonenum = "";
		filterPost.phonenum = "";
		filterPost.order_id = "";
		filterPost.express_id = "";
		filterPost.payment_id = "";
		filterPost.invoice_express_id = "";
		switch($(".search-select").val()){
			case "recipient":filterPost.recipient = $(".search-input").val();break;
			case "address_phonenum":filterPost.address_phonenum = $(".search-input").val();break;
			case "phonenum":filterPost.phonenum = $(".search-input").val();break;
			case "order_id":filterPost.order_id = $(".search-input").val();break;
			case "express_id":filterPost.express_id = $(".search-input").val();break;
			case "payment_id":filterPost.payment_id = $(".search-input").val();break;
			case "invoice_express_id":filterPost.invoice_express_id = $(".search-input").val();break;
			default:break;
		}
		console.log(filterPost);
	});
	//点击换页
	$(".page-num").click(function(){
		$(".page-num").removeClass("selected");
		$(this).addClass("selected");
		filterPost.page_id = $(this).text();
		$.orderListAjax(filterPost);
	});
	// 点击搜索按钮
	$(".submit-search").click(function(){
		$.orderListAjax(filterPost);
	});
	// 侧边栏 slidebar 快捷筛选
	$(".slide-button").click(function(){
		$(".slide-button").removeClass("on");
		$(this).addClass("on");
		$(".order-status-options").removeAttr("checked");
		switch($(this).attr("name")){
			case "all":$(".order-status-options").prop("checked","checked");break;
			case "paid":$("#order_status_paid").prop("checked","checked");break;
			case "making":$("#order_status_making").prop("checked","checked");break;
			case "ship":$("#order_status_ship").prop("checked","checked");break;
			case "fix":$("#order_status_fix").prop("checked","checked");break;
			case "close":$("#order_status_close").prop("checked","checked");break;
			default:break;
		}
		$("#submit_order_status").click();
	});
	
	//console.log(filterPost);
	$.filterBoard = function(){
		// 提交订单状态筛选条件
		$("#submit_order_status").click(function(){
		    console.log("this is another test");
			var order_status = [];
			$(".order-status-options").each(function(){
				if(this.checked){
					order_status.push($(this).val());
				}
			});
			//console.log(order_status);
			if(order_status.length == 0){
				$(this).parent().prev().text("请至少选择一种状态！");
				return false;
			}
			filterPost.order_status = order_status;
			$(this).parent().parent().slideUp();
			// 添加筛选标签
			addFilterArrayTag(["订单状态"], "order-status-tag");
		    $.batchChangeTag(filterPost);
			$(this).parent().parent().slideUp();
		});
		
		// 提交下单日期状态筛选条件
		$("#submit_order_create").click(function(){
			var from_create_date,to_create_date;
			var from_year = $("#create_from_year").val();
			var from_month = $("#create_from_month").val();
			var from_day = $("#create_from_day").val();
			var to_year = $("#create_to_year").val();
			var to_month = $("#create_to_month").val();
			var to_day = $("#create_to_day").val();
			console.log(from_year);
			if(!$.verifyYear(from_year)||!$.verifyYear(to_year)){
				$(this).parent().prev().text("请输入正确的年份！");
				return false;
			}else{
				$(this).parent().prev().text("");
			}
			if(!$.verifyMonth(from_month)||!$.verifyMonth(to_month)){
				$(this).parent().prev().text("请输入正确的月份！");
				return false;
			}else{
				$(this).parent().prev().text("");
			}
			if(!$.verifyDay(from_day)||!$.verifyDay(to_day)){
				$(this).parent().prev().text("请输入正确的日期！");
				return false;
			}else{
				$(this).parent().prev().text("");
			}
			from_create_date = from_year + "-" + from_month + "-" + from_day;
			to_create_date = to_year + "-" + to_month + "-" + to_day;
			var start = new Date(from_create_date.replace("-","/"));
			var end = new Date(to_create_date.replace("-","/"));
			if(end < start){
				$(this).parent().prev().text("结束日期不能小于开始日期！");
				return false;
			}else{
				$(this).parent().prev().text("");
			}
			filterPost.from_create_date = from_create_date;
			filterPost.to_create_date = to_create_date;
			$(this).parent().parent().slideUp();
			// 添加筛选标签
			addFilterArrayTag(["下单日期"], "order-create-tag");
			$.orderListAjax(filterPost);
			$(this).parent().parent().slideUp();
		});
		// 提交订单详情筛选条件
		$("#submit_color").click(function(){
			var product_id = [];
			$(".color-options").each(function(){
				if(this.checked){
					product_id.push($(this).val());
				}
			});
			if(product_id.length == 0){
				$(this).parent().prev().text("请至少选择一种状态！");
				return false;
			}else{
				$(this).parent().prev().text("");
			}
			filterPost.product_id = product_id;
			$(this).parent().parent().slideUp();
			// 添加筛选标签
			addFilterArrayTag(["产品颜色"], "color-tag");
			$.orderListAjax(filterPost);
			$(this).parent().parent().slideUp();
		});
		// 提交特殊加工筛选条件
		$("#submit_special_need").click(function(){
			var special_need = [];
			$(".special-need-options").each(function(){
				if($(this).hasClass("on")){
					special_need.push($(this).text());
				}
			});
			if(special_need.length == 0){
				$(this).parent().prev().text("请至少选择一种状态！");
				return false;
			}else{
				$(this).parent().prev().text("");
			}
			filterPost.special_need = special_need;
			$(this).parent().parent().slideUp();
			// 添加筛选标签
			addFilterArrayTag(special_need, "special-need-tag");
			$.orderListAjax(filterPost);
			$(this).parent().parent().slideUp();
		});
		// 提交交货日期筛选条件
		$("#submit_delivery_date").click(function(){
			if($("#need_delivery_date").is(":checked")){
				filterPost.delivery_date = "";
			}else{
				var delivery_year = $("#delivery_year").val();
				var delivery_month = $("#delivery_month").val();
				var delivery_day = $("#delivery_day").val();
				if(!$.verifyYear(delivery_year)){
					$(this).parent().prev().text("请输入正确的年份！");
					return false;
				}else{
					$(this).parent().prev().text("");
				}
				if(!$.verifyMonth(delivery_month)){
					$(this).parent().prev().text("请输入正确的月份！");
					return false;
				}else{
					$(this).parent().prev().text("");
				}
				if(!$.verifyDay(delivery_day)){
					$(this).parent().prev().text("请输入正确的日期！");
					return false;
				}else{
					$(this).parent().prev().text("");
				}
				var delivery_date = delivery_year + "-" + delivery_month + "-" +delivery_day;
				filterPost.delivery_date = delivery_date;
			}
			$(this).parent().parent().slideUp();
			// 添加筛选标签
			addFilterArrayTag(["交货日期"], "delivery-date-tag");
			$.orderListAjax(filterPost);
			$(this).parent().parent().slideUp();
		});
		// 提交状态筛选条件
		$("#submit_status").click(function(){
			var status = [];
			$(".status-options").each(function(){
				if($(this).hasClass("on")){
					status.push($(this).text());
				}
			});
			if(status.length == 0){
				$(this).parent().prev().text("请至少选择一种状态！");
				return false;
			}else{
				$(this).parent().prev().text("");
			}
			filterPost.status = status;
			$(this).parent().parent().slideUp();
			// 添加筛选标签
			addFilterArrayTag(status, "status-tag");
			$.orderListAjax(filterPost);
			$(this).parent().parent().slideUp();
		});
		// 提交发货地点筛选条件
		$("#submit_ship_from").click(function(){
			var ship_from = [];
			$(".ship-from-options").each(function(){
				if($(this).hasClass("on")){
					ship_from.push($(this).text());
				}
			});
			if(ship_from.length == 0){
				$(this).parent().prev().text("请至少选择一种地点！");
				return false;
			}else{
				$(this).parent().prev().text("");
			}
			filterPost.ship_from = ship_from;
			$(this).parent().parent().slideUp();
			// 添加筛选标签
			addFilterArrayTag(["发货地点"], "ship-from-tag");
			$.orderListAjax(filterPost);
			$(this).parent().parent().slideUp();
		});
		// 提交发货批次筛选条件
		$("#submit_ship_date").click(function(){
			if($("#need_ship_date").is(":checked")){
				filterPost.ship_date = "";
			}else{
				var ship_year = $("#ship_year").val();
				var ship_month = $("#ship_month").val();
				var ship_day = $("#ship_day").val();
				if(!$.verifyYear(ship_year)){
					$(this).parent().prev().text("请输入正确的年份！");
					return false;
				}else{
					$(this).parent().prev().text("");
				}
				if(!$.verifyMonth(ship_month)){
					$(this).parent().prev().text("请输入正确的月份！");
					return false;
				}else{
					$(this).parent().prev().text("");
				}
				if(!$.verifyDay(ship_day)){
					$(this).parent().prev().text("请输入正确的日期！");
					return false;
				}else{
					$(this).parent().prev().text("");
				}
				var ship_date = ship_year + "-" + ship_month + "-" +ship_day;
				filterPost.ship_date = ship_date;
			}
			$(this).parent().parent().slideUp();
			// 添加筛选标签
			addFilterArrayTag(["发货批次"], "ship-date-tag");
			$.orderListAjax(filterPost);
			$(this).parent().parent().slideUp();
		});
		// 提交发票信息筛选条件
		$("#submit_invoice").click(function(){
			var invoice_category = [];
			$(".invoice-options").each(function(){
				if($(this).hasClass("on")){
					var invoice;
					switch($(this).text()){
						case "不需要发票":invoice = 0;break;
						case "公司":invoice = 2;break;
						case "个人":invoice = 1;break;
						default:break;
					}
					invoice_category.push(invoice);
				}
			});
			if(invoice_category.length == 0){
				$(this).parent().prev().text("请至少选择一种发票类别！");
				return false;
			}else{
				$(this).parent().prev().text("");
			}
			filterPost.invoice_category = invoice_category;
			$(this).parent().parent().slideUp();
			// 添加筛选标签
			addFilterArrayTag(["发票信息"], "invoice-tag");
			$.orderListAjax(filterPost);
			$(this).parent().parent().slideUp();
		});
		// 提交快递信息筛选条件
		$("#submit_express_status").click(function(){
			var express_status = [];
			$(".express-status-options").each(function(){
				if($(this).hasClass("on")){
					express_status.push($(this).text());
				}
			});
			if(express_status.length == 0){
				$(this).parent().prev().text("请至少选择一种快递状态！");
				return false;
			}else{
				$(this).parent().prev().text("");
			}
			filterPost.express_status = express_status;
			$(this).parent().parent().slideUp();
			// 添加筛选标签
			addFilterArrayTag(["快递信息"], "express-status-tag");
			$.orderListAjax(filterPost);
			$(this).parent().parent().slideUp();
		});
		// 提交支付信息筛选条件
		$("#submit_payment_method").click(function(){
			var payment_method = [];
			$(".payment-method-options").each(function(){
				if($(this).hasClass("on")){
					payment_method.push($(this).text());
				}
			});
			if(payment_method.length == 0){
				$(this).parent().prev().text("请至少选择一种付款方式！");
				return false;
			}else{
				$(this).parent().prev().text("");
			}
			filterPost.payment_method = payment_method;
			$(this).parent().parent().slideUp();
			// 添加筛选标签
			addFilterArrayTag(["支付信息"], "payment-method-tag");
			$.orderListAjax(filterPost);
			$(this).parent().parent().slideUp();
		});


		// 根据筛选条件添加标签 适用于数组类条件
		function addFilterArrayTag(array, tag){
			$(".filter-condition").each(function(){
				if($(this).hasClass(tag)){
					$(this).remove();
				}
			});
			$.each(array, function(n, value){
				$("#filter_detail_tag").append(
					"<span class='filter-condition " +
					tag +
					"'>\"" +
					value +
					"\"</span>");
			});
		}
	};
	// 绑定filter方法到filterboard
	$.filterBoard();
});