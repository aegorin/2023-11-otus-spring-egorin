package ru.otus.hw.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.services.RequestCountingService;

import java.io.IOException;

@RequiredArgsConstructor
public class LogHttpRequestFilter implements Filter {

    private final RequestCountingService requestCountingService;

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws ServletException, IOException {
        requestCountingService.incrementRequest();
        chain.doFilter(request, response);
    }
}
