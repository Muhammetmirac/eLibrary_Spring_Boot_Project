package com.runners.controller;

import com.runners.dto.UserDTO;
import com.runners.dto.request.AdminUserUpdateRequest;
import com.runners.dto.request.UpdatePasswordRequest;
import com.runners.dto.request.UserUpdateRequest;
import com.runners.dto.response.LibraryResponse;
import com.runners.dto.response.ResponseMessage;
import com.runners.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    //getAllUser
    @GetMapping("/auth/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
      List<UserDTO> userDTOList =  userService.getAllUsers();
        return  ResponseEntity.ok(userDTOList);

    }

    //getCurrentUser --online olan kullanıcıyı getirme
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEMBER')")
    public ResponseEntity<UserDTO> getCurrentUser(){
      UserDTO userDTO =  userService.getPrincibal();

      return ResponseEntity.ok(userDTO);
    }

    //getPageable
    @GetMapping("/pages")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDTO>> getAllUserPages(@RequestParam("page") int page,
                                                        @RequestParam("size") int size,
                                                        @RequestParam("sort") String prop,
                                                        @RequestParam(value = "direction",
                                                        required = false,
                                                        defaultValue = "DESC") Sort.Direction direction){
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        Page<UserDTO> userDTOPage = userService.getUserWithPage(pageable);

        return ResponseEntity.ok(userDTOPage);
    }

    //getUserId
    @GetMapping("/auth/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserDTOById(@PathVariable Long id){
       UserDTO userDTO = userService.getUserById(id);
       return ResponseEntity.ok(userDTO);
    }

    // update password
    @PatchMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEMBER')")
    public ResponseEntity<LibraryResponse> updatePassword(@Valid @RequestBody UpdatePasswordRequest updateRequest){

        userService.updatePassword(updateRequest);

        LibraryResponse response = new LibraryResponse(ResponseMessage.UPDATE_PASSWORD_RESPONSE_MESSAGE,true);

        return ResponseEntity.ok(response);
    }

    //user update
    @PutMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEMBER')")
    public ResponseEntity<LibraryResponse> updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest){
        userService.updateUser(userUpdateRequest);

        LibraryResponse response = new LibraryResponse(ResponseMessage.USER_UPDATE_MESSAGE_RESPONSE,true);
        return ResponseEntity.ok(response);
    }

    //admin başka user update
    @PutMapping("/auth/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibraryResponse> updateOneUser(@Valid @RequestBody AdminUserUpdateRequest adminUserUpdateRequest,
                                                         @PathVariable ("id") Long id){
        userService.updateUserById(id,adminUserUpdateRequest);
        LibraryResponse response = new LibraryResponse(ResponseMessage.USER_UPDATE_MESSAGE_RESPONSE,true);
        return ResponseEntity.ok(response);

    }

    //delete
    @DeleteMapping("/auth/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LibraryResponse> deleteUser(@PathVariable Long id){
        userService.removeUser(id);
        LibraryResponse response = new LibraryResponse(ResponseMessage.USER_DELETE_MESSAGE_RESPONSE,true);

        return ResponseEntity.ok(response);
    }





}
