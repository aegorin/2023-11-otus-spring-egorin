package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Command;
import ru.otus.hw.service.TestRunnerService;

@Command(group = "Application commands")
@RequiredArgsConstructor
public class ShellCommands {

    private final TestRunnerService testRunnerService;

    @Command(command = "begin", alias = "start", description = "Start a quiz")
    public void beginTesting() {
        testRunnerService.run();
    }
}
