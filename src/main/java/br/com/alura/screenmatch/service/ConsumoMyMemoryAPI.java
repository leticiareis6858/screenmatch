package br.com.alura.screenmatch.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.net.URLDecoder;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConsumoMyMemoryAPI {
    public enum Linguagem {
        PORTUGUES("pt-br"), INGLES("en");

        public String siglaLinguagem;

        Linguagem(String lang) {
            this.siglaLinguagem = lang;
        }

        public static Linguagem getLang(String lang) {
            for (Linguagem self : Linguagem.values()) {
                if (self.siglaLinguagem.equalsIgnoreCase(lang)) {
                    return self;
                }
            }
            throw new NoSuchElementException("Linguagem não encontrada");
        }
    }

    public static String obterTraducao(String text) {
        ObjectMapper mapper = new ObjectMapper();

        String json = get(text, Linguagem.INGLES, Linguagem.PORTUGUES);

        MyMemoryResponseValue traducao;
        try {
            traducao = mapper.readValue(json, MyMemoryResponseValue.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        traducao.dadosResposta.textoTraduzido = URLDecoder.decode(traducao.dadosResposta.textoTraduzido, StandardCharsets.UTF_8);

        return traducao.dadosResposta.textoTraduzido;
    }

    public static String get(String texto, Linguagem lingua1, Linguagem lingua2) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(MyMemoryURLGenerator.urlEncodeQuery(texto, lingua1, lingua2))).build();

        try {
            HttpResponse<String> resposta = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (resposta.statusCode() == 200) {
                return resposta.body();
            } else {
                throw new IOException("Erro ao se comunicar com My Memory");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static class MyMemoryURLGenerator {
        public static String urlEncodeQuery(String text, Linguagem lingua1, Linguagem lingua2) {
            String texto = URLEncoder.encode(text, StandardCharsets.UTF_8);
            String langpair = URLEncoder.encode(lingua1.siglaLinguagem + "|" + lingua2.siglaLinguagem, StandardCharsets.UTF_8);

            String url = "https://api.mymemory.translated.net/get?q=" + texto + "&langpair=" + langpair;

            return url;
        }
    }

    public static class MyMemoryResponseValue {
        @JsonAlias(value = "responseData")
        public DadosResposta dadosResposta;

        public static class DadosResposta {
            @JsonAlias(value = "translatedText")
            public String textoTraduzido;
        }
    }
}