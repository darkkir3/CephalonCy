package me.darkkir3.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils 
{
	public static String multiplyEffectNumbers(String value, float multiplier)
	{
		StringBuilder description = new StringBuilder(value);
		    
		//extract all decimals
		Pattern pattern = Pattern.compile("(\\d+(?:\\.\\d+)?)");
		Matcher matcher = pattern.matcher(description);
		
		int end = 0;
		while (matcher.find(end)) 
		{
			String match = matcher.group();
			double numberFound = Double.valueOf(match);
		    numberFound *= multiplier;
		    int i = (int) numberFound;
		    
		    description = description.replace(matcher.start(), matcher.end(), numberFound == i ? String.valueOf(i) : String.valueOf(numberFound));
		    end = matcher.end() + 3; //extra space for special chars
		    if(end >= description.length())
		    {
		    	break;
		    }
		 }
		 
		
		return description.toString();
	}
	
	public static ArrayList<Double> extractNumbers(String value)
	{
		ArrayList<Double> numbersFound = new ArrayList<Double>();
		StringBuilder description = new StringBuilder(value);
	    
		//extract all decimals
		Pattern pattern = Pattern.compile("(\\d+(?:\\.\\d+)?)");
		Matcher matcher = pattern.matcher(description);
		
		int end = 0;
		while (matcher.find(end)) 
		{
			String match = matcher.group();
			numbersFound.add(Double.valueOf(match));
		    end = matcher.end() + 1;
		 }
		 
		
		return numbersFound;
	}
	
	public static String adjustEffectNumbers(String value, ArrayList<Double> additions)
	{
		StringBuilder description = new StringBuilder(value);
		    
		//extract all decimals
		Pattern pattern = Pattern.compile("(\\d+(?:\\.\\d+)?)");
		Matcher matcher = pattern.matcher(description);
		
		int end = 0;
		int c = 0;
		while (c < additions.size() && matcher.find(end)) 
		{
			String match = matcher.group();
			double numberFound = Double.valueOf(match);
			if(c < additions.size())
			{
				numberFound += additions.get(c++);
			}
		    int i = (int) numberFound;
		    
		    description = description.replace(matcher.start(), matcher.end(), numberFound == i ? String.valueOf(i) : String.valueOf(numberFound));
		    end = matcher.end() + 1;
		    c++;
		 }
		 
		
		return description.toString();
	}
}
