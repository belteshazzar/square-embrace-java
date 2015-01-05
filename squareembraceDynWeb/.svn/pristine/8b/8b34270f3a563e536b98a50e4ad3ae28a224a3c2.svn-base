package com.belteshazzar.sqrm;

public enum SqrmError
{

	INVALID_EMAIL(100, "Invalid email provide"), TITLE_REQUIRED, BODY_REQUIRED, NOTE_REQUIRED, INVALID_USERNAME_PASSWORD, ALREADY_LOGGED_IN;

	private int code;
	private String msg;

	private SqrmError()
	{
		this(0, null);
	}

	private SqrmError(int code, String msg)
	{
		this.code = code;
		this.msg = msg;
	}

	public int getCode()
	{
		return code;
	}

	public String getMessage()
	{
		return msg;
	}
}
