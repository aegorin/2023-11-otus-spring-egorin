package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.command.annotation.Command;

@RequiredArgsConstructor
@Command(group = "Batch commands")
public class BatchCommands {

    private final JobLauncher jobLauncher;

    private final Job migrateToNoSqlJob;

    @Command(command = "sm", description = "Start migration to NoSQL")
    public void startMigrationToNoSQL() throws Exception {
        jobLauncher.run(migrateToNoSqlJob, new JobParameters());
    }
}
