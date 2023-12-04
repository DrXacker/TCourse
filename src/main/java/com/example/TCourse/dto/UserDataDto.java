package com.example.TCourse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDataDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
}
