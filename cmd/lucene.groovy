import org.crsh.command.CRaSHCommand
import org.crsh.cmdline.annotations.Usage
import org.crsh.cmdline.annotations.Required
import org.crsh.cmdline.annotations.Command
import org.crsh.cmdline.annotations.Argument
import org.crsh.command.ScriptException
import org.crsh.cmdline.completers.*

import org.apache.lucene.store.*;
import org.apache.lucene.index.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.document.*;
import org.apache.lucene.search.*;

import java.io.*;

@Usage("lucene integration")
class lucene extends CRaSHCommand {
  @Usage("init a lucene directory")
  @Command
  Object init() throws ScriptException {
    if (dir != null)
      throw new ScriptException("Already initialized");
    dir = new RAMDirectory();
    return "Initialized";
  }
  
  @Usage("index a document")
  @Command
  Object index(@Usage("the path of the document to index") @Argument(completer = FileCompleter.class) @Required String path) throws ScriptException {
    def file = new File(path);
    if (!file.exists() || !file.file)
      throw new ScriptException("The file $path is not a valid path");
    if (dir == null)
      throw new ScriptException("No directory initialized");
    def txt = file.text;
    def doc = new Document();
    doc.add(new Field("path", file.absolutePath, Field.Store.YES, Field.Index.NOT_ANALYZED));
    doc.add(new Field("content", txt, Field.Store.NO, Field.Index.ANALYZED));
    def writer = new IndexWriter(dir, new WhitespaceAnalyzer(), IndexWriter.MaxFieldLength.UNLIMITED);
    writer.updateDocument(new Term("path", file.absolutePath), doc);
    writer.close();
    return "indexed $file.absolutePath";
  }
  
  @Usage("search documents")
  @Command
  Object search(@Usage("the query") @Argument @Required String query) throws ScriptException {
    if (dir == null)
      throw new ScriptException("No directory initialized");
    IndexSearcher searcher = new IndexSearcher(dir);
    Term t = new Term("content", query);
    Query q = new TermQuery(t);
    TopDocs docs = searcher.search(q, 10);
    def reader = IndexReader.open(dir);
    def res = docs.scoreDocs.collect { scoreDoc -> 
      def doc = reader.document(scoreDoc.doc);
      return doc.get("path");
    }
    searcher.close();
    return "$docs.totalHits results : $res";
  }

  
}