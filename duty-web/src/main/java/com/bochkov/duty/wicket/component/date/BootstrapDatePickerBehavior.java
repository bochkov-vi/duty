package com.bochkov.duty.wicket.component.date;

import de.agilecoders.wicket.webjars.request.resource.WebjarsCssResourceReference;
import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.string.Strings;

@Getter
@Setter
@Accessors(chain = true)
public class BootstrapDatePickerBehavior extends Behavior {
    DatePickerConfig config = new DatePickerConfig();

    TextField component;

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);
        response.render(JavaScriptHeaderItem.forReference(new WebjarsJavaScriptResourceReference("resources/webjars/bootstrap-datepicker/current/js/bootstrap-datepicker.min.js")));
        response.render(CssHeaderItem.forReference(new WebjarsCssResourceReference("resources/webjars/bootstrap-datepicker/current/css/bootstrap-datepicker.min.css")));
        if (!Strings.isEmpty(config.getLanguage())) {
            response.render(JavaScriptHeaderItem.forReference(new WebjarsJavaScriptResourceReference("/resources/webjars/bootstrap-datepicker/current/locales/bootstrap-datepicker." + config.getLanguage() + ".min.js")));
        }
        response.render(OnDomReadyHeaderItem.forScript(createScript()));
    }

    @Override
    public void bind(Component component) {
        super.bind(component);
        this.component = (TextField) component;
        component.setOutputMarkupId(true);
        config.setFormat(Dates.toJavaScriptDateFormat(((AbstractTextComponent.ITextFormatProvider) component).getTextFormat()));
    }

    public String createScript() {
        String script = "$('#" + component.getMarkupId() + "').datepicker(" + config.toJson() + ")";
        return script;
    }

    public String getContainer() {
        return config.getContainer();
    }

    public BootstrapDatePickerBehavior setContainer(String container) {
        config.setContainer(container);
        return this;
    }

    public Boolean getAutoclose() {
        return config.getAutoclose();
    }

    public BootstrapDatePickerBehavior setAutoclose(Boolean autoclose) {
        config.setAutoclose(autoclose);
        return this;
    }

    public String getBeforeShowCentury() {
        return config.getBeforeShowCentury();
    }

    public BootstrapDatePickerBehavior setBeforeShowCentury(String beforeShowCentury) {
        config.setBeforeShowCentury(beforeShowCentury);
        return this;
    }

    public String getDaysOfWeekDisabled() {
        return config.getDaysOfWeekDisabled();
    }

    public BootstrapDatePickerBehavior setDaysOfWeekDisabled(String daysOfWeekDisabled) {
        config.setDaysOfWeekDisabled(daysOfWeekDisabled);
        return this;
    }

    public Boolean getImmediateUpdates() {
        return config.getImmediateUpdates();
    }

    public BootstrapDatePickerBehavior setImmediateUpdates(Boolean immediateUpdates) {
        config.setImmediateUpdates(immediateUpdates);
        return this;
    }

    public String getTitle() {
        return config.getTitle();
    }

    public BootstrapDatePickerBehavior setTitle(String title) {
        config.setTitle(title);
        return this;
    }

    public Boolean getClearBtn() {
        return config.getClearBtn();
    }

    public BootstrapDatePickerBehavior setClearBtn(Boolean clearBtn) {
        config.setClearBtn(clearBtn);
        return this;
    }

    public String getMultidateSeparator() {
        return config.getMultidateSeparator();
    }

    public BootstrapDatePickerBehavior setMultidateSeparator(String multidateSeparator) {
        config.setMultidateSeparator(multidateSeparator);
        return this;
    }

    public String getLanguage() {
        return config.getLanguage();
    }

    public BootstrapDatePickerBehavior setLanguage(String language) {
        config.setLanguage(language);
        return this;
    }

    public Boolean getToggleActive() {
        return config.getToggleActive();
    }

    public BootstrapDatePickerBehavior setToggleActive(Boolean toggleActive) {
        config.setToggleActive(toggleActive);
        return this;
    }

    public String getFormat() {
        return config.getFormat();
    }

    public BootstrapDatePickerBehavior setFormat(String format) {
        config.setFormat(format);
        return this;
    }

    public Boolean getTodayHighlight() {
        return config.getTodayHighlight();
    }

    public BootstrapDatePickerBehavior setTodayHighlight(Boolean todayHighlight) {
        config.setTodayHighlight(todayHighlight);
        return this;
    }

    public String getBeforeShowDay() {
        return config.getBeforeShowDay();
    }

    public BootstrapDatePickerBehavior setBeforeShowDay(String beforeShowDay) {
        config.setBeforeShowDay(beforeShowDay);
        return this;
    }

    public Boolean getShowOnFocus() {
        return config.getShowOnFocus();
    }

    public BootstrapDatePickerBehavior setShowOnFocus(Boolean showOnFocus) {
        config.setShowOnFocus(showOnFocus);
        return this;
    }

    public String getDisableTouchKeyboard() {
        return config.getDisableTouchKeyboard();
    }

    public BootstrapDatePickerBehavior setDisableTouchKeyboard(String disableTouchKeyboard) {
        config.setDisableTouchKeyboard(disableTouchKeyboard);
        return this;
    }

    public Boolean getTodayBtn() {
        return config.getTodayBtn();
    }

    public BootstrapDatePickerBehavior setTodayBtn(Boolean todayBtn) {
        config.setTodayBtn(todayBtn);
        return this;
    }

    public String getDaysOfWeekHighlighted() {
        return config.getDaysOfWeekHighlighted();
    }

    public BootstrapDatePickerBehavior setDaysOfWeekHighlighted(String daysOfWeekHighlighted) {
        config.setDaysOfWeekHighlighted(daysOfWeekHighlighted);
        return this;
    }

    public Integer getWeekStart() {
        return config.getWeekStart();
    }

    public BootstrapDatePickerBehavior setWeekStart(Integer weekStart) {
        config.setWeekStart(weekStart);
        return this;
    }

    public String getDefaultViewDate() {
        return config.getDefaultViewDate();
    }

    public BootstrapDatePickerBehavior setDefaultViewDate(String defaultViewDate) {
        config.setDefaultViewDate(defaultViewDate);
        return this;
    }

    public String getCalendarWeeks() {
        return config.getCalendarWeeks();
    }

    public BootstrapDatePickerBehavior setCalendarWeeks(String calendarWeeks) {
        config.setCalendarWeeks(calendarWeeks);
        return this;
    }

    public String getDatesDisabled() {
        return config.getDatesDisabled();
    }

    public BootstrapDatePickerBehavior setDatesDisabled(String datesDisabled) {
        config.setDatesDisabled(datesDisabled);
        return this;
    }

    public String getInputs() {
        return config.getInputs();
    }

    public Boolean getMultidate() {
        return config.getMultidate();
    }

    public BootstrapDatePickerBehavior setMultidate(Boolean multidate) {
        config.setMultidate(multidate);
        return this;
    }

    public String getOrientation() {
        return config.getOrientation();
    }

    public BootstrapDatePickerBehavior setOrientation(String orientation) {
        config.setOrientation(orientation);
        return this;
    }

    public String getMaxViewMode() {
        return config.getMaxViewMode();
    }

    public BootstrapDatePickerBehavior setMaxViewMode(String maxViewMode) {
        config.setMaxViewMode(maxViewMode);
        return this;
    }

    public String getStartView() {
        return config.getStartView();
    }

    public BootstrapDatePickerBehavior setStartView(String startView) {
        config.setStartView(startView);
        return this;
    }

    public String getBeforeShowDecade() {
        return config.getBeforeShowDecade();
    }

    public BootstrapDatePickerBehavior setBeforeShowDecade(String beforeShowDecade) {
        config.setBeforeShowDecade(beforeShowDecade);
        return this;
    }

    public String getTemplates() {
        return config.getTemplates();
    }

    public BootstrapDatePickerBehavior setTemplates(String templates) {
        config.setTemplates(templates);
        return this;
    }

    public String getMinViewMode() {
        return config.getMinViewMode();
    }

    public BootstrapDatePickerBehavior setMinViewMode(String minViewMode) {
        config.setMinViewMode(minViewMode);
        return this;
    }

    public Boolean getAssumeNearbyYear() {
        return config.getAssumeNearbyYear();
    }

    public BootstrapDatePickerBehavior setAssumeNearbyYear(Boolean assumeNearbyYear) {
        config.setAssumeNearbyYear(assumeNearbyYear);
        return this;
    }

    public String getEndDate() {
        return config.getEndDate();
    }

    public BootstrapDatePickerBehavior setEndDate(String endDate) {
        config.setEndDate(endDate);
        return this;
    }

    public String getBeforeShowMonth() {
        return config.getBeforeShowMonth();
    }

    public BootstrapDatePickerBehavior setBeforeShowMonth(String beforeShowMonth) {
        config.setBeforeShowMonth(beforeShowMonth);
        return this;
    }

    public Boolean getKeepEmptyValues() {
        return config.getKeepEmptyValues();
    }

    public BootstrapDatePickerBehavior setKeepEmptyValues(Boolean keepEmptyValues) {
        config.setKeepEmptyValues(keepEmptyValues);
        return this;
    }

    public Boolean getForceParse() {
        return config.getForceParse();
    }

    public BootstrapDatePickerBehavior setForceParse(Boolean forceParse) {
        config.setForceParse(forceParse);
        return this;
    }

    public String getBeforeShowYear() {
        return config.getBeforeShowYear();
    }

    public BootstrapDatePickerBehavior setBeforeShowYear(String beforeShowYear) {
        config.setBeforeShowYear(beforeShowYear);
        return this;
    }

    public BootstrapDatePickerBehavior setInputs(String inputs) {
        config.setInputs(inputs);
        return this;
    }

    public String getEnableOnReadonly() {
        return config.getEnableOnReadonly();
    }

    public BootstrapDatePickerBehavior setEnableOnReadonly(String enableOnReadonly) {
        config.setEnableOnReadonly(enableOnReadonly);
        return this;
    }

    public String getStartDate() {
        return config.getStartDate();
    }

    public BootstrapDatePickerBehavior setStartDate(String startDate) {
        config.setStartDate(startDate);
        return this;
    }

    public Integer getZIndexOffset() {
        return config.getZIndexOffset();
    }

    public BootstrapDatePickerBehavior setZIndexOffset(Integer zIndexOffset) {
        config.setZIndexOffset(zIndexOffset);
        return this;
    }

    public Boolean getKeyboardNavigation() {
        return config.getKeyboardNavigation();
    }

    public BootstrapDatePickerBehavior setKeyboardNavigation(Boolean keyboardNavigation) {
        config.setKeyboardNavigation(keyboardNavigation);
        return this;
    }

    public String toJson() {
        return config.toJson();
    }

    public String getDestroyScript() {
        return "$('#" + component.getMarkupId() + "').datepicker('destroy');";
    }
}
