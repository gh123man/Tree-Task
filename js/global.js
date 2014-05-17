var $nav, $window, $header;
$( document ).ready(function() {
    $nav = $('#nav');
    $window = $(window);
    $content = $("#content");

    setTimeout(function() {
        if ($window.scrollTop() < 1) {
            $window.scrollTo(20, 300);
            $window.scrollTo(10, 200);
        }
    }, 3000);

    $("#header-scroll").click(function(){
        $(window).scrollTo($("#content").position().top,500);
        location = "#content"
    });

    $window.scroll(function() {
        $nav.toggleClass('stick', $window.scrollTop() > $content.position().top);
        console.log($window.scrollTop())
        console.log($nav.position().top)
    });

});