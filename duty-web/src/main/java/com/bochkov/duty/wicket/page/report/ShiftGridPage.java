package com.bochkov.duty.wicket.page.report;

import com.bochkov.duty.jpa.entity.Report;
import com.bochkov.duty.jpa.repository.ReportRepository;
import com.bochkov.duty.wicket.page.BootstrapPage;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ShiftGridPage extends BootstrapPage<Report> {

    @SpringBean
    ReportRepository reportRepository;


    public ShiftGridPage(Report report) {
        super();
        Integer pk = report.getId();
        setModel(LoadableDetachableModel.of(() -> reportRepository.findById(pk).get()));
    }


}
