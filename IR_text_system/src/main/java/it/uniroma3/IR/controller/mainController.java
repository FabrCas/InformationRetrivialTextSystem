package it.uniroma3.IR.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.IR.model.RisultatoDoc;
import it.uniroma3.IR.service.Indicizzatore;
import it.uniroma3.IR.service.Interrogatore;
import it.uniroma3.IR.service.Risposta;

@Controller
public class mainController {
	
	@Autowired(required=true)
	private Indicizzatore indicizzatore;
	
	/*cancellare @Autowired se non sono stati indicizzati i documenti, poichè è
	 *  Spring non può inizializzare l'interrogatore
	 */
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
		// da sostituire con una ricerca fuzzy -> this.interrogatore.ricercaNormale(ricerca);
		this.interrogatore.setAnalyzer(this.indicizzatore.getAnalyzer());
		this.interrogatore.ricercaFuzzy(ricerca);
		
		
		
		
		
		//totale risultati
		Risposta risp= this.interrogatore.getRisposta();
		model.addAttribute("hits",risp.totaleHits());
		//forse intedevi...
		List<String> possibiliParoleRicercate= risp.risultatiFuzzy();
		for(String a: possibiliParoleRicercate)
			System.out.println(a +"\n");
		model.addAttribute("paroleTrovate", possibiliParoleRicercate);
		//lista documenti con parola ricercata
		List<RisultatoDoc> listaRisultati=risp.risultatiDocumenti();
		model.addAttribute("listaRisultati", listaRisultati);
		
		return "risultatiRicerca.html";
	}
	
	
	//vista dei documenti 
	@RequestMapping(value="/toDOC1-La clonazione di Dolly")
	public String toDoc1() {
		return "DOC1-La clonazione di Dolly.html";
	}
	
	@RequestMapping(value="/toDOC2-VR")
	public String toDoc2() {
		return "DOC2-VR.html";
	}

	
	@RequestMapping(value="/toDOC3-Empire State Building")
	public String toDoc3() {
		return "DOC3-Empire State Building.html";
	}

	
	@RequestMapping(value="/toDOC4- Articolo 11 e 13")
	public String toDoc4() {
		return "DOC4- Articolo 11 e 13.html";
	}
	
	@RequestMapping("/home")
	public String toHome(){
		return "index.html";
	}


}
