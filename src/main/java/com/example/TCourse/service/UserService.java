package com.example.TCourse.service;

import com.example.TCourse.dto.UserDataDto;
import com.example.TCourse.dto.UserDto;
import com.example.TCourse.dto.mapper.UserDataMapper;
import com.example.TCourse.dto.mapper.UserMapper;
import com.example.TCourse.entity.User;
import com.example.TCourse.exception.ConflictException;
import com.example.TCourse.exception.NotFoundException;
import com.example.TCourse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private final UserDataMapper userDataMapper = Mappers.getMapper(UserDataMapper.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }

    public void registerUser(UserDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ConflictException("Username is already taken");
        }
        var user = userMapper.map(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::map);
    }

    public ResponseEntity<UserDataDto> getUser(@PathVariable Long id){
        UserDataDto user = userRepository.findById(id).map(userDataMapper::map)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return ResponseEntity.ok(user);
    }
}