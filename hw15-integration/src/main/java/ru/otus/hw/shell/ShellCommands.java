package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.CommandRegistration;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import ru.otus.hw.integration.ReportGateway;
import ru.otus.hw.service.ReportPrinter;

import java.util.Collection;

@RequiredArgsConstructor
@Command(group = "Report commands")
public class ShellCommands {

    private final ReportGateway reportGateway;

    private final ReportPrinter reportPrinter;

    @Command(command = "report", description = "Get an annual report on employees")
    String report(@Option(arity = CommandRegistration.OptionArity.ONE_OR_MORE) Collection<String> employeeNames) {
        var annualReport = reportGateway.annualByQuarterPersonReports(employeeNames);
        return reportPrinter.toString(annualReport);
    }
}
