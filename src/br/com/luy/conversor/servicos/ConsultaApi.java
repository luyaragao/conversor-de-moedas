package br.com.luy.conversor.servicos;

import br.com.luy.conversor.modelo.Conversao;
import br.com.luy.conversor.modelo.Moeda;
import com.google.gson.Gson;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public class ConsultaApi {

    private final String apiKey;
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public ConsultaApi() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            props.load(fis);
            this.apiKey = props.getProperty("apiKey");
            if (this.apiKey == null || this.apiKey.trim().isEmpty()) {
                throw new RuntimeException("A chave 'apiKey' não foi encontrada ou está vazia no arquivo config.properties.");
            }
        }catch (IOException e) {
            throw new RuntimeException("Erro ao ler o arquivo de configuração 'config.properties'. Verifique se ele existe na raiz do projeto.", e);
        }
    }

    public Conversao converter(String origem, String destino, double valor) {
        // A chave da API agora é lida da variável de instância.
        String endereco = "https://v6.exchangerate-api.com/v6/" + this.apiKey +
                "/pair/" + origem + "/" + destino;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endereco))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.out.println("Erro retornado pela API: " + response.body());
                return null;
            }
            Moeda moeda = gson.fromJson(response.body(), Moeda.class);
            double convertido = valor * moeda.taxaDeConversao();
            return new Conversao(origem, destino, valor, convertido);

        } catch (IOException | InterruptedException e) {
            System.out.println("Erro de comunicação. Verifique sua conexão com a internet ou o endereço da API!");
            return null;
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
            return null;
        }
    }
}
