import java.io.*;
import java.util.*;
public class Graph {

	// A graph contains nodes and edges. Edges are captures in adjacency lists of nodes
	public Node[] nodes;

	public Graph(){
		nodes = new Node[456631];
		int i=0;
		for(i=0;i<456631;i++){
			nodes[i] = new Node();
			nodes[i].id = i;
		}
	}

	// Read details of all 4 layers from input file
	public void initialise()	{
		try 
		{
			/*****************************FOLLOW***************************************/
			File inFile = new File("social_sorted_adjlist.csv");
			FileInputStream fstream = new FileInputStream(inFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int i;
			while ((strLine = br.readLine()) != null)   
			{
				StringTokenizer stt = new StringTokenizer(strLine," ");
				String token = stt.nextToken();
				i = Integer.parseInt(token);

				while(stt.hasMoreTokens())
				{
					token = stt.nextToken();
					(nodes[i].follow).add(Integer.parseInt(token));
				}
			}

			/*******************************MENTION********************************/

			inFile = new File("mention_sorted_adjlist.csv");
			fstream = new FileInputStream(inFile);
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));

			while ((strLine = br.readLine()) != null)   
			{
				StringTokenizer stt = new StringTokenizer(strLine," ");
				String token = stt.nextToken();
				i = Integer.parseInt(token);

				while(stt.hasMoreTokens())
				{
					token = stt.nextToken();
					(nodes[i].mention).add(Integer.parseInt(token));
				}
			}

			/*********************************REPLY********************************/

			inFile = new File("reply_sorted_adjlist.csv");
			fstream = new FileInputStream(inFile);
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));

			while ((strLine = br.readLine()) != null)   
			{
				StringTokenizer stt = new StringTokenizer(strLine," ");
				String token = stt.nextToken();
				i = Integer.parseInt(token);

				while(stt.hasMoreTokens())
				{
					token = stt.nextToken();
					//System.out.println(Integer.parseInt(token));
					(nodes[i].reply).add(Integer.parseInt(token));
				}
			}

			/********************************RETWEET**********************************/

			inFile = new File("retweet_sorted_adjlist.csv");
			fstream = new FileInputStream(inFile);
			in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));

			while ((strLine = br.readLine()) != null)   
			{
				StringTokenizer stt = new StringTokenizer(strLine," ");
				String token = stt.nextToken();
				i = Integer.parseInt(token);

				while(stt.hasMoreTokens())
				{
					token = stt.nextToken();
					(nodes[i].retweet).add(Integer.parseInt(token));
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	void display(){
		for(int i=0; i<456631; i++){
			System.out.println("id : " + nodes[i].id + " state : " + nodes[i].state + " follow: " + nodes[i].follow + " retweet : " + nodes[i].retweet + " reply : " + nodes[i].reply + " mention : " + nodes[i].mention);
		}
	}

	double calcFraction(int s){
		int count = 0;
		for(int i=0; i<456631; i++){
			if(nodes[i].state == s)count++;
		}
		return count/456631.0 * 100;
	}

	void runHighInfluenceSimulation(double betaVal,double R1,double R2){

		// beta = 0 for single contagion model
		// beta  > 0 for multistage contagion model
		double beta = 0;
		
		// Two runs. First run, beta = 0 => Single stage model. Second run, beta = 0.5 => Multi stage model
		for(int k=0; k<2; k++){

			System.out.println("Beta = " + beta);

			// Initialise all nodes as inactive
			for(int i=0; i<456631; i++){
				nodes[i].state = 0;
			}

			// Initialise 2% random nodes as active
			for(int i=0; i<9000; i++){
				nodes[i].state = 1;
			}

			// Initial fractions
			double hyperactive = calcFraction(2);
			double active = 100 - calcFraction(0);
			System.out.println("Active : " + active + "   Hyperactive : " + hyperactive);
			//System.out.printf("%4.4f\n", active);
			
			// 20 iterations over graph sufficient for equilibrium
			for(int iteration=0; iteration<20; iteration++){

				// For all nodes
				for(int i=0; i<456631; i++){

					//if(nodes[i].state == 2)continue;
					
					// Calculate m1 and m2 over four layers
					double m1_follow = 0, m1_reply = 0, m1_retweet = 0, m1_mention = 0;
					double m2_follow = 0, m2_reply = 0, m2_retweet = 0, m2_mention = 0;
					double f = 0, f1 = 0, f2 = 0, f3 = 0, f4 = 0;

					for(int j : nodes[i].follow){
						if(j >= 0 && nodes[j].state > 0)m1_follow++;
						if(j >= 0 && nodes[j].state == 2)m2_follow++;
					}
					f1 = (m1_follow + beta*m2_follow)/(nodes[i].follow).size(); //if -1 only, then follow = 0 and size = 1

					for(int j : nodes[i].reply){
						if(j >= 0 && nodes[j].state > 0)m1_reply++;
						if(j >= 0 && nodes[j].state == 2)m2_reply++;
					}
					f2 = (m1_reply + beta*m2_reply)/(nodes[i].reply).size(); //if -1 only, then reply = 0 and size = 1

					for(int j : nodes[i].mention){
						if(j >= 0 && nodes[j].state > 0)m1_mention++;
						if(j >= 0 && nodes[j].state == 2)m2_mention++;
					}
					f3 = (m1_mention + beta*m2_mention)/(nodes[i].mention).size(); //if -1 only, then mention = 0 and size = 1

					for(int j : nodes[i].retweet){
						if(j >= 0 && nodes[j].state > 0)m1_retweet++;
						if(j >= 0 && nodes[j].state == 2)m2_retweet++;
					}
					f4 = (m1_retweet + beta*m2_retweet)/(nodes[i].retweet).size(); //if -1 only, then retweet = 0 and size = 1

					// Total peer pressure - simple or weighted mean
					f = (f1 + f2 + f3 + f4)/4;

					// Update activity state based on whether or not peer pressure crosses threshold
					if(f >= R1 && nodes[i].state != 2)nodes[i].state = 1;
					if(f >= R2)nodes[i].state = 2;


					//Fraction activated after ith iteration
					/*
					if(i % 100000 == 0){
						hyperactive = calcFraction(2);
						active = 100 - calcFraction(0);
						//System.out.println("Active : " + active + "   Hyperactive : " + hyperactive);
						System.out.println(active + " " + hyperactive);
					}
					*/
				}
				
				// Print fractions in all states after every iteration
				hyperactive = calcFraction(2);
				active = 100 - calcFraction(0);
				System.out.println("Active : " + active + "   Hyperactive : " + hyperactive);
				//System.out.printf("%4.4f\n", active);
			}
			//Update beta for second round
			beta = betaVal;
		}
	}

	void runLowInfluenceSimulation(double betaVal,double R1,double R2){

		// beta = 1 for single contagion model - All influence from semiactive nodes subtracted
		//beta  < 1 for multistage contagion model - Account for partial influence
		
		double beta = 1;
		for(int k=0; k<2; k++){

			System.out.println("Beta = " + beta);

			// Initialise all nodes as inactive
			for(int i=0; i<456631; i++){
				nodes[i].state = 0;
			}

			// Initialise 2% random nodes as active
			for(int i=0; i<9000; i++){
				nodes[i].state = 2;
			}

			//Fraction activated before 0th iteration
			double active = calcFraction(2);
			double semiactive = 100 - calcFraction(0);
			//System.out.println("Active : " + active + "   Semiactive : " + semiactive);


			// 20 iterations over graph - sufficient for equilibrium
			for(int iteration=0; iteration<20; iteration++){


				// For all nodes
				for(int i=0; i<456631; i++){

					//if(nodes[i].state == 2)continue;

					// Calculate m1 and m2 over four layers
					double m1_follow = 0, m1_reply = 0, m1_retweet = 0, m1_mention = 0;
					double m2_follow = 0, m2_reply = 0, m2_retweet = 0, m2_mention = 0;
					double f = 0, f1 = 0, f2 = 0, f3 = 0, f4 = 0;

					for(int j : nodes[i].follow){
						if(j >= 0 && nodes[j].state > 0)m1_follow++;
						if(j >= 0 && nodes[j].state == 1)m2_follow++;
					}
					f1 = (m1_follow - beta*m2_follow)/(nodes[i].follow).size(); //if -1 only, then follow = 0 and size = 1

					for(int j : nodes[i].reply){
						if(j >= 0 && nodes[j].state > 0)m1_reply++;
						if(j >= 0 && nodes[j].state == 1)m2_reply++;
					}
					f2 = (m1_reply - beta*m2_reply)/(nodes[i].reply).size(); //if -1 only, then reply = 0 and size = 1

					for(int j : nodes[i].mention){
						if(j >= 0 && nodes[j].state > 0)m1_mention++;
						if(j >= 0 && nodes[j].state == 1)m2_mention++;
					}
					f3 = (m1_mention - beta*m2_mention)/(nodes[i].mention).size(); //if -1 only, then mention = 0 and size = 1

					for(int j : nodes[i].retweet){
						if(j >= 0 && nodes[j].state > 0)m1_retweet++;
						if(j >= 0 && nodes[j].state == 1)m2_retweet++;
					}
					f4 = (m1_retweet - beta*m2_retweet)/(nodes[i].retweet).size(); //if -1 only, then retweet = 0 and size = 1

					// Net peer pressure - simple or weighted mean from the four layers
					f = (f1 + f2 + f3 + f4)/4;

					// Update activity state based on whether or not peer pressure crosses threshold
					if(f >= R1 && nodes[i].state != 2)nodes[i].state = 1;
					if(f >= R2)nodes[i].state = 2;
					
					/*
					if(i % 100000 == 0){
						active = calcFraction(2);
						semiactive = 100 - calcFraction(0);
						System.out.println(semiactive + " " + active);
					}
					*/
					
				}
				
				// Print fractions after every iteration
				active = calcFraction(2);
				semiactive = 100 - calcFraction(0);
				System.out.println("Active : " + active + "   Semiactive : " + semiactive);

			}
			
			// Update beta for second round
			beta = betaVal;
		}
	}

}
