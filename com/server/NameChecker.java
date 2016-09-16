package com.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameChecker
{
	static Pattern nameRule = Pattern.compile("^[a-zA-z]\\w{2,15}$");
	
	public static boolean nameCheck(String name)
	{
		Matcher m = nameRule.matcher(name);
		return m.find();
	}
}
