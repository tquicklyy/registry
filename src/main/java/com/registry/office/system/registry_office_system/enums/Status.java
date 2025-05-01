package com.registry.office.system.registry_office_system.enums;

public enum Status {
    WAIT("Ожидает"),
    IN_WORK("В работе"),
    ACCEPT("Подтверждена"),
    CANCEL("Отменена"),
    ALL("Все статусы");

    private final String description;

    Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
