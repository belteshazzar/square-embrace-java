package com.belteshazzar.sqrm.json;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="SearchResults")
public class SearchResultsJSON
{
    public long generatedIn;
    public int count;
    public List<SearchResultJSON> results;
    
    public SearchResultsJSON()
    {
    }
}
