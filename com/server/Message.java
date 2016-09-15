package com.server;

public class Message
{
	JsonOperator jsonOperator;
	String identiyt;
	
	public Message(String jsonString, String identiyt)
	{
		super();
		try
		{
			this.jsonOperator = new JsonOperator(jsonString);
			this.identiyt = identiyt;
		} catch (Exception e)
		{
			// TODO: handle exception
		}
	}
	
}
