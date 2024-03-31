package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();

    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=194f8ef8";
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private SerieRepository repositorio;
    private List<Serie> series = new ArrayList<>();
    private Optional<Serie> serieBusca;

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1. Buscar séries
                    2. Buscar episódios     
                    3. Listar séries buscadas    
                    4. Buscar séries salvas usando trecho do título ou título completo
                    5. Buscar séries por ator
                    6. Buscar séries por ator e avaliação mínima
                    7. Top 5 séries salvas
                    8. Buscar séries por categoria/gênero
                    9. Filtrar séries por número máximo de temporadas e mínimo de avaliação
                    10. Buscar episódios por trecho
                    11. Buscar série por título
                    12. Buscar o 5 melhores episódios de uma série
                    13. Buscar episódios lançados a partir de certo ano
                    0. Sair                                 
                    """;

            System.out.println(menu);
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerie();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriesPorTrecho();
                    break;
                case 5:
                    buscarSeriesPorAtor();
                    break;
                case 6:
                    buscarSeriesPorAtorEAvaliacao();
                    break;
                case 7:
                    buscarTop5Series();
                    break;
                case 8:
                    buscarSeriesPorCategoria();
                    break;
                case 9:
                    filtrarSeriesPorTemporadaEAvaliacao();
                    break;
                case 10:
                    buscarEpisodioPorTrecho();
                    break;
                case 11:
                    buscarSeriePorTitulo();
                    break;
                case 12:
                    buscarTop5EpisodiosPorSerie();
                    break;
                case 13:
                    buscarEpisodiosDepoisDeTalAno();
                    break;
                case 0:
                    System.out.println("Encerrando a aplicação...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerie() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para a busca:");
        var nomeSerie = scanner.nextLine();

        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
        listarSeriesBuscadas();
        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = scanner.nextLine();

        Optional<Serie> serie = repositorio.seriePorNome(nomeSerie);

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        } else {
            System.out.println("Série não econtrada!");
        }
    }

    private void listarSeriesBuscadas() {
        series = repositorio.findAll();

        if (!series.isEmpty()) {
            series.stream()
                    .sorted(Comparator.comparing(Serie::getGenero))
                    .forEach(System.out::println);
        } else {
            System.out.println("Não há nenhuma série salva no banco de dados!");
        }
    }

    private void buscarSeriesPorTrecho() {
        System.out.println("Digite um trecho para pesquisar séries que o contem no título: ");
        var trechoTitulo = scanner.nextLine();
        List<Serie> seriesEncontradas = repositorio.seriesPorTrecho(trechoTitulo);

        if (!seriesEncontradas.isEmpty()) {
            System.out.println("Séries encontradas:");
            seriesEncontradas.forEach(System.out::println);
        } else {
            System.out.println("Nenhuma série encontrada com o título informado.");
        }
    }

    private void buscarSeriesPorAtor() {
        System.out.println("Qual o nome para a busca? ");
        var nomeAtor = scanner.nextLine();
        List<Serie> seriesEncontradas = repositorio.seriesPorAtor(nomeAtor);

        if (!seriesEncontradas.isEmpty()) {
            System.out.println("Séries em que " + nomeAtor + " trabalhou: ");
            seriesEncontradas.forEach(s -> System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
        } else {
            System.out.println("Nenhuma série encontrada com o ator informado.");
        }
    }

    private void buscarSeriesPorAtorEAvaliacao() {
        System.out.println("Qual o nome para a busca? ");
        var nomeAtor = scanner.nextLine();

        System.out.println("Exibir séries a partir de que avaliação mínima? ");
        var avaliacao = scanner.nextDouble();

        List<Serie> seriesEncontradas = repositorio.seriesPorAtorEAvaliacao(nomeAtor, avaliacao);

        if (!seriesEncontradas.isEmpty()) {
            System.out.println("Séries em que " + nomeAtor + " trabalhou: ");
            seriesEncontradas.forEach(s -> System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
        } else {
            System.out.println("Nenhuma série encontrada com o ator informado.");
        }
    }

    private void buscarTop5Series() {
        List<Serie> serieTop = repositorio.seriesTop5();

        if (!serieTop.isEmpty()) {
            serieTop.forEach(s -> System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
        } else {
            System.out.println("Não há nenhuma série salva no banco de dados!");
        }
    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Deseja buscar séries de que categoria/gênero? ");
        var categoriaInformada = scanner.nextLine();
        Categoria categoria = Categoria.fromPortugues(categoriaInformada);

        List<Serie> seriesPorCategoria = repositorio.seriesPorGenero(categoria);

        if (!seriesPorCategoria.isEmpty()) {
            System.out.println("Séries da categoria " + categoriaInformada + ":");
            seriesPorCategoria.forEach(s -> System.out.println("| " + "Gênero: " + s.getGenero() + " | " + "Titulo: " + s.getTitulo() + " | \n"));
        } else {
            System.out.println("Nenhuma série encontrada com a categoria/gênero " + categoriaInformada + "!");
        }
    }

    private void filtrarSeriesPorTemporadaEAvaliacao() {
        System.out.println("Filtrar séries até quantas temporadas? ");
        var totalTemporadas = scanner.nextInt();

        System.out.println("Com avaliação a partir de que valor? ");
        var avaliacao = scanner.nextDouble();

        List<Serie> filtroSeries = repositorio.seriesPorTemporadaEAvaliacao(totalTemporadas, avaliacao);

        if (!filtroSeries.isEmpty()) {
            System.out.println("Séries com até " + totalTemporadas + " temporadas e avaliação mínima de " + avaliacao);
            filtroSeries.forEach(s ->
                    System.out.println(s.getTitulo() + "  - avaliação: " + s.getAvaliacao()));
        } else {
            System.out.println("Não há nenhuma série salva no banco de dados que tenha até " + totalTemporadas + " e avaliação mínima de " + avaliacao + "!");
        }

    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Qual o nome do episódio para busca?");
        var trechoEpisodio = scanner.nextLine();

        List<Episodio> episodiosEncontrados = repositorio.episodiosPorTrecho(trechoEpisodio);

        if (!episodiosEncontrados.isEmpty()) {
            episodiosEncontrados.forEach(e ->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getNumeroEpisodio(), e.getTitulo()));
        } else {
            System.out.println("Nenhum episódio encontrado!");
        }
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Busque uma série pelo nome: ");
        var nomeSerie = scanner.nextLine();
        serieBusca = repositorio.seriePorNome(nomeSerie);

        if (serieBusca.isPresent()) {
            System.out.println("Dados da série: " + serieBusca.get());

        } else {
            System.out.println("Série não encontrada!");
        }

    }

    private void buscarTop5EpisodiosPorSerie() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()) {
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s Avaliação %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getNumeroEpisodio(), e.getTitulo(), e.getAvaliacao()));
        }
    }

    private void buscarEpisodiosDepoisDeTalAno() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()) {
            Serie serie = serieBusca.get();
            System.out.println("Digite o ano limite de lançamento: ");
            var anoLancamento = scanner.nextInt();
            scanner.nextLine();

            List<Episodio> episodiosAno = repositorio.episodiosPorSerieEAno(serie, anoLancamento);

            if(!episodiosAno.isEmpty()){
                episodiosAno.forEach(System.out::println);
            } else{
                System.out.println("Não existe nenhum episódio dessa série lançado a partir de "+anoLancamento+"!");
            }
        }
    }
}
