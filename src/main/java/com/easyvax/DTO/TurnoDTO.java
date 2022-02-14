package com.easyvax.DTO;

import com.easyvax.model.Turno;
import com.easyvax.model.Utente;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TurnoDTO {

    public Long id;
    public LocalDate giorno;
    public String inizioTurno;
    public String fineTurno;


    public TurnoDTO(Turno turno) {
        this.id = turno.getId();
        this.giorno = turno.getGiorno();
        this.inizioTurno = turno.getInizioTurno();
        this.fineTurno=turno.getFineTurno();
    }
}
