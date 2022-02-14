package com.easyvax.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CentroVaccinale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String nome;

    @NonNull
    private String localita;

    @NonNull
    private String cap;

    @OneToMany(mappedBy = "centroVaccinale", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Personale> personale = new ArrayList<>();

    @OneToMany(mappedBy="centro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Somministrazione> somministrazioni = new ArrayList<>();
}

