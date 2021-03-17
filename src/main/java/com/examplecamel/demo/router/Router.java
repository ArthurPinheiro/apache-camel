package com.examplecamel.demo.router;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.examplecamel.demo.model.TestRestResponseDTO;

@Component
public class Router extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		restConfiguration().component("servlet").bindingMode(RestBindingMode.json).apiContextPath("/swagger")
				.apiContextRouteId("swagger").apiProperty("api.title", "Person Integration")
				.apiProperty("api.description", "Lista Endpoints").apiProperty("api.version", "1.0.0")

				.contextPath("/api").apiProperty("host", "localhost").apiProperty("port", "8080")
				.apiProperty("schemes", "http");

		rest().get("/hello-world").produces(MediaType.APPLICATION_JSON_VALUE).route().setBody(constant("Welcome"));

		rest("/test/rest").description("Chamando API CEP").get().id("rest-test").bindingMode(RestBindingMode.auto)
				.skipBindingOnErrorCode(true).description("Chamando API CEP").outType(TestRestResponseDTO.class)
				.responseMessage().code(HttpStatus.OK.value()).message("msg").endResponseMessage()
				.consumes(MediaType.APPLICATION_JSON_VALUE).produces(MediaType.APPLICATION_JSON_VALUE)
				.to(CepRouter.REST_TEST_ROUTE);

		rest("/git").description("Chamando a API do GitHub").get().id("git-hub").bindingMode(RestBindingMode.auto)
				.skipBindingOnErrorCode(true).description("Chamando API CEP").outType(GitHubRouter.class)
				.responseMessage().code(HttpStatus.OK.value()).message("msg").endResponseMessage()
				.consumes(MediaType.APPLICATION_JSON_VALUE).produces(MediaType.APPLICATION_JSON_VALUE)
				.to(GitHubRouter.GITHUB_ROUTER);

	}

}
