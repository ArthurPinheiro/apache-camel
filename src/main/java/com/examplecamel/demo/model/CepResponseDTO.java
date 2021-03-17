package com.examplecamel.demo.model;

import lombok.Data;

@Data
public class CepResponseDTO {

	private String cep;
	private String logradouro;
	private String complemento;
	private String bairro;
	private String localidade;
	private String uf;
	private String ibge;
	private String gia;
	private int ddd;
	private int siafi;

}
