package com.caophu2305.popfigure.dto.auth;

import java.time.LocalDateTime;

public record AuthUserResponse(
        Long id,
        String name,
        String full_name,
        String email,
        LocalDateTime email_verified_at,
        String phone_number,
        Long role_id,
        String role_name,
        String status
) {
}

