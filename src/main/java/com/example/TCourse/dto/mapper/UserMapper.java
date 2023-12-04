package com.example.TCourse.dto.mapper;

import com.example.TCourse.dto.UserDto;
import com.example.TCourse.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto map(User source);

    User map(UserDto source);
}
