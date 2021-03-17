package com.examplecamel.demo.router;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.examplecamel.demo.handler.GitHubErrorProcess;
import com.examplecamel.demo.model.GitHubRequestDTO;
import com.examplecamel.demo.model.GitHubResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class GitHubRouter extends RouteBuilder {
	
	public static final String GITHUB_ROUTER = "direct:gitHubRouter";

	@Override
	public void configure() throws Exception {
		
		onException(Exception.class)
		.handled(true)
		.process(new GitHubErrorProcess());
		
		from(GITHUB_ROUTER)
			.routeId("gitHubRouter")
			.log("[GITHUB_ROUTER], Iniciando chamada")
			.setHeader(Exchange.HTTP_METHOD, constant(HttpMethod.GET))
			.setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON))
			.toD("https://api.github.com/users/ArthurPinheiro?bridgeEndpoint=true")
			.process(toResponseGitTO())
			.marshal()
			.json(JsonLibrary.Jackson)
			.log("${body}");
	}

	private Processor toResponseGitTO() {
		return exchange -> {
			String responseJson = exchange.getIn().getBody(String.class);
			ObjectMapper mapper = new ObjectMapper();
			GitHubRequestDTO request = mapper.readValue(responseJson, GitHubRequestDTO.class);
			GitHubResponseDTO response = new GitHubResponseDTO();
			
			response.setLogin(request.getLogin());
			response.setBio(request.getBio());
			response.setCompany(request.getCompany());
			response.setLocation(request.getLocation());
			
			exchange.getIn().setBody(response);
		};
	}

}
