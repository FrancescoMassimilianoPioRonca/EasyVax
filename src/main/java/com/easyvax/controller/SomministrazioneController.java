package com.easyvax.controller;

import com.easyvax.dto.SomministrazioneDTO;
import com.easyvax.service.service.SomministrazioneService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/somministrazione")
@CrossOrigin("*")
public class SomministrazioneController {

    private final SomministrazioneService somministrazioneService;

    @GetMapping("/findAll")
    public List<SomministrazioneDTO> findAll(){
        return somministrazioneService.findAll();
    }

    @GetMapping("/getDetails")
    public SomministrazioneDTO getDetails(@Valid @NotNull() @RequestParam Long id){
        return somministrazioneService.getDetails(id);
    }


    @GetMapping("/findByUtente")
    public List<SomministrazioneDTO> findByUtente(@Valid @NotNull() @RequestParam String cf){
        return somministrazioneService.findByUtente(cf);
    }

    @GetMapping("/findByCod")
    public SomministrazioneDTO findByCod(@Valid @NotNull() @RequestParam String cod){
        return somministrazioneService.findByCod(cod);
    }

    @PostMapping("/insertSomministrazione")
    public SomministrazioneDTO insertSomministrazione(@NonNull @RequestBody SomministrazioneDTO somministrazioneDTO){
        return somministrazioneService.insertSomministrazione(somministrazioneDTO);
    }

    @DeleteMapping("/deleteSomministrazione")
    public List<SomministrazioneDTO> deleteSomministrazione(@Valid @NotNull(message = "Il campo non deve essere vuoto") @RequestParam Long id){
        return somministrazioneService.deletePrenotazione(id);
    }

    @PutMapping("/updateSomministrazione")
    public SomministrazioneDTO updateSomministrazione(@Valid @RequestParam String code , @RequestBody SomministrazioneDTO somministrazioneDTO){
        return somministrazioneService.updateSomministrazione(code,somministrazioneDTO);
    }
}
