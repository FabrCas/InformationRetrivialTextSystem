package it.uniroma3.IR.service;

import java.io.IOException;
import java.nio.file.Paths;

import javax.swing.SortOrder;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortedNumericSortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Interrogatore {
	
//passa le query all'indicizzatore
	
	private static final String INDEX_DIR= "static/indexedFiles";
	private static final int NUM_RESULT= 100;
	
	private IndexSearcher searcher;
	private Risposta risposta; 
	
	public Interrogatore () throws Exception{
		// creo il ricercatore di Lucene, che ricerca sopra a ogni indexReader
		 this.searcher = creaSearcher();
	}
	
	
	public void searchInContent(String testoRicerca) throws Exception {
		
		//creo la query di ricerca 
		QueryParser qp= new QueryParser("contenuto", new StandardAnalyzer());
		Query query= qp.parse(testoRicerca);
		
		//cerco l'indice (ordinato) 
		Sort sort= new Sort (new SortedNumericSortField("title", SortField.Type.STRING));
		TopDocs hits= this.searcher.search(query,NUM_RESULT, sort);
		
		this.risposta= new Risposta(hits);
	}
	
	
	private IndexSearcher creaSearcher() throws IOException{
		
		Directory dir= FSDirectory.open(Paths.get(INDEX_DIR));
		
		//comunico all'interfaccia reader (che accede in ogni momento all'indice di Lucene) la directory con i token
		IndexReader reader= DirectoryReader.open(dir);
		
		//l'index searcher
		IndexSearcher searcher= new IndexSearcher (reader);
		return searcher;
	}
	
	public Risposta getRisposta () {
		return this.risposta;
	}
	
	
    /* Prima versione (old) 
    public List<Document> RicercaInIndice(Query query) {
        try {
            IndexReader indexReader = DirectoryReader.open(memoryIndex);
            IndexSearcher searcher = new IndexSearcher(indexReader);
            TopDocs topDocs = searcher.search(query, 100);
            List<Document> documents = new ArrayList<>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                documents.add(searcher.doc(scoreDoc.doc));
            }

            return documents;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    
    //versione polimorfa con ordinamento
    
    public List<Document> RicercaInIndice(Query query, Sort sort) {
        try {
            IndexReader indexReader = DirectoryReader.open(memoryIndex);
            IndexSearcher searcher = new IndexSearcher(indexReader);
            TopDocs topDocs = searcher.search(query, 10, sort);
            List<Document> documents = new ArrayList<>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                documents.add(searcher.doc(scoreDoc.doc));
            }

            return documents;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    */
	
}
