package com.easyvax.DTO;

import com.easyvax.model.CentroVaccinale;
import com.easyvax.model.Somministrazione;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SomministrazioneDTO {

    public Long id;
    public LocalDate data;
    public String codiceSomm;
    public String ora;
    public Long idUtente;
    public Long idCentro;
    public Long idVaccino;

    public SomministrazioneDTO(Somministrazione somministrazione) {
        this.id = somministrazione.getId();
        this.data = somministrazione.getDataSomministrazione();
        this.ora = somministrazione.getOraSomministrazione();
        this.idUtente = somministrazione.getUtente().getId();
        this.idCentro = somministrazione.getCentro().getId();
        this.idVaccino = somministrazione.getVaccino().getId();
        this.codiceSomm = somministrazione.getCodiceSomm();
    }
}
