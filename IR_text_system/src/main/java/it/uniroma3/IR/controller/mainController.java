package it.uniroma3.IR.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class mainController {
	
	@RequestMapping(value="/toDoc1")
	public String toDoc1() {
		return "DOC1.html";
	}

}
