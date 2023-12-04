package com.example.TCourse.dto.mapper;

import com.example.TCourse.dto.UserDataDto;
import com.example.TCourse.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDataMapper {

    UserDataDto map(User source);

    User map(UserDataDto source);
}