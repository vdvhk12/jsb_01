package org.example.jsb_01.user;

import lombok.Getter;

@Getter
public enum SiteUserRole {

    //1. Enum 상수 선언
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    //2. 필드 선언
    private final String value;

    //3. 생성자 선언
    SiteUserRole(String value) {
        this.value = value;
    }
}
