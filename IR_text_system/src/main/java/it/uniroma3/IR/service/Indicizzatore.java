package it.uniroma3.IR.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;
import org.springframework.beans.factory.annotation.Autowired;

public class Indicizzatore {
	
    private Directory memoryIndex;
    @Autowired
    private Analyzer analyzer;


    public Indicizzatore(Directory memoryIndex, Analyzer analizzatore) {
        super();
        this.memoryIndex = memoryIndex;
        this.analyzer = analizzatore;
    }
    
    public void indicizzaCartella(String PathCartella) throws Exception{
    	File file= new File("/IR_text_system/src/main/resources/static/Articoli");
    	File[] fileArray = file.listFiles();
    	String corpo;
    	String line;
    	
    	for (File f: fileArray) {
    		corpo= ""; 
    		System.out.println("caricamento del file"+ f.toString()+"...");                  
    		FileReader fReader= new FileReader(f);                                                         //fileReader prende il path, versione polimorfa ???
    		BufferedReader reader = new BufferedReader(fReader);
    		
    		line= reader.readLine();
    		while (line!=null) {
    			corpo += line;
    			line= reader.readLine();
    		}
    		reader.close();
    		this.indicizzaDocumenti(f.getName(), corpo);
    	}
    }
    
    
    public void indicizzaDocumenti(String titolo, String corpo) {

        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        try {
            IndexWriter writter = new IndexWriter(memoryIndex, indexWriterConfig);
            Document document = new Document();

            document.add(new TextField("title", titolo, Field.Store.YES));
            document.add(new TextField("body", corpo, Field.Store.YES));
            document.add(new SortedDocValuesField("title", new BytesRef(titolo)));

            writter.addDocument(document);
            writter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void CancellaDocumento(Term term) {
        try {
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            IndexWriter writter = new IndexWriter(memoryIndex, indexWriterConfig);
            writter.deleteDocuments(term);
            writter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
