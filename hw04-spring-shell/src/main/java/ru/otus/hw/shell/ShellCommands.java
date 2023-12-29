package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.Availability;
import org.springframework.shell.AvailabilityProvider;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.CommandAvailability;
import ru.otus.hw.domain.Student;
import ru.otus.hw.service.ResultService;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestService;

@Command(group = "Application commands")
@RequiredArgsConstructor
public class ShellCommands {

    private final StudentService studentService;

    private final TestService testService;

    private final ResultService resultService;

    private Student student;

    @Command(command = "login", description = "Enter your first and last name")
    @CommandAvailability(provider = "studentOutLogged")
    public void loginStudent() {
        student = studentService.determineCurrentStudent();
    }

    @Command(command = "begin", alias = "start", description = "Start a quiz")
    @CommandAvailability(provider = "studentLogged")
    public void beginTesting() {
        var testResult = testService.executeTestFor(student);
        resultService.showResult(testResult);
    }

    @Command(description = "Logout")
    @CommandAvailability(provider = "studentLogged")
    public void logout() {
        student = null;
    }

    @Bean
    private AvailabilityProvider studentLogged() {
        return () -> student != null ? Availability.available() : Availability.unavailable("You are not authorised");
    }

    @Bean
    private AvailabilityProvider studentOutLogged() {
        return () -> student == null ? Availability.available() : Availability.unavailable("You are already logged in");
    }
}
