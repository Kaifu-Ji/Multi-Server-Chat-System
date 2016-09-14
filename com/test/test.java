package com.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test
{

	public static void main(String[] args)
	{
		Pattern nameRule = Pattern.compile("^[a-zA-z]\\w{2,15}$");
		Matcher m = nameRule.matcher("adss1ad");
		System.out.println(m.find());

	}

}
