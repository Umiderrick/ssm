    $(document).ready(function(){
     	$.orderListAjax = function(filterPost){
     	    console.log("this is test");
     		console.log(filterPost);
     		$.ajax({
     			type: 'POST',
     			contentType: "application/json;charset=utf-8",
     			data: JSON.stringify({
     				"order_id": "0"
     			}),
     			dataType: 'JSONP',
     			success: function(data){
     				console.log("mac success");
     				console.log(data);
     				$.renderOrderList(data);
     			},
     			error: function(err){
     				console.log("mac fail");
     				console.log(err);
     				$(".mac-status").text("mac 添加失败！");
     			}
     		})
     		console.log("按照筛选条件加载！")
     	}
	// 批量修改的 ajax 提交
	$.orderBatchAjax = function(batchPost){
		$.ajax({
			type: 'POST',
			url: 'http://localhost/learnphp/ajaxjson.php',
			contentType: "application/json;charset=utf-8",
			data: JSON.stringify(batchPost),
			dataType: 'JSONP',
			success: function(data){
				console.log("mac success");
				console.log(data);
				$.renderOrderList(data);
			},
			error: function(err){
				console.log("mac fail");
				console.log(err);
				$(".mac-status").text("mac 添加失败！");
			}
		})
		console.log("提交了批量修改");
	}
	// 渲染订单列表，同时更新订单和产品数量
	$.renderOrderList = function(orderList){
		$(".filter-board").slideUp();
		$(".order-detail-table").children().remove();
		var order_num = 0;
		var product_num = 0;
		$.each(orderList, function(n, value){
			order_num++;
			product_num = product_num + Number(value.product_count);
			var rowColor,special_need,special_need_detail,invoice_category;
			switch(value.order_status){
				case "已付款":rowColor = "blue-row";break;
				case "制作中":rowColor = "pink-row";break;
				case "已发货":rowColor = "yellow-row";break;
				case "维修中":rowColor = "green-row";break;
				case "已关闭":rowColor = "gray-row";break;
				default:break;
			}
			switch(value.special_need){
				case "null":special_need = "无特殊需求";break;
				default:break;
			}
			switch(value.invoice_category){
				case "0":invoice_need = "不需要发票";invoice_category = "";break;
				case "1":invoice_need = "需要发票";invoice_category = "个人发票";break;
				case "2":invoice_need = "需要发票";invoice_category = "公司发票";break;
				default:break;
			}
			var row = "";
			$(".order-detail-table").append(row);
		})
		$(".order-num").text(order_num);
		$(".product-num").text(product_num);
		// remark 点击下拉
		$(".remark").click(function(){
			$(this).hide();
			$(this).next().show();
			return false;
		})
		$(".remark-content").click(function(){
			$(this).hide();
			$(this).prev().show();
			return false;
		})
		// 批量与详细修改 order
		$.changeOrder();
	}
})