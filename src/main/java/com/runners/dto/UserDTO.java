package com.runners.dto;

import com.runners.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String name;


    private String surname;


    private String email;


    private String password;


    private String phoneNumber;


    private String address;

    private  Boolean builtIn ;


    private Set<String> roles ;

    public void  setRoles(Set<Role> roles){
    Set<String>roleStr = new HashSet<>();

    roles.forEach(role->{
        roleStr.add(role.getType().getName());
    });
    this.roles=roleStr;

    }


}
