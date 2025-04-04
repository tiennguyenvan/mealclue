package com.example.mealclue.controller;

public class Utils {
    public enum RegionType {
        COUNTRY,
        DISTRICT,
        PROVINCE
    }

    private static String getFSA(String postalCode) {
        if (postalCode == null || postalCode.length() < 3) return "";
        return postalCode.replaceAll("\\s+", "").substring(0, 3).toUpperCase();
    }

    public static RegionType getCommonRegion(String postal1, String postal2) {
        String fsa1 = getFSA(postal1);
        String fsa2 = getFSA(postal2);

        if (fsa1.equals(fsa2)) {
            return RegionType.DISTRICT;
        }

        if (!fsa1.isEmpty() && !fsa2.isEmpty() && fsa1.charAt(0) == fsa2.charAt(0)) {
            return RegionType.PROVINCE;
        }

        return RegionType.COUNTRY;
    }
}
