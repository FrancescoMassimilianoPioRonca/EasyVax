package com.easyvax.controller;


import com.easyvax.service.impl.PdfGeneratorServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;

@AllArgsConstructor
@RestController
@RequestMapping("/pdf")
@CrossOrigin("*")
public class PdfGeneratorController {

    private final PdfGeneratorServiceImpl pdfGeneratorService;

    @GetMapping("/generate")
    public void  generatePdf(@RequestParam String codSomm, HttpServletResponse response){
        response.setContentType("application/pdf");
        String headerKey="Content-Disposition";
        String headerValue="attachement; filename=Ricevuta.pdf";

        response.setHeader(headerKey,headerValue);

        try {
            this.pdfGeneratorService.export(codSomm,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
