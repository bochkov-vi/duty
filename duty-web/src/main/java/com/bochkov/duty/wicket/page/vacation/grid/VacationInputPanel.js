function vacationInputPanel(id,) {
    const daysInYear = 365;
    $('#' + id).resizable({
        handles: "e,w",
        maxWidth: function () {
            let total = $(this).closest("div.flex-fill").width();
            let left = $(this).left;
            return total - left;
        },
        resize: function (e, ui) {
            let left = ui.position.left;
            let width = ui.size.width;
            let total = $(this).closest("div.flex-fill").width();
            let maxWidth = total - left;
            $(this).css("max-width", maxWidth);
            $(this).css("minLeft", 0);
            $('#' + id).find("span[data=start]").text(calculateDate(left / total, daysInYear).toLocaleDateString("ru-RU"));
            $('#' + id).find("span[data=end]").text(calculateDate((width + left) / total, daysInYear).toLocaleDateString("ru-RU"));
            if (ui.size.width > maxWidth)
                ui.size.width = maxWidth;
            /*$('#' + id).tooltip({
                "title": left + 'x' + width
            });*/
        },
        stop: function (event, ui) {
            let left = ui.position.left;
            let width = ui.size.width;
            let total = $(this).closest("div.flex-fill").width();
            let maxWidth = total - left;
            if (ui.size.width > maxWidth)
                ui.size.width = maxWidth;
            $('#' + id).find("span[data=start]").text(calculateDate(left / total, daysInYear).toLocaleDateString("ru-RU"));
            $('#' + id).find("span[data=end]").text(calculateDate((width + left) / total, daysInYear).toLocaleDateString("ru-RU"));
        }
    });
}
function addDays(date, days) {
    let result = new Date(date);
    result.setDate(result.getDate() + days);
    return result;
}

function calculateDate(x, daysInYear) {
    let date = new Date(new Date().getFullYear(),0,1);
    const days = daysInYear * x;
    date = addDays(date,days);
    return date;
}