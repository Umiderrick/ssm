$(document).ready(function(){
	$(".page-num").mouseover(function(){
		$(this).addClass("big3");
		$(this).next().addClass("big2");
		$(this).prev().addClass("big2");
		$(this).prev().prev().addClass("big1");
		$(this).next().next().addClass("big1");
	});
	$(".page-num").mouseout(function(){
		$(".page-num").removeClass("big1 big2 big3");
	})
});