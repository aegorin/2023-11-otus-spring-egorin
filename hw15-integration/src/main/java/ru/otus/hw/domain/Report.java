package ru.otus.hw.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Report {

    @Getter
    private final Period period;

    @Getter
    private int total;

    private final List<Report> reportLines = new ArrayList<>();

    public Report(Period period) {
        this.period = period;
    }

    public Report(Period period, int reportValue) {
        this.period = period;
        this.total = reportValue;
    }

    public void addLine(Report reportLine) {
        total += reportLine.getTotal();
        reportLines.add(reportLine);
    }

    public List<Report> getLines() {
        return reportLines;
    }
}
