package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner scanner=new Scanner(System.in);
    private ConsumoAPI consumo= new ConsumoAPI();

    private ConverteDados conversor=new ConverteDados();
    private final String ENDERECO="https://www.omdbapi.com/?t=";
    private final String API_KEY="&apikey=194f8ef8";
    public void exibeMenu(){
        System.out.println("Digite o nome da série para a busca:");
        var nomeSerie=scanner.nextLine();
        var json=consumo.obterDados(ENDERECO+nomeSerie.replace(" ","+")+API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

        System.out.println(dados);

        List<DadosTemporada> temporadas=new ArrayList<>();

		for(int i=1;i<=dados.totalTemporadas();i++){
			json=consumo.obterDados(ENDERECO+nomeSerie.replace(" ","+")+"&season="+i+API_KEY);
			DadosTemporada dadosTemporada=conversor.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);

//        for(int i=0;i<dados.totalTemporadas(); i++){
//            List<DadosEpisodio> episodiosTemporada=temporadas.get(i).episodios();
//            for(int j=0;j< episodiosTemporada.size();j++){
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }
//        👆 esse for de maneira mais simplificada: 👇

        temporadas.forEach(t-> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio> dadosEpisodios=temporadas.stream()
                .flatMap(t->t.episodios().stream())
                .collect(Collectors.toList()); //ou ".toList();" porém o toList gera uma lista de dados imutável...

        System.out.println("\nTop 5 episódios:");
        dadosEpisodios.stream()
            .filter(e->!e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodio> episodios=temporadas.stream()
                .flatMap(t->t.episodios().stream()
                        .map(d->new Episodio(t.numero(),d))
                ).collect(Collectors.toList());

       episodios.forEach(System.out::println);

        System.out.println("\nExibir episódios a partir de que ano? ");
        var ano=scanner.nextInt();
        scanner.nextLine();

        LocalDate dataBusca=LocalDate.of(ano,1,1);

        DateTimeFormatter formatador=DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e->e.getDataLancamento()!=null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e-> System.out.println(
                        "Temporada: "+e.getTemporada()+
                                " Episódio: "+e.getTitulo()+
                                " Data de lançamento: "+e.getDataLancamento().format(formatador)
                ));

    }
}
