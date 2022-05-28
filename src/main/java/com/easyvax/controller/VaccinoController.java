package com.easyvax.controller;

import com.easyvax.dto.VaccinoDTO;
import com.easyvax.service.service.VaccinoService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/vaccino")
@CrossOrigin("*")

/**
 * -Nella classe VaccinoController vengono gestiti e organizzati tutti gli endpoint relativi ai vaccini.
 * -I path delle api, ovvero delle attività che si possono svolgere relative all'amministratore, iniziano con:
 * "http://localhost:8080/api/vaccino/...".
 * -Nei metodi presenti in questa classe vengono semplicemente richiamati i metodi dela classe VaccinoService
 * per il controllo e la validità dei dati in input delle request dal front-end.
 * -Infine tutte le response ricevute dal livello "service" verranno inviare al front-end.
 */

public class VaccinoController {

    public final VaccinoService vaccinoService;

    @GetMapping("/findAll")
    public List<VaccinoDTO> findAll() {
        return vaccinoService.findAll();
    }

    @GetMapping("/findByCasaFarmaceutica")
    public List<VaccinoDTO> findByCasaFarmaceutica(@Valid @NotNull() @RequestParam String casaFarmaceutica) {
        return vaccinoService.findByCasaFarmaceutica(casaFarmaceutica);
    }

    @GetMapping("/findByNome")
    public VaccinoDTO findByNome(@Valid @NotNull() @RequestParam String nome) {
        return vaccinoService.findByNome(nome);
    }

    @PostMapping("/insertVaccino")
    public VaccinoDTO insertVaccino(@NonNull @RequestBody VaccinoDTO vaccinoDTO) {
        return vaccinoService.insertVaccino(vaccinoDTO);
    }

    @DeleteMapping("/deleteVaccino")
    public boolean deleteVaccino(@Valid @NotNull(message = "Il campo non deve essere vuoto") @RequestParam Long id) {
        return vaccinoService.deleteVaccino(id);
    }

    @PutMapping("/updateVaccino")
    public List<VaccinoDTO> updateVaccino(@Valid @RequestBody VaccinoDTO vaccinoDTO) {
        return vaccinoService.updateVaccino(vaccinoDTO);
    }

}
