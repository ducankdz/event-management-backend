package com.ptit.event_management.mappers;


import com.ptit.event_management.dtos.EventDTO;
import com.ptit.event_management.models.Event;
import com.ptit.event_management.responses.EventResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);
    Event toEntity(EventDTO dto);
    EventDTO toDto(Event event);
    EventResponse toEventResponse(Event event);
    void updateEventFromDto(EventDTO dto, @MappingTarget Event event);
}
