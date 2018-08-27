package com.realab.Validation;

import android.support.annotation.NonNull;
import android.util.ArrayMap;


import com.realab.Validation.Annotation.GreaterThan;
import com.realab.Validation.Annotation.Required;
import com.realab.Validation.Rule.Rule;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Validator {

    private Object model;
    private final static List<Class> availableAnnotation = new ArrayList<Class>() {{
        add(Required.class);
        add(GreaterThan.class);
    }};
    private ArrayMap<String, String> invalidFields = new ArrayMap<>();

    public Validator(@NonNull Object model) {
        this.model = model;
    }

    public ErrorList getErrors() {
        return new ErrorList(invalidFields);
    }

    public Boolean validate() throws IllegalArgumentException {
        if (model == null) {
            throw new IllegalArgumentException("Model can't be null");
        }

        final Field[] modelFields = model.getClass().getDeclaredFields();
        for (Field modelField : modelFields) {
            try {
                validateField(modelField);
            } catch (IllegalArgumentException e) {
                invalidFields.put(modelField.getName(), e.getMessage());
            }
        }
        return isValid();
    }

    private Boolean isValid() {
        return invalidFields.isEmpty();
    }

    private void validateField(Field field) throws IllegalArgumentException {
        for (Class annotation : availableAnnotation) {
            if (field.isAnnotationPresent(annotation)) {
                Annotation fieldAnnotation = field.getAnnotation(annotation);
                String annotationClassName = annotation.getSimpleName();
                Object attributeValue = null;
                field.setAccessible(true);

                try {
                    attributeValue = field.get(model);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Cann't access to field");
                }

                try {
                    Class ruleClass = Class.forName(this.getClass().getPackage().getName() + ".Rule." + annotationClassName);
                    if (Rule.class.isAssignableFrom(ruleClass)) {
                        Rule rule = (Rule) ruleClass.newInstance();
                        Properties properties = new Properties();
                        for (Method method : fieldAnnotation.getClass().getDeclaredMethods()) {
                            try {
                                properties.put(method.getName(), fieldAnnotation.getClass().getMethod(method.getName()).invoke(fieldAnnotation));
                            } catch (NoSuchMethodException ignored) {

                            }
                        }
                        if (!rule.validate(attributeValue, properties)) {
                            throw new IllegalArgumentException(rule.reason());
                        }
                    }
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException("Rule not found");
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
