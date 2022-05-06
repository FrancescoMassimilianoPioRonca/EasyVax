package com.easyvax.dto;

import com.easyvax.model.CentroVaccinale;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CentroVaccinaleDTO {

    public Long id;
    public String indirizzo;
    public String nome;
    public Long idProvincia;

    public CentroVaccinaleDTO(CentroVaccinale centroVaccinale) {
        this.id = centroVaccinale.getId();
        this.indirizzo = centroVaccinale.getIndirizzo();
        this.nome = centroVaccinale.getNome();
        this.idProvincia = centroVaccinale.getProvincia().getId();
    }
}
