package com.belteshazzar.wiki;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class WikiPK implements Serializable
{
	private static final long	serialVersionUID	= -7530056015214570294L;

	@Column(name = "TITLE", nullable = false)
	private String title;

    @Column(name = "REVISION", nullable = false)
    private int revision;
    
    public WikiPK()
    {
    }
    
    public int hashCode()
    {
    	return (title==null?0:title.hashCode()) + revision;
    }
    
    public boolean equals(Object o)
    {
    	if (o==null) return false;
    	if (!(o instanceof WikiPK)) return false;

    	WikiPK pk = (WikiPK)o;
		return pk.title.equals(title) && pk.revision == revision;
    }

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public int getRevision()
	{
		return revision;
	}

	public void setRevision(int revision)
	{
		this.revision = revision;
	}
}
