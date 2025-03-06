package com.example.recomendacao.demo.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodio(@JsonAlias("Title") String titulo, 
                            @JsonAlias("Episode") int numeroEpisodio, 
                            @JsonAlias("imdbRating") String avaliacao, 
                            @JsonAlias("Released") String dataLancamento) {

}
