package com.easyvax.DTO;

import com.easyvax.model.Utente;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UtenteDTO {

    public Long id;
    public String nome;
    public String cognome;
    private String codFiscale;
    private LocalDate dataNascita;
    private String password;
    private String ruolo;

    public Long residenza;


    public UtenteDTO(Utente utente) {
        this.id = utente.getId();
        this.nome = utente.getNome();
        this.cognome = utente.getCognome();
        this.dataNascita=utente.getDataNascita();
        this.ruolo= utente.getRuolo();
        this.codFiscale=utente.getCodFiscale();
        this.password=utente.getPassword();
        this.residenza=utente.getProvincia().getId();
    }
}
