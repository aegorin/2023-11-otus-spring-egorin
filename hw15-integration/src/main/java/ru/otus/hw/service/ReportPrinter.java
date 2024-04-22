package ru.otus.hw.service;

import org.springframework.stereotype.Service;
import ru.otus.hw.domain.AnnualReport;
import ru.otus.hw.domain.Period;
import ru.otus.hw.domain.Person;
import ru.otus.hw.domain.PersonReport;
import ru.otus.hw.domain.Report;

import java.io.StringWriter;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Service
public class ReportPrinter {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public String toString(AnnualReport annualReport) {
        StringWriter stringWriter = new StringWriter();
        stringWriter.write("Report:\n");
        for (PersonReport personReport : annualReport.personReports()) {
            writePerson(stringWriter, personReport.person());
            writeReport(stringWriter, personReport.report());
        }
        return stringWriter.toString();
    }

    private void writePerson(StringWriter writer, Person person) {
        writeIndent(writer, 4);
        writer.write("Person: ");
        writer.write(person.name());
        writer.write("\n");
    }

    private void writeReport(StringWriter writer, Report report) {
        for (Report line : report.getLines()) {
            writeIndent(writer, 8);
            writer.write("%s:%6d%n".formatted(toString(line.getPeriod()), line.getTotal()));
        }
        writeIndent(writer, 8);
        writer.write("%23s:%6d%n".formatted("Total", report.getTotal()));
    }

    private void writeIndent(StringWriter writer, int indent) {
        char[] indents = new char[indent];
        Arrays.fill(indents, ' ');
        writer.write(indents, 0, indent);
    }

    private String toString(Period period) {
        return dateFormatter.format(period.startDateInclusive()) +
               " - " +
               dateFormatter.format(period.endDateInclusive());
    }
}
