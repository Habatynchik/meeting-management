package com.example.meetingmanagement.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Data
public class UserDto {
    @NotBlank
    @Size(min = 4, max = 64, message = "sizeOutOfBounds")
    @Pattern(regexp = RegExp.EMAIL, message = "emailNotMatchTemplate")
    private String email;

    @NotBlank
    @Size(min = 8, max = 64, message = "sizeOutOfBounds")
    @Pattern(regexp = RegExp.PASSWORD, message = "passwordNotMatchTemplate")
    private String password;

    @NotBlank
    private String passwordCopy;
}
