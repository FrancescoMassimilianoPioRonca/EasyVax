package com.easyvax.model;

import com.easyvax.dto.VaccinoDTO;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Vaccino {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String nome;

    @NonNull
    private String casaFarmaceutica;

    @OneToMany(mappedBy="vaccino" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Somministrazione> somministrazioni = new ArrayList<>();


    public Vaccino(VaccinoDTO vaccinoDTO) {
        this.id = vaccinoDTO.getId();
        this.nome = vaccinoDTO.getNome();
        this.casaFarmaceutica = vaccinoDTO.getCasaFarmaceutica();
    }
}
