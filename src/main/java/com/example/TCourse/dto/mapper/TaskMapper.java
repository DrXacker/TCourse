package com.example.TCourse.dto.mapper;

import com.example.TCourse.dto.TaskDto;
import com.example.TCourse.entity.Task;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TaskMapper {

    TaskDto map(Task source);

    @Mappings({
            @Mapping(target = "isCompleted", expression = "java(false)"),
            @Mapping(target = "body", source = "taskDto.body"),
            @Mapping(target = "username", expression = "java(username)"),
            @Mapping(target = "priority", source = "taskDto.priority"),
            @Mapping(target = "category", source  = "taskDto.category"),
            @Mapping(target = "startDateTime", source = "taskDto.startDateTime"),
            @Mapping(target = "endDateTime", source = "taskDto.endDateTime")
    })
    Task map(TaskDto taskDto, String username);
}