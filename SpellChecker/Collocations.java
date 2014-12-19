import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.Map.Entry;

public class Collocations {
	HashMap<String, Double> one_gram_probabilities = new HashMap<String, Double>();
	HashMap<String, Double> two_gram_probabilities = new HashMap<String, Double>();
	HashMap<String, Double> three_gram_probabilities = new HashMap<String, Double>();
	HashMap<String, Double> four_gram_probabilities = new HashMap<String, Double>();
	HashMap<String, Double> five_gram_probabilities = new HashMap<String, Double>();
	
	LinkedHashMap<String, LinkedHashMap<String, Double>> wordCollocations2gram = new LinkedHashMap<String, LinkedHashMap<String, Double>>();
	LinkedHashMap<String, LinkedHashMap<String, Double>> wordCollocations3gram = new LinkedHashMap<String, LinkedHashMap<String, Double>>();
	LinkedHashMap<String, LinkedHashMap<String, Double>> wordCollocations4gram = new LinkedHashMap<String, LinkedHashMap<String, Double>>();
	
	LinkedHashMap<String, BigInteger> word2gram = new LinkedHashMap<String, BigInteger>();
	LinkedHashMap<String, BigInteger> word3gram = new LinkedHashMap<String, BigInteger>();
	LinkedHashMap<String, BigInteger> word4gram = new LinkedHashMap<String, BigInteger>();
	
	LinkedHashMap<String, LinkedHashMap<String, Integer>> wordPOS2gram = new LinkedHashMap<String, LinkedHashMap<String, Integer>>();
	LinkedHashMap<String, LinkedHashMap<String, Integer>> wordPOS3gram = new LinkedHashMap<String, LinkedHashMap<String, Integer>>();
	LinkedHashMap<String, LinkedHashMap<String, Integer>> wordPOS4gram = new LinkedHashMap<String, LinkedHashMap<String, Integer>>();
	
	LinkedHashMap<Integer, ArrayList<String>> list = new LinkedHashMap<Integer, ArrayList<String>>();
	CandidateGeneration candidategeneration = new CandidateGeneration();
	HashMap<String, Double> probabilities = new HashMap<String, Double>();
	WordSpellCheck wordspellcheck = new WordSpellCheck();
	String correct_sent = new String();
	String correct_tuple = new String();
	
	double MRR1 = 0.0;
	double MRR2 = 0.0;
	
	long total1 = 0;
	long total2 = 0;
	long total3 = 0;
	
	Collocations()
	{
		candidategeneration.partitionsInDictionary();
		wordspellcheck.read_dictionary();
		wordspellcheck.read_matrix();
		wordspellcheck.generate_unigrams();
	}
	
	public void preprocessing2gramDictionary()
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("sensPOS2gram.txt"));
			String Line;
			long total = 0;
			while ((Line = br.readLine()) != null)
			{
				String[] array = Line.split("\\s+");
				total = total + Long.parseLong(array[0]);
			}
			total1 = total;
			br.close();
			br = new BufferedReader(new FileReader("sensPOS2gram.txt"));
			while ((Line = br.readLine()) != null)
			{
				String[] array = Line.split("\\s+");
				String ngram = new String();
				ngram = array[1].toLowerCase() + "_" + array[2].toLowerCase();
				BigInteger temp3 = new BigInteger(array[0]) ;
				
				if(word2gram.containsKey(ngram))
				{
					word2gram.put(ngram, word2gram.get(ngram).add(temp3));
				}
				else
					word2gram.put(ngram, temp3);
				for(int i = 1; i <= 2; i++)
				{
					if(wordPOS2gram.containsKey(array[i].toLowerCase()))
					{
						if(wordPOS2gram.get(array[i].toLowerCase()).containsKey(array[i+2]))
						{
							int temp = wordPOS2gram.get(array[i].toLowerCase()).get(array[i+2]) + Integer.parseInt(array[0]);
							int temp1 = wordPOS2gram.get(array[i].toLowerCase()).get("@") + Integer.parseInt(array[0]);
							wordPOS2gram.get(array[i].toLowerCase()).put("@", temp1);
							wordPOS2gram.get(array[i].toLowerCase()).put(array[i+2], temp);
						}
						else
						{
							int temp = wordPOS2gram.get(array[i].toLowerCase()).get("@") + Integer.parseInt(array[0]);
							wordPOS2gram.get(array[i].toLowerCase()).put("@", temp);
							wordPOS2gram.get(array[i].toLowerCase()).put(array[i+2], Integer.parseInt(array[0]));
						}
					}
					else
					{
						LinkedHashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
						temp.put("@", Integer.parseInt(array[0]));
						temp.put(array[i+2].toLowerCase(), Integer.parseInt(array[0]));
						wordPOS2gram.put(array[i].toLowerCase(), temp);
					}
					if(wordCollocations2gram.containsKey(array[i].toLowerCase()))
					{
						String test = new String();
						if(i == 1)
							test = "_ "+array[i+3];
						else if(i == 2)
							test = array[i+1] + " _";
						if(wordCollocations2gram.get(array[i].toLowerCase()).containsKey(test))
						{
							double temp = wordCollocations2gram.get(array[i].toLowerCase()).get(test) + (double)Integer.parseInt(array[0])/total;
							wordCollocations2gram.get(array[i].toLowerCase()).put(test, temp);
						}
						else
						{
							wordCollocations2gram.get(array[i].toLowerCase()).put(test, (double)Integer.parseInt(array[0])/total);
						}
					}
					else
					{
						LinkedHashMap<String, Double> temp = new LinkedHashMap<String, Double>();
						String str = new String();
						if(i == 1)
							str = "_ " + array[i+3];
						else if(i == 2)
							str = array[i+1] + " _";
						temp.put(str, (double)Integer.parseInt(array[0])/total);
						wordCollocations2gram.put(array[i].toLowerCase(), temp);
					}
				}
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public void preprocessing3gramDictionary()
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("sensPOS3gram.txt"));
			String Line;
			long total = 0;
			while ((Line = br.readLine()) != null)
			{
				String[] array = Line.split("\\s+");
				total = total + Long.parseLong(array[0]);
			}
			total2 = total;
			br.close();
			br = new BufferedReader(new FileReader("sensPOS3gram.txt"));
			while ((Line = br.readLine()) != null)
			{
				String[] array = Line.split("\\s+");
				String ngram = new String();
				ngram = array[1].toLowerCase() + "_" + array[2].toLowerCase() + "_" + array[3].toLowerCase();
				BigInteger temp3 = new BigInteger((array[0]));
				if(word3gram.containsKey(ngram))
				{
					
					word3gram.put(ngram, word3gram.get(ngram).add(temp3));
				}
				else
					word3gram.put(ngram, (temp3));
				for(int i = 1; i <= 3; i++)
				{
					if(wordPOS3gram.containsKey(array[i].toLowerCase()))
					{
						if(wordPOS3gram.get(array[i].toLowerCase()).containsKey(array[i+3]))
						{
							int temp = wordPOS3gram.get(array[i].toLowerCase()).get(array[i+3]) + Integer.parseInt(array[0]);
							int temp1 = wordPOS3gram.get(array[i].toLowerCase()).get("@") + Integer.parseInt(array[0]);
							wordPOS3gram.get(array[i].toLowerCase()).put("@", temp1);
							wordPOS3gram.get(array[i].toLowerCase()).put(array[i+3], temp);
						}
						else
						{
							int temp = wordPOS3gram.get(array[i].toLowerCase()).get("@") + Integer.parseInt(array[0]);
							wordPOS3gram.get(array[i].toLowerCase()).put("@", temp);
							wordPOS3gram.get(array[i].toLowerCase()).put(array[i+3], Integer.parseInt(array[0]));
						}
					}
					else
					{
						LinkedHashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
						temp.put("@", Integer.parseInt(array[0]));
						temp.put(array[i+3].toLowerCase(), Integer.parseInt(array[0]));
						wordPOS3gram.put(array[i].toLowerCase(), temp);
					}
					if(wordCollocations3gram.containsKey(array[i].toLowerCase()))
					{
						String test = new String();
						if(i == 1)
							test = "_ "+array[i+4]+" "+array[i+5];
						else if(i == 2)
							test = array[i+2] + " _ "+array[i+4];
						else
							test = array[i+1] + " " + array[i+2] +" _";
						if(wordCollocations3gram.get(array[i].toLowerCase()).containsKey(test))
						{
							double temp = wordCollocations3gram.get(array[i].toLowerCase()).get(test) + (double)Integer.parseInt(array[0])/total;
							wordCollocations3gram.get(array[i].toLowerCase()).put(test, temp);
						}
						else
						{
							wordCollocations3gram.get(array[i].toLowerCase()).put(test, (double)Integer.parseInt(array[0])/total);
						}
					}
					else
					{
						LinkedHashMap<String, Double> temp = new LinkedHashMap<String, Double>();
						String str = new String();
						if(i == 1)
							str = "_ "+array[i+4]+" "+array[i+5];
						else if(i == 2)
							str = array[i+2] + " _ "+array[i+4];
						else
							str = array[i+1] + " " + array[i+2] +" _";
						temp.put(str, (double)Integer.parseInt(array[0])/total);
						wordCollocations3gram.put(array[i].toLowerCase(), temp);
					}
				}
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public void preprocessing4gramDictionary()
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("sensPOS4gram.txt"));
			String Line;
			long total = 0;
			while ((Line = br.readLine()) != null)
			{
				String[] array = Line.split("\\s+");
				total = total + Long.parseLong(array[0]);
			}
			total3 = total;
			br.close();
			br = new BufferedReader(new FileReader("sensPOS4gram.txt"));
			while ((Line = br.readLine()) != null)
			{
				String[] array = Line.split("\\s+");
				String ngram = new String();
				ngram = array[1].toLowerCase() + "_" + array[2].toLowerCase() + "_" + array[3].toLowerCase() + "_" + array[4].toLowerCase();
				BigInteger temp3 = new BigInteger(array[0]);
				if(word4gram.containsKey(ngram))
				{
					
					word4gram.put(ngram, word4gram.get(ngram).add(temp3));
				}
				else
					word4gram.put(ngram, temp3);
				for(int i = 1; i <= 4; i++)
				{
					if(wordPOS4gram.containsKey(array[i].toLowerCase()))
					{
						if(wordPOS4gram.get(array[i].toLowerCase()).containsKey(array[i+4]))
						{
							int temp = wordPOS4gram.get(array[i].toLowerCase()).get(array[i+4]) + Integer.parseInt(array[0]);
							int temp1 = wordPOS4gram.get(array[i].toLowerCase()).get("@") + Integer.parseInt(array[0]);
							wordPOS4gram.get(array[i].toLowerCase()).put("@", temp1);
							wordPOS4gram.get(array[i].toLowerCase()).put(array[i+4], temp);
						}
						else
						{
							int temp = wordPOS4gram.get(array[i].toLowerCase()).get("@") + Integer.parseInt(array[0]);
							wordPOS4gram.get(array[i].toLowerCase()).put("@", temp);
							wordPOS4gram.get(array[i].toLowerCase()).put(array[i+4], Integer.parseInt(array[0]));
						}
					}
					else
					{
						LinkedHashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
						temp.put("@", Integer.parseInt(array[0]));
						temp.put(array[i+4].toLowerCase(), Integer.parseInt(array[0]));
						wordPOS4gram.put(array[i].toLowerCase(), temp);
					}
					if(wordCollocations4gram.containsKey(array[i].toLowerCase()))
					{
						String test = new String();
						if(i == 1)
							test = "_ "+array[i+5]+" "+array[i+6] + " "+array[i+7];
						else if(i == 2)
							test = array[i+3] + " _ "+array[i+5] + " " + array[i+6];
						else if(i == 3)
							test = array[i+2] + " " + array[i+3] +" _ "+ array[i+5];
						else
							test = array[i+1] +" "+array[i+2] + " "+array[i+3]+" _" ;
						if(wordCollocations4gram.get(array[i].toLowerCase()).containsKey(test))
						{
							double temp = wordCollocations4gram.get(array[i].toLowerCase()).get(test) + (double)Integer.parseInt(array[0])/total;
							wordCollocations4gram.get(array[i].toLowerCase()).put(test, temp);
						}
						else
						{
							wordCollocations4gram.get(array[i].toLowerCase()).put(test, (double)Integer.parseInt(array[0])/total);
						}
					}
					else
					{
						LinkedHashMap<String, Double> temp = new LinkedHashMap<String, Double>();
						String str = new String();
						if(i == 1)
							str = "_ "+array[i+5]+" "+array[i+6] + " "+array[i+7];
						else if(i == 2)
							str = array[i+3] + " _ "+array[i+5] + " " + array[i+6];
						else if(i == 3)
							str = array[i+2] + " " + array[i+3] +" _ "+ array[i+5];
						else
							str = array[i+1] +" "+array[i+2] + " "+array[i+3]+" _" ;
						temp.put(str, (double)Integer.parseInt(array[0])/total);
						wordCollocations4gram.put(array[i].toLowerCase(), temp);
					}
				}
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public void POStagging2Gram(String[] queryWords, int wordPosition, HashMap<String, Double> search1)
	{
		LinkedHashMap<String, Integer> temp;
		if(wordPosition != 0)
		{
			if(wordPOS2gram.containsKey(queryWords[wordPosition-1]))
			{
				temp = wordPOS2gram.get(queryWords[wordPosition-1]);
				int den = temp.get("@");
				Iterator it = temp.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pairs = (Map.Entry)it.next();
					search1.put(pairs.getKey().toString()+" _", (double)Integer.parseInt(pairs.getValue().toString())/den);
				}
			}
		}
		if(wordPosition != queryWords.length-1)
		{
			if(wordPOS2gram.containsKey(queryWords[wordPosition+1]))
			{
				temp = wordPOS2gram.get(queryWords[wordPosition+1]);
				int den = temp.get("@");
				Iterator it = temp.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pairs = (Map.Entry)it.next();
					search1.put("_ " + pairs.getKey().toString(), (double)Integer.parseInt(pairs.getValue().toString())/den);
				}
			}
		}
	}
	
	public void POStagging3Gram(String[] queryWords, int wordPosition, HashMap<String, Double> search2)
	{
		LinkedHashMap<String, Integer> temp1;
		LinkedHashMap<String, Integer> temp2;
		if(wordPosition >= 2)
		{
			if(wordPOS3gram.containsKey(queryWords[wordPosition-1]) && wordPOS3gram.containsKey(queryWords[wordPosition-2]))
			{
				temp1 = wordPOS3gram.get(queryWords[wordPosition-1]);
				temp2 = wordPOS3gram.get(queryWords[wordPosition-2]);
				int den1 = temp1.get("@");
				int den2 = temp2.get("@");
				Iterator it1 = temp1.entrySet().iterator();
				while (it1.hasNext()) {
					Map.Entry pairs1 = (Map.Entry)it1.next();
					Iterator it2 = temp2.entrySet().iterator();
					while (it2.hasNext()) {
						Map.Entry pairs2 = (Map.Entry)it2.next();
						search2.put(pairs2.getKey().toString() + " " + pairs1.getKey().toString() + " _"
								, ((double)Integer.parseInt(pairs2.getValue().toString())/den2)*((double)Integer.parseInt(pairs1.getValue().toString())/den1));
					}
				}
			}
		}
		if(wordPosition < queryWords.length-2)
		{
			if(wordPOS3gram.containsKey(queryWords[wordPosition+1]) && wordPOS3gram.containsKey(queryWords[wordPosition+2]))
			{
				temp1 = wordPOS3gram.get(queryWords[wordPosition+1]);
				temp2 = wordPOS3gram.get(queryWords[wordPosition+2]);
				int den1 = temp1.get("@");
				int den2 = temp2.get("@");
				Iterator it1 = temp1.entrySet().iterator();
				while (it1.hasNext()) {
					Map.Entry pairs1 = (Map.Entry)it1.next();
					Iterator it2 = temp2.entrySet().iterator();
					while (it2.hasNext()) {
						Map.Entry pairs2 = (Map.Entry)it2.next();
						search2.put("_ " + pairs1.getKey().toString() + " " + pairs2.getKey().toString()
								, ((double)Integer.parseInt(pairs2.getValue().toString())/den2)*((double)Integer.parseInt(pairs1.getValue().toString())/den1));
					}
				}
			}
		}
		if(wordPosition >= 1 && wordPosition < queryWords.length-1)
		{
			if(wordPOS3gram.containsKey(queryWords[wordPosition+1]) && wordPOS3gram.containsKey(queryWords[wordPosition-1]))
			{
				temp1 = wordPOS3gram.get(queryWords[wordPosition-1]);
				temp2 = wordPOS3gram.get(queryWords[wordPosition+1]);
				int den1 = temp1.get("@");
				int den2 = temp2.get("@");
				Iterator it1 = temp1.entrySet().iterator();
				while (it1.hasNext()) {
					Map.Entry pairs1 = (Map.Entry)it1.next();
					Iterator it2 = temp2.entrySet().iterator();
					while (it2.hasNext()) {
						Map.Entry pairs2 = (Map.Entry)it2.next();
						search2.put(pairs1.getKey().toString() + " _ " + pairs2.getKey().toString()
								, ((double)Integer.parseInt(pairs2.getValue().toString())/den2)*((double)Integer.parseInt(pairs1.getValue().toString())/den1));
					}
				}
			}
		}
	}
	
	public void POStagging4Gram(String[] queryWords, int wordPosition, HashMap<String, Double> search3)
	{
		LinkedHashMap<String, Integer> temp11;
		LinkedHashMap<String, Integer> temp21;
		LinkedHashMap<String, Integer> temp31;
		if(wordPosition >= 3)
		{
			if(wordPOS4gram.containsKey(queryWords[wordPosition-1]) && wordPOS4gram.containsKey(queryWords[wordPosition-2]) && wordPOS4gram.containsKey(queryWords[wordPosition-3]))
			{
				temp11 = wordPOS4gram.get(queryWords[wordPosition-1]);
				temp21 = wordPOS4gram.get(queryWords[wordPosition-2]);
				temp31 = wordPOS4gram.get(queryWords[wordPosition-3]);
				int den1 = temp11.get("@");
				int den2 = temp21.get("@");
				int den3 = temp31.get("@");
				Iterator it1 = temp11.entrySet().iterator();
				while (it1.hasNext()) {
					Map.Entry pairs1 = (Map.Entry)it1.next();
					Iterator it2 = temp21.entrySet().iterator();
					while (it2.hasNext()) {
						Map.Entry pairs2 = (Map.Entry)it2.next();
						Iterator it3 = temp31.entrySet().iterator();
						while (it3.hasNext()) {
							Map.Entry pairs3 = (Map.Entry)it3.next();
							search3.put(pairs3.getKey().toString() + " " + pairs2.getKey().toString() + " " + pairs1.getKey().toString() + " _"
								, ((double)Integer.parseInt(pairs3.getValue().toString())/den3)*((double)Integer.parseInt(pairs2.getValue().toString())/den2)*((double)Integer.parseInt(pairs1.getValue().toString())/den1));
						}
					}
				}
			}
		}
		if(wordPosition < queryWords.length-3)
		{
			if(wordPOS4gram.containsKey(queryWords[wordPosition+1]) && wordPOS4gram.containsKey(queryWords[wordPosition+2])
					&& wordPOS4gram.containsKey(queryWords[wordPosition+3]))
			{
				temp11 = wordPOS4gram.get(queryWords[wordPosition+1]);
				temp21 = wordPOS4gram.get(queryWords[wordPosition+2]);
				temp31 = wordPOS4gram.get(queryWords[wordPosition+3]);
				int den1 = temp11.get("@");
				int den2 = temp21.get("@");
				int den3 = temp31.get("@");
				Iterator it1 = temp11.entrySet().iterator();
				while (it1.hasNext()) {
					Map.Entry pairs1 = (Map.Entry)it1.next();
					Iterator it2 = temp21.entrySet().iterator();
					while (it2.hasNext()) {
						Map.Entry pairs2 = (Map.Entry)it2.next();
						Iterator it3 = temp31.entrySet().iterator();
						while (it3.hasNext()) {
							Map.Entry pairs3 = (Map.Entry)it3.next();
							search3.put("_ " + pairs1.getKey().toString() + " " + pairs2.getKey().toString() + " " + pairs3.getKey().toString()
								, ((double)Integer.parseInt(pairs3.getValue().toString())/den3)*((double)Integer.parseInt(pairs2.getValue().toString())/den2)*((double)Integer.parseInt(pairs1.getValue().toString())/den1));
						}
					}
				}
			}
		}
		if(wordPosition >= 1 && wordPosition < queryWords.length-2)
		{
			if(wordPOS4gram.containsKey(queryWords[wordPosition+1]) && wordPOS4gram.containsKey(queryWords[wordPosition-1])
					&& wordPOS4gram.containsKey(queryWords[wordPosition+2]))
			{
				temp11 = wordPOS4gram.get(queryWords[wordPosition-1]);
				temp21 = wordPOS4gram.get(queryWords[wordPosition+1]);
				temp31 = wordPOS4gram.get(queryWords[wordPosition+2]);
				int den1 = temp11.get("@");
				int den2 = temp21.get("@");
				int den3 = temp31.get("@");
				Iterator it1 = temp11.entrySet().iterator();
				while (it1.hasNext()) {
					Map.Entry pairs1 = (Map.Entry)it1.next();
					Iterator it2 = temp21.entrySet().iterator();
					while (it2.hasNext()) {
						Map.Entry pairs2 = (Map.Entry)it2.next();
						Iterator it3 = temp31.entrySet().iterator();
						while (it3.hasNext()) {
							Map.Entry pairs3 = (Map.Entry)it3.next();
							search3.put(pairs1.getKey().toString() + " _ " + pairs2.getKey().toString()+" "+ pairs3.getKey().toString()
								, ((double)Integer.parseInt(pairs3.getValue().toString())/den3)*((double)Integer.parseInt(pairs2.getValue().toString())/den2)*((double)Integer.parseInt(pairs1.getValue().toString())/den1));
						}
					}
				}
			}
		}
		if(wordPosition >= 2 && wordPosition < queryWords.length-1)
		{
			if(wordPOS4gram.containsKey(queryWords[wordPosition+1]) && wordPOS4gram.containsKey(queryWords[wordPosition-1])
					&& wordPOS4gram.containsKey(queryWords[wordPosition-2]))
			{
				temp11 = wordPOS4gram.get(queryWords[wordPosition-2]);
				temp21 = wordPOS4gram.get(queryWords[wordPosition-1]);
				temp31 = wordPOS4gram.get(queryWords[wordPosition+1]);
				int den1 = temp11.get("@");
				int den2 = temp21.get("@");
				int den3 = temp31.get("@");
				Iterator it1 = temp11.entrySet().iterator();
				while (it1.hasNext()) {
					Map.Entry pairs1 = (Map.Entry)it1.next();
					Iterator it2 = temp21.entrySet().iterator();
					while (it2.hasNext()) {
						Map.Entry pairs2 = (Map.Entry)it2.next();
						Iterator it3 = temp31.entrySet().iterator();
						while (it3.hasNext()) {
							Map.Entry pairs3 = (Map.Entry)it3.next();
							search3.put(pairs1.getKey().toString() + " " + pairs2.getKey().toString()+" _ "+ pairs3.getKey().toString()
								, ((double)Integer.parseInt(pairs3.getValue().toString())/den3)*((double)Integer.parseInt(pairs2.getValue().toString())/den2)*((double)Integer.parseInt(pairs1.getValue().toString())/den1));
						}
					}
				}
			}
		}
	}
	
	public void findExactMatch(ArrayList<String> confusionSet, String[] queryWords, int wordPosition)
	{
		String nGram = new String();
		probabilities.clear();
		for(int i = 0; i < confusionSet.size(); i++)
		{
			double maximum = 0.0;
			probabilities.put(confusionSet.get(i), maximum);
			if(wordPosition != 0)
			{
				nGram = queryWords[wordPosition-1]+"_"+confusionSet.get(i);
				if(word2gram.containsKey(nGram))
				{
					BigInteger z = new BigInteger("2");
					BigInteger m = (word2gram.get(nGram).multiply(z)).divide(BigInteger.valueOf(total1));
					Integer a = 1;
					int e =  editDistance(queryWords[wordPosition],confusionSet.get(i));
					for(int k = 0; k < 4; k++)
						a = a*e;
					m = m.divide(BigInteger.valueOf(a.intValue()));
					
					if(m.doubleValue() > maximum )
					{
						maximum = m.doubleValue();
						probabilities.put(confusionSet.get(i), maximum);
					}
				}
			}
			if(wordPosition != queryWords.length-1)
			{
				nGram = confusionSet.get(i)+"_"+queryWords[wordPosition+1];
				if(word2gram.containsKey(nGram))
				{
					BigInteger z = new BigInteger("2");
					BigInteger m = (word2gram.get(nGram).multiply(z)).divide(BigInteger.valueOf(total1));
					Integer a = 1;
					int e =  editDistance(queryWords[wordPosition],confusionSet.get(i));
					for(int k = 0; k < 4; k++)
						a = a*e;
					m = m.divide(BigInteger.valueOf(a.intValue()));
					
					if(m.doubleValue() > maximum )
					{
						maximum = m.doubleValue();
						probabilities.put(confusionSet.get(i), maximum);
					}
				}
			}
			/*********3 gram**********/
			if(wordPosition != 0 && wordPosition != queryWords.length-1)
			{
				nGram = queryWords[wordPosition-1]+"_"+confusionSet.get(i)+"_"+queryWords[wordPosition+1];
				if(word3gram.containsKey(nGram))
				{
					BigInteger z = new BigInteger("5");
					BigInteger m = (word3gram.get(nGram).multiply(z)).divide(BigInteger.valueOf(total2));
					Integer a = 1;
					int e =  editDistance(queryWords[wordPosition],confusionSet.get(i));
					for(int k = 0; k < 4; k++)
						a = a*e;
					m = m.divide(BigInteger.valueOf(a.intValue()));
					
					if( m.doubleValue() > maximum)
					{
						maximum = m.doubleValue();
						probabilities.put(confusionSet.get(i), maximum);
					}
				}
			}
			if(wordPosition < queryWords.length-2)
			{
				nGram = confusionSet.get(i)+"_"+queryWords[wordPosition+1]+"_"+queryWords[wordPosition+2];
				if(word3gram.containsKey(nGram))
				{
					BigInteger z = new BigInteger("5");
					BigInteger m = (word3gram.get(nGram).multiply(z)).divide(BigInteger.valueOf(total2));
					Integer a = 1;
					int e =  editDistance(queryWords[wordPosition],confusionSet.get(i));
					for(int k = 0; k < 4; k++)
						a = a*e;
					m = m.divide(BigInteger.valueOf(a.intValue()));
					
					if( m.doubleValue() > maximum)
					{
						maximum = m.doubleValue();
						probabilities.put(confusionSet.get(i), maximum);
					}
				}
			}
			if(wordPosition >= 2)
			{
				nGram = queryWords[wordPosition-2]+"_"+queryWords[wordPosition-1]+"_"+confusionSet.get(i);
				if(word3gram.containsKey(nGram))
				{
					BigInteger z = new BigInteger("5");
					BigInteger m = (word3gram.get(nGram).multiply(z)).divide(BigInteger.valueOf(total2));
					Integer a = 1;
					int e =  editDistance(queryWords[wordPosition],confusionSet.get(i));
					for(int k = 0; k < 4; k++)
						a = a*e;
					m = m.divide(BigInteger.valueOf(a.intValue()));
					
					if( m.doubleValue() > maximum)
					{
						maximum = m.doubleValue();
						probabilities.put(confusionSet.get(i), maximum);
					}
				}
			}
			/*******4 gram*******/
			if(wordPosition >= 1 && wordPosition < queryWords.length-2)
			{
				nGram = queryWords[wordPosition-1]+"_"+confusionSet.get(i)+"_"+queryWords[wordPosition+1]+"_"+queryWords[wordPosition+2];
				if(word4gram.containsKey(nGram))
				{
					BigInteger z = new BigInteger("10");
					BigInteger m = (word4gram.get(nGram).multiply(z)).divide(BigInteger.valueOf(total3));
					Integer a = 1;
					int e =  editDistance(queryWords[wordPosition],confusionSet.get(i));
					for(int k = 0; k < 4; k++)
						a = a*e;
					m = m.divide(BigInteger.valueOf(a.intValue()));
					
					if( m.doubleValue() > maximum)
					{
						maximum = m.doubleValue();
						probabilities.put(confusionSet.get(i), maximum);
					}
				}
			}
			if(wordPosition >= 2 && wordPosition < queryWords.length-1)
			{
				nGram = queryWords[wordPosition-2]+"_"+queryWords[wordPosition-1]+"_"+confusionSet.get(i)+"_"+queryWords[wordPosition+1];
				if(word4gram.containsKey(nGram))
				{
					BigInteger z = new BigInteger("10");
					BigInteger m = (word4gram.get(nGram).multiply(z)).divide(BigInteger.valueOf(total3));
					Integer a = 1;
					int e =  editDistance(queryWords[wordPosition],confusionSet.get(i));
					for(int k = 0; k < 4; k++)
						a = a*e;
					m = m.divide(BigInteger.valueOf(a.intValue()));
					
					if( m.doubleValue() > maximum)
					{
						maximum = m.doubleValue();
						probabilities.put(confusionSet.get(i), maximum);
					}
				}
			}
			if(wordPosition >= 3)
			{
				nGram = queryWords[wordPosition-3]+"_"+queryWords[wordPosition-2]+"_"+queryWords[wordPosition-1]+"_"+confusionSet.get(i);
				if(word4gram.containsKey(nGram))
				{
					BigInteger z = new BigInteger("10");
					BigInteger m = (word4gram.get(nGram).multiply(z)).divide(BigInteger.valueOf(total3));
					Integer a = 1;
					int e =  editDistance(queryWords[wordPosition],confusionSet.get(i));
					for(int k = 0; k < 4; k++)
						a = a*e;
					m = m.divide(BigInteger.valueOf(a.intValue()));
					
					if( m.doubleValue() > maximum)
					{
						maximum = m.doubleValue();
						probabilities.put(confusionSet.get(i), maximum);
					}
				}
			}
			if(wordPosition < queryWords.length-3)
			{
				nGram = confusionSet.get(i)+"_"+queryWords[wordPosition+1]+"_"+queryWords[wordPosition+2]+"_"+queryWords[wordPosition+3];
				if(word4gram.containsKey(nGram))
				{
					BigInteger z = new BigInteger("10");
					BigInteger m = (word4gram.get(nGram).multiply(z)).divide(BigInteger.valueOf(total3));
					Integer a = 1;
					int e =  editDistance(queryWords[wordPosition],confusionSet.get(i));
					for(int k = 0; k < 4; k++)
						a = a*e;
					m = m.divide(BigInteger.valueOf(a.intValue()));
					
					if( m.doubleValue() > maximum)
					{
						maximum = m.doubleValue();
						probabilities.put(confusionSet.get(i), maximum);
					}
				}
			}
		}
	}
	
	public String findCorrectReplacement(ArrayList<String> confusionSet, String[] queryWords, int wordPosition)
	{
		HashMap<String, Double> search1 = new HashMap<String, Double>();
		HashMap<String, Double> search2 = new HashMap<String, Double>();
		HashMap<String, Double> search3 = new HashMap<String, Double>();
				
		/************* 2 grams **************/
		POStagging2Gram(queryWords, wordPosition, search1);
		/************* 3 grams **************/
		POStagging3Gram(queryWords, wordPosition, search2);
		/************* 4 grams **************/
		POStagging4Gram(queryWords, wordPosition, search3);
		
		for(int j = 0; j < confusionSet.size(); j++)
		{
			double maximum = 0;
			maximum = probabilities.get(confusionSet.get(j));
			if(wordCollocations2gram.containsKey(confusionSet.get(j)))
			{
				if(!search1.isEmpty())
				{
					Iterator it = search1.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry pairs = (Map.Entry)it.next();
						if(wordCollocations2gram.get(confusionSet.get(j)).containsKey(pairs.getKey().toString()))
						{
							double z = wordCollocations2gram.get(confusionSet.get(j)).get(pairs.getKey().toString())*Double.parseDouble(pairs.getValue().toString());
							int a = 1;
							int e =  editDistance(queryWords[wordPosition],confusionSet.get(j));
							for(int k = 0; k < 4; k++)
								a = a*e;
							z = z/a;
							
							if( z > maximum){
								maximum = z;
								probabilities.put(confusionSet.get(j), z);
							}
						}
					}
				}
			}
			if(wordCollocations3gram.containsKey(confusionSet.get(j)))
			{
				if(!search2.isEmpty())
				{
					Iterator it = search2.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry pairs = (Map.Entry)it.next();
						if(wordCollocations3gram.get(confusionSet.get(j)).containsKey(pairs.getKey().toString()))
						{
							double z = 2*wordCollocations3gram.get(confusionSet.get(j)).get(pairs.getKey().toString())*Double.parseDouble(pairs.getValue().toString());
							int a = 1;
							int e =  editDistance(queryWords[wordPosition],confusionSet.get(j));
							for(int k = 0; k < 4; k++)
								a = a*e;
							z = z/a;
							
							if( z > maximum){
								maximum = z;
								probabilities.put(confusionSet.get(j), z);
							}
						}
					}
				}
			}
			if(wordCollocations4gram.containsKey(confusionSet.get(j)))
			{
				if(!search3.isEmpty())
				{
					Iterator it = search3.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry pairs = (Map.Entry)it.next();
						if(wordCollocations4gram.get(confusionSet.get(j)).containsKey(pairs.getKey().toString()))
						{
							double z = wordCollocations4gram.get(confusionSet.get(j)).get(pairs.getKey().toString())*Double.parseDouble(pairs.getValue().toString());
							int a = 1;
							int e =  editDistance(queryWords[wordPosition],confusionSet.get(j));
							for(int k = 0; k < 4; k++)
								a = a*e;
							z = z/a;
							
							if( z > maximum){
								maximum = z;
								probabilities.put(confusionSet.get(j), z);
							}
						}
					}
				}
			}
		}
		Map<String, Double> Sorted_Probabilities = new HashMap<String, Double>();
		Sorted_Probabilities = sortByValues(probabilities);
		int count = 0;
		String ret = new String();
		
		String[] correct_words = correct_sent.toLowerCase().split("\\s+");
		int pos = 0;
		
		Iterator it = Sorted_Probabilities.entrySet().iterator();
	    while (it.hasNext() && count < 5) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        System.out.println(pairs.getKey() + " = " + pairs.getValue());
	        if(count == 0)
	    		ret = pairs.getKey().toString();
	        if(!correct_sent.equals("") && pairs.getKey().equals(correct_words[wordPosition]))
	        {
	        	pos = count+1;
	        }
	        count++;
	    }
	    if(pos != 0)
	    	MRR1 += (double)1/pos;
	    System.out.println();
	    return ret;
	}
	
	public int editDistance(String str1, String str2)
	{
		ArrayList<ArrayList<Integer>> matrix = new ArrayList<ArrayList<Integer>>();
        for(int i = 0; i <= str1.length(); i++) {  
            ArrayList<Integer> row = new ArrayList<Integer>();  
            for (int j = 0; j <= str2.length(); j++)  
                row.add(0);  
            matrix.add(row);  
        }

		for(int i = 0; i <= str1.length(); i++)
			matrix.get(i).set(0, i);
		for(int j = 0; j <= str2.length(); j++)
			matrix.get(0).set(j, j);
		
		for(int i = 1; i <= str1.length(); i++)
		{
			for(int j = 1; j <= str2.length(); j++)
			{
				int cost;
				if (str1.charAt(i-1) == str2.charAt(j-1))
					cost = 0;
				else 
					cost = 1;
				
				int temp = minimum( matrix.get(i-1).get(j) + 1, 
									matrix.get(i).get(j-1) + 1,
									matrix.get(i-1).get(j-1) + cost ); 
				matrix.get(i).set(j, temp);
				
				if(i > 2 && j > 2 && (str1.charAt(i-1) == str2.charAt(j-2)) && (str1.charAt(i-2) == str2.charAt(j-1)))
				{
					int temp2 = minimum( matrix.get(i).get(j), 
										 matrix.get(i-2).get(j-2) + cost,
										 -1 );
					matrix.get(i).set(j, temp2);
				}
			}
		}
		
		return matrix.get(str1.length()).get(str2.length());
	}
	
	
	public int minimum(int a, int b, int c)
	{
		if(c == -1 && a <= b)
			return a;
		else if(c == -1)
			return b;
		if(a <= b && a <= c)
			return a;
		if(b <= c && b <= a)
			return b;
		else
			return c;
	}
	
	public static <K extends Comparable,V extends Comparable> Map<K,V> sortByValues(Map<K,V> map){
        List<Map.Entry<K,V>> entries = new LinkedList<Map.Entry<K,V>>(map.entrySet());
      
        Collections.sort(entries, new Comparator<Map.Entry<K,V>>() {

            @Override
            public int compare(Entry<K, V> o1, Entry<K, V> o2) {
            	return o2.getValue().compareTo(o1.getValue());
            }
        });
        Map<K,V> sortedMap = new LinkedHashMap<K,V>();
      
        for(Map.Entry<K,V> entry: entries){
            sortedMap.put(entry.getKey(), entry.getValue());
        }
      
        return sortedMap;
    }
	
	public void phraseCorrection(String query)
	{
		MRR1 = 0.0;
		MRR2 = 0.0;
		sentenceEnumeration(query);
		String[] queryWords = query.toLowerCase().split("\\s+");
		ArrayList<String> candid = new ArrayList<String>();
		System.out.println("\nIndividual Probabilities");
		int n_incorrect = 0;
		for(int i = 0; i < queryWords.length; i++)
		{
			candid.clear();
			candidategeneration.findPotentialCandidates(queryWords[i]);
			if(candidategeneration.candidates.size() == 0);
			else{
				n_incorrect++;
				wordspellcheck.incorrect_word = queryWords[i];
				wordspellcheck.true_candidates(candidategeneration.candidates);
				candid.addAll(wordspellcheck.tenCandidates);
				findExactMatch(candid, queryWords, i);
				queryWords[i] = findCorrectReplacement(candid, queryWords, i);
			}
		}
		MRR1 = MRR1/n_incorrect;
		System.out.print("Correct Sentence: ");
		for(int i = 0; i < queryWords.length; i++)
			System.out.print(queryWords[i]+" ");
	}
	
	public void sentenceEnumeration(String query)
	{
		String[] queryWords = query.toLowerCase().split("\\s+");
		LinkedHashMap<String, ArrayList<String>> positions = new LinkedHashMap<String, ArrayList<String>>();
		int n = 0;
		ArrayList<Integer> error_list = new ArrayList<>();
		int check = 0;
		
		String[] combi = correct_sent.toLowerCase().split("\\s+");
		for(int i = 0; i < queryWords.length; i++)
		{
			candidategeneration.findPotentialCandidates(queryWords[i]);
			if(candidategeneration.candidates.size() != 0)
			{
				wordspellcheck.incorrect_word = queryWords[i];
				ArrayList<String> new_cands = new ArrayList<>();
				wordspellcheck.true_candidates(candidategeneration.candidates);
				new_cands.addAll(wordspellcheck.tenCandidates);
				positions.put(queryWords[i], new_cands);
				list.put(i, new ArrayList<String>());
				error_list.add(i);
				if(!correct_sent.equals(""))
				{
					if(check == 0){
						check = 1;
						correct_tuple = combi[i];
					}
					else
						correct_tuple += " "+combi[i];
				}
				n++;
			}
		}
		
		if(error_list.size() > 1 )
		{
			ArrayList<String> recur = new ArrayList<>();
			multiple_errors2(positions, error_list, query , recur);
			print_probabilities();
		}    
	}
	
	public void multiple_errors2(LinkedHashMap<String, ArrayList<String>> word_confusion, ArrayList<Integer> error_list,
			String input, ArrayList<String> recursion)
	{
		if(recursion.size() == error_list.size())
		{
			String recursion_str = new String();
			for(int  i =0; i< recursion.size();i++)
			{
				if(i!=recursion.size()-1)
					recursion_str = recursion_str + recursion.get(i) + " ";
				else
					recursion_str = recursion_str + recursion.get(i) ;
			}
			one_gram_probabilities.put(recursion_str, 0.0);
			two_gram_probabilities.put(recursion_str, 0.0);
			three_gram_probabilities.put(recursion_str, 0.0);
			four_gram_probabilities.put(recursion_str, 0.0);
			five_gram_probabilities.put(recursion_str, 0.0);
			ArrayList<String> l_context_words = new ArrayList<String>();
			ArrayList<String> r_context_words= new ArrayList<String>();
			ArrayList<String> candidates = new ArrayList<String>();
			ArrayList<String> new_input = new ArrayList<String>();
			StringTokenizer stt = new StringTokenizer(input," ");
			ArrayList<String> input_tokens = new ArrayList<String>();
			String token = new String();
			while(stt.hasMoreTokens())
			{
				token = stt.nextToken();
				input_tokens.add(token);
			}
			stt = new StringTokenizer(input," ");
			int count = 0;
			int index = 0;
			while(stt.hasMoreTokens())
			{
				token = stt.nextToken();
				if(index < error_list.size())
				{
					if(count == error_list.get(index))
					{
						new_input.add(recursion.get(index));
						index++;
					}
					else
					{
						new_input.add(token);
					}
				}
				else
				{
					new_input.add(token);
				}
				count++;
			}
			
			String new_input_str = new String();
			
			for(int  i =0; i< new_input.size();i++)
			{
				if(i!=new_input.size()-1)
					new_input_str = new_input_str + new_input.get(i) + " ";
				else
					new_input_str = new_input_str + new_input.get(i) ;
			}

			String incorrect_word = new String();
			String replaced_word = new String();
			index = 0;
			int flag2 = 1;
			while(true)
			{
				int flag = 1;
				flag2=1;
				if(index == error_list.size())
					break;
				count = 0;
				l_context_words = new ArrayList<String>();
				r_context_words = new ArrayList<String>();
				candidates = new ArrayList<String>();
				stt = new StringTokenizer(new_input_str," ");
				while(stt.hasMoreTokens())
				{
					token = stt.nextToken();
					if(index < error_list.size() && flag2==1)
					{
						if(count == error_list.get(index))//incorrect
						{
							incorrect_word = input_tokens.get(count);
							replaced_word = token;
							flag2 = 0;
							flag = 0;
							index++;
							if(stt.hasMoreTokens())
								token = stt.nextToken();
							else break;
						}
					}
					if(flag==1)
						l_context_words.add(token);
					else
						r_context_words.add(token);	
					count++;
				}
				
				String str = new String();
				for(int i = 0; i < l_context_words.size(); i++){
					str += l_context_words.get(i) + " ";
				}
				str += replaced_word+" ";
				for(int i = 0; i < r_context_words.size(); i++){
					str += r_context_words.get(i) + " ";
				}

				double two =  mult_analysis_two_grams(replaced_word, l_context_words, r_context_words );
				double three = mult_analysis_three_grams(replaced_word, l_context_words, r_context_words );
				double four = mult_analysis_four_grams(replaced_word, l_context_words, r_context_words );
				
				two_gram_probabilities.put(recursion_str, two_gram_probabilities.get(recursion_str)+ two);
				three_gram_probabilities.put(recursion_str, three_gram_probabilities.get(recursion_str)+ three);
				four_gram_probabilities.put(recursion_str, four_gram_probabilities.get(recursion_str)+ four);
			}
		}
		else
		{
			int i = recursion.size();
			int index = error_list.get(i);
			StringTokenizer stt = new StringTokenizer(input," ");
			String token  =new String();
			int count = 0;
			while(stt.hasMoreTokens())
			{
				token = stt.nextToken();
				if(count == index)
					break;
				count ++;
			}
			ArrayList<String> candidates = new ArrayList<String>();
			candidates = word_confusion.get(token);
			for(i= 0; i< candidates.size();i++)
			{
				recursion.add(candidates.get(i));
				multiple_errors2(word_confusion, error_list, input, recursion);
				recursion.remove(recursion.size()-1);
			}
		}
	}
	
	public double mult_analysis_two_grams(String replacement, ArrayList<String> l_context_words, ArrayList<String> r_context_words)
	{
		double probability = 1.0;
		String bigram = ""; 
		int lindex = l_context_words.size()-1;
		int rindex = 0;
		if(l_context_words.size() >= 1)
		{
			bigram = l_context_words.get(lindex)+"_"+replacement;
			if(word2gram.get(bigram)!=null)
				probability =  (word2gram.get(bigram)).doubleValue();
		}
		if(r_context_words.size() >= 1)
		{
			bigram = replacement+ "_"+r_context_words.get(rindex);
			if(word2gram.get(bigram)!=null)
				probability = probability * (word2gram.get(bigram)).doubleValue();
		}

		double c = probability - 1.0;
		if(!(Math.abs(c) <= 0.000001))
			return probability;
		else return 0.0;
	}
	
	public double mult_analysis_three_grams(String replcement, ArrayList<String> l_context_words, ArrayList<String> r_context_words)
	{
		double probability = 1.0;
		String trigram = new String();
		int lindex = l_context_words.size()-1;
		int rindex = 0;
		if(l_context_words.size() >= 2)
		{
			trigram = l_context_words.get(lindex-1)+"_"+l_context_words.get(lindex)+"_"+replcement;
			if(word3gram.get(trigram)!=null)
				probability = word3gram.get(trigram).doubleValue();
		}
		if(l_context_words.size() >= 1 && r_context_words.size()>=1)
		{
			trigram = l_context_words.get(lindex)+"_"+replcement+"_"+r_context_words.get(rindex);
			if(word3gram.get(trigram)!=null)
				probability = probability * word3gram.get(trigram).doubleValue();
		}
		if( r_context_words.size()>=2)
		{
			trigram = replcement+"_"+r_context_words.get(rindex)+"_"+r_context_words.get(rindex+1);
			if(word3gram.get(trigram)!=null)
				probability = probability * word3gram.get(trigram).doubleValue();
		}

		double c = probability - 1.0;
		if(!(Math.abs(c) <= 0.000001))
			return probability;
		else return 0.0;
	}
	
	public double mult_analysis_four_grams(String replacement, ArrayList<String> l_context_words, ArrayList<String> r_context_words)
	{
		double probability = 1.0;
		String quadgram = new String();
		int lindex = l_context_words.size()-1;
		int rindex = 0;
		if(l_context_words.size()>=3)
		{
			quadgram = l_context_words.get(lindex-2)+"_"+l_context_words.get(lindex-1)+"_"+l_context_words.get(lindex)+"_"+replacement;
			if(word4gram.get(quadgram)!=null)
				probability =  word4gram.get(quadgram).doubleValue();
		}
		if(l_context_words.size()>=2 && r_context_words.size()>=1)
		{
			quadgram = l_context_words.get(lindex-1)+"_"+l_context_words.get(lindex)+"_"+replacement+"_"+r_context_words.get(rindex);
			if(word4gram.get(quadgram)!=null)
				probability = probability* word4gram.get(quadgram).doubleValue();
		}
		if(l_context_words.size()>=1 && r_context_words.size()>=2)
		{
			quadgram = l_context_words.get(lindex)+"_"+replacement+"_"+r_context_words.get(rindex)+"_"+r_context_words.get(rindex+1);
			if(word4gram.get(quadgram)!=null)
				probability = probability* word4gram.get(quadgram).doubleValue();
		}
		if(r_context_words.size()>=3)
		{
			quadgram = replacement+"_"+r_context_words.get(rindex)+"_"+r_context_words.get(rindex+1)+"_"+r_context_words.get(rindex+2);
			if(word4gram.get(quadgram)!=null)
				probability = probability* word4gram.get(quadgram).doubleValue();
			else 
			{
				//count = 1
			}
		}
		double c = probability - 1.0;
		if(!(Math.abs(c) <= 0.000001))
			return probability;
		else return 0.0;
	}
	
	
	public void print_probabilities()
	{
		
		int count = 0;
		Map<String, Double> Sorted_one_gram_Probabilities = new HashMap<String, Double>();
		Map<String, Double> Sorted_two_gram_Probabilities = new HashMap<String, Double>();
		Map<String, Double> Sorted_three_gram_Probabilities = new HashMap<String, Double>();
		Map<String, Double> Sorted_four_gram_Probabilities = new HashMap<String, Double>();
		Map<String, Double> Sorted_five_gram_Probabilities = new HashMap<String, Double>();
		Map<String, Double> final_Probabilities = new HashMap<String, Double>();
		Map<String, Double> Sorted_final_Probabilities = new HashMap<String, Double>();
		
		double five_sum = 0.0;
		double four_sum = 0.0;
		double three_sum = 0.0;
		double two_sum = 0.0;
		double one_sum = 0.0;
		HashSet<Double> distinct = new HashSet<Double>();
		
		distinct = new HashSet<Double>();
		{
			Sorted_four_gram_Probabilities = (sortByValues(four_gram_probabilities)) ;
			count = 0;
			Iterator it = Sorted_four_gram_Probabilities.entrySet().iterator();
		    while (it.hasNext()) 
		    {
		        Map.Entry pairs = (Map.Entry)it.next();
				four_sum = four_sum + Sorted_four_gram_Probabilities.get(pairs.getKey());
		    }
		    count = 0;
		  
			it = Sorted_four_gram_Probabilities.entrySet().iterator();
		    while (it.hasNext() && count < 5) 
		    {
		        Map.Entry pairs = (Map.Entry)it.next();
		        double c = Sorted_four_gram_Probabilities.get(pairs.getKey()) - 0.0;
				if (!(Math.abs(c) <= 0.000001))
		        {
		        	if(!distinct.contains(c/four_sum))
					{
						distinct.add(c/four_sum);
						if(final_Probabilities.get(pairs.getKey().toString()) != null)
			        		final_Probabilities.put(pairs.getKey().toString(), 
			        				final_Probabilities.get(pairs.getKey().toString())+c/four_sum);
			        	else
			        		final_Probabilities.put(pairs.getKey().toString(), c/four_sum);
						count++;
					}
		        	
		        }
		    }
		}
		distinct = new HashSet<Double>();
		{
			Sorted_three_gram_Probabilities = (sortByValues(three_gram_probabilities)) ;
			Iterator it = Sorted_three_gram_Probabilities.entrySet().iterator();
		    while (it.hasNext()) 
		    {
		        Map.Entry pairs = (Map.Entry)it.next();
				three_sum = three_sum + Sorted_three_gram_Probabilities.get(pairs.getKey());
		    }
		    count = 0;

		    it = Sorted_three_gram_Probabilities.entrySet().iterator();
		    while (it.hasNext() && count < 5) 
		    {
		        Map.Entry pairs = (Map.Entry)it.next();
		        double c = Sorted_three_gram_Probabilities.get(pairs.getKey()) - 0.0;
				if (!(Math.abs(c) <= 0.000001))
		        {
		        	if(!distinct.contains(c/three_sum))
					{
						distinct.add(c/three_sum);
						if(final_Probabilities.get(pairs.getKey().toString()) != null)
			        		final_Probabilities.put(pairs.getKey().toString(), 
			        				final_Probabilities.get(pairs.getKey().toString())+c/three_sum);
			        	else
			        		final_Probabilities.put(pairs.getKey().toString(), c/three_sum);
						count++;
					}
		        	
		        }
		    }
		}
		distinct = new HashSet<Double>();
		{
			Sorted_two_gram_Probabilities = (sortByValues(two_gram_probabilities)) ;
			Iterator it = Sorted_two_gram_Probabilities.entrySet().iterator();
		    while (it.hasNext()) 
		    {
		        Map.Entry pairs = (Map.Entry)it.next();
				two_sum = two_sum + Sorted_two_gram_Probabilities.get(pairs.getKey());
				
		    }
		    count = 0;

		    it = Sorted_two_gram_Probabilities.entrySet().iterator();
		    while (it.hasNext() && count < 5) 
		    {
		        Map.Entry pairs = (Map.Entry)it.next();
		        double c = Sorted_two_gram_Probabilities.get(pairs.getKey()) - 0.0;
				if (!(Math.abs(c) <= 0.000001))
		        {
		        	if(!distinct.contains(c/two_sum))
					{
						distinct.add(c/two_sum);
						if(final_Probabilities.get(pairs.getKey().toString()) != null)
			        		final_Probabilities.put(pairs.getKey().toString(), 
			        				final_Probabilities.get(pairs.getKey().toString())+c/two_sum);
			        	else
			        		final_Probabilities.put(pairs.getKey().toString(), c/two_sum);
						count++;
					}
		        	
		        }
		    }
		}
		
		Sorted_final_Probabilities = (sortByValues(final_Probabilities)) ;
		Iterator it = Sorted_final_Probabilities.entrySet().iterator();
		count = 0;
		distinct = new HashSet<Double>();
		System.out.println("Tuples Probabilities");
		
	    while (it.hasNext() && count < 5) 
	    {
	        Map.Entry pairs = (Map.Entry)it.next();
	        
	        System.out.println(pairs.getKey() + "  "+ Sorted_final_Probabilities.get(pairs.getKey()));
	        if(!distinct.contains(Sorted_final_Probabilities.get(pairs.getKey())))
			{
	        	if(pairs.getKey().toString().equals(correct_tuple))
	        		MRR2 = (double)1/(count+1);
				distinct.add(Sorted_final_Probabilities.get(pairs.getKey()));
				count++;
			}
	    }
	    
	    one_gram_probabilities.clear();
	    two_gram_probabilities.clear();
	    three_gram_probabilities.clear();
	    four_gram_probabilities.clear();
	    five_gram_probabilities.clear();

	}
}