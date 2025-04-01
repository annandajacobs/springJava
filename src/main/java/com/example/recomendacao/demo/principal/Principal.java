package com.example.recomendacao.demo.principal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.example.recomendacao.demo.model.DadosEpisodio;
import com.example.recomendacao.demo.model.DadosSerie;
import com.example.recomendacao.demo.model.DadosTemporada;
import com.example.recomendacao.demo.model.Episodio;
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
        
        List<Episodio> episodios = temporadas.stream()
            .flatMap(t -> t.episodios().stream()
                    .map(d -> new Episodio(t.numeroTemporada(), d))
            ).collect(Collectors.toList());
        
        episodios.forEach(System.out::println);

        System.out.println("\nTOP 5 EPISODIOS");
        episodios.stream()
            .sorted(Comparator.comparing(Episodio::getAvaliacao).reversed())
            .limit(5)
            .forEach(System.out::println);
    
        System.out.println("\nA partir de que ano você deseja ver os epsódios? ");
        var ano = leitura.nextInt();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodios.stream()
            .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
            .forEach(e -> System.out.println(
                "Temporada: " + e.getTemporada() + 
                " Episódio: " + e.getTitulo() +
                " Data Lançamento: " + e.getDataLancamento().format(formatador)
            ));

        System.out.println("\nDigite um trecho do título do episódio: ");
        var trechoTitulo = leitura.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();
        if(episodioBuscado.isPresent()){
            System.out.println("Episódio encontrado!");
            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
        } else {
            System.out.println("Episódio não encontrado!");
        }

    }
}
