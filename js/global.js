$( document ).ready(function() {
    console.log('ready');

    $("#header-scroll").click(function(){
        console.log('click');
        $(window).scrollTo($("#content").position().top,500);
        location = "#content"
    });
});