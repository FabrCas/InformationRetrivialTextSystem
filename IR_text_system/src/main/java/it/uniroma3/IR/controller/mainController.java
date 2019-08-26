package it.uniroma3.IR.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.IR.service.Indicizzatore;
import it.uniroma3.IR.service.Interrogatore;
import it.uniroma3.IR.service.Risposta;

@Controller
public class mainController {
	
	@Autowired(required=true)
	private Indicizzatore indicizzatore;
	
	@Autowired
	private Interrogatore interrogatore;
	
	
	//Indicizzazione dei documenti
	@RequestMapping(value="/toIndex", method= RequestMethod.POST)
	public String toIndex() throws Exception {
		this.indicizzatore.indicizzaCartella();
		return "conferma.html";
	}
	
	
	//ricerca nei documenti
	@RequestMapping(value="/toFind", method= RequestMethod.POST)
	public String toFind(@RequestParam("search_input") String ricerca, Model model) throws Exception{
		this.interrogatore.searchInContent(ricerca);
		Risposta risp= this.interrogatore.getRisposta();
		model.addAttribute("risultati", risp.toString1());
		return "risultatiRicerca.html";
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
