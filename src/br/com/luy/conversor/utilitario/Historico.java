package br.com.luy.conversor.utilitario;

import br.com.luy.conversor.modelo.Conversao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Historico {
    private final List<Conversao> conversoes = new ArrayList<>();

    public void salvar(Conversao conversao) {
        conversoes.add(conversao);
    }

    public void salvarEmArquivo() {
        if (conversoes.isEmpty()) {
            return;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter escrita = new FileWriter("historico.json")) {
            escrita.write(gson.toJson(conversoes));
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo de histórico: " + e.getMessage());
        }
    }
}

