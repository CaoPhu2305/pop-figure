package com.caophu2305.popfigure.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest {

    private String name;
    private String password;

}
