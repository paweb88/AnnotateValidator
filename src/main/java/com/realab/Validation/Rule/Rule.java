package com.realab.Validation.Rule;

import android.support.annotation.Nullable;

import java.util.Properties;

public interface Rule {
    Boolean validate(Object value, @Nullable Properties properties);
    String reason();
}
