package ru.otus.hw.service;

import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Period;
import ru.otus.hw.domain.Person;
import ru.otus.hw.domain.PersonReport;
import ru.otus.hw.domain.Report;

import java.time.Month;
import java.util.List;
import java.util.Random;

import static java.time.LocalDate.now;
import static java.time.LocalDate.of;

@Service
public class ReportServiceImpl implements ReportService {

    private final Random random = new Random();

    @Override
    public Report createReportPrevYear() {
        int year = now().getYear() - 1;
        var yearReport = new Report(new Period(of(year, Month.JANUARY, 1), of(year, Month.DECEMBER, 31)));
        yearReport.addLine(new Report(new Period(of(year, Month.JANUARY, 1), of(year, Month.MARCH, 31))));
        yearReport.addLine(new Report(new Period(of(year, Month.APRIL, 1), of(year, Month.JUNE, 30))));
        yearReport.addLine(new Report(new Period(of(year, Month.JULY, 1), of(year, Month.SEPTEMBER, 30))));
        yearReport.addLine(new Report(new Period(of(year, Month.OCTOBER, 1), of(year, Month.DECEMBER, 31))));
        return yearReport;
    }

    @Override
    public List<PersonReport> createQuarterReports(PersonReport annualPersonReport) {
        var person = annualPersonReport.person();
        var quarterLines = annualPersonReport.report().getLines();
        return quarterLines.stream().map(report -> new PersonReport(person, report)).toList();
    }

    @Override
    public PersonReport computeTotal(Person person, Period period) {
        int total = random.nextInt(10, 200);
        return new PersonReport(person, new Report(period, total));
    }
}
