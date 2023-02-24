package com.runners.domain;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_cmessage")
public class ContactMessage {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Setter(AccessLevel.NONE)
        private Long id;

        @NotBlank
        @Size(min = 2,max = 20,message = "Your name '${validateValue}' must be between {min} and {max} chars long")
        @Column(length = 20,nullable = false)
        private String name;

        @NotBlank
        @Size(min = 10,max = 200,message = "Your subject '${validateValue}' must be between {min} and {max} chars long")
        @Column(length = 200,nullable = false)
        private String subject;

        @NotBlank
        @Size(min = 10,max = 250,message = "Your message '${validateValue}' must be between {min} and {max} chars long")
        @Column(length = 250,nullable = false)
        private String body;

        @Email
        @Size(min = 5,max = 25,message = "Your e-mail '${validateValue}' must be between {min} and {max} chars long")
        @Column(length = 25,nullable = false)
        private String email;






}
