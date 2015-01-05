package com.belteshazzar.wiki;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.ws.rs.core.UriInfo;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.belteshazzar.sqrm.SqrmError;
import com.belteshazzar.sqrm.SqrmException;
import com.belteshazzar.sqrm.json.WikiJSON;
import com.belteshazzar.sqrm.json.WikiRequestJSON;
import com.belteshazzar.sqrm.json.WikiTagJSON;

@Stateless
public class WikiUtils
{
	public static final String RAW = "raw";
	public static final String JS = "js";
	public static final String WIKI = "wiki";
	public static final String HTML = "html";
	public static final String HTMLJS = "htmljs";

	private static ScriptEngineManager manager = new ScriptEngineManager();

	private static StandardAnalyzer analyzer = new StandardAnalyzer(
			Version.LUCENE_41);
	private static Directory index = null;
	static
	{
		try
		{
			index = FSDirectory.open(new File(
					"C:\\glassfish4\\glassfish\\domains\\domain1\\solr-index"));

			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_41,
					analyzer);
			IndexWriter indexWriter = new IndexWriter(index, config);
			indexWriter.close();
		}
		catch (IOException ioex)
		{
			ioex.printStackTrace();
		}
	}

	@PersistenceContext(unitName = "seJPA")
	private EntityManager em;

	public Wiki create(WikiRequestJSON cwr)
	{
		if (cwr == null) throw new SqrmException(SqrmError.INVALID_EMAIL);

		Wiki wiki = new Wiki();
		wiki.setId(new WikiPK());
		wiki.getId().setTitle(cwr.title);
		wiki.getId().setRevision(0);
		wiki.setBody(cwr.body);
		wiki.setCreatedAt(new Date());
		wiki.setRevisedAt(new Date());
		wiki.setNote(cwr.note);
		List<WikiTag> tags = new LinkedList<WikiTag>();
		if (cwr.tags != null)
		{
			for (WikiTagJSON st : cwr.tags)
			{
				tags.add(new WikiTag(wiki, st.name, st.value, true));
			}
		}
		wiki.setTags(tags);

		extractTags(wiki);

		em.persist(wiki);

		return wiki;
	}

	public Wiki read(String title)
	{
		Wiki w = getByTitle(title);
		return w;
	}

	public Wiki update(String title, WikiRequestJSON uwr)
	{
		Wiki w = getByTitle(title);
		if (w == null) return null;

		Wiki wiki = new Wiki();
		WikiPK id = new WikiPK();

		id.setTitle(w.getTitle());
		id.setRevision(w.getRevision() + 1);

		wiki.setId(id);
		wiki.setBody(uwr.body);
		wiki.setNote(uwr.note);
		wiki.setRevisedAt(new Date());
		wiki.setCreatedAt(w.getCreatedAt());

		List<WikiTag> tags = new LinkedList<WikiTag>();
		if (uwr.tags != null)
		{
			for (WikiTagJSON st : uwr.tags)
			{
				tags.add(new WikiTag(wiki, st.name, st.value, true));
			}
		}
		wiki.setTags(tags);

		extractTags(wiki);

		em.persist(wiki);

		// TODO: remove previous revisions in index
		index(wiki);

		return wiki;
	}

	public boolean delete(String title)
	{
		Wiki w = getByTitle(title);
		if (w == null) return false;

		Wiki wiki = new Wiki();
		WikiPK id = new WikiPK();
		id.setTitle(w.getId().getTitle());
		id.setRevision(w.getId().getRevision() + 1);

		wiki.setId(id);
		wiki.setBody(w.getBody());
		wiki.setNote(w.getNote());
		wiki.setCreatedAt(w.getCreatedAt());
		wiki.setRevisedAt(new Date());
		em.persist(wiki);

		return true;
	}

	public Wiki getByTitle(String title)
	{
		if (em == null) return null;
		List<?> r = em
				.createQuery(
						"SELECT w from Wiki w WHERE w.id.title = ?1 ORDER BY w.id.revision DESC")
				.setParameter(1, title).setMaxResults(1).getResultList();
		if (r.size() != 1) return null;
		return (Wiki) r.get(0);
	}

	public Wiki getByTitle(String title, int revision)
	{
		if (em == null) return null;
		List<?> r = em
				.createQuery(
						"SELECT w from Wiki w WHERE w.id.title = ?1 AND w.id.revision = ?2")
				.setParameter(1, title).setParameter(2, revision)
				.setMaxResults(1).getResultList();
		if (r.size() != 1) return null;
		return (Wiki) r.get(0);
	}

	public List<Wiki> getByTag(String tag)
	{
		List<Wiki> r = em
				.createQuery(
						"SELECT w FROM Wiki w JOIN w.tags t WHERE t.tag = ?1 AND w.id.revision = (SELECT MAX(ww.id.revision) FROM Wiki ww WHERE ww.id.title = w.id.title)",
						// "SELECT w,MAX(w.id.revision) FROM Wiki w JOIN w.tags t WHERE t.tag = ?1 GROUP BY w.id.title",
						Wiki.class).setParameter(1, tag).getResultList();
		return r;
	}

	public List<SearchResult> search(String qStr)
	{
		final int HITS_PER_PAGE = 20;

		try
		{

			IndexReader indexReader = DirectoryReader.open(index);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			Query q = new QueryParser(Version.LUCENE_41, "body", analyzer)
					.parse(qStr);

			TopScoreDocCollector collector = TopScoreDocCollector.create(
					HITS_PER_PAGE, true);
			indexSearcher.search(q, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;

			List<SearchResult> results = new LinkedList<SearchResult>();
			for (int i = 0; i < hits.length; i++)
			{
				Document doc = indexSearcher.doc(hits[i].doc);
				Wiki wiki = getByTitle(doc.get("title"),
						Integer.parseInt(doc.get("revision")));
				if (wiki != null) results.add(new SearchResult(wiki,
						hits[i].score));
			}
			return results;
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public class SearchResult
	{
		public Wiki wiki;
		public float score;

		public SearchResult(Wiki wiki, float score)
		{
			this.wiki = wiki;
			this.score = score;
		}
	}

	public void index(Wiki wiki)
	{
		try
		{
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_41,
					analyzer);
			IndexWriter indexWriter = new IndexWriter(index, config);

			Document doc = new Document();
			doc.add(new TextField("title", wiki.getTitle(), Field.Store.NO));
			doc.add(new IntField("revision", wiki.getId().getRevision(),
					Field.Store.YES));
			doc.add(new TextField("body", wiki.getBody(), Field.Store.NO));
			doc.add(new TextField("note", (wiki.getNote() == null ? "" : wiki
					.getNote()), Field.Store.NO));

			indexWriter.addDocument(doc);
			indexWriter.close();
		}
		catch (IOException ioex)
		{
			ioex.printStackTrace();
		}
	}

	// public static String escapeQuotes(String s)
	// {
	// return s.replaceAll(Pattern.quote("\""),
	// Matcher.quoteReplacement("\\\""));
	// }

	// public static String functionNameOf(String name)
	// {
	// return name.replaceAll("[^a-z-A-Z]", "");
	// }
	public String functionNameOf(Wiki wiki)
	{
		return "fn" + wiki.getTitle().replace('-', '_') + 'v'
				+ wiki.getId().getRevision();
	}

	// public static WikiType getType(String body)
	// {
	// System.err.println("getType");
	// if (body==null) return WikiType.SERVER;
	//
	// StringBuilder wikiScript = new StringBuilder();
	// String line;
	//
	// String functionName = "test";
	//
	// try (WikiReader wr= new WikiReader(functionName,new StringReader(body)))
	// {
	// while ((line=wr.readLine())!=null)
	// {
	// wikiScript.append(line);
	// }
	// }
	// catch (IOException e)
	// {
	// e.printStackTrace();
	// }
	//
	// ScriptEngine engine = manager.getEngineByName("JavaScript");
	//
	// try
	// {
	// engine.eval(wikiScript.toString());
	// }
	// catch (ScriptException e)
	// {
	// e.printStackTrace();
	// }
	//
	// try
	// {
	// Object pre = engine.eval("new " + functionName + "().pre");
	// if (pre instanceof Boolean && ((Boolean)pre).booleanValue()) return
	// WikiType.CLIENT_PRE;
	// Object inline = engine.eval("new " + functionName + "().inline");
	// if (inline instanceof Boolean && ((Boolean)inline).booleanValue()) return
	// WikiType.CLIENT_INLINE;
	// Object post = engine.eval("new " + functionName + "().post");
	// if (post instanceof Boolean && ((Boolean)post).booleanValue()) return
	// WikiType.CLIENT_POST;
	// return WikiType.SERVER;
	// }
	// catch (ScriptException e)
	// {
	// e.printStackTrace();
	// return WikiType.SERVER;
	// }
	// }

	public WikiJSON execute(UriInfo uriInfo, Wiki wiki)
	{
		if (wiki == null) return null;

		WikiJSON wj = new WikiJSON(wiki);

		ScriptEngine engine = manager.getEngineByName("JavaScript");
		try
		{
			engine.eval("var document = new Object();");
		}
		catch (ScriptException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		WikiScript wikiScript = new WikiScript(this, engine, uriInfo, wiki);
		engine.put("sqrm", wikiScript);
		engine.put("se", wikiScript);

		collectJS(wiki, engine, wikiScript);
		StringWriter writer = new StringWriter();
		engine.getContext().setWriter(writer);

		Invocable inv = (Invocable) engine;
		String functionName = functionNameOf(wiki);

		try
		{
			inv.invokeFunction(functionName);
		}
		catch (NoSuchMethodException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ScriptException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// convert to html
		StringReader reader = new StringReader(writer.toString());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		new WikiRenderer(new BufferedReader(reader), ps);

		try
		{
			wj.html = baos.toString("ISO-8859-1");
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return wj;
	}

	public void collectJS(Wiki wiki, ScriptEngine engine, WikiScript wikiScript)
	{
		if (wiki == null)
		{
			return;
		}

		for (WikiTag t : wiki.getTags())
		{
			collectJS(getByTitle(t.getTag()), engine, wikiScript);
		}

		StringBuilder script = new StringBuilder();
		String line;

		String functionName = functionNameOf(wiki);
		wikiScript.addFunctionName(wiki.getTitle(), functionName);

		try (WikiReader wr = new WikiReader(functionName, wiki))
		{
			while ((line = wr.readLine()) != null)
			{
				script.append(line);
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try
		{
			engine.eval(script.toString());
		}
		catch (ScriptException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void extractTags(Wiki wiki)
	{
		List<WikiTag> tags = wiki.getTags();
		String line;

		try (BufferedReader br = new BufferedReader(new StringReader(
				wiki.getBody())))
		{
			while ((line = br.readLine()) != null)
			{
				line = line.trim();

				if (line.length() > 0 && line.charAt(0) == '#')
				{
					String[] tag = line.substring(1).split("\\s+", 2);

					if (tag.length == 1)
					{
						tags.add(new WikiTag(wiki, tag[0]));
					}
					else
					{
						tags.add(new WikiTag(wiki, tag[0], tag[1]));
					}
				}
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
