package ru.otus.hw.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;
import java.util.Map;

@ConfigurationProperties(prefix = "test")
public class AppProperties implements TestFileNameProvider, TestConfig, LocaleConfig {

    private final Map<String, String> fileNameByLocaleTag;

    private final int rightAnswersCountToPass;

    private final Locale locale;

    public AppProperties(Map<String, String> fileNameByLocaleTag, int rightAnswersCountToPass, String locale) {
        this.fileNameByLocaleTag = fileNameByLocaleTag;
        this.rightAnswersCountToPass = rightAnswersCountToPass;
        this.locale = Locale.forLanguageTag(locale);
    }

    @Override
    public String getTestFileName() {
        return fileNameByLocaleTag.get(locale.toLanguageTag());
    }

    @Override
    public int getRightAnswersCountToPass() {
        return rightAnswersCountToPass;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }
}
