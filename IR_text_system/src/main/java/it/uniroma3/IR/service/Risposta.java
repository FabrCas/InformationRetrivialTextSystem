package it.uniroma3.IR.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

//classe che crea le possibili risposte per la ricerca 
public class Risposta {
	
	private TopDocs risultatiRicerca;
	private String termineRicercato;
	private IndexSearcher searcher;
	
	public Risposta (TopDocs hits, String testo, IndexSearcher searcher) {
		this.risultatiRicerca= hits;
		this.termineRicercato= testo;
		this.searcher= searcher;
	}
	
	public List<String> toString2(){
		return null;
		
	}
	public List<String> toString1(){
		
		System.out.println("Creazione risultati ricerca...");
		List<String> messaggioRicerca= new ArrayList<String>();
		
		messaggioRicerca.add("numero di documenti in cui ci sono stati riscontri per [" + this.termineRicercato
				+ "]: " + this.risultatiRicerca.totalHits.value+ "\n");
		
		/*
		 * int risNum= 0;
		for (ScoreDoc score:risultatiRicerca.scoreDocs) {
			messaggioRicerca.add("risultato n°"+risNum+": "+
		 "[Documento "+ score.doc+ "] ->" + score.toString()+ "| | ");
			risNum++;
		}
		*/
		for (ScoreDoc score: risultatiRicerca.scoreDocs) {
			try {
				Document d= this.searcher.doc(score.doc);
				messaggioRicerca.add(d.get("title") +  ", valori ricerca: " +score.toString()) ;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return messaggioRicerca;
	}
	
	
	//serie di metodi toString...
	
	
}
