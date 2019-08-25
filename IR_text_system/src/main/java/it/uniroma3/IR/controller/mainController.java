package it.uniroma3.IR.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.IR.service.Indicizzatore;

@Controller
public class mainController {
	
	@Autowired(required=true)
	private Indicizzatore indicizzatore;
	
	//Indicizzazione dei documenti
	@RequestMapping(value="/toIndex", method= RequestMethod.POST)
	public String toIndex() throws Exception {
		this.indicizzatore.indicizzaCartella();
		return "conferma.html";
	}

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
	
	@RequestMapping("/home")
	public String toHome(){
		return "index.html";
	}


}
