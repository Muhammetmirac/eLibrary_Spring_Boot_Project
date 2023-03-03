package com.runners.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequest {

    @NotBlank(message = "Please Provide Old Password")
    private String oldPassword;
    @NotBlank(message = "Please Provide New Password")
    @Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{4,15})",
            message = "Sifreniz en az 1 büyük, 1 küçük harf, 1 rakam ve 1 karakter içermelidir ")
    private String newPassword;
}
