package com.easyvax.model;

import com.easyvax.DTO.SomministrazioneDTO;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Somministrazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String oraSomministrazione;

    @NonNull
    private LocalDate dataSomministrazione;

    @NonNull
    private String codiceSomm;

    @ManyToOne
    @JoinColumn(name="id_utente")
    private Utente utente;

    @ManyToOne
    @JoinColumn(name="id_vaccino")
    private Vaccino vaccino;

    @ManyToOne
    @JoinColumn(name="id_centro")
    private CentroVaccinale centro;


    public Somministrazione(SomministrazioneDTO somministrazioneDTO)
    {
        this.id = somministrazioneDTO.getId();
        this.oraSomministrazione = somministrazioneDTO.getOra();
        this.dataSomministrazione = somministrazioneDTO.getData();
        this.codiceSomm = somministrazioneDTO.codiceSomm;
    }



}
