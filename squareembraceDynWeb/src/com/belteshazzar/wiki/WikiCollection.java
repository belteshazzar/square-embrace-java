package com.belteshazzar.wiki;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class WikiCollection extends LinkedList<Wiki>
{
	private static final long serialVersionUID = -1557249125909922904L;

	public WikiCollection()
	{
		super();
	}

	public int count()
	{
		return this.size();
	}

	public WikiCollection sortByCreatedAt()
	{
		Collections.sort(this, CREATED_AT_COMPARATOR);
		return this;
	}

	public WikiCollection sortByRevisedAt()
	{
		Collections.sort(this, REVISED_AT_COMPARATOR);
		return this;
	}

	private static final Comparator<Wiki> CREATED_AT_COMPARATOR = new Comparator<Wiki>() {
		@Override
		public int compare(Wiki w1, Wiki w2)
		{
			return w2.getCreatedAt().compareTo(w1.getCreatedAt());
		}
	};

	private static final Comparator<Wiki> REVISED_AT_COMPARATOR = new Comparator<Wiki>() {
		@Override
		public int compare(Wiki w1, Wiki w2)
		{
			return w2.getRevisedAt().compareTo(w1.getRevisedAt());
		}

	};
}
