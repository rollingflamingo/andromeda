package com.annotation.exception;

import java.lang.reflect.Method;
import java.util.List;

public class InvalidFieldException extends RuntimeException {
    public InvalidFieldException(String message) {
        super(message);
    }

    public InvalidFieldException(Method method) {
        super(method.getName().replaceAll("^(get|is|has)", "").toLowerCase() + " cannot be null and has no viable alternatives");
    }

    public InvalidFieldException(Method method, List<String> alternatives) {
        this(method.getName().replaceAll("^(get|is|has)", "").toLowerCase() + " cannot be null" + (alternatives.size() > 0
                ? ", viable alternatives: " + alternatives.stream().map(name -> name.replaceAll("^(get|is|has)", "")).map(name -> name.substring(0, 1).toLowerCase().concat(name.substring(1))).reduce((s1, s2) ->s1 +", "+s2).orElse("")
                : " and has no viable alternatives")
                + ".");
    }
}