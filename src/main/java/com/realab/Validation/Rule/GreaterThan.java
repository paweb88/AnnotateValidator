package com.realab.Validation.Rule;

import java.util.Properties;

public class GreaterThan implements Rule {

    private final static String THAN = "than";
    private Integer then;

    @Override
    public Boolean validate(Object value, Properties properties) {
        if (properties.containsKey(THAN)) {
            then = (Integer) properties.get(THAN);
            return (Integer) value > then;
        }
        return false;
    }

    @Override
    public String reason() {
        return "Must be greater then: " + String.valueOf(then);
    }

}
