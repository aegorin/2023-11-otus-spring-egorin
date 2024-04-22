package ru.otus.hw.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.domain.AnnualReport;

import java.util.Collection;

@MessagingGateway
public interface ReportGateway {

    @Gateway(requestChannel = "annualPersonReport.input")
    AnnualReport annualByQuarterPersonReports(Collection<String> personNames);
}
