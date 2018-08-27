package com.realab.Validation.Rule;

import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Required implements Rule {

    private Map<Class<?>, EmptyChecker> checkerMap = new HashMap<Class<?>, EmptyChecker>()
    {{
            put(String.class, new StringEmptyChecker());
            put(HashMap.class, new MapEmptyChecker());
    }};
    @Override
    public Boolean validate(Object value, @Nullable Properties properties) {
        if (value == null) {
            return false;
        } else {
            EmptyChecker checker = checkerMap.get(value.getClass());
            return !checker.isEmpty(value);
        }
    }

    @Override
    public String reason() {
        return "Can't be null";
    }

    private interface EmptyChecker<T> {
         Boolean isEmpty(T obj);
    }

    private class StringEmptyChecker implements EmptyChecker<String> {
        @Override
        public Boolean isEmpty(String obj) {
            return obj.isEmpty();
        }
    }

    private class MapEmptyChecker implements EmptyChecker<Map> {
        @Override
        public Boolean isEmpty(Map obj) {
            return obj.size() == 0;
        }
    }
}
