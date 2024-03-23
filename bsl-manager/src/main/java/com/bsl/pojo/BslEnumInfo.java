package com.bsl.pojo;

public class BslEnumInfo extends BslEnumInfoKey {
    private String enumEnglishName;
    private String enumChineseName;

    private String enumValue;

    private Integer enumOrder;

    @Override
    public String getEnumEnglishName() {
        return enumEnglishName;
    }

    @Override
    public void setEnumEnglishName(String enumEnglishName) {
        this.enumEnglishName = enumEnglishName;
    }

    public String getEnumChineseName() {
        return enumChineseName;
    }

    public void setEnumChineseName(String enumChineseName) {
        this.enumChineseName = enumChineseName == null ? null : enumChineseName.trim();
    }

    public String getEnumValue() {
        return enumValue;
    }

    public void setEnumValue(String enumValue) {
        this.enumValue = enumValue == null ? null : enumValue.trim();
    }

    public Integer getEnumOrder() {
        return enumOrder;
    }

    public void setEnumOrder(Integer enumOrder) {
        this.enumOrder = enumOrder;
    }
}