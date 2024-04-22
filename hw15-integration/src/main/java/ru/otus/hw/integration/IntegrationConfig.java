package ru.otus.hw.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.aggregator.MessageGroupProcessor;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.Message;
import ru.otus.hw.domain.AnnualReport;
import ru.otus.hw.domain.Period;
import ru.otus.hw.domain.Person;
import ru.otus.hw.domain.PersonReport;
import ru.otus.hw.domain.Report;
import ru.otus.hw.service.ReportService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableIntegration
public class IntegrationConfig {

    private final ReportService reportService;

    @Bean
    public IntegrationFlow annualPersonReport() {
        return f -> f
                .split()
                .transform(this, "personNameToPersonReport")
                .route(PersonReport.class, r -> r.person().name(), r -> r.defaultSubFlowMapping(sf -> sf
                        .split(PersonReport.class, reportService::createQuarterReports)
                        .<PersonReport>handle((p, h) -> reportService.computeTotal(p.person(), p.report().getPeriod()))
                        .aggregate(a -> a.outputProcessor(groupQuarterToAnnualPersonReport()))
                ))
                .aggregate(a -> a.outputProcessor(groupReports()));
    }

    public PersonReport personNameToPersonReport(String personName) {
        return new PersonReport(new Person(personName), reportService.createReportPrevYear());
    }

    public MessageGroupProcessor groupReports() {
        return group -> {
            List<PersonReport> reportList = new ArrayList<>(group.size());
            for (Message<?> message : group.getMessages()) {
                reportList.add((PersonReport) message.getPayload());
            }
            Comparator<PersonReport> byTotal = Comparator.comparingInt(r -> r.report().getTotal());
            reportList.sort(byTotal.reversed());
            return new AnnualReport(reportList);
        };
    }

    public MessageGroupProcessor groupQuarterToAnnualPersonReport() {
        return group -> {
            List<PersonReport> reportList = new ArrayList<>(group.size());
            for (Message<?> message : group.getMessages()) {
                reportList.add((PersonReport) message.getPayload());
            }
            var person = reportList.get(0).person();
            var beginPeriod = reportList.get(0).report().getPeriod();
            var endPeriod = reportList.get(reportList.size() - 1).report().getPeriod();
            var report = new Report(new Period(beginPeriod.startDateInclusive(), endPeriod.endDateInclusive()));
            reportList.forEach(p -> report.addLine(p.report()));
            return new PersonReport(person, report);
        };
    }
}
