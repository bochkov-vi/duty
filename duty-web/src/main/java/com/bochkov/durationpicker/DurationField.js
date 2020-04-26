var DurationPicker = function (el) {
    function insertFormatted(inputBox, minuteValue, popoverbox) {
        const hours = Math.floor(minuteValue / 60);
        minuteValue %= 60;
        const minutes = Math.floor(minuteValue);
        const formattedHours = String(hours).padStart(2, '0');
        const formattedMinutes = String(minutes).padStart(2, '0');
        inputBox.val(formattedHours + ':' + formattedMinutes);
        insertIntoPopover(popoverbox, formattedHours, formattedMinutes);
    };

    function durationToMinutes(value) {
        const sectioned = value.split(':');
        if (sectioned.length !== 2) {
            return 0;
        } else {
            return Number(sectioned[1]) + Number(sectioned[0] * 60);
        }
    };

    function validateInput(event) {
        const sectioned = event.target.value.split(/\D/);
        if (sectioned.length !== 2) {
            event.target.value = '00:00'; // fallback to default
            return;
        }
        if (isNaN(sectioned[0])) {
            sectioned[0] = '00';
        }
        if (isNaN(sectioned[1]) || sectioned[1] < 0) {
            sectioned[1] = '00';
        }
        if (sectioned[1] > 59 || sectioned[1].length > 2) {
            sectioned[1] = '59';
        }
        event.target.value = sectioned.join(':');
    };

    function changeValue(inputBox, direction, adjustmentFactor, popoverbox) {
        let minuteValue = durationToMinutes(inputBox.val());
        switch (direction) {
            case 'up':
                minuteValue -= minuteValue % 15;
                minuteValue += adjustmentFactor;
                break;
            case 'down':
                if (minuteValue % 15 > 0) {
                    minuteValue -= minuteValue % 15;
                } else {
                    minuteValue -= adjustmentFactor;
                }
                if (minuteValue < 0) {
                    minuteValue = 0;
                }
                break;
        }
        // const fixedValue = matchConstraints(inputBox, minuteValue);
        insertFormatted(inputBox, minuteValue/*fixedValue*/, popoverbox);

    };

    function insertIntoPopover(box, h, m) {
        box.find('#hours').html(h);
        box.find('#minutes').html(m);
    }

    $(el).on('change', validateInput);

    var p = $(el).popover({
        content: function () {
            var content = '<div class="row"><div id="hour-up" class="col-6"><i class="fa fa-2x fa-arrow-up"></i></div><div id="minute-up" class="col-6"><i class="fa fa-2x fa-arrow-up"></i></i></div></div>' +
                '<div class="row"><div id="hours" class="col-6 text-center">09</div><div id="minutes" class="col-6 text-center">15</i></div></div>' +
                '<div class="row"><div id="hour-down" class="col-6"><i class="fa fa-2x fa-arrow-down"></i></div><div id="minute-down" class="col-6"><i class="fa fa-2x fa-arrow-down"></i></i></div></div>';
            console.log(content);
            return content;
        },
        trigger:'click',
        html: true
    }).on('shown.bs.popover', function () {
        var $input = $(this);
        var $popup = $('#' + $input.attr("aria-describedby"));
        if ($popup) {
            insertFormatted($input, durationToMinutes($input.val()), $popup)
            $popup.find('#hour-up').on('click', function () {
                changeValue($input, 'up', 60, $popup);
            });
            $popup.find('#hour-down').on('click', function () {
                changeValue($input, 'down', 60, $popup);
            });
            $popup.find('#minute-down').on('click', function () {
                changeValue($input, 'down', 15, $popup);
            });

            $popup.find('#minute-up').on('click', function () {
                changeValue($input, 'up', 15, $popup);
            });


        }
    });
    console.log(p);
}



