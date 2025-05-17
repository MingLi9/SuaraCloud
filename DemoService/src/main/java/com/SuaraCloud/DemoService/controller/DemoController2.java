package com.SuaraCloud.DemoService.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class DemoController2 {

    @GetMapping("/protected-resource")
    public String getProtectedResource(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        return "Protected resource accessed by user: " + userId;
    }

    @GetMapping("/public/open-resource")
    public String getPublicResource() {
        return "This is a public resource";
    }
}