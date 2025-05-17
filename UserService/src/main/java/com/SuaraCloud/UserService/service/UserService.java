package com.SuaraCloud.UserService.service;

import com.SuaraCloud.UserService.exception.NotDataOwnerException;
import com.SuaraCloud.UserService.model.User;
import com.SuaraCloud.UserService.repository.UserRepository;
import com.SuaraCloud.UserService.dto.UserDto;
import com.SuaraCloud.UserService.dto.UserRequest;
import com.SuaraCloud.UserService.exception.ResourceNotFoundException;
import com.SuaraCloud.UserService.exception.UserAlreadyExistsException;
import jakarta.ws.rs.NotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id, String tokenId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if(!user.getTokenId().equals(tokenId)) {
            throw new NotDataOwnerException("Only the user can CRUD their own account");
        }

        return convertToDto(user);
    }

    public UserDto getUserByTokenId(String tokenid) {
        if (!userRepository.existsByTokenId(tokenid)) {
            throw new ResourceNotFoundException("Token id was not found: " + tokenid);
        }

        User user = userRepository.findUserByTokenId(tokenid);
        return convertToDto(user);
    }

    @Transactional
    public UserDto createUser(String tokenId) {
        if (userRepository.existsByTokenId(tokenId)) {
            throw new UserAlreadyExistsException("Token id already in use: " + tokenId);
        }

        User user = new User();
        user.setTokenId(tokenId);

        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    @Transactional
    public UserDto updateUser(Long id, UserRequest userRequest, String tokenId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if(!user.getTokenId().equals(tokenId)) {
            throw new NotDataOwnerException("Only the user can CRUD their own account");
        }

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());

        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id, String tokenId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if(!user.getTokenId().equals(tokenId)) {
            throw new NotDataOwnerException("Only the user can CRUD their own account");
        }
        userRepository.deleteById(id);
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setTokenId(user.getTokenId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}