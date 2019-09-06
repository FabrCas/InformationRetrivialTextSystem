package it.uniroma3.IR.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import it.uniroma3.IR.model.RisultatoDoc;

//classe che crea le possibili risposte per la ricerca 
public class Risposta {
	
	private TopDocs risultatiRicerca;
	private String termineRicercato;
	private IndexSearcher searcher;
	private List<String> frammentiTesto;
	
	public Risposta (TopDocs hits, String testoRicerca, IndexSearcher searcher) {
		this.risultatiRicerca= hits;
		this.termineRicercato= testoRicerca;
		this.searcher= searcher;
		this.frammentiTesto= null;
	}
	
	public Risposta (TopDocs hits, String testoRicerca, IndexSearcher searcher, List<String> frammentiTesto) {
		this.risultatiRicerca= hits;
		this.termineRicercato= testoRicerca;
		this.searcher= searcher;
		this.frammentiTesto= frammentiTesto;
	}
	
	public String totaleHits() {
		return "numero di documenti in cui ci sono stati riscontri per [" + this.termineRicercato
				+ "]: " + this.risultatiRicerca.totalHits.value+ "\n";
	}
	
	public List<String> risultatiFuzzy() {
		List<String> risultati= new ArrayList<String>();
		String temp, temp1, temp2;
		boolean trovato= false;
		if(this.frammentiTesto!=null) {
			for(String frammento:frammentiTesto ) {
				trovato= false;
				Scanner scanner = new Scanner(frammento);
				while(scanner.hasNext() && !trovato ) {
					temp=scanner.next();
					if((temp.contains("<B>")) && (temp.contains("</B>"))) {
						trovato= true;
						temp1= temp.replace("<B>", "");
						temp2= temp1.replace("</B>","");
						if((!(temp2.equals(this.termineRicercato))) && (!(risultati.contains(temp2))))
							risultati.add(temp2);
					}
				}
				scanner.close();
			}
			return risultati;
		}
		else {
			return risultati;
		}
	}

	public List<RisultatoDoc> risultatiDocumenti(){
		System.out.println("Creazione risultati ricerca...");
		List<RisultatoDoc> messaggioRicerca= new ArrayList<RisultatoDoc>();
		
		/*
		 * int risNum= 0;
		for (ScoreDoc score:risultatiRicerca.scoreDocs) {
			messaggioRicerca.add("risultato n°"+risNum+": "+
		 "[Documento "+ score.doc+ "] ->" + score.toString()+ "| | ");
			risNum++;
		}
		*/
		RisultatoDoc risultatoParziale;
		for (ScoreDoc score: risultatiRicerca.scoreDocs) {
			try {
				risultatoParziale= new RisultatoDoc();
				Document d= this.searcher.doc(score.doc);
				risultatoParziale.setTitolo(getNomeDocumento(d.get("title")));
				risultatoParziale.setScore(modficaScore((score.toString())));
				risultatoParziale.setPaginahtml(getPagina(d.get("title")));
				messaggioRicerca.add(risultatoParziale);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return messaggioRicerca;
	}
	
	
	private String getPagina(String nomeFile) {
		String nomePagina;
		nomePagina= nomeFile.replace(".txt", ".html");
		return nomePagina;
	}
	
	private String getNomeDocumento(String titolo) {   //ovvero senza il .txt
		String nomeDocumento;
		nomeDocumento= titolo.replace(".txt", "");
		System.out.println(nomeDocumento);
		return nomeDocumento;
	}
	
	private String modficaScore(String score) {
		String scoreMod="";
		Scanner scanner = new Scanner(score);
		if(scanner.hasNext())
			scoreMod=scoreMod + scanner.next() + " ";
		if(scanner.hasNext())
			scoreMod=scoreMod + scanner.next();
		scanner.close();
		
		return scoreMod;
	}
	
	
}
