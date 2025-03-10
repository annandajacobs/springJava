package com.example.recomendacao.demo.principal;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.example.recomendacao.demo.model.DadosEpisodio;
import com.example.recomendacao.demo.model.DadosSerie;
import com.example.recomendacao.demo.model.DadosTemporada;
import com.example.recomendacao.demo.service.ConsumoApi;
import com.example.recomendacao.demo.service.ConverteDados;

public class Principal {
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=9f32a5d0";

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo =  new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    public void exibirMenu(){
        System.out.println("Digite o nome da séria para a busca: ");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie serie = conversor.obterDados(json, DadosSerie.class);
        System.out.println(serie);

        List<DadosTemporada> temporadas = new ArrayList<>();

		for(int i = 1; i <= serie.totalTemporadas(); i++) {
			json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&Season=" + i + API_KEY);
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);

        // for(int i = 0; i < serie.totalTemporadas(); i++){
        //     List<DadosEpisodio> episodiosTemporadas = temporadas.get(i).episodios();
        //     for(int j = 0; j < episodiosTemporadas.size(); j++){
        //         System.out.println(episodiosTemporadas.get(j).titulo());
        //     }
        // }

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo()))); //lambda
        temporadas.forEach(System.out::println);
    }
}
