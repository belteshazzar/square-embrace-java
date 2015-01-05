package com.belteshazzar.sqrm.json;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="SearchResult")
public class SearchResultJSON
{
	public String title;
	public int revision;
	public float score;
	
	public SearchResultJSON()
	{
		
	}

	public SearchResultJSON(String title, int revision,float score)
	{
		this.title = title;
		this.revision = revision;
		this.score = score;
	}
}
