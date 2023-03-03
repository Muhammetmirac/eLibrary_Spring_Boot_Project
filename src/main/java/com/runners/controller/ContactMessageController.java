package com.runners.controller;


import com.runners.domain.ContactMessage;
import com.runners.dto.request.ContactMessageRequest;
import com.runners.dto.response.ContactMessageDTO;
import com.runners.dto.response.LibraryResponse;
import com.runners.dto.response.ResponseMessage;
import com.runners.service.ContactMessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<LibraryResponse> deleteContactMessage(@PathVariable("id") Long id){

        contactMessageService.deleteContactMessage(id);

        LibraryResponse libraryResponse = new LibraryResponse(String.format(ResponseMessage.CONTACT_MESSAGE_DELETE_RESPONSE,id),
                                                                            true);

        return new ResponseEntity<>(libraryResponse,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibraryResponse> updateContactMessage(@PathVariable("id") Long id,
                                                                @Valid @RequestBody ContactMessageRequest contactMessageRequest){

        contactMessageService.updateContactMessage(id,contactMessageRequest);

        LibraryResponse libraryResponse = new LibraryResponse();

        libraryResponse.setMessage(String.format(ResponseMessage.CONTACT_MESSAGE_UPDATE_RESPONSE,id));

        libraryResponse.setSuccess(true);

        return new ResponseEntity<>(libraryResponse,HttpStatus.OK);
    }


    @GetMapping("/page")
    public ResponseEntity<Page<ContactMessageDTO>> getAllContactMessageWithPages(@RequestParam("page") int page,
                                                                                 @RequestParam("size") int size){

       Pageable pageable = PageRequest.of(page,size);

      Page<ContactMessageDTO> contactMessageDTOPage = contactMessageService.getAllPage(pageable);

      return ResponseEntity.ok(contactMessageDTOPage);
    }



}