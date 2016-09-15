package com.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test
{

	public static void main(String[] args) throws IOException
	{
		ArrayList<String> a = new ArrayList<>();
		a.add("sss");
		a.add("ddd");
		String [] t = new String[1];
		String[] s =  a.toArray(t);
		for (String string : s)
		{
			System.out.println(string);
		}

		for (String string : t)
		{
			System.out.println(string);
		}
	}

}
