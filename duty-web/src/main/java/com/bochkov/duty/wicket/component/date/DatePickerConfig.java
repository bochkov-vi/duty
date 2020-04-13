package com.bochkov.duty.wicket.component.date;

import com.github.openjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.wicket.util.io.IClusterable;

@Getter
@Setter
@Accessors(chain = true)
public class DatePickerConfig implements IClusterable {
    String language;

    String format;

    private Boolean autoclose;//	false

    private Boolean assumeNearbyYear;//	false

    private String beforeShowDay;//

    private String beforeShowMonth;//

    private String beforeShowYear;//

    private String beforeShowDecade;//

    private String beforeShowCentury;//

    private String calendarWeeks;//	false

    private Boolean clearBtn;//	false

    private String container;//	‘body’

    private String datesDisabled;//	[]

    private String daysOfWeekDisabled;//	[]

    private String daysOfWeekHighlighted;//	[]

    private String defaultViewDate;//	today

    private String disableTouchKeyboard;//	false

    private String enableOnReadonly;//	true

    private String endDate;//	Infinity

    private Boolean forceParse;//	true

    private Boolean immediateUpdates;//	false

    private String inputs;//

    private Boolean keepEmptyValues;//	false

    private Boolean keyboardNavigation;//	true

    private String maxViewMode;//	4 ‘centuries’

    private String minViewMode;//	0 ‘days’

    private Boolean multidate;//	false

    private String multidateSeparator;//	‘,’

    private String orientation;//	‘auto’

    private Boolean showOnFocus;//	true

    private String startDate;//	-Infinity

    private String startView;//	0 ‘days’ (current month)

    private String templates;//

    private String title;//	‘’

    private Boolean todayBtn;//	false

    private Boolean todayHighlight;//	true

    private Boolean toggleActive;//	false

    private Integer weekStart;//	0 (Sunday)

    private Integer zIndexOffset;//	10

    public String toJson() {
        return new JSONObject(this).toString();
    }
}
