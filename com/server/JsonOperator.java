package com.server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@SuppressWarnings("unchecked")
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

	public static String responseNewIdentity(String name, boolean canJoin)
	{
		JSONObject jb = new JSONObject();
		jb.put("type", "newidentity");
		jb.put("approved", canJoin ? "true" : "false");
		return jb.toJSONString();
	}

	public static String lockIdentity(String clientName, String serverName)
	{
		JSONObject jb = new JSONObject();
		jb.put("type", "lockidentity");
		jb.put("serverid", serverName);
		jb.put("identity", clientName);
		return jb.toJSONString();
	}

	public static String lockRoom(String roomName, String serverName)
	{
		JSONObject jb = new JSONObject();
		jb.put("type", "lockroomid");
		jb.put("serverid", serverName);
		jb.put("roomid", roomName);
		return jb.toJSONString();
	}

	public static String releaseIdentity(String clientName, String serverName)
	{
		JSONObject jb = new JSONObject();
		jb.put("type", "releaseidentity");
		jb.put("serverid", serverName);
		jb.put("identity", clientName);
		return jb.toJSONString();
	}

	// {"type" : "releaseroomid", "serverid" : "s1", "roomid" : "jokes",
	// "approved":"true"}
	public static String releaseRoomID(String serverName, String roomName, boolean approved)
	{
		JSONObject jb = new JSONObject();
		jb.put("type", "releaseroomid");
		jb.put("serverid", serverName);
		jb.put("roomid", roomName);
		jb.put("approved", approved ? "true" : "false");
		return jb.toJSONString();
	}

	public static String roomChange(String clientname, String from, String to)
	{
		JSONObject result = new JSONObject();
		result.put("type", "roomchange");
		result.put("identity", clientname);
		result.put("former", from);
		result.put("roomid", to);
		return result.toJSONString();
	}

	public static String responseLockIdentity(String clientName, String serverName, boolean locked)
	{
		JSONObject jb = new JSONObject();
		jb.put("type", "lockidentity");
		jb.put("serverid", serverName);
		jb.put("identity", clientName);
		jb.put("locked", locked ? "true" : "false");
		return jb.toJSONString();
	}

	// {"type" : "lockroomid", "serverid" : "s2", "roomid" : "jokes", "locked" :
	// "false"}
	public static String responseLockRoom(String roomName, String serverName, boolean locked)
	{
		JSONObject jb = new JSONObject();
		jb.put("type", "lockroomid");
		jb.put("serverid", serverName);
		jb.put("roomid", roomName);
		jb.put("locked", locked ? "true" : "false");
		return jb.toJSONString();
	}

	public static String constructList(String[] rooms)
	{
		JSONObject jb = new JSONObject();
		JSONArray room = new JSONArray();
		for (String s : rooms)
		{
			room.add(s);
		}
		jb.put("type", "roomlist");
		jb.put("rooms", room);
		return jb.toJSONString();
	}

	/*
	 * { "type" : "roomcontents", "roomid" : "jokes", "identities" :
	 * ["Adel","Chenhao","Maria"], "owner" : "Adel" }
	 */
	public static String who(String[] clientInRoom, String roomName, String roomOnwer)
	{
		JSONObject jb = new JSONObject();
		JSONArray clientsInRoom = new JSONArray();
		for (String client : clientInRoom)
		{
			clientsInRoom.add(client);
		}
		jb.put("type", "roomcontents");
		jb.put("roomid", roomName);
		jb.put("identities", clientsInRoom);
		jb.put("owner", roomOnwer);
		return jb.toJSONString();
	}

	// {"type" : "createroom", "roomid" : "jokes", "approved" : "false"}
	public static String createRoom(String roomName, boolean approved)
	{
		JSONObject jb = new JSONObject();
		jb.put("type", "createroom");
		jb.put("roomid", roomName);
		jb.put("approved", approved ? "true" : "false");
		return jb.toJSONString();
	}

}
