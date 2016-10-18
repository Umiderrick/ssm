$(document).ready(function(){
	// 提交订单状态批量修改
	$("#submit_batch_order_status").click(function(){
		var order_status = $("input[name='orderStatus']:checked").val();
		console.log(order_status);
		$(".batch-change-tag-show").next().text(order_status).addClass("batch-changed");
		// 給做过修改的 order 打标签
		$.batchChangeTag();
		$(this).parent().parent().slideUp();
	});
	// 提交交货日期批量修改
	$("#submit_batch_delivery_date").click(function(){
		var batch_delivery_year = $("#batch_delivery_year").val();
		var batch_delivery_month = $("#batch_delivery_month").val();
		var batch_delivery_day = $("#batch_delivery_day").val();
		if(!$.verifyYear(batch_delivery_year)){
			$(this).parent().prev().text("请输入正确的年份！");
			return false;
		}
		if(!$.verifyMonth(batch_delivery_month)){
			$(this).parent().prev().text("请输入正确的月份！");
			return false;
		}
		if(!$.verifyDay(batch_delivery_day)){
			$(this).parent().prev().text("请输入正确的日期！");
			return false;
		}
		batch_delivery_date = batch_delivery_year + "-" + batch_delivery_month + "-" + batch_delivery_day;
		$(".batch-change-tag-show").parent().parent().children(".batch-delivery-date").text(batch_delivery_date).addClass("batch-changed");
		$(this).parent().parent().slideUp();
		$.batchChangeTag();
	});
	// 提交发货地点批量修改
	$(".batch-ship-from-options").click(function(){
		$(".batch-ship-from-options").removeClass("on");
		$(this).addClass("on");
	});
	$("#submit_batch_ship_from").click(function(){
		var ship_from;
		$(".batch-ship-from-options").each(function(){
			if($(this).hasClass("on")){
				ship_from = $(this).text();
			}
		});
		console.log(ship_from);
		$(".batch-change-tag-show").parent().parent().children(".batch-ship-from").text(ship_from).addClass("batch-changed");
		$(this).parent().parent().slideUp();
		$.batchChangeTag();
	});
	// 提交发货地点批量修改
	$("#submit_batch_ship_date").click(function(){
		var batch_ship_year = $("#batch_ship_year").val();
		var batch_ship_month = $("#batch_ship_month").val();
		var batch_ship_day = $("#batch_ship_day").val();
		if(!$.verifyYear(batch_ship_year)){
			$(this).parent().prev().text("请输入正确的年份！");
			return false;
		}
		if(!$.verifyMonth(batch_ship_month)){
			$(this).parent().prev().text("请输入正确的月份！");
			return false;
		}
		if(!$.verifyDay(batch_ship_day)){
			$(this).parent().prev().text("请输入正确的日期！");
			return false;
		}
		batch_ship_date = batch_ship_year + "-" + batch_ship_month + "-" + batch_ship_day;
		$(".batch-change-tag-show").parent().parent().children(".batch-ship-date").text(batch_ship_date).addClass("batch-changed");
		$(this).parent().parent().slideUp();
		$.batchChangeTag();
	});
	// 做过修改的 order 打标签，同时其他批量修改面板收起
	$.batchChangeTag = function(){
		$(".batch-change-tag-show").parent().parent().each(function(){
			if(!$(this).hasClass("beChanged")){
				$(this).addClass("beChanged");
			}
		});
		console.log($(".beChanged").length);
		$(".batch-change-board").slideUp();
	}
});