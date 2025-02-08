package com.example.backend.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UserAccountDTO {
    private String email;
    private String password;
    private String name;
}
