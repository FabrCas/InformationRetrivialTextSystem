package it.uniroma3.IR.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

/*tutti i file nella cartella "inputFiles" verranno indicizzati.
creiamo 3 campi:
-path: il file path [Field.Store.YES]
-title: il titolo del file [Field.Store.YES]
-contents: il contenuto del file [Field.Store.YES]
*/

public class Indicizzatore {
	
	/* folders, paths*/
	//output folder
	private static final String INDEX_DIR= "static/indexedFiles";
	private Directory dirIndexedFiles;
	//input folder
	private static final String INPUT_DIR="static/inputFiles";
	
	
	/*lucene's classes*/
    private Analyzer analyzer;
    private IndexWriterConfig iwc;
    private IndexWriter writer ; 

    public Indicizzatore() {
    	try {
    		
    		//istanza di org.apache.lucene.store.Directory
			this.dirIndexedFiles= FSDirectory.open(Paths.get(INDEX_DIR));
			//analizzatore con le stop word di default 
			this.analyzer= new StandardAnalyzer();
			//configurazione dell'indexWriter
			this.iwc= new IndexWriterConfig(analyzer);
			this.iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);                              //Creates a new index if one does not exist, otherwise it opens the index and documents will be appended.
			//l'indexWriter, scrive nuovi index file per la cartella "static/indexedFiles"
			this.writer= new IndexWriter(dirIndexedFiles, iwc);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void indicizzaCartella() throws Exception{
    	File file= new File(INPUT_DIR);
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
    		this.indicizzaDocumento(f.getName(), corpo, f);
    	}
    }
    
  /*
   * There are two basic ways a document can be written into Lucene.

Indexed - The field is analyzed and indexed, and can be searched.
Stored - The field's full text is stored and will be returned with search results.
If a document is indexed but not stored, you can search for it, but it won't be returned with search results.

   */
    public void indicizzaDocumento(String titolo, String corpo, File file ) {
        try {
        	//creazione di un documento di Lucene
            Document document = new Document();

            document.add(new StringField("title", titolo, Field.Store.YES));
            document.add(new TextField("contents", corpo, Field.Store.YES));
            document.add(new StringField("path", file.toString(), Field.Store.YES));
            document.add(new SortedDocValuesField("title", new BytesRef(titolo)));

            writer.addDocument(document);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void CancellaDocumento(Term term) {
        try {
            this.writer.deleteDocuments(term);
            this.writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
