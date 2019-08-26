package it.uniroma3.IR.service;

import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

//classe che crea le possibili risposte per la ricerca 
public class Risposta {
	
	private TopDocs risultatiRicerca;
	
	public Risposta (TopDocs hits) {
		this.risultatiRicerca= hits;
	}
	
	public String toString1() {
		StringBuilder messaggioRicerca= new StringBuilder();
		messaggioRicerca.append("Risultati della ricerca:\n");
		messaggioRicerca.append("numero di riscontri: " + this.risultatiRicerca.totalHits.toString()+ "\n");
		int risNum= 0;
		for (ScoreDoc score:risultatiRicerca.scoreDocs) {
			messaggioRicerca.append("risultato n°"+risNum+": "+
		"[Documento "+ score.doc+ "] ->" + score.toString());
		}
		return messaggioRicerca.toString();
	}
	
	
	//serie di metodi toString...
	
	
}
