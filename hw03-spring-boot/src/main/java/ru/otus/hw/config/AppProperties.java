package ru.otus.hw.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "test")
public class AppProperties implements TestFileNameProvider, TestConfig {

    private final String testFileName;

    private final int rightAnswersCountToPass;

    public AppProperties(String fileName,
                         int rightAnswersCountToPass) {
        this.testFileName = fileName;
        this.rightAnswersCountToPass = rightAnswersCountToPass;
    }

    @Override
    public String getTestFileName() {
        return testFileName;
    }

    @Override
    public int getRightAnswersCountToPass() {
        return rightAnswersCountToPass;
    }
}
