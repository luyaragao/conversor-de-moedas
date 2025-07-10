package br.com.luy.conversor.modelo;

import com.google.gson.annotations.SerializedName;

public record Moeda(
        @SerializedName("base_code")
        String moedaBase,

        @SerializedName("target_code")
        String moedaAlvo,

        @SerializedName("conversion_rate")
        double taxaDeConversao
) {}