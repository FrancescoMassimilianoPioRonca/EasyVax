package com.easyvax.controller;

import com.easyvax.dto.CentroVaccinaleDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class MainController {

    @RequestMapping("/login.html")
    public String login() {
        return "login.html";
    }
}
