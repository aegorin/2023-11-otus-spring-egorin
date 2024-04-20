package ru.otus.hw;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.integration.ReportGateway;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationTest {

    @Autowired
    private ReportGateway reportGateway;

    @Test
    void testFlow() {
        var annualReport = reportGateway.annualByQuarterPersonReports(List.of("test-person-name"));
        assertNotNull(annualReport);

        var personReports = annualReport.personReports();
        assertTrue(personReports != null && !personReports.isEmpty());

        var personReport = personReports.get(0);
        assertNotNull(personReport);
        assertEquals("test-person-name", personReport.person().name());

        var report = personReport.report();
        assertNotNull(report);
        assertTrue(report.getTotal() > 0);
    }
}