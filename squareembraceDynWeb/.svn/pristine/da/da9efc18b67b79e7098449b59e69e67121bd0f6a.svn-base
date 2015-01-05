package com.belteshazzar.wiki;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.ws.rs.core.UriInfo;

public class WikiScript
{
	private WikiUtils wikiUtils;
	private ScriptEngine engine;
	private UriInfo uriInfo;
	private Wiki wiki;
	private Map<String, String> functionMap;

	public WikiScript(WikiUtils wikiUtils, ScriptEngine engine,
			UriInfo uriInfo, Wiki wiki)
	{
		this.wikiUtils = wikiUtils;
		this.engine = engine;
		this.uriInfo = uriInfo;
		this.wiki = wiki;
		functionMap = new Hashtable<String, String>();
	}

	public void addFunctionName(String wikiName, String fnName)
	{
		functionMap.put(wikiName, fnName);
	}

	public Wiki get(String title)
	{
		Wiki w = wikiUtils.getByTitle(title);
		return w;
	}

	public void hash(String title)
	{
		Wiki w = wikiUtils.getByTitle(title);
		hash(w);
	}

	public void hash(Wiki w)
	{

		wikiUtils.collectJS(w, engine, this);

		Invocable inv = (Invocable) engine;
		String functionName = wikiUtils.functionNameOf(w);

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
	}

	public void resolveAndInvoke(String name, Object... args)
	{
		String fn = functionMap.get(name);
		System.out.println("invoking: " + name + " = " + fn);

		Invocable inv = (Invocable) engine;
		try
		{
			inv.invokeFunction(fn, args);
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

	}

	public Wiki me()
	{
		return wiki;
	}

	public String param(String param)
	{
		return uriInfo.getQueryParameters().getFirst(param);
	}

	public String escape(String markup)
	{
		String result = markup.replaceAll("<", "&lt;").replaceAll(">", "&gt;")
				.replaceAll("\\|", "&#124;").replaceAll("\n", "<BR>");

		return result;
	}

	public WikiCollection getByTag(String tag)
	{
		WikiCollection col = new WikiCollection();

		List<Wiki> ws = wikiUtils.getByTag(tag);

		for (int i = 0; i < ws.size(); i++)
		{
			col.add(ws.get(i));
		}

		return col;
	}

}
