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
    public String nome;
    public String cognome;
    private String codFiscale;
    private String CapResidenza;
    private String residenza;
    private String capResidenza;
    private LocalDate dataNascita;
    private String password;
    private Long idCentro;

    public PersonaleDTO(Personale personale) {
        this.id = personale.getId();
        this.nome = personale.getNome();
        this.cognome = personale.getCognome();
        this.dataNascita=personale.getDataNascita();
        this.capResidenza=personale.getCapResidenza();
        this.codFiscale=personale.getCodFiscale();
        this.password=personale.getPassword();
        this.residenza=personale.getResidenza();
        this.idCentro=personale.getCentroVaccinale().getId();
    }
}
