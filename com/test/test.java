package com.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test
{

	public static void main(String[] args) throws IOException
	{
		File f = new File("servers_conf.txt");
		BufferedReader configReader = new BufferedReader(new FileReader(f));
		String info = configReader.readLine();
		System.out.println(info);

	}

}
