$(document).ready(function(){
	// 批量修改
	var batchPost = [];
	$("#batch_change_start").click(function(){
		if($(".order-detail-table").children()[0] !== undefined){
			if($(this).hasClass("batch-change-color")){
				// 出现确认模态框
				$(".submit-modal").fadeIn(200);
				$(".submit-modal-cancel").click(function(){
					$(".submit-modal").fadeOut(200);
				});
				$(".submit-modal-confirm").click(function(){
					$(".submit-modal").fadeOut(200);
					$(".batch-change-icon").slideUp(500);
					$(".batch-change-color").next().text("修改");
					$(".batch-change-color").removeClass("batch-change-color");
					$(".order-detail-row").removeClass("white-row");
					$(".batch-change-board").slideUp();
					$(".batch-change-tag-show").addClass("batch-change-tag-hide").removeClass("batch-change-tag-show");
					// ajax 提交批量修改内容
					// 从 renderAjax 调用方法
					$(".beChanged").each(function(){
						var version = $(this).attr("version");
						var order_id = $(this).attr("order");
						var order_status = $(this).find(".order-status-text").text();
						var delivery_date = $(this).find(".batch-delivery-date").text();
						var ship_from = $(this).find(".batch-ship-from").text();
						var ship_date = $(this).find(".batch-ship-date").text();
						var oneOrder = {
							"version": version,
							"order_id": order_id,
							"order_status": order_status,
							"delivery_date": delivery_date,
							"ship_from": ship_from,
							"ship_date": ship_date
						};
						batchPost.push(oneOrder);
					});
					console.log(batchPost);
					$.orderBatchAjax(batchPost);
					$.orderListAjax(filterPost);
				})
				
			}else{
				$(".batch-change-icon").slideDown(500).css("display","block");
				$(this).addClass("batch-change-color");
				$(this).next().text("完成");
				// 收起所有 filter-board 
				$(".filter-board").slideUp();

			}
		}
	});
});