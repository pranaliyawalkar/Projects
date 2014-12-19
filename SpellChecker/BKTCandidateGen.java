import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class BKTCandidateGen{
	//ArrayList<HashMap<String, String>> partitions = new ArrayList<HashMap<String, String>>();
	ArrayList<BKTree> partitions = new ArrayList<BKTree>();
	List<String> candidates = new ArrayList<String>();
	ArrayList<Integer> candidatesEditDis = new ArrayList<Integer>();
	String query = new String();
	String queryBitVector;
	int maxEditDistance;
	int totalResults;
	
	public void partitionsInDictionary()
	{
		for(int i = 0; i < 40; i++)
		{
			partitions.add(new BKTree());
		}
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("new_dict"));
			String Line;
			while ((Line = br.readLine()) != null)
			{
				String[] words = Line.toLowerCase().split("\\s+");
				partitions.get(words[0].length()-1).add(words[0]);
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
		query = str;
		totalResults = 0;
		candidates.clear();
		candidatesEditDis.clear();

		if(query.length() <= 3)
			maxEditDistance = 1;			
		else if(query.length() <= 6)
			maxEditDistance = 2;
		else if(query.length() <= 10)
			maxEditDistance = 3;
		else if(query.length() <= 20)
			maxEditDistance = 4;
		else
			maxEditDistance = 6;
		
		for(int i = (query.length()-1)-maxEditDistance; i < query.length()+maxEditDistance; i++)
		{
			if(i < 0)
				continue;
			candidates.addAll(partitions.get(i).search(query, maxEditDistance));
		}
	}
}
