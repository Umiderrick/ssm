$(document).ready(function(){
	//获取url中的参数
	var postAjax;
    $.getUrlParam = function(name){
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);  //匹配目标参数
        if (r != null) return unescape(r[2]); return null; //返回参数值
    };
	var version = $.getUrlParam("version");
	var order_id = $.getUrlParam("orderId");
	var postOrder = {
		"version": version,
		"order_id": order_id
	};
	console.log(postOrder);
	$.ajax({
		type: 'POST',
		url: 'http://localhost/learnphp/ajaxOrderJson.php',
		contentType: "application/json;charset=utf-8",
		data: JSON.stringify(postOrder),
		dataType: 'JSONP',
		success: function(data){
			console.log("mac success");
			console.log(data);
			$.renderOrderDetail(data);
			postAjax = data;
		},
		error: function(err){
			console.log("mac fail");
			console.log(err);
			$(".mac-status").text("mac 添加失败！");
		}
	});
	$.renderOrderDetail = function(data){
		$(".one-change-log").remove();
		$(".right-board").remove();
		// orderDetail 渲染
		var productName, specialNeed, needInvoice, invoiceCategory, invoiceTitle, invoiceExpress, invoiceExpressId;
		if(data[0].product_id <= 6){
			productName = "VINCI 1.0";
		}
		if(data[0].special_need == "null"){
			specialNeed = "无";
		}
		switch(data[0].invoice_category){
			case '0':needInvoice = "不需要";invoiceCategory = "无";invoiceTitle = "无";break;
			case '1':needInvoice = "需要";invoiceCategory = "个人发票";invoiceTitle = "个人";break;
			case '2':needInvoice = "需要";invoiceCategory = "公司发票";invoiceTitle = data[0].invoice_title;break;
			default:break;
		}
		if(data[0].express.invoice_id == "null"){
			invoiceExpress = "未寄出";
			invoiceExpressId = "无";
		}else{
			invoiceExpress = "已快递";
			invoiceExpressId = data[0].express.invoice_id;
		}
		var orderDetail = '';
		$(".left-board").after(orderDetail);
		// 渲染备注信息
		var remarkLog = "";
		$.each(data[0].log_list, function(n, value){
			remarkLog = remarkLog +
			'';
		});
		$(".log-body").append(remarkLog);
		// 渲染耳机展示图
		$.renderProductImage(data[0].product_id);
		$.changeDetail(data[0].order_status);
    };
    $.renderProductImage = function(id){
		console.log(id);
		switch(id){
			case "1":$(".vinci-product-images").removeClass().addClass("white-vinci vinci-product-images");break;
			case "2":$(".vinci-product-images").removeClass().addClass("yellow-vinci vinci-product-images");break;
			case "3":$(".vinci-product-images").removeClass().addClass("red-vinci vinci-product-images");break;
			case "4":$(".vinci-product-images").removeClass().addClass("purple-vinci vinci-product-images");break;
			case "5":$(".vinci-product-images").removeClass().addClass("green-vinci vinci-product-images");break;
			case "6":$(".vinci-product-images").removeClass().addClass("black-vinci vinci-product-images");break;
			default:break;
		}
	};
	$.changeDetail = function(order_status){
		$(".order-change-icon").click(function(){
			// 修改模式&展示模式切换
			if($(".order-change-explain").text() == "修改订单信息"){
				// 切换到修改模式
				$(this).addClass("start-change-icon");
				$(".order-change-explain").text("完成订单修改");
				// 判断订单状态，决定哪些选项可以修改
				$.decideInputActive(order_status);
				// 修改订单状态
				$.changeOrderStatus();
			}else{
				// 提示模态框 模态框事件
				$(".submit-modal").fadeIn(200);
				$(".submit-modal-cancel").click(function(){
					$(".submit-modal").fadeOut(200);
				});
				$(".submit-modal-confirm").click(function(){
					$(".submit-modal").fadeOut(200);
					// 渲染订单状态button
					$(".order-status").remove();
					$(".left-order-detail").prepend("<button class='order-status'>订单状态：" + $("#order_status").text() + "</button>");
					// input 不可修改
					$(".table-row-input").removeClass("table-row-input-active").attr("disabled","disabled");
					$(".order-change-icon").removeClass("start-change-icon");
					$(".order-change-explain").text("修改订单信息");
					console.log(1);
					postAjax[0].product_color = $("#product_color").val();
					switch(postAjax[0].product_color){
						case "黑色":postAjax[0].product_id = 6;break;
						case "白色":postAjax[0].product_id = 1;break;
						case "黄色":postAjax[0].product_id = 2;break;
						case "紫色":postAjax[0].product_id = 4;break;
						case "红色":postAjax[0].product_id = 3;break;
						case "绿色":postAjax[0].product_id = 5;break;
					}
					postAjax[0].delivery_date = $("#delivery_date").val();
					postAjax[0].address = {};
					postAjax[0].address.recipient = $("#address_recipient").val();
					postAjax[0].address.detail = $("#address").val();
					postAjax[0].address.phonenum = $("#address_phonenum").val();
					postAjax[0].order_status = $("#order_status").text();
					postAjax[0].ship_from = $("#ship_from").val();
					postAjax[0].ship_date = $("#ship_date").val();
					postAjax[0].express.express_id = $("#express_id").val();
					postAjax[0].invoice_category = $("#invoice_category").attr("category");
					postAjax[0].invoice_title = $("#invoice_title").val();
					postAjax[0].express.status = $("#invoice_express").val();
					postAjax[0].express.invoice_id = $("#invoice_express_id").val();
					console.log(postAjax);
					// ajax 提交详细信息
				})
			}
		})
	};
	// 模态框事件
	$(".submit-modal-cancel").click(function(){
		$(".submit-modal").fadeOut(200);
	})
});