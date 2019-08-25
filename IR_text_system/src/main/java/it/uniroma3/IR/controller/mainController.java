package it.uniroma3.IR.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class mainController {

	//vista dei documenti 
	@RequestMapping(value="/toDoc1")
	public String toDoc1() {
		return "DOC1.html";
	}
	
	@RequestMapping(value="/toDoc2")
	public String toDoc2() {
		return "DOC2.html";
	}

	
	@RequestMapping(value="/toDoc3")
	public String toDoc3() {
		return "DOC3.html";
	}

	
	@RequestMapping(value="/toDoc4")
	public String toDoc4() {
		return "DOC4.html";
	}


}
