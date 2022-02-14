package com.easyvax.DTO;

import com.easyvax.model.CentroVaccinale;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CentroVaccinaleDTO {

    public Long id;
    public String cap;
    public String località;
    public String nome;

    public CentroVaccinaleDTO(CentroVaccinale centroVaccinale) {
        this.id = centroVaccinale.getId();
        this.cap = centroVaccinale.getCap();
        this.località = centroVaccinale.getLocalita();
        this.nome = centroVaccinale.getNome();
    }
}
