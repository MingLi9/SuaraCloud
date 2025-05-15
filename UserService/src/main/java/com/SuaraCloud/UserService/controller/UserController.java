package com.SuaraCloud.UserService.controller;
import com.SuaraCloud.UserService.dto.UserDto;
import com.SuaraCloud.UserService.dto.UserRequest;
import com.SuaraCloud.UserService.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id, HttpServletRequest request) {
        String tokenId = (String) request.getAttribute("tokenId");
        return ResponseEntity.ok(userService.getUserById(id, tokenId));
    }

    @GetMapping("/token/{tokenId}")
    public ResponseEntity<UserDto> getUserByTokenId(@PathVariable String tokenId) {
        return ResponseEntity.ok(userService.getUserByTokenId(tokenId));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(HttpServletRequest request) {
        String tokenId = (String) request.getAttribute("tokenId");
        UserDto createdUser = userService.createUser(tokenId);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest, HttpServletRequest request) {
        String tokenId = (String) request.getAttribute("tokenId");
        return ResponseEntity.ok(userService.updateUser(id, userRequest, tokenId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        String tokenId = (String) request.getAttribute("tokenId");
        userService.deleteUser(id, tokenId);
        return ResponseEntity.noContent().build();
    }
}
