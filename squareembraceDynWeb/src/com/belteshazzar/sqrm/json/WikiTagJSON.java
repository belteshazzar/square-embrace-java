package com.belteshazzar.sqrm.json;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="WikiTag")
public class WikiTagJSON
{
	public String name;
	public String value;
	
	public WikiTagJSON()
	{	
	}

	public WikiTagJSON(String name, String value)
	{
		this.name = name;
		this.value = value;
	}
}