package com.myspringmvc.config;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.regex.Pattern;

public class Handler {
    public Object controller;
    public Method method;
    public Pattern pattern;

    public Handler(Object controller, Method method, Pattern pattern) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Handler handler = (Handler) o;
        return Objects.equals(controller, handler.controller) &&
                Objects.equals(method, handler.method) &&
                Objects.equals(pattern, handler.pattern);
    }

    @Override
    public int hashCode() {

        return Objects.hash(controller, method, pattern);
    }
}
