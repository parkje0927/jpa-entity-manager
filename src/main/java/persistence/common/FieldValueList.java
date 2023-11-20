package persistence.common;

import persistence.annotations.Transient;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FieldValueList {
    private final List<FieldValue> fieldValueList;

    public FieldValueList(Object entity) {
        List<FieldValue> fieldValueListForConstructor = new ArrayList<>();
        for (Field field : entity.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Transient.class)) {
                continue;
            }

            try {
                FieldValue fieldValue = new FieldValue(field, field.get(entity).toString());
                fieldValueListForConstructor.add(fieldValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        this.fieldValueList = fieldValueListForConstructor;
    }

    public List<FieldValue> getFieldValueList() {
        return fieldValueList;
    }

    public List<FieldValue> getIdFVList() {
        return this.fieldValueList.stream().filter(fieldValue -> fieldValue.isId())
                .collect(Collectors.toList());
    }

    public List<FieldValue> getAttributeFVList() {
        return this.fieldValueList.stream().filter(fieldValue -> !fieldValue.isId())
                .collect(Collectors.toList());
    }
}
