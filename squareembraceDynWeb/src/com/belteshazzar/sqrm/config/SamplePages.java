package com.belteshazzar.sqrm.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.belteshazzar.sqrm.json.WikiRequestJSON;
import com.belteshazzar.wiki.WikiUtils;

@Singleton
@Startup
public class SamplePages
{
	// use the rest interface bean
	@EJB
	WikiUtils wikiUtils;

	// TODO: this should be externalised somehow, configurable
	@PostConstruct
	private void init()
	{
		load("create");
//		load("update");
//		load("blog");
//		load("blogpost1");
//		load("blogpost1", 2);
//		load("blogpost2");
//		load("login");
	}

	private void load(String name)
	{
		create(name);
	}

	private void load(String name, int version)
	{
		update(name, version);
	}

	private void update(String name, int version)
	{
		if (wikiUtils.read(name) == null) return;

		try
		{
			InputStream is = getClass().getClassLoader().getResourceAsStream(
					"com/belteshazzar/sqrm/config/" + name + "v" + version
							+ ".txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder contents = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null)
			{
				contents.append(line);
				contents.append('\n');
			}
			is.close();

			WikiRequestJSON cwr = new WikiRequestJSON();
			cwr.body = contents.toString();
			cwr.note = "auto generated";
			wikiUtils.update(name, cwr);
		}
		catch (IOException ioex)
		{
			// TODO:
		}
	}

	/**
	 * Create the specified wiki page from the file source with the same name.
	 * 
	 * @param name
	 */
	private void create(String name)
	{
		if (wikiUtils.read(name) != null) return;

		try
		{
			InputStream is = getClass().getClassLoader().getResourceAsStream(
					"com/belteshazzar/sqrm/config/" + name + ".txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder contents = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null)
			{
				contents.append(line);
				contents.append('\n');
			}
			is.close();

			WikiRequestJSON cwr = new WikiRequestJSON();
			cwr.title = name;
			cwr.body = contents.toString();
			cwr.note = "auto generated";

			wikiUtils.create(cwr);
		}
		catch (IOException ioex)
		{
			// TODO:
		}
	}

}