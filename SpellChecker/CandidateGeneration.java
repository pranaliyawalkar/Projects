import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class CandidateGeneration{
	ArrayList<HashMap<String, String>> partitions = new ArrayList<HashMap<String, String>>();
	ArrayList<String> candidates = new ArrayList<String>();
	ArrayList<Integer> candidatesEditDis = new ArrayList<Integer>();
	String query = new String();
	String queryBitVector;
	int maxEditDistance;
	int totalResults;
	
	public void partitionsInDictionary()
	{
		for(int i = 0; i < 40; i++)
		{
			partitions.add(new HashMap<String, String>());
		}
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("new_dict"));
			String Line;
			while ((Line = br.readLine()) != null)
			{
				String bitVector = new String("00000000000000000000000000");
				int i;
				for( i = 0; i < Line.length(); i++)
				{
					if(Line.charAt(i) == '\t')
						break;

					int n = (bitVector.charAt(Line.charAt(i)-'a') - '0') + 1;
					String temp = bitVector.substring(0,Line.charAt(i)-'a') + n + bitVector.substring(Line.charAt(i)-'a'+1);
					bitVector = temp;
				}
				partitions.get(i-1).put(Line.substring(0,i).toLowerCase(), bitVector);
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println(e + " wrong");
		}
	}
	
	public int xor(char c1, char c2)
	{
		int difference = c1 - c2;
		if(difference < 0)
			return -difference;
		return difference;
	}
	
	public void findPotentialCandidates(String str)
	{
		query = str.toLowerCase();
		totalResults = 0;
		candidates.clear();
		candidatesEditDis.clear();
		queryBitVector = new String("00000000000000000000000000");
		for(int i = 0; i < query.length(); i++)
		{
			//if(query.charAt(i) >= 'a' && query.charAt(i) <='z')
			//{
				int n = (queryBitVector.charAt(query.charAt(i)-'a') - '0') + 1;
				String temp = queryBitVector.substring(0, query.charAt(i)-'a')+n+ queryBitVector.substring(query.charAt(i)-'a'+1);
				queryBitVector = temp;
			//}
		}

		if(query.length() <= 3)
			maxEditDistance = 1;
		else if(query.length() <= 5)
			maxEditDistance = 2;
		else if(query.length() <= 7)
			maxEditDistance = 3;
		else if(query.length() <= 12)
			maxEditDistance = 4;
		else if(query.length() <= 20)
			maxEditDistance = 5;
		else
			maxEditDistance = 6;
		
		Iterator it = partitions.get(query.length()-1).entrySet().iterator();
		while (it.hasNext()) {
			int LB = 0;
			Map.Entry pairs = (Map.Entry)it.next();
			String dictionaryWord = pairs.getValue().toString();
			
			for(int j = 0; j < 26; j++)
				LB = LB + xor(queryBitVector.charAt(j), dictionaryWord.charAt(j));
			
			if(LB <= 2*maxEditDistance)		//Eliminate words for which LB() > 2*maxEditDistance
			{
				if(query.equals(pairs.getKey().toString()))
				{
					candidates.clear();
					candidatesEditDis.clear();
					return;
				}
				// find words for which EditDis <= maxEditDistance
				int editdis = editDistance(query, pairs.getKey().toString());
				
				if( editdis <= maxEditDistance){
					candidates.add(pairs.getKey().toString());
					candidatesEditDis.add(editdis);
					totalResults++;
				}
			}
		}
		
		// Pruning away few partitions
		for(int i = (query.length()-1)-maxEditDistance; i < query.length()+maxEditDistance; i++)
		{
			if(i < 0)
				continue;
			if(i == query.length()-1)
				continue;
			it = partitions.get(i).entrySet().iterator();
			while (it.hasNext()) {
				int LB = 0;
				Map.Entry pairs = (Map.Entry)it.next();
				String dictionaryWord = pairs.getValue().toString();
				
				for(int j = 0; j < 26; j++)
					LB = LB + xor(queryBitVector.charAt(j), dictionaryWord.charAt(j));
				
				if(LB <= 2*maxEditDistance)		//Eliminate words for which LB() > 2*maxEditDistance
				{
					// find words for which EditDis <= maxEditDistance
					int editdis = editDistance(query, pairs.getKey().toString());
					
					if( editdis <= maxEditDistance){
						candidates.add(pairs.getKey().toString());
						candidatesEditDis.add(editdis);
						totalResults++;
					}
				}
			}
		}
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
				
				if(i >= 2 && j >= 2 && (str1.charAt(i-1) == str2.charAt(j-2)) && (str1.charAt(i-2) == str2.charAt(j-1)))
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
}
