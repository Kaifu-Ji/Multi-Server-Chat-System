package com.server;

public class Message
{
	JsonOperator jsonOperator;
	String identity;
	
	public Message(String jsonString, String identiyt)
	{
		super();
		try
		{
			this.jsonOperator = new JsonOperator(jsonString);
			this.identity = identiyt;
		} catch (Exception e)
		{
			// TODO: handle exception
		}
	}
	
}
