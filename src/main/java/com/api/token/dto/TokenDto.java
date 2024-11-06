package com.api.token.dto;

import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenDto {
    private String userId;
    private String password;
    private boolean isAuth;
    private String token;

}
