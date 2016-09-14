package com.server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonOperator
{
	JSONObject job;
	String type;
	public JSONObject getJSonObject()
	{
		return job;
	}
	public String getType()
	{
		return type;
	}
	public JsonOperator(String JsonString) throws ParseException
	{
		JSONParser jParser = new JSONParser();
		this.job = (JSONObject) jParser.parse(JsonString);
		this.type = (String) job.get("type");
	}
	public String get(String key)
	{
		return (String) job.get(key);
	}
	public static String responseNewIdentity(String name,boolean canJoin)
	{
		JSONObject jb = new JSONObject();
		jb.put("type", "newidentity");
		jb.put("approved", canJoin?"true":"false");
		return jb.toJSONString();
	}
	public static String lockMessage(String clientName,String serverName)
	{
		JSONObject jb = new JSONObject();
		jb.put("type", "lockidentity");
		jb.put("serverid", serverName);
		jb.put("identity", clientName);
		return jb.toJSONString();
	}
	
}
