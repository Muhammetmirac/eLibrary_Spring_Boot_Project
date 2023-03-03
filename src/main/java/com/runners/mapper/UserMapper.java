package com.runners.mapper;

import com.runners.domain.User;
import com.runners.dto.UserDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // pojo-->dto
    UserDTO userToUserDTO(User user);


    // List<Pojo> ---> List<DTO>
    List<UserDTO> usersToUserDTOList(List<User> users);

}
