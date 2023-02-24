package com.runners.controller;


import com.runners.dto.request.ContactMessageRequest;
import com.runners.dto.response.ContactMessageDTO;
import com.runners.dto.response.LibraryResponse;
import com.runners.dto.response.ResponseMessage;
import com.runners.service.ContactMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/contactmessage")
public class ContactMessageController {

    private final ContactMessageService contactMessageService;



    public ContactMessageController(ContactMessageService contactMessageService) {
        this.contactMessageService = contactMessageService;
    }

    //create
    @PostMapping
    public ResponseEntity<LibraryResponse> createContactMessage(@Valid @RequestBody ContactMessageRequest contactMessageRequest){

        contactMessageService.saveContactMessage(contactMessageRequest);
        LibraryResponse libraryResponse = new LibraryResponse(ResponseMessage.CONTACT_MESSAGE_CREATE_RESPONSE_MESSAGE,true);
        return new ResponseEntity<>(libraryResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ContactMessageDTO>> getAllContactMessage(){
       List<ContactMessageDTO> contactMessageDTOList =  contactMessageService.getAllContactMessage();

        return ResponseEntity.ok(contactMessageDTOList);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ContactMessageDTO> getWithIdContactMessage(@PathVariable Long id){
       ContactMessageDTO contactMessageDTO = contactMessageService.getWithIdContactMessage(id);
        return  ResponseEntity.ok(contactMessageDTO);
    }
}
