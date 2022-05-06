package com.easyvax.controller;


import com.easyvax.service.impl.PdfGeneratorServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;

@AllArgsConstructor
@RestController
@RequestMapping("/api/pdf")
@CrossOrigin("*")

/**
 * Questo controller genera il pdf che sarà poi inviato tramite email in fase di inseriemnto di una nuova prenotazione.
 * In questo controller, si vanno a settare quelli che sono gli HTTP-Header necessari quali il content-type e il content-disposition per settare il nome di default.
 * Prende come paramentro il codice della sommministrazione
 */
public class PdfGeneratorController {

    private final PdfGeneratorServiceImpl pdfGeneratorService;

    @GetMapping("/generate")
    public void generatePdf(@RequestParam String codSomm, HttpServletResponse response) {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachement; filename=Ricevuta.pdf";

        response.setHeader(headerKey, headerValue);

        try {
            this.pdfGeneratorService.export(codSomm, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
