package io.dataease.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import static org.mockito.Mockito.mock;

/**
 * Stub implementation of HttpServletRequest that returns a fixed request URI.
 * Extends HttpServletRequestWrapper and overrides only getRequestURI();
 * all other methods delegate to a Mockito mock so no servlet container is required.
 */
public class StubHttpServletRequest extends HttpServletRequestWrapper {

    private final String requestURI;

    public StubHttpServletRequest(String requestURI) {
        super(mock(HttpServletRequest.class));
        this.requestURI = requestURI;
    }

    @Override
    public String getRequestURI() {
        return requestURI;
    }
}
