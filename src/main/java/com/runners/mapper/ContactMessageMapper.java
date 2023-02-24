package com.runners.mapper;

import com.runners.domain.ContactMessage;
import com.runners.dto.request.ContactMessageRequest;
import com.runners.dto.response.ContactMessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContactMessageMapper {
    //POJO to DTO
    ContactMessageDTO contactMessageToDTO(ContactMessage contactMessage);


    //DTO to POJO
    @Mapping(target = "id", ignore = true)
    ContactMessage contactMessageRequestToContactMessage(ContactMessageRequest contactMessageRequest);



    // List<ContactMessage> ----> List<ContactMessageDTO>
    List<ContactMessageDTO> map(List<ContactMessage> contactMessageList);

}
