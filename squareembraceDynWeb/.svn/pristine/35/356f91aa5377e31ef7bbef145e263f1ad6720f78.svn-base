package com.belteshazzar.sqrm.json;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.belteshazzar.wiki.Wiki;
import com.belteshazzar.wiki.WikiTag;

@XmlRootElement(name="Wiki")
public class WikiJSON
{
    public String title;
    public int revision;
    public String body;
    public String note;
    public List<WikiTagJSON> tags;
    
    public long generatedIn;
    public String html;
    
    public WikiJSON()
    {
    	
    }

	public WikiJSON( Wiki wiki )
	{
		title = wiki.getTitle();
		revision = wiki.getRevision();
		body = wiki.getBody();
		note = wiki.getNote();
		tags = new LinkedList<WikiTagJSON>();
		for (WikiTag tag : wiki.getTags())
		{
			tags.add(new WikiTagJSON(tag.getTag(),tag.getValue()));
		}
	}

}
