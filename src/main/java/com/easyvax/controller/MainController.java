package com.easyvax.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class MainController {

    @GetMapping(value = "/custom-login")
    public void login(HttpServletResponse response) throws IOException {

        response.sendRedirect("/login-form.html");
    }

    @GetMapping(value = "/registrazioneUser")
    public String registrazioneUser(HttpServletResponse response) {

        return "registrazioneUser.html";
    }

    @GetMapping(value = "/registrazioneAdmin")
    public String registrazioneAdmin(HttpServletResponse response) {

        return "registrazioneAdmin.html";
    }





}
