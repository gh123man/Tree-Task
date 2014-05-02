var $nav1;
var $nav2;
var $nav3;
var $nav4;
var $nav5;
var $nav6;
var $nav7;

$(function(){
    var $window = $(window),$navigation = $('#navBox'), top = $navigation.offset().top;
    
    $('.mainTitle').delay(200).fadeIn(1000);
    $('.secondTitle').delay(800).fadeIn(1000);
    $('.scrollText').delay(2000).fadeIn(1000);
    
    
    setTimeout(function() {
        if ($window.scrollTop() < 1) {
            $window.scrollTo(25, 400);
            $window.scrollTo(15, 300);
        }
    }, 3000);
    
    var $section1 = $('.section-1');
    var $section2 = $('.section-2');
    var $section3 = $('.section-3');
    var $section4 = $('.section-4');

    $window.scroll(function() {
        updateScroll();
    });
    
     
    $window.resize(function() {
         updateScroll();
    });
    
    $nav1 = $('.nav-1');
    $nav2 = $('.nav-2');
    $nav3 = $('.nav-3');
    $nav4 = $('.nav-4');
    $nav5 = $('.nav-5');
    $nav6 = $('.nav-6');
    $nav7 = $('.nav-7');
    
    var $box1 = $('.box-1');
    var $box2 = $('.box-2');
    var $box3 = $('.box-3');
    var $box4 = $('.box-4');
    var $box5 = $('.box-5');
    var $box6 = $('.box-6');
    var $box7 = $('.box-7');

    function updateScroll() {
        $navigation.toggleClass('stick', $window.scrollTop() > top);
        
        $section1.css('top', $window.scrollTop() * 2 * -1);
        
        $section2.css('top', ($window.height() - 700) + ($window.scrollTop() / 2)  * -1);
        $section3.css('top', ($window.height() * 1.9) + $window.scrollTop() / 1.1 * -1);
        $section4.css('top', ($window.height() + 500) + ($window.scrollTop() / 2)  * -1);
        
        
        if ($(window).scrollTop() > $box1.offset().top - ($(window).height() / 3)) {
            $('.navAt').removeClass('navAt');
            $nav1.addClass('navAt');
        } 
        if ($(window).scrollTop() > $box2.offset().top - ($(window).height() / 3) ) {
            $('.navAt').removeClass('navAt');
            $nav2.addClass('navAt');
        }
        if ($(window).scrollTop() > $box3.offset().top - ($(window).height() / 3) ) {
            $('.navAt').removeClass('navAt');
            $nav3.addClass('navAt');
        }
        if ($(window).scrollTop() > $box4.offset().top - ($(window).height() / 3) ) {
            $('.navAt').removeClass('navAt');
            $nav4.addClass('navAt');
        }
        if ($(window).scrollTop() > $box5.offset().top - ($(window).height() / 3) ) {
            $('.navAt').removeClass('navAt');
            $nav5.addClass('navAt');
        }
        if ($(window).scrollTop() > $box6.offset().top - ($(window).height() / 3) ) {
            $('.navAt').removeClass('navAt');
            $nav6.addClass('navAt');
        }
        if ($(window).scrollTop() > $box7.offset().top - ($(window).height() / 3) ) {
            $('.navAt').removeClass('navAt');
            $nav7.addClass('navAt');
        }
    
    
    }
    
    
    
});
