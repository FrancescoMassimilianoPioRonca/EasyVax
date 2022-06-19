package com.easyvax.dto;

import com.easyvax.model.Personale;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonaleDTO {

    public Long id;
    @NonNull
    private Long idCentro;
    @NonNull
    private Long idUtente;

    public PersonaleDTO(Personale personale) {
        this.id = personale.getId();
        this.idUtente = personale.getUtente().getId();
        this.idCentro = personale.getCentroVaccinale().getId();
    }
}
