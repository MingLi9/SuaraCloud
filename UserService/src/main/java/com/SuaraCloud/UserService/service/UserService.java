package com.SuaraCloud.UserService.service;

import com.SuaraCloud.UserService.RabbitMQ.Payload;
import com.SuaraCloud.UserService.RabbitMQ.RabbitMQEvent;
import com.SuaraCloud.UserService.exception.NotDataOwnerException;
import com.SuaraCloud.UserService.model.User;
import com.SuaraCloud.UserService.repository.UserRepository;
import com.SuaraCloud.UserService.dto.UserDto;
import com.SuaraCloud.UserService.dto.UserRequest;
import com.SuaraCloud.UserService.exception.ResourceNotFoundException;
import com.SuaraCloud.UserService.exception.UserAlreadyExistsException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public UserService(UserRepository userRepository, RabbitTemplate rabbitTemplate) {
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
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
            return createUser(tokenid);
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

        sendMQTTMessage(savedUser.getId(), "create_user");

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
        sendMQTTMessage(id, "update_user");
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
        sendMQTTMessage(id, "delete_user");
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

    private void sendMQTTMessage(Long id, String eventName) {
        RabbitMQEvent payload = new RabbitMQEvent<>(eventName, new Payload(id), "UserService", ZonedDateTime.now());
        rabbitTemplate.convertAndSend("suara-broadcast-exchange", "", payload);
    }
}