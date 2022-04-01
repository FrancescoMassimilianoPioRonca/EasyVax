package com.easyvax.controller;


import com.easyvax.dto.RichiestaDTO;
import com.easyvax.dto.SomministrazioneDTO;
import com.easyvax.model.Richiesta;
import com.easyvax.service.service.RichiestaService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/richiesta")
@CrossOrigin("*")
public class RichiestaController {

    private final RichiestaService richiestaService;

    @GetMapping("/getRichiesteUtente")
    public List<RichiestaDTO> getDetails(@Valid @NotNull() @RequestParam Long id){
        return richiestaService.getRichiesteUtente(id);
    }

    @GetMapping("/getRichiestePersonale")
    public List<RichiestaDTO> getDetailsPersonal(@Valid @NotNull() @RequestParam Long id){
        return richiestaService.getRichiestePersonale(id);
    }

    @DeleteMapping("/deleteRichiesta")
    public void deleteRichiesta(@Valid @NotNull() @RequestParam Long id){
         richiestaService.deleteRichiesta(id);
    }

    @PostMapping("/insertRichiesta")
    public RichiestaDTO insertRichiesta(@NonNull @RequestBody RichiestaDTO richiestaDTO){
        return richiestaService.insertRichiesta(richiestaDTO);
    }

    @PutMapping("/accettaRichiesta")
    public void accettaRichiesta(@NonNull @RequestParam Long id){
         richiestaService.accettaRichiesta(id);
    }

    @PutMapping("/rejectRichiesta")
    public void rejectichiesta(@NonNull @RequestParam Long id){
        richiestaService.rifiutaRichiesta(id);
    }
}
