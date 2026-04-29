package com.caophu2305.popfigure.dto.auth;

public record AuthResponse(
        AuthUserResponse user,
        String token,
        String token_type,
        Long expires_in
) {
}

