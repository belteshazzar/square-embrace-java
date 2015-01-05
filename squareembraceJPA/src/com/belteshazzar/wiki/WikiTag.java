package com.belteshazzar.wiki;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "WikiTag")
@Table(name = "WIKI_TAG", uniqueConstraints = @UniqueConstraint(columnNames =
{ "UUID" }))
@XmlRootElement(name = "WikiTag")
public class WikiTag
{
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "UUID", nullable = false)
	private String uuid = UUID.randomUUID().toString();

	// @Column(name="WIKI_UUID")
	// private String wikiUUID;
	//
	// @Column(name="WIKI_REVISION")
	// private int wikiRevision;

	@ManyToOne(optional = false)
	@JoinColumns(
	{
			@JoinColumn(name = "WIKI_TITLE", referencedColumnName = "TITLE"),
			@JoinColumn(name = "WIKI_REVISION",
					referencedColumnName = "REVISION") })
	private Wiki wiki;

	private String tag = null;

	private String value = null;

	private boolean isSystem;

	public WikiTag()
	{

	}

	public WikiTag(Wiki wiki, String tag)
	{
		this(wiki, tag, null);
	}

	public WikiTag(Wiki wiki, String tag, String value)
	{
		this(wiki, tag, value, false);
	}

	public WikiTag(Wiki wiki, String tag, String value, boolean isSystem)
	{
		this.setWiki(wiki);
		this.setTag(tag);
		this.setValue(value);
		this.setSystem(isSystem);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public Wiki getWiki()
	{
		return wiki;
	}

	public void setWiki(Wiki wiki)
	{
		this.wiki = wiki;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public String getTag()
	{
		return tag;
	}

	public void setTag(String tag)
	{
		this.tag = tag;
	}

	public boolean isSystem()
	{
		return isSystem;
	}

	public void setSystem(boolean isSystem)
	{
		this.isSystem = isSystem;
	}
}
