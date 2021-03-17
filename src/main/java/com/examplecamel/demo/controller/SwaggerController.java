package com.examplecamel.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SwaggerController {

	@RequestMapping("/swagger-ui")
	public String redirectUi() {
		return ("redirect:/swagger-ui.html");
	}
}
