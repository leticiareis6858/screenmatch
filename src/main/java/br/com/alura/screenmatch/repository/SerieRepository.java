package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    @Query("SELECT s FROM Serie s WHERE s.titulo = :nomeSerie")
     Optional<Serie> seriePorNome(String nomeSerie);

    @Query("SELECT s FROM Serie s WHERE s.titulo ILIKE %:trechoTitulo%")
    List<Serie> seriesPorTrecho(String trechoTitulo);

    @Query("SELECT s FROM Serie s WHERE s.atores ILIKE %:nomeAtor")
    List<Serie> seriesPorAtor(String nomeAtor);

    @Query("SELECT s FROM Serie s WHERE s.atores ILIKE %:nomeAtor AND s.avaliacao >= :avaliacao")
    List<Serie> seriesPorAtorEAvaliacao(String nomeAtor, Double avaliacao);

    @Query("SELECT s FROM Serie s ORDER BY s.avaliacao DESC FETCH FIRST 5 ROWS ONLY")
    List<Serie> seriesTop5();

    @Query("SELECT s FROM Serie s WHERE s.genero = :categoria")
    List<Serie> seriesPorGenero(Categoria categoria);

    @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacao >= :avaliacao")
    List<Serie> seriesPorTemporadaEAvaliacao(int totalTemporadas, double avaliacao);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodio%")
    List<Episodio> episodiosPorTrecho(String trechoEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> topEpisodiosPorSerie(Serie serie);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episodio> episodiosPorSerieEAno(Serie serie, int anoLancamento);
}
