package com.examplecamel.demo.router;

import org.apache.camel.Processor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.examplecamel.demo.model.CepResponseDTO;
import com.examplecamel.demo.model.TestRestResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CepRouter extends RouteBuilder {

    public static final String REST_TEST_ROUTE = "direct:restTestRoute";

    @Override
    public void configure() throws Exception {
        from(REST_TEST_ROUTE)
            .routeId("restTestRoute")
            .log("[REST_TEST_ROUTE] Iniciando chamada")
            .setHeader(Exchange.HTTP_METHOD, constant(HttpMethod.GET)) // define o cabeçalho - informando fará uso de um método http
            .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON)) // define o cabeçalho - informando que o tipo de conteúdo será trafegado através do json
			.toD("https://viacep.com.br/ws/59150600/json/?bridgeEndpoint=true") // envia uma msg para um endpoint
            .process(toResponseDTO())
            .marshal() //
            .json(JsonLibrary.Jackson)
            .log("${body}");
        
    }

    private Processor toResponseDTO() {
        return exchange -> {
            String resposeJson = exchange.getIn().getBody(String.class);
            ObjectMapper mapper = new ObjectMapper();
            CepResponseDTO response = mapper.readValue(resposeJson, CepResponseDTO.class);
            TestRestResponseDTO responseDTO = new TestRestResponseDTO();

            responseDTO.setCep(response.getCep());
            responseDTO.setBairro(response.getBairro());
            responseDTO.setLocalidade(response.getLocalidade());
            responseDTO.setLogradouro(response.getLogradouro());
            responseDTO.setUf(response.getUf());

            exchange.getIn().setBody(responseDTO);

        };
    }
}
