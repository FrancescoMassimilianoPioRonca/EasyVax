package com.easyvax.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Personale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String nome;

    @NonNull
    private String cognome;

    @NonNull
    private String codFiscale;

    @NonNull
    private String residenza;

    @NonNull
    private String capResidenza;

    @NonNull
    private String ruolo;

    @NonNull
    private String password;

    @NonNull
    private LocalDate dataNascita;

    @ManyToOne
    @JoinColumn(name = "id_centroVaccinale")
    private CentroVaccinale centroVaccinale;

    @OneToMany(mappedBy="personale", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Turno> turni = new ArrayList<>();
}
