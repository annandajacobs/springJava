package com.example.recomendacao.demo.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(@JsonAlias("Title") String titulo, 
                        @JsonAlias("totalSeasons") int totalTemporadas,
                        @JsonAlias("imdbRating") String avaliacao) {

}
