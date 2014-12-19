import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.Map.Entry;
import org.apache.commons.codec.language.DoubleMetaphone;

public class WordSpellCheck {
	public HashMap<String, BigInteger> Dictionary = new HashMap<String, BigInteger>();
	public HashMap<String, Double> Probabilities = new HashMap<String, Double>();
	BigInteger [][] digrams = new BigInteger [27][26];
	BigInteger []unigrams = new BigInteger[27];
	HashMap<String, Double> answers = new HashMap<String, Double>();
	
	int [][] new_insertion_confusion_matrix = new int[27][26];
	int [][] new_deletion_confusion_matrix = new int[27][26];
	int [][] new_subs_confusion_matrix = new int[26][26];
	int [][] new_trans_confusion_matrix = new int[27][26];
	
	ArrayList<String> tenCandidates = new ArrayList<String>();
	String incorrect_word = new String();
	String correct = new String();
	public void read_dictionary()
	{
		for(int i = 0; i< 27;i++)
			for(int j = 0; j< 26; j++)
				digrams[i][j]  = BigInteger.ZERO;
		try
		{
			File inFile = new File("new_dict");
			FileInputStream fstream = new FileInputStream(inFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			BigInteger i;
			while ((strLine = br.readLine()) != null)   
			{
				StringTokenizer stt = new StringTokenizer(strLine,"	");
				String token = stt.nextToken();
				while(stt.hasMoreTokens())
				{
					String temp = stt.nextToken();
					i = new BigInteger(temp);
					Dictionary.put(token, i);
					generate_digrams_from_dictionary(token);
				}
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public void generate_digrams_from_dictionary(String word)
	{
		BigInteger freq = Dictionary.get(word);
		for(int i =0; i< word.length()-1 ; i++)
		{
			digrams[word.charAt(i)- 'a'][word.charAt(i+1)- 'a'] =  digrams[word.charAt(i)- 'a'][word.charAt(i+1)- 'a'].add(freq);
		}
		digrams[26][word.charAt(0)- 'a'] =  digrams[26][word.charAt(0)- 'a'].add(freq);		
	}
	
	public void generate_unigrams()
	{
		for(int i = 0;i<26;i++)
		{
			unigrams[i]= BigInteger.ZERO;
			for(int j =0 ;j<26;j++)
			{
				unigrams[i] = digrams[i][j].add(unigrams[i]);
			}
			unigrams[26] = BigInteger.valueOf(78825);
		}
	}
		
 	public int true_candidates(ArrayList<String> candidates)
	{
		Probabilities.clear();
		tenCandidates.clear();
		
		for(int i = 0; i< candidates.size(); i++)
		{
			DoubleMetaphone db = new DoubleMetaphone();
			String correct_metaphone  = db.doubleMetaphone(candidates.get(i));
			String wrong_metaphone  = db.doubleMetaphone(incorrect_word);
			int edit_dist = edit_distance(wrong_metaphone, correct_metaphone);
			
			double probab = spell_checker_editDistance(incorrect_word, candidates.get(i));
			probab = probab/( edit_dist + 0.5);
			Probabilities.put(candidates.get(i), probab);
		}	    
		
		
		Map<String, Double> Sorted_Probabilities = new HashMap<String, Double>();
		Sorted_Probabilities = sortByValues(Probabilities);
		int count = 0;
		Iterator it = Sorted_Probabilities.entrySet().iterator();
	    while (it.hasNext() && count < 10) 
	    {
	        Map.Entry pairs = (Map.Entry)it.next();
			tenCandidates.add(pairs.getKey().toString());
	        count++;
	    }
	    int ret = 0;
	    if(!correct.equals(""))
	    {
			if(Dictionary.get(correct) == null)
			{
				return -2;
			}
			count = 0;
			it = Sorted_Probabilities.entrySet().iterator();
		    while (it.hasNext() && count < 5) 
		    {
		        Map.Entry pairs = (Map.Entry)it.next();
		        //System.out.println(pairs.getKey() + " = " + pairs.getValue() + "  ");
		        if(pairs.getKey().toString().equals(correct))
		        	ret =  count+1;
		        count++;
		    }
		}
	    return ret;
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
	
	public int edit_distance(String str1, String str2)
	{
		ArrayList<ArrayList<Integer>> matrix = new ArrayList<ArrayList<Integer>>();
        for(int i = 0; i <= str1.length(); i++) 
        {  
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
	
	public double spell_checker_editDistance(String str1, String str2)
	{
		ArrayList<ArrayList<Integer>> matrix = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> directions = new ArrayList<ArrayList<Integer>>();
		double probability = 1.0;
		probability = (Dictionary.get(str2).doubleValue()) + 1;
		
        for(int i = 0; i <= str1.length(); i++) 
        {  
            ArrayList<Integer> row = new ArrayList<Integer>();  
            for (int j = 0; j <= str2.length(); j++)  
                row.add(0);  
            matrix.add(row);  
        }
        for(int i = 0; i <= str1.length(); i++) 
        {  
            ArrayList<Integer> row = new ArrayList<Integer>();  
            for (int j = 0; j <= str2.length(); j++)  
                row.add(0);  
            directions.add(row);  
        }

		for(int i = 0; i <= str1.length(); i++)
		{
			matrix.get(i).set(0, i);
			directions.get(i).set(0, 1);
		}
		for(int j = 0; j <= str2.length(); j++)
		{
			matrix.get(0).set(j, j);
			directions.get(0).set(j, 2);
		}
		
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
				
				if(temp == matrix.get(i-1).get(j) + 1)
					directions.get(i).set(j, 1); //insert
				if(temp == matrix.get(i).get(j-1) + 1)
					directions.get(i).set(j, 2); //delete
				if(temp == matrix.get(i-1).get(j-1) + cost)
					directions.get(i).set(j, 3); //subs
				if(cost == 0)
					directions.get(i).set(j, 4); //nothing
				
				
				if(i >= 2 && j >= 2 && (str1.charAt(i-1) == str2.charAt(j-2)) && (str1.charAt(i-2) == str2.charAt(j-1)))
				{
					int temp2 = minimum( matrix.get(i).get(j), 
										 matrix.get(i-2).get(j-2) + cost,
										 -1 );
					matrix.get(i).set(j, temp2);
					if(temp2 == matrix.get(i-2).get(j-2) + cost)
						directions.get(i).set(j, 5); //trans
				}
			}
		}
		int i = str1.length();
		int j = str2.length();
		while(i>=1 || j>=1)
		{
			if(directions.get(i).get(j) == 1) //insert
			{
				if(i-2!=-1)
				{
					probability = probability * (double)(new_insertion_confusion_matrix[str1.charAt(i-2)- 'a'][ str1.charAt(i-1) - 'a']+1) ;
					probability = probability / (( unigrams[str1.charAt(i-2)- 'a'].doubleValue()+1)+1);
					if((( (unigrams[str1.charAt(i-2)- 'a'].doubleValue()+1)+1) < 2))
						System.out.println((( unigrams[str1.charAt(i-2)- 'a'].doubleValue()+1)+1));

				}
				else
				{
					probability = probability * (double)(new_insertion_confusion_matrix[26][ str1.charAt(i-1) - 'a']+1) ;
					probability = probability / ((unigrams[26].doubleValue()+1)+1);
					if((((unigrams[26].doubleValue()+1)+1) < 2))
					System.out.println(((unigrams[26].doubleValue()+1)+1));
				}
					
				i = i -1;
			}

			else if(directions.get(i).get(j) == 2) //delete
			{
				if(j-2!=-1)
				{
					probability = probability * (double)(new_deletion_confusion_matrix[str2.charAt(j-2)- 'a'][ str2.charAt(j-1) - 'a']+1) ;
					probability = probability / ((digrams[str2.charAt(j-2)- 'a'][ str2.charAt(j-1) - 'a'].doubleValue()+1)+1);
					if(((digrams[str2.charAt(j-2)- 'a'][ str2.charAt(j-1) - 'a'].doubleValue()+1)+1) < 2)
					System.out.println(((digrams[str2.charAt(j-2)- 'a'][ str2.charAt(j-1) - 'a'].doubleValue()+1)+1));
				}
				else
				{
					probability = probability * (double)(new_deletion_confusion_matrix[26][ str2.charAt(j-1) - 'a']+1) ;
					probability = probability / ((digrams[26][str2.charAt(j-1) - 'a'].doubleValue()+1)+1);
					if(((digrams[26][str2.charAt(j-1) - 'a'].doubleValue()+1)+1) < 2)
					System.out.println(((digrams[26][str2.charAt(j-1) - 'a'].doubleValue()+1)+1));
				}
				j = j-1;
			}

			else if(directions.get(i).get(j) == 3) //subs
			{
				probability = probability * (double)(new_subs_confusion_matrix[str1.charAt(i-1)- 'a'][str2.charAt(j-1)- 'a']+1);
				probability = probability /  ((unigrams[str2.charAt(j-1)- 'a'].doubleValue()+1)+1);
				if(((unigrams[str2.charAt(j-1)- 'a'].doubleValue()+1)+1) < 2)
				System.out.println(((unigrams[str2.charAt(j-1)- 'a'].doubleValue()+1)+1));
				i = i -1;
				j = j -1;
			}

			else if(directions.get(i).get(j) == 4) //nothing
			{
				i = i -1;
				j = j -1;
			}

			else if (directions.get(i).get(j) == 5) //transpose
			{
				probability = probability * (double)(new_trans_confusion_matrix[str2.charAt(j-2)- 'a'][str2.charAt(j-1)- 'a']+1);
				probability = probability / ( (digrams[str2.charAt(j-2)- 'a'][str2.charAt(j-1) - 'a'].doubleValue()+1)+1);
				if(( (digrams[str2.charAt(j-2)- 'a'][str2.charAt(j-1) - 'a'].doubleValue()+1)+1) < 2)
				System.out.println(( (digrams[str2.charAt(j-2)- 'a'][str2.charAt(j-1) - 'a'].doubleValue()+1)+1));
				i = i -2;
				j = j -2;
			}
		}
		probability = probability * Math.pow(10, 48);
		probability = Math.log10(probability);
		probability  = probability / Math.pow((double)matrix.get(str1.length()).get(str2.length()), 2);
		
		return probability;
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
	
	public void read_matrix()
	{
		try
		{
			File inFile = new File("confusion_add.txt");
			FileInputStream fstream = new FileInputStream(inFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int i = 0;
			int j = 0;
			int k = 0;
			while ((strLine = br.readLine()) != null)   
			{
				StringTokenizer stt = new StringTokenizer(strLine," ");
				k = 0 ;
				while(stt.hasMoreTokens())
				{
					String temp = stt.nextToken();
					i = Integer.parseInt(temp);
					new_insertion_confusion_matrix[j][k] = i;
					//System.out.print(insertion_confusion_matrix[j][k] + "  ");
					k++;
				}
				//System.out.println();
				j++;
			}
			
			inFile = new File("confusion_del.txt");
			fstream = new FileInputStream(inFile);
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			i = 0;
			j = 0;
			k = 0;
			while ((strLine = br.readLine()) != null)   
			{
				StringTokenizer stt = new StringTokenizer(strLine," ");
				k = 0 ;
				while(stt.hasMoreTokens())
				{
					String temp = stt.nextToken();
					i = Integer.parseInt(temp);
					new_deletion_confusion_matrix[j][k] = i;
					k++;
				}
				j++;
			}
			
			inFile = new File("confusion_subs.txt");
			fstream = new FileInputStream(inFile);
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			i = 0;
			j = 0;
			k = 0;
			while ((strLine = br.readLine()) != null)   
			{
				StringTokenizer stt = new StringTokenizer(strLine," ");
				k = 0 ;
				while(stt.hasMoreTokens())
				{
					String temp = stt.nextToken();
					i = Integer.parseInt(temp);
					new_subs_confusion_matrix[j][k] = i;
					k++;
				}
				j++;
			}
			inFile = new File("confusion_trans.txt");
			fstream = new FileInputStream(inFile);
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			i = 0;
			j = 0;
			k = 0;
			while ((strLine = br.readLine()) != null)   
			{
				StringTokenizer stt = new StringTokenizer(strLine," ");
				k = 0 ;
				while(stt.hasMoreTokens())
				{
					String temp = stt.nextToken();
					i = Integer.parseInt(temp);
					new_trans_confusion_matrix[j][k] = i;
					k++;
				}
				j++;
			}
		}
		catch(Exception e)
		{
			System.out.println("File not found");
		}
	}
}
