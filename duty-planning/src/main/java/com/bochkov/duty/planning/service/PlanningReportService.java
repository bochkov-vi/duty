package com.bochkov.duty.planning.service;

import com.bochkov.duty.jpa.entity.Report;
import com.bochkov.duty.planning.model.PlanningReport;

public interface PlanningReportService {
    PlanningReport loadReport(Integer id);

    PlanningReport create(Report report);
}
