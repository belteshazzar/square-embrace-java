package com.belteshazzar.wiki;

public enum WikiType
{
	SERVER,CLIENT_PRE,CLIENT_INLINE,CLIENT_POST;

	public static WikiType forVar(String varName)
	{
		switch (varName.length())
		{
			case 3: return (varName.equalsIgnoreCase("pre")?CLIENT_PRE:SERVER);
			case 4: return (varName.equalsIgnoreCase("post")?CLIENT_POST:SERVER);
			case 6: return (varName.equalsIgnoreCase("inline")?CLIENT_INLINE:SERVER);
			default: return SERVER;
		}
	}
}
