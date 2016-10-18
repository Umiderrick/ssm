$(document).ready(function(){
	var currentDate = new Date();
	// 验证4位年份
	$.verifyYear = function(year){
		var reg = /^\d{4}$/;
		return reg.test(year);
	};
	// 验证2位月份
	$.verifyMonth = function(month){
		var reg = /^\d{1,2}$/;
		return reg.test(month);
	};
	// 验证2位日
	$.verifyDay = function(day){
		var reg = /^\d{1,2}$/;
		return reg.test(day);
	} 
});