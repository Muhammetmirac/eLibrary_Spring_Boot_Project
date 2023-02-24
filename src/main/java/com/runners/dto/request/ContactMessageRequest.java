package com.runners.dto.request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactMessageRequest {
    @Size(min = 2, max = 50, message = "Your name '${validatedValue}' must be beetween {min} and {max} chars long") // name kısmına girdiği değeri görmek için yazıyoruz
    @NotBlank(message = "Please provide your name")
    private String name;
    @Size(min = 5, max = 50, message = "Your subject '${validatedValue}' must be beetween {min} and {max} chars long")
    @NotBlank(message = "Please provide your subject")
    private String subject;

    @Size(min = 5, max = 200, message = "Your body '${validatedValue}' must be beetween {min} and {max} chars long")
    @NotBlank(message = "Please provide your body")
    private String body;

    @Email(message = "Provide valid email")
    private String email;
}
