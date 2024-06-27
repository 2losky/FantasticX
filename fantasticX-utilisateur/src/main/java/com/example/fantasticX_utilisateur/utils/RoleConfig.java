package com.example.fantasticX_utilisateur.utils;

public enum RoleConfig {
    USER("USR", "USER"),
    ADMIN("ADM", "ADMIN"),
    CUSTOMER_MANAGER("CTM", "CUSTOMER_MANAGER"),
    PRODUCT_MANAGER("PTM", "PRODUCT_MANAGER"),
    BILLS_MANAGER("BLM","BILLS_MANAGER");

    private String Code;
    private String text;

    RoleConfig(String code, String text) {
        Code = code;
        this.text = text;
    }

    public String getCode() {
        return Code;
    }

    public String getText() {
        return text;
    }

}
