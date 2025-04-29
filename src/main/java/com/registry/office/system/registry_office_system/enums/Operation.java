package com.registry.office.system.registry_office_system.enums;

public enum Operation {
    MARRIAGE("Регистрация брака"),
    BIRTH("Регистрация рождения"),
    DIVORCE("Регистрация развода"),
    DEATH("Регистрация смерти");

    private final String description;

    Operation(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
