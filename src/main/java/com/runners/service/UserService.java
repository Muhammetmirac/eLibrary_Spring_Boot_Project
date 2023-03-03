package com.runners.service;

import com.runners.domain.Role;
import com.runners.domain.User;
import com.runners.domain.enums.RoleType;
import com.runners.dto.UserDTO;
import com.runners.dto.request.AdminUserUpdateRequest;
import com.runners.dto.request.RegisterRequest;
import com.runners.dto.request.UpdatePasswordRequest;
import com.runners.dto.request.UserUpdateRequest;
import com.runners.exception.BadRequestException;
import com.runners.exception.ResourceNotFoundException;
import com.runners.exception.message.ErrorMessage;
import com.runners.mapper.UserMapper;
import com.runners.repository.UserRepository;
import com.runners.exception.ConflictException;
import com.runners.security.SecurityUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, RoleService roleService, @Lazy PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_EXCEPTION, email)));
        return user;
    }


    public void saveUser(RegisterRequest registerRequest) {

        //mail var mı yok mu
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_ERROR_MESSAGE, registerRequest.getEmail()));
        }

        //role tanımlama
        Role role = roleService.findByType(RoleType.ROLE_MEMBER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        //password encoder
        String encodePassword = passwordEncoder.encode(registerRequest.getPassword());

        //kullanıcı bilgileri setlenip DB ye gönderilecek
        User user = new User();

        user.setName(registerRequest.getName());
        user.setSurname(registerRequest.getSurname());
        user.setAddress(registerRequest.getAddress());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodePassword);
        user.setRoles(roles);

        userRepository.save(user);
    }


    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return userMapper.usersToUserDTOList(users);
    }

    public UserDTO getPrincibal() {
        User user = getCurrentUser();
        UserDTO userDTO = userMapper.userToUserDTO(user);
        return userDTO;

    }

    public User getCurrentUser() {
        String email = SecurityUtils.getCurrentUserLogin().orElseThrow(() ->
                new ResourceNotFoundException(ErrorMessage.CURRENT_PRINCIPAL_NOT_FOUND_EXCEPTION));

        User user = getUserByEmail(email);
        return user;

    }

    public Page<UserDTO> getUserWithPage(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);
        return dtoPage(page);

    }

    private Page<UserDTO> dtoPage(Page<User> userPage) {
        return userPage.map(user -> userMapper.userToUserDTO(user));

    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.USER_ID_NOT_FOUND_EXCEPTION, id)));

        return userMapper.userToUserDTO(user);
    }

    public void updatePassword(UpdatePasswordRequest updateRequest) {
        User user = getCurrentUser();

        //builtin kontrol
        builtIn(user);
        // oldPassword doğru mu?
        if (!passwordEncoder.matches(updateRequest.getOldPassword(), user.getPassword())) {
            throw new BadRequestException(ErrorMessage.PASSWORD_NOT_MATCHED_MESSAGE);
        }
        //newPassword encode edilecek
        String encodePassword = passwordEncoder.encode(updateRequest.getNewPassword());

        user.setPassword(encodePassword);

        userRepository.save(user);
    }

    //builtIn kontol için yardımcı metod
    public void builtIn(User user) {
        if (user.getBuiltIn()) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }
    }

    @Transactional
    public void updateUser(UserUpdateRequest userUpdateRequest) {
        User user = getCurrentUser();

        //builtIn kontol
        builtIn(user);
        //email kontol
        boolean emailExist = userRepository.existsByEmail(userUpdateRequest.getEmail());
        if (emailExist && !userUpdateRequest.getEmail().equals(user.getEmail())) {
            throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_ERROR_MESSAGE, user.getEmail()));
        }

        userRepository.update(user.getId(),
                userUpdateRequest.getName(),
                userUpdateRequest.getSurname(),
                userUpdateRequest.getAddress(),
                userUpdateRequest.getPhoneNumber(),
                userUpdateRequest.getEmail());

    }

    public void updateUserById(Long id, AdminUserUpdateRequest adminUserUpdateRequest) {
        User user = getById(id);

        //builtin kontorl
        builtIn(user);

        //mail kontrol
        boolean emailExist = userRepository.existsByEmail(adminUserUpdateRequest.getEmail());
        if (emailExist && !adminUserUpdateRequest.getEmail().equals(user.getEmail())) {
            throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_ERROR_MESSAGE, user.getEmail()));
        }

        //password kontrol

        if (adminUserUpdateRequest.getPassword() == null) {
            adminUserUpdateRequest.setPassword(user.getPassword());
        } else {
            String encodePassword = passwordEncoder.encode(adminUserUpdateRequest.getPassword());
            adminUserUpdateRequest.setPassword(encodePassword);
        }

        //role DB kayıt yapısı kontrol ve convert-->
        // rollerimiz bizim tanımladıgımız string ifade şeklinde onları ROLE_ADMIN ve ROLE_MEMBER şekline getiriyoruz

        Set<String> roleSTR = adminUserUpdateRequest.getRoles();
        Set<Role> roleSet = roleConvert(roleSTR);


        //gelen dataları setleme
        user.setName(adminUserUpdateRequest.getName());
        user.setSurname(adminUserUpdateRequest.getSurname());
        user.setEmail(adminUserUpdateRequest.getEmail());
        user.setPhoneNumber(adminUserUpdateRequest.getPhoneNumber());
        user.setAddress(adminUserUpdateRequest.getAddress());
        user.setBuiltIn(adminUserUpdateRequest.getBuiltIn());
        user.setPassword(adminUserUpdateRequest.getPassword());

        user.setRoles(roleSet);

        userRepository.save(user);

    }



    //yardımcı metod
    public User getById(Long id) {
        User user = userRepository.findUserById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.USER_ID_NOT_FOUND_EXCEPTION, id)));
        return user;
    }



    //Role kontolu için yardımcı metod
    private Set<Role> roleConvert(Set<String> role) {
        Set<Role> roles = new HashSet<>();

        if (role == null) {
            Role assignRole = roleService.findByType(RoleType.ROLE_MEMBER);
            roles.add(assignRole);
        } else {
            role.forEach(r -> {
                if (r.equals(RoleType.ROLE_ADMIN.getName())) {
                    Role adminRole = roleService.findByType(RoleType.ROLE_ADMIN);
                    roles.add(adminRole);
                } else {
                    Role memberRole = roleService.findByType(RoleType.ROLE_MEMBER);
                    roles.add(memberRole);
                }
            });

        }
        return roles;
    }

    public void removeUser(Long id) {
        User user = getById(id);

        //builtIn
        builtIn(user);

        userRepository.delete(user);
    }
}