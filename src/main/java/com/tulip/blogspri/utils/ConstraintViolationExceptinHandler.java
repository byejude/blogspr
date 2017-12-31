package com.tulip.blogspri.utils;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class ConstraintViolationExceptinHandler {

    public static String getMessasge(ConstraintViolationException e){
        List<String> msgList = new ArrayList<>();
        for (ConstraintViolation<?> constraintViolation:e.getConstraintViolations()
             ) {
            msgList.add(constraintViolation.getMessage());
        }
        String mesages = StringUtils.join(msgList.toArray(),";");
        return mesages;
    }
}
