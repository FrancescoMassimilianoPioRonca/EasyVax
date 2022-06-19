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


/**
 * Questo Controller è più "esterno" al progetto in quanto serve escusivamente per
 * gestire la visualizzazione delle pagine in html.
 * A regime, quando il front end verrà sviluppato il mainController può essere eliminato
 */
@Controller
public class MainController {

    //Ritorna la pagina di login
    @GetMapping(value = "/custom-login")
    public void login(HttpServletResponse response) throws IOException {

        response.sendRedirect("/login-form.html");
    }

    //Ritorna la pagina di registrazione per USER
    @GetMapping(value = "/registrazioneUser")
    public String registrazioneUser(HttpServletResponse response) {

        return "registrazioneUser.html";
    }

    //Ritorna la pagina di registrazione per ADMIN
    @GetMapping(value = "/registrazioneAdmin")
    public String registrazioneAdmin(HttpServletResponse response) {

        return "registrazioneAdmin.html";
    }





}
