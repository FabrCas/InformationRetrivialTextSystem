package it.uniroma3.IR.service;

import org.apache.lucene.search.TopDocs;

//classe che crea le possibili risposte per la ricerca 
public class Risposta {
	
	private TopDocs risultatiRicerca;
	
	public Risposta (TopDocs hits) {
		this.risultatiRicerca= hits;
	}
	
	
	//serie di metodi toString...
	
	

}
