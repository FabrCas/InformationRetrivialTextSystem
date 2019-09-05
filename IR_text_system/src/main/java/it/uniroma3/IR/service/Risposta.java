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
	
	public Risposta (TopDocs hits, String testoRicerca, IndexSearcher searcher) {
		this.risultatiRicerca= hits;
		this.termineRicercato= testoRicerca;
		this.searcher= searcher;
	}
	
	public String totaleHits() {
		return "numero di documenti in cui ci sono stati riscontri per [" + this.termineRicercato
				+ "]: " + this.risultatiRicerca.totalHits.value+ "\n";
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
		System.out.println(nomePagina);
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
