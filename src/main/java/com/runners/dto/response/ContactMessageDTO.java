package com.runners.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactMessageDTO {
    private Long id;

    private String name;

    private String subject;

    private String body;

    private String email;

}