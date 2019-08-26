package it.uniroma3.IR.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

//classe che crea le possibili risposte per la ricerca 
public class Risposta {
	
	private TopDocs risultatiRicerca;
	
	public Risposta (TopDocs hits) {
		this.risultatiRicerca= hits;
	}
	
	public List<String> toString2(){
		return null;
		
	}
	public List<String> toString1() {
		
		System.out.println("Creazione risultati ricerca...");
		List<String> messaggioRicerca= new ArrayList<String>();
		
		messaggioRicerca.add("numero di riscontri: " + this.risultatiRicerca.totalHits.value+ "\n");
		int risNum= 0;
		/*
		for (ScoreDoc score:risultatiRicerca.scoreDocs) {
			messaggioRicerca.add("risultato n°"+risNum+": "+
		 "[Documento "+ score.doc+ "] ->" + score.toString()+ "| | ");
			risNum++;
		}
		*/
		return messaggioRicerca;
	}
	
	
	//serie di metodi toString...
	
	
}
