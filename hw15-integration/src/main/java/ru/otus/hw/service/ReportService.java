package ru.otus.hw.service;

import ru.otus.hw.domain.Period;
import ru.otus.hw.domain.Person;
import ru.otus.hw.domain.PersonReport;
import ru.otus.hw.domain.Report;

import java.util.List;

public interface ReportService {

    Report createReportPrevYear();

    List<PersonReport> createQuarterReports(PersonReport annualPersonReport);

    PersonReport computeTotal(Person person, Period period);
}
