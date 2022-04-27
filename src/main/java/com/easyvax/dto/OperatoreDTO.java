package com.easyvax.dto;

import com.easyvax.model.Operatore;
import com.easyvax.model.Personale;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OperatoreDTO {

    public Long id;
    @NonNull
    private Long idCentro;
    @NonNull
    private Long idUtente;

    public OperatoreDTO(Operatore operatore) {
        this.id = operatore.getId();
        this.idUtente=operatore.getUtente().getId();
        this.idCentro=operatore.getCentroVaccinale().getId();
    }
}
