package com.runners.service;

import com.runners.domain.ContactMessage;
import com.runners.dto.request.ContactMessageRequest;
import com.runners.dto.response.ContactMessageDTO;
import com.runners.exception.ResourceNotFoundException;
import com.runners.exception.message.ErrorMessage;
import com.runners.mapper.ContactMessageMapper;
import com.runners.repository.ContactMessageRepository;
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
}
