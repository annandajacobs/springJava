package com.example.recomendacao.demo.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosTemporada(@JsonAlias("Season") String numeroTemporada,
                            @JsonAlias("Episodes") List<DadosEpisodio> episodios) {

}
