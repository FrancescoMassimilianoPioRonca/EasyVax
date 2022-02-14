package com.easyvax.DTO;

import com.easyvax.model.Utente;
import com.easyvax.model.Vaccino;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VaccinoDTO {

    public Long id;
    public String nome;
    public String casaFarmaceutica;
    private LocalDate dataApprovazioneVaccino;

    public VaccinoDTO(Vaccino vaccino) {
        this.id = vaccino.getId();
        this.nome = vaccino.getNome();
        this.casaFarmaceutica = vaccino.getCasaFarmaceutica();
        this.dataApprovazioneVaccino = vaccino.getDataApprovazioneVaccino();
    }

}
