package com.easyvax.DTO;

import com.easyvax.model.CentroVaccinale;
import com.easyvax.model.Personale;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonaleDTO {

    public Long id;
    @NonNull
    public String ruolo;
    @NonNull
    private Long idCentro;
    @NonNull
    private Long idUtente;

    public PersonaleDTO(Personale personale) {
        this.id = personale.getId();
        this.ruolo= personale.getRuolo();
        this.idUtente=personale.getUtente().getId();
        this.idCentro=personale.getCentroVaccinale().getId();
    }
}
