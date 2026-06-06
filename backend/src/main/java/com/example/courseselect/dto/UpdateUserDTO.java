package com.example.courseselect.dto;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private String realName;
    private String email;
    private String phone;
    private Integer gender;
}
