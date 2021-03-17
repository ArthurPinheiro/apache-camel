package com.examplecamel.demo.model;

import lombok.Data;

@Data
public class TestRestResponseDTO {

	private String cep;
	private String logradouro;
	private String bairro;
	private String localidade;
	private String uf;

}
