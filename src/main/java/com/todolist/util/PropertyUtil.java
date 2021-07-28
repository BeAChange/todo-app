package com.todolist.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class PropertyUtil {

    public static String[] getNullPropertiesString(Object source) {
        Set<String> emptyNames = getNullProperties(source);
        String[] result = new String[emptyNames.size()];

        return emptyNames.toArray(result);
    }

    public static Set<String> getNullProperties(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null)
                emptyNames.add(pd.getName());
        }
        return emptyNames;
    }


    public static Set<String> getNotNullProperties(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> names = new HashSet<String>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue != null)
                names.add(pd.getName());
        }

        return names;
    }
}
