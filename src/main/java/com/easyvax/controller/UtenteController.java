package com.easyvax.controller;

import com.easyvax.DTO.PersonaleDTO;
import com.easyvax.DTO.UtenteDTO;
import com.easyvax.model.Utente;
import com.easyvax.service.service.PersonleService;
import com.easyvax.service.service.UtenteService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/utente")

public class UtenteController {

    private final UtenteService utenteService;

    @GetMapping("/findAll")
    public List<UtenteDTO> findAll(){
        return utenteService.findAll();
    }

    @GetMapping("/getDetails")
    public UtenteDTO getDetails(@Valid @NotNull() @RequestParam Long id){
        return utenteService.getDetails(id);
    }



   /* @GetMapping("/findByRuolo")
    public PersonaleDTO findByRuolo(@Valid @NotNull() @RequestParam String cf){
        return personaleService.findByCodFiscale(cf);
    }*/

    @PostMapping("/insertUtente")
    public UtenteDTO insertUtente(@NonNull @RequestBody UtenteDTO utenteDTO){
        return utenteService.insertUtente(utenteDTO);
    }

    @DeleteMapping("/deleteUtente")
    public List<UtenteDTO> deleteUtente(@Valid @NotNull(message = "Il campo non deve essere vuoto") @RequestParam Long id){
        return utenteService.deleteUtente(id);
    }

    @PutMapping("/updateUtente")
    public List<UtenteDTO> updateAnagrafica(@Valid @RequestBody UtenteDTO utenteDTO){
        return utenteService.updateAnagrafica(utenteDTO);
    }
}
