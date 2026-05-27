package com.caophu2305.popfigure.dto.request;

import com.caophu2305.popfigure.entity.Order;
import com.caophu2305.popfigure.entity.UserAddress;
import com.caophu2305.popfigure.entity.UserRole;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationRequest {
    private String name;

    private String hash_password;

    private String email;

    private String phone;
}
