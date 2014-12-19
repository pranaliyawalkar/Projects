import java.io.*;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.*;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		WordSpellCheck wordspellcheck = new WordSpellCheck();
		CandidateGeneration candidategeneration = new CandidateGeneration();
		Collocations colloc = new Collocations();
		
		/****** Preprocessing ******/
		long t1 = System. currentTimeMillis();
		colloc.preprocessing4gramDictionary();
		colloc.preprocessing3gramDictionary();
		colloc.preprocessing2gramDictionary();
		candidategeneration.partitionsInDictionary();
		wordspellcheck.read_dictionary();
		wordspellcheck.read_matrix();
		wordspellcheck.generate_unigrams();
		long t2 = System.currentTimeMillis();
		System.out.println("\nIndex construction time taken : " + (t2-t1));
		
		while(true)
		{
			Scanner in = new Scanner(System.in);
			String wordOrSentence = new String();
			System.out.print("Check word/Phrase/Sentence/Exit?:");
			wordOrSentence = in.nextLine();
			if(wordOrSentence.equals("exit") || wordOrSentence.equals("e") || wordOrSentence.equals("Exit")){
				break;
			}
			if(wordOrSentence.equals("w") || wordOrSentence.equals("word")){
				System.out.println("Enter word: ");
	            String input = in.nextLine();
	            t1 = System.currentTimeMillis();
	            candidategeneration.findPotentialCandidates(input);
	            wordspellcheck.incorrect_word = input;
	            wordspellcheck.correct = "";
	            wordspellcheck.true_candidates(candidategeneration.candidates);
	            for(int i=0; i < 5;i++)
	            	System.out.println(wordspellcheck.tenCandidates.get(i));
	            t2 = System.currentTimeMillis();
	            System.out.println("\nTime taken : " + (t2-t1));
			}
			else{
				String query = new String();
				System.out.println("Enter phrase/Sentence: ");
				query = in.nextLine();
				t1 = System.currentTimeMillis();
				colloc.correct_sent = "";
				colloc.phraseCorrection(query);
				t2 = System.currentTimeMillis();
				System.out.println("\nTime taken : " + (t2-t1));
			}
		}
		/*try 
		{
			double total_MRR = 0.0;
			long total_time = 0;
			File inFile = new File("test7");
			FileInputStream fstream = new FileInputStream(inFile);
			DataInputStream in1 = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in1));
			String strLine;
			BigInteger i;
			while ((strLine = br.readLine()) != null)   
			{
				String query = new String();
				String[] queryWords = strLine.toLowerCase().split("\t");
				query = queryWords[0].toLowerCase();
				colloc.correct_sent = queryWords[1].toLowerCase();
				t1 = System.currentTimeMillis();
				colloc.phraseCorrection(query);
				t2 = System.currentTimeMillis();
				if(colloc.MRR1 > colloc.MRR2)
					total_MRR += colloc.MRR1;
				else
					total_MRR += colloc.MRR2;
				System.out.println("\nIndividual MRR: "+colloc.MRR1 +" Tuple MRR: "+colloc.MRR2);
				total_time += (t2-t1);
			}
			System.out.println("Final MRR = "+total_MRR/29);
			System.out.println("\nTime taken : " + total_time);
		}
		catch(Exception e)
		{
			System.out.println("file not found");
		}
		
		/*WordSpellCheck wordspellcheck = new WordSpellCheck();
		CandidateGeneration candidategeneration = new CandidateGeneration();
		
		long t1 = System. currentTimeMillis();
		candidategeneration.partitionsInDictionary();
		wordspellcheck.read_dictionary();
		wordspellcheck.read_matrix();
		wordspellcheck.generate_unigrams();
		long t2 = System. currentTimeMillis();
		System.out.println("\nIndex construction time taken : " + (t2-t1));
		
		int correct_count = 0;
		int total_count = 0;
		double MRR = 0.0;
		int total_time = 0;
		try
		{
			File inFile = new File("test3");
			FileInputStream fstream = new FileInputStream(inFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			BigInteger i;
			while ((strLine = br.readLine()) != null)   
			{
				String[] s = strLine.toLowerCase().split("\\s+");
				StringTokenizer stt = new StringTokenizer(strLine,"\t");
				String token1 = stt.nextToken().toLowerCase();
				total_count++;
				t1 = System. currentTimeMillis();
				candidategeneration.findPotentialCandidates(token1);
				t2 = System. currentTimeMillis();
				total_time += (t2-t1);
				wordspellcheck.incorrect_word = token1;
				wordspellcheck.correct = s[1];
				int temp = wordspellcheck.true_candidates(candidategeneration.candidates);
				if(temp != -2 && temp!=0){
					correct_count  = correct_count + 1;
					MRR += (double)1/temp;
				}
				if(temp == -2)
				{
					//System.out.println(s[0]+" "+s[1]);
					total_count = total_count-1;
				}
				if(temp==0)
				{
					System.out.println(token1);
				}
			}
			br.close();
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		System.out.println(total_count + "  " + correct_count);
		System.out.println("MRR = "+MRR/total_count);
		System.out.println("total time = "+total_time);*/
	}
}
