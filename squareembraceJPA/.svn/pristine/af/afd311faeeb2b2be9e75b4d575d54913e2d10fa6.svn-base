package com.belteshazzar.wiki;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "Wiki")
@Table(name = "WIKI")
@XmlRootElement(name = "Wiki")
public class Wiki
{
	@EmbeddedId
	private WikiPK id;

	@Lob
	private String body;

	private String note;

	@Basic
	@Temporal(value = TemporalType.DATE)
	private Date createdAt;

	@Basic
	@Temporal(value = TemporalType.DATE)
	private Date revisedAt;

	@OneToMany(mappedBy = "wiki", cascade = CascadeType.PERSIST)
	private List<WikiTag> tags;

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Wiki[");
		sb.append(this.getTitle());
		sb.append("/");
		sb.append(this.getRevision());
		sb.append(",");
		sb.append(this.getBody());
		sb.append(",");
		sb.append(this.getNote());
		sb.append(",");
		sb.append(this.getCreatedAt());
		sb.append(",");
		sb.append(this.getRevisedAt());
		sb.append("]");

		return sb.toString();
	}

	public Wiki()
	{

	}

	public WikiPK getId()
	{
		return id;
	}

	public void setId(WikiPK id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return id.getTitle();
	}

	public int getRevision()
	{
		return id.getRevision();
	}

	public int nextRevision()
	{
		id.setRevision(id.getRevision() + 1);
		return id.getRevision();
	}

	public String getBody()
	{
		return body;
	}

	public void setBody(String body)
	{
		this.body = body;
	}

	public String getNote()
	{
		return note;
	}

	public void setNote(String note)
	{
		this.note = note;
	}

	public Date getCreatedAt()
	{
		return createdAt;
	}

	public void setCreatedAt(Date createdAt)
	{
		this.createdAt = createdAt;
	}

	public Date getRevisedAt()
	{
		return revisedAt;
	}

	public void setRevisedAt(Date revisedAt)
	{
		this.revisedAt = revisedAt;
	}

	public List<WikiTag> getTags()
	{
		return tags;
	}

	public void setTags(List<WikiTag> tags)
	{
		this.tags = tags;
	}

}
