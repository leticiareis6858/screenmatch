package br.com.alura.screenmatch.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.screenmatch.service.SerieService;
import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService service;

    @GetMapping()
    public List<SerieDTO> obterSeries() {
        return service.obterTodasAsSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> obterTop5Series() {
        return service.obterTop5Series();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> obterLancamentos() {
        return service.obterLancamentos();
    }

    @GetMapping("/{id}")
    public SerieDTO obterSeriePorId(@PathVariable Long id) {
        return service.obterSeriePorId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTodasTemporadas(@PathVariable Long id) {
        return service.obterTodasTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{numeroTemporada}")
    public List<EpisodioDTO> obterTemporadasPorNumero(@PathVariable Long id, @PathVariable Long numeroTemporada) {
        return service.obterTemporadasPorNumero(id, numeroTemporada);
    }

    @GetMapping("/categoria/{genero}")
    public List<SerieDTO> obterSeriesPorGenero(@PathVariable String genero) {
        return service.obterSeriesPorGenero(genero);
    }

    @GetMapping("/{id}/temporadas/top")
    public List<EpisodioDTO> obterTopEpisodiosPorSerie(@PathVariable Long id) {
        return service.obterTopEpisodiosPorSerie(id);
    }
}