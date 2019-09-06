package it.uniroma3.IR.service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;

/*cancellare @Component se non sono stati indicizzati i documenti, poichè è
 *  Spring non può inizializzare l'interrogatore
 */
@Component
public class Interrogatore {
	
//passa le query all'indicizzatore
	
	private static final String INDEX_DIR= "src/main/resources/static/indexedFiles";
	private static final int NUM_RESULT= 100;
	
	private IndexSearcher searcher;
	private Risposta risposta; 
	private Analyzer analyzer;
	private IndexReader reader;
	
	public Interrogatore (){
		// creo il ricercatore di Lucene, che ricerca sopra a ogni indexReader
		 try {
			this.searcher = creaSearcher();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void ricercaNormale(String testoRicerca) throws Exception {
		
		//creo la query di ricerca 
		QueryParser qp= new QueryParser("contents", new StandardAnalyzer());
		Query query= qp.parse(testoRicerca);
		
		//cerco l'indice (ordinato) 
		//Sort sort= new Sort (new SortedNumericSortField("title", SortField.Type.STRING));
		TopDocs hits= this.searcher.search(query,NUM_RESULT);
		
		this.risposta= new Risposta(hits, testoRicerca, searcher);
	}
	
	public void ricercaFuzzy(String testoRicerca) throws Exception {
	
		Term termineRicerca= new Term("contents",testoRicerca);
		FuzzyQuery fuzzyQuery= new FuzzyQuery(termineRicerca);  //2° parametro int maxEdits, ovvero la massima edit distance (n. operazioni per trasformare termine ricerca in termine ricercato
		/*massima edit distance = 2 (default)*/
		TopDocs fuzzyHits= this.searcher.search(fuzzyQuery,NUM_RESULT);


		QueryScorer scorer = new QueryScorer(fuzzyQuery, "contents");
		Highlighter highlighter= new Highlighter(scorer);

		List<String> frammentiTesto= new ArrayList<String>();
		String frammentoTesto;
		if(testoRicerca.length()>=3) {
			for (ScoreDoc score: fuzzyHits.scoreDocs) {
				try {
					Document doc = this.searcher.doc(score.doc);
					String contenuto= doc.get("contents");
					@SuppressWarnings("deprecation")
					TokenStream tokestream= TokenSources.getAnyTokenStream(reader, score.doc, "contents", this.analyzer);
					frammentoTesto= highlighter.getBestFragment(tokestream, contenuto);
					frammentiTesto.add(frammentoTesto);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			this.risposta= new Risposta(fuzzyHits, testoRicerca, searcher,frammentiTesto );
		}
		else {
			this.risposta= new Risposta(fuzzyHits, testoRicerca, searcher);
		}
	}
	
	private IndexSearcher creaSearcher() throws IOException{
		
		Directory dir= FSDirectory.open(Paths.get(INDEX_DIR));
		
		//comunico all'interfaccia reader (che accede in ogni momento all'indice di Lucene) la directory con i token
		this.reader= DirectoryReader.open(dir);
		
		//l'index searcher
		IndexSearcher searcher= new IndexSearcher (reader);
		return searcher;
	}
	
	public Risposta getRisposta () {
		return this.risposta;
	}
	
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
	
}
