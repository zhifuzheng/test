$(function() {
	var _this1 = null;
	$('.nav>li').hover(function() {
		_this1 = $(this);
		_this1.find('.second-nav').show();
		var _this2 = null;
		_this1.find('.second-nav').find('li').hover(function() {
			_this2 = $(this);
			_this2.find('.third-nav').show();
			_this2.find('.third-nav').hover(function() {
				$(this).show();
			}, function() {
				$(this).hide();
			});
		}, function() {
			_this2.find('.third-nav').hide();
		});
	}, function() {
		_this1.find('.second-nav').hide();
	});
	var isHome = true;
	$('a', '.menu').each(function() {
		if(location.href == this.href) {
			$('.nav>li').removeClass('com');
			var tId = $(this).attr('alt');
			$('#' + tId).addClass('com');
			isHome = false;
		}
	});
	if(isHome) {
		$('#index').addClass('com');
	}
});