$(document).ready(function(){
  $('.bxslider').bxSlider({
	  minSlides:8,
	  maxSlides:8,
	  slideWidth:180,
	  slideMargin:10,
	  speed:5000,
	  pause:7000,
	  auto:true,
	  infiniteLoop: true,
	  nextSelector:'#next',
	  prevSelector:'#prev',
	  pager:false
	  
  });
});