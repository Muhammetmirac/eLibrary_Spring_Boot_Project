package com.runners.service;

import com.runners.domain.Role;
import com.runners.domain.enums.RoleType;
import com.runners.exception.ResourceNotFoundException;
import com.runners.exception.message.ErrorMessage;
import com.runners.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    public Role findByType(RoleType type) {
      Role role =  roleRepository.findByType(type).orElseThrow(()->
              new ResourceNotFoundException(String.format(ErrorMessage.ROLE_NOT_FOUND_EXCEPTION,type.getName())));

      return role ;
    }
}
