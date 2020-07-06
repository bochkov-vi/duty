function enableDetectScroll(element) {
    $(element).scroll(function () {
        console.log($(this).scrollLeft() + $(this).innerWidth() + ":" + $(this)[0].scrollWidth);
        if ($(this).scrollLeft() + $(this).innerWidth() >= $(this)[0].scrollWidth) {
            console.log("scroll-end")
            $(this).trigger('scroll-end');
        }
    })
}

function enableCellHover() {
    $("[data-employee-id]").mouseover(function () {
        $("[data-employee-id='" + $(this).data("employee-id") + "']").addClass("border-primary border-bottom border-top border-right-0 border-left-0 bg-light text-dark disabled ");
    });
    $("[data-employee-id]").mouseleave(function () {
        $("[data-employee-id='" + $(this).data("employee-id") + "']").removeClass("border-primary border-bottom border-top border-right-0 border-left-0 bg-light text-dark");
    });


    $("[data-date-id]").mouseover(function () {
        $("[data-date-id='" + $(this).data("date-id") + "']").addClass("border-primary border-left border-right border-top-0 border-bottom-0 bg-light text-dark");
    });
    $("[data-date-id]").mouseleave(function () {
        $("[data-date-id='" + $(this).data("date-id") + "']").removeClass("border-primary border-left border-right border-top-0 border-bottom-0 bg-light text-dark");
    });
};

function scrollToDateColumn(containerId, dateId) {
    const cont = $('#' + containerId);
    const elem = $("[data-date-id='" + dateId + "']");
    const contOffsetLeft = cont.offset().left;
    const elemOffsetLeft = elem.offset().left;
    const contScrollLeft = cont.scrollLeft();
    const scrollLeft = elemOffsetLeft - contOffsetLeft + contScrollLeft;
    cont.animate({
        scrollLeft: scrollLeft
    }, 500)

}
