package com.runners.domain.enums;


public enum RoleType {

    ROLE_MEMBER("Member"),

    ROLE_ADMIN("Administrator");

    private String name;

    RoleType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

  
}
