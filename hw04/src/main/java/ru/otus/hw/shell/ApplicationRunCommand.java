package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.security.LoginContext;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent
@RequiredArgsConstructor
public class ApplicationRunCommand {
    private final TestRunnerService testRunnerService;

    private final LoginContext loginContext;

    private final StudentService studentService;


    @ShellMethod(key = "run-test", value = "Start test for username")
    @ShellMethodAvailability(value = "isTestCommandAvailable")
    public void runTest() {
        testRunnerService.run();
    }
    @ShellMethod(key = "login", value = "Login command" )
    public String login() {
        loginContext.login(studentService.determineCurrentStudent());
        return String.format("Добро пожаловать: %s", loginContext.getCurrentStudent());
    }

    private Availability isTestCommandAvailable() {
        return loginContext.isUserLoggedIn()
                ? Availability.available()
                : Availability.unavailable("Сначала залогиньтесь");
    }
}

