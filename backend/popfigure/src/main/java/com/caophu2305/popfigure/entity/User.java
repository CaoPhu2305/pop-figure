package com.caophu2305.popfigure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 5, message = "USERNAME_INVALID")
    private String username;

    @NotBlank
    private String name;

    @NotBlank
    private String hash_password;

    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    @OneToMany(mappedBy = "user")
    private List<UserRole> userRoles;

    @OneToMany(mappedBy = "user")
    private List<UserAddress> userAddresses;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;
}
