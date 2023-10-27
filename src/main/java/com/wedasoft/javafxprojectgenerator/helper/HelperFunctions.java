package com.wedasoft.javafxprojectgenerator.helper;

public class HelperFunctions {

    public static boolean isNumeric(
            String str) {

        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
