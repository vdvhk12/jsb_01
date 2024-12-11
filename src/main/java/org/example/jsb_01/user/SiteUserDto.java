package org.example.jsb_01.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SiteUserDto {

    private Long id;
    private String username;
    private String email;

    public static SiteUserDto toDto(SiteUser siteUser) {
        return SiteUserDto.builder()
            .id(siteUser.getId())
            .username(siteUser.getUsername())
            .email(siteUser.getEmail())
            .build();
    }
}
