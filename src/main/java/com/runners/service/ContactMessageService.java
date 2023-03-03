package com.runners.service;

import com.runners.domain.ContactMessage;
import com.runners.dto.request.ContactMessageRequest;
import com.runners.dto.response.ContactMessageDTO;
import com.runners.exception.ResourceNotFoundException;
import com.runners.exception.message.ErrorMessage;
import com.runners.mapper.ContactMessageMapper;
import com.runners.repository.ContactMessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class ContactMessageService {
    private final ContactMessageRepository contactMessageRepository;

    private final ContactMessageMapper contactMessageMapper;

    public ContactMessageService(ContactMessageRepository contactMessageRepository, ContactMessageMapper contactMessageMapper) {
        this.contactMessageRepository = contactMessageRepository;
        this.contactMessageMapper = contactMessageMapper;
    }

    public void saveContactMessage(ContactMessageRequest contactMessageRequest) {
      ContactMessage contactMessage = contactMessageMapper.contactMessageRequestToContactMessage(contactMessageRequest);
      contactMessageRepository.save(contactMessage);
    }


    public List<ContactMessageDTO> getAllContactMessage() {
      List<ContactMessage> contactMessageList = contactMessageRepository.findAll();
     List<ContactMessageDTO> contactMessageDTOList = contactMessageMapper.map(contactMessageList);
      return contactMessageDTOList;
    }

    public ContactMessageDTO getWithIdContactMessage(Long id) {
       ContactMessage contactMessage = contactMessageRepository.findById(id).orElseThrow(()->
               new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION, id)));
      ContactMessageDTO contactMessageDTO =  contactMessageMapper.contactMessageToDTO(contactMessage);
      return contactMessageDTO;
    }

    public void deleteContactMessage(Long id) {
/*
        ContactMessage contactMessage = contactMessageRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION, id)));

        contactMessageRepository.delete(contactMessage);

*/
        boolean existId = contactMessageRepository.existsById(id);

        if (!existId){

            throw new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION,id));

        }

        contactMessageRepository.deleteById(id);

    }

    public void updateContactMessage(Long id, ContactMessageRequest contactMessageRequestDTO) {

        ContactMessage contactMessage = contactMessageRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_EXCEPTION, id)));

        contactMessage.setName(contactMessageRequestDTO.getName());
        contactMessage.setEmail(contactMessageRequestDTO.getEmail());
        contactMessage.setSubject(contactMessageRequestDTO.getSubject());
        contactMessage.setBody(contactMessageRequestDTO.getBody());

        contactMessageRepository.save(contactMessage);
    }

    public Page<ContactMessageDTO> getAllPage(Pageable pageable) {

     Page<ContactMessage> contactMessagePage = contactMessageRepository.findAll(pageable);
        return contactMessagePage.map(x -> contactMessageMapper.contactMessageToDTO(x));
    }


    //page<POJO> to page<DTO>
//    public Page<ContactMessageDTO> getPages(){
//
//    }

}

