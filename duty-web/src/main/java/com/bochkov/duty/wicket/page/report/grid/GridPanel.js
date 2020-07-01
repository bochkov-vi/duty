function enableDetectScroll(element) {
    $(element).scroll(function () {
        console.log( $(this).scrollLeft() + $(this).innerWidth() + ":" +$(this)[0].scrollWidth);
        if ($(this).scrollLeft() + $(this).innerWidth() >= $(this)[0].scrollWidth) {
            console.log("scroll-end")
            $(this).trigger('scroll-end');
        }
    })
}
