package com.easyvax.controller;

import com.easyvax.DTO.UtenteDTO;
import com.easyvax.DTO.VaccinoDTO;
import com.easyvax.service.service.VaccinoService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/vaccino")
public class VaccinoController {

    public final VaccinoService vaccinoService;

    @GetMapping("/findAll")
    public List<VaccinoDTO> findAll(){
        return vaccinoService.findAll();
    }

    @GetMapping("/findByCasaFarmaceutica")
    public List<VaccinoDTO> findByCasaFarmaceutica(@Valid @NotNull() @RequestParam String casaFarmaceutica){
        return vaccinoService.findByCasaFarmaceutica(casaFarmaceutica);
    }

    @PostMapping("/insertVaccino")
    public VaccinoDTO insertVaccino(@NonNull @RequestBody VaccinoDTO vaccinoDTO){
        return vaccinoService.insertVaccino(vaccinoDTO);
    }

    @DeleteMapping("/deleteVaccino")
    public List<VaccinoDTO> deleteVaccino(@Valid @NotNull(message = "Il campo non deve essere vuoto") @RequestParam Long id){
        return vaccinoService.deleteVaccino(id);
    }

    @PutMapping("/updateVaccino")
    public List<VaccinoDTO> updateVaccino(@Valid @RequestBody VaccinoDTO vaccinoDTO){
        return vaccinoService.updateVaccino(vaccinoDTO);
    }

}