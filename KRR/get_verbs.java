import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

//CLASS FILE FOR GET_VERBS WHICH GETS ALL THE VERBS FROM THE INPUT FILES AND STORED THEM IN THE ARRAYS OF OBJECTS MENTIONED BELOW
public class get_verbs {
	mtrans[] mtrans_verbs = new mtrans[500]; //AN ARRAY OF OBJECTS OF TYPE MTRANS FOR THE MTRANS VERBS
	speak[] speak_verbs = new speak[500]; //AN ARRAY OF OBJECTS OF TYPE SPEAK FOR THE SPEAK VERBS
	ptrans[] ptrans_verbs = new ptrans[500]; //AN ARRAY OF OBJECTS OF TYPE PTRANS FOR THE PTRANS VERBS
	atrans[] atrans_verbs = new atrans[500]; //AN ARRAY OF OBJECTS OF TYPE ATRANS FOR THE ATRANS VERBS
	propel[] propel_verbs = new propel[500];//AN ARRAY OF OBJECTS OF TYPE PROPEL FOR THE PROPEL VERBS
	mbuild[] mbuild_verbs = new mbuild[500];//AN ARRAY OF OBJECTS OF TYPE MBUILD FOR THE MBUILD VERBS
	do_something1[] do_something_1_verbs = new do_something1[500]; //AN ARRAY OF OBJECTS OF TYPE DO_SOMETHING1 FOR THE DO-SOMETHING1 VERBS
	do_something2[] do_something_2_verbs = new do_something2[500]; //AN ARRAY OF OBJECTS OF TYPE DO_SOMETHING2 FOR THE DO-SOMETHING2 VERBS
	expel[] expel_verbs = new expel[500]; //AN ARRAY OF OBJECTS OF TYPE EXPEL FOR THE EXPEL VERBS
	grasp[] grasp_verbs = new grasp[500]; //AN ARRAY OF OBJECTS OF TYPE GRASP FOR THE GRASP VERBS
	ingest[] ingest_verbs = new ingest[500]; //AN ARRAY OF OBJECTS OF TYPE INGEST FOR THE INGEST VERBS
	attend[] attend_verbs = new attend[500]; //AN ARRAY OF OBJECTS OF TYPE ATTEND FOR THE ATTEND VERBS
	state[] state_verbs = new state[500];  //AN ARRAY OF OBJECTS OF TYPE STATE FOR THE STATE VERBS
	int k,expelcount,graspcount,ingestcount,attendcount,statecount; //COUNT OF THE NUMBER OF VERBS

	public void mtrans()
	{
		try 
		{
			File inFile = new File("MTRANS"); //READING THE MTRANS FILE CONTAINING THE MTRANS VERBS
		    FileInputStream fstream = new FileInputStream(inFile);
		    DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int i=0;
			try 
			{
				while ((strLine = br.readLine()) != null)   
				{
					StringTokenizer stt = new StringTokenizer(strLine," "); //TOKENIZING EACH LINE OF THE FILE READ
					mtrans_verbs[i] = new mtrans();
					int j=0;
					int to_be_changed=0; //BASED ON THE ACTION, THE STATES OF THE ACTOR / OBJECT MAY UNDERGO CHANGE. THIS VARIABLE TELLS WHETHER THE OBJECT OR THE ACTOR IS UNDERGOING THE STATE CHANGE
					String field_to_be_changed = new String(); //GIVES THE PARTICULAR STATE OF THE ENTITY BEING CHANGED. LIKE PHYSICAL STATE, MENTAL STATE.
			        while (stt.hasMoreTokens()){
			            String token = stt.nextToken(); //GET NEXT TOKEN IN THE LINE READ
			            if(token.substring(0,1).equals("$"))
			            	mtrans_verbs[i].conceptual_tense = token.substring(1); //GET TENSE OF THE VERB
			            //System.out.println(token);
			           if(j==0)
			           {
			        	   mtrans_verbs[i].name = token; //GET NAME
			        	   mtrans_verbs[i].token = token; //GET IDENTIFER
			           }
			           if(j==3)
			           {
			        	   to_be_changed = token.charAt(token.length()-1) - '0'; //GET THE ENTITY THAT UDNERGOES CHANGE (1 -> ACTOR , 2 -> OBJECT)
			        	   //System.out.println(to_be_changed);
			           }
			           if(j==4)
			           {
			        	   field_to_be_changed = token; //GET THE FEILD TO BE CHANGED. LIKE MENTAL_STATE, PHYSICAL_STATE.
			        	  // System.out.println(field_to_be_changed);
			           }
			           if(j==5)
			           {
			        	   if(to_be_changed==1) //ACTOR TO BE CHANGED
			        	   {
			        		  // System.out.println(field_to_be_changed);
			        		   if(field_to_be_changed.equals( "mental_state"))
			        		   {
			        			   //System.out.println("check");
			        			   mtrans_verbs[i].actor.mental_state = Integer.parseInt(token); //CHANFGE THE MENTAL STATE OF THE ACTOR
			        			   //System.out.println(Integer.parseInt(token));
			        		   }
			        		   if(field_to_be_changed .equals("physical_state"))
			        			   mtrans_verbs[i].actor.physical_state = Integer.parseInt(token); //CHANGE THE PHYSICAL STATE OF THE ACTOR
			        		   if(field_to_be_changed.equals("health"))
			        			   mtrans_verbs[i].actor.health = Integer.parseInt(token);//CHANGE THE HEALTH STATE OF THE ACTOR
			        		   if(field_to_be_changed.equals("anger"))
			        			   mtrans_verbs[i].actor.anger = Integer.parseInt(token); //CHANGE THE ANGER STATE OF THE ACTOR
			        		   if(field_to_be_changed.equals("fear") )
			        			   mtrans_verbs[i].actor.fear = Integer.parseInt(token); //CHANGE THE FEAR STATE OF THE ACTOR
			        	   }
			        	   if(to_be_changed==2) //CHANGE STATES OF THE DESTINATION OF THE ACTION
			        	   {
			        		   //System.out.println(field_to_be_changed);
			        		   if(field_to_be_changed.equals("mental_state"))
			        		   {
			        			   //System.out.println("check");
			        			   mtrans_verbs[i].dest.mental_state = Integer.parseInt(token); //CHANGE THE MENTAL STATE STATE OF THE DESTINATION
			        			   //System.out.println(Integer.parseInt(token));
			        		   }
			        		   if(field_to_be_changed .equals("physical_state"))
			        			   mtrans_verbs[i].dest.physical_state = Integer.parseInt(token); //CHANGE THE PHYSICAL STATE STATE OF THE DESTINATION
			        		   if(field_to_be_changed.equals("health"))
			        			   mtrans_verbs[i].dest.health = Integer.parseInt(token);//CHANGE THE HEALTH STATE STATE OF THE DESTINATION
			        		   if(field_to_be_changed.equals("anger"))
			        			   mtrans_verbs[i].dest.anger = Integer.parseInt(token); //CHANGE THE MENTAL ANGER STATE OF THE DESTINATION
			        		   if(field_to_be_changed.equals("fear") )
			        			   mtrans_verbs[i].dest.fear = Integer.parseInt(token); //CHANGE THE FEAR STATE STATE OF THE DESTINATION
			        	   }
			        	   
			           }
			           j++; //GET NEXT TOKEN FROM THE LINE
			        }
			        //STORES THE CD FORM OF THE MTRANS VERB
			        mtrans_verbs[i].part_of_speech = "verb";
			        mtrans_verbs[i].cd_form = "(MTRANS"
																				+"(FROM VAR1)"
																				+"(OBJECT VAR2)"
																				+"(ACTOR VAR3)"
																				+"(CONC_TENSE VAR4)"
																				+"(USING VAR5)"
																				+"(ADVERB VAR6)"
																				+	")";
					i++; //GET NEXT LINE AND VERB
				}
				int k;
				//UNCOMMENT THE FOLLOWING 2 PRINT STATEMENTS TO CHECK THE OUTPUT OF STORING THE DATA STRUCTURES.
				for(k=0;k<i;k++)
				{
					//System.out.println(mtrans_verbs[k].name + " Initial State " + mtrans_verbs[k].source.mental_state + "Final State " + mtrans_verbs[k].dest.mental_state);
					//System.out.println(mtrans_verbs[k].name + " "+ mtrans_verbs[k].conceptual_tense);
				}
					br.close();
			} 
			//EXCEPTION CONDITION
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			//STORING THE SOURCE AND DESTINATION OF SPECIAL TYPE OF WORDS HAVING THESE FIELDS AS LTM / CP.
			for(k=0;k<i;k++)
			{
				if(mtrans_verbs[k].name.equals("remember") || mtrans_verbs[k].name.equals("think") ||mtrans_verbs[k].name.equals("recall"))
				{
					mtrans_verbs[k].source.name = "LTM"; //STORE THE SOURCE AS LTM FOR WORDS LIKE REMEMBER, THINK, RECALL
					mtrans_verbs[k].dest.name = "CP"; //STORE THE DESTINATION AS CP FOR WORDS LIKE REMEMBER, THINK, RECALL
				}
				if(mtrans_verbs[k].name.equals("read"))
				{
					mtrans_verbs[k].dest.name = "CP"; //STORE THE DESTINATION AS CP FOR WORDS LIKE READ
					mtrans_verbs[k].source.name = "EYE"; //STORE THE DOURCE AS EYE FOR WORDS LIKE READ
				}
				if(mtrans_verbs[k].name.equals("understand"))
				{
					mtrans_verbs[k].dest.name = "LTM"; //STORE THE DESTINATION AS LTM FOR WORDS LIKE UNDERSTAND
					mtrans_verbs[k].source.name = "CP";//STORE THE SOURCE AS CP FOR WORDS LIKE UNDERSTAND
				}
				if(mtrans_verbs[k].name.equals("hate") || mtrans_verbs[k].name.equals("like") || mtrans_verbs[k].name.equals("dislike")){
					mtrans_verbs[k].source.name = "LTM"; //STORE THE SOURCE AS LTM FOR WORDS LIKE HATE LIKE DISLIKE
					mtrans_verbs[k].dest.name = "CP"; //STORE THE DESTINATION AS CP FOR WORDS LIKE HATE LIKE DISLIKE
				}
			}	
		} 
		catch (FileNotFoundException fe)
		{
		    fe.printStackTrace();
		}
			
		
	}
	public void speak()
	{
		try 
		{
			File inFile = new File("SPEAK"); //READING THE SPEAK FILE CONTAINING THE SPEAK VERBS
		    FileInputStream fstream = new FileInputStream(inFile);
		    DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int i=0;
			try 
			{
				while ((strLine = br.readLine()) != null)   
				{
					StringTokenizer stt = new StringTokenizer(strLine," "); //TOKENIZING EACH LINE OF THE FILE READ
					speak_verbs[i] = new speak();
					int j=0;
					int to_be_changed=0; //BASED ON THE ACTION, THE STATES OF THE ACTOR / OBJECT MAY UNDERGO CHANGE. THIS VARIABLE TELLS WHETHER THE OBJECT OR THE ACTOR IS UNDERGOING THE STATE CHANGE
					String field_to_be_changed = new String(); //GIVES THE PARTICULAR STATE OF THE ENTITY BEING CHANGED. LIKE PHYSICAL STATE, MENTAL STATE.
			        while (stt.hasMoreTokens()){
			            String token = stt.nextToken(); //GET NEXT TOKEN IN THE LINE READ
			            if(token.substring(0,1).equals("$"))
			            	speak_verbs[i].conceptual_tense = token.substring(1); //GET TENSE OF THE VERB
			            //System.out.println(token);
			           if(j==0)
			           {
			        	   speak_verbs[i].name = token; //GET NAME
			        	   speak_verbs[i].token = token; //GET IDENTIFIER
			           }
			           if (j==1 && (token.equals("(INTERROGATIVE)")))
			           {
			        	   speak_verbs[i].interrogative =1; //TO SEE IF THE SPEAK TYPE OF VERB IS INTERROGATIVE LIKE ASK. 
			           }
			           if(j==3)
			           {
			        	   to_be_changed = token.charAt(token.length()-1) - '0'; //GET THE ENTITY THAT UDNERGOES CHANGE (1 -> ACTOR , 2 -> OBJECT)
			        	   //System.out.println(to_be_changed);
			           }
			           if(j==4)
			           {
			        	   field_to_be_changed = token; //GET THE ENTITY THAT UDNERGOES CHANGE (1 -> ACTOR , 2 -> OBJECT)
			        	  // System.out.println(field_to_be_changed);
			           }
			           if(j==5)
			           {
			        	   if(to_be_changed==1)
			        	   {
			        		  // System.out.println(field_to_be_changed);
			        		   if(field_to_be_changed.equals( "mental_state"))
			        		   {
			        			   //System.out.println("check");
			        			   speak_verbs[i].source.mental_state = Integer.parseInt(token); //CHANFGE THE MENTAL STATE OF THE ACTOR
			        			   //System.out.println(Integer.parseInt(token));
			        		   }
			        		   if(field_to_be_changed .equals("physical_state"))
			        			   speak_verbs[i].source.physical_state = Integer.parseInt(token); //CHANGE THE PHYSICAL STATE OF THE ACTOR
			        		   if(field_to_be_changed.equals("health"))
			        			   speak_verbs[i].source.health = Integer.parseInt(token); //CHANGE THE HEALTH STATE OF THE ACTOR
			        		   if(field_to_be_changed.equals("anger"))
			        			   speak_verbs[i].source.anger = Integer.parseInt(token); //CHANGE THE ANGER STATE OF THE ACTOR
			        		   if(field_to_be_changed.equals("fear") )
			        			   speak_verbs[i].source.fear = Integer.parseInt(token); //CHANGE THE FEAR STATE OF THE ACTOR
			        	   }
			        	   if(to_be_changed==2) //CHANGE STATES OF THE DESTINATION OF THE ACTION
			        	   {
			        		   //System.out.println(field_to_be_changed);
			        		   if(field_to_be_changed.equals("mental_state"))
			        		   {
			        			   //System.out.println("check");
			        			   speak_verbs[i].dest.mental_state = Integer.parseInt(token); //CHANGE THE MENTAL STATE STATE OF THE DESTINATION
			        			   //System.out.println(Integer.parseInt(token));
			        		   }
			        		   if(field_to_be_changed .equals("physical_state"))
			        			   speak_verbs[i].dest.physical_state = Integer.parseInt(token); //CHANGE THE PHYSICAL STATE STATE OF THE DESTINATION
			        		   if(field_to_be_changed.equals("health"))
			        			   speak_verbs[i].dest.health = Integer.parseInt(token); //CHANGE THE HEALTH STATE STATE OF THE DESTINATION
			        		   if(field_to_be_changed.equals("anger"))
			        			   speak_verbs[i].dest.anger = Integer.parseInt(token); //CHANGE THE MENTAL ANGER STATE OF THE DESTINATION
			        		   if(field_to_be_changed.equals("fear") )
			        			   speak_verbs[i].dest.fear = Integer.parseInt(token); //CHANGE THE FEAR STATE STATE OF THE DESTINATION
			        	   }
			        	   
			           }
			           j++; //GET NEXT TOKEN FROM THE LINE
			        }
			      //STORES THE CD FORM OF THE SPEAK VERB
			        speak_verbs[i].part_of_speech = "verb";
			        speak_verbs[i].cd_form = "(SPEAK"
																				+"(FROM VAR1)"
																				+"(TO VAR2)"
																				+"(OBJECT VAR3)"
																				+"(CONC_TENSE VAR4)"
																				+"(USING VAR5)"
																				+"(ADVERB VAR6)"
																				+")";

					i++; //GET NEXT LINE AND VERB
				}
				int k;
				//UNCOMMENT THE FOLLOWING 2 PRINT STATEMENTS TO CHECK THE OUTPUT OF STORING THE DATA STRUCTURES.
				for(k=0;k<i;k++)
				//	System.out.println(speak_verbs[k].name + " "+ speak_verbs[k].conceptual_tense);
				br.close();
			} 
			//EXCEPTION CONDITION
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		} 
		catch (FileNotFoundException fe)
		{
		    fe.printStackTrace();
		}
		
	}
	public void ptrans()
	{
		try 
		{
			File inFile = new File("PTRANS"); //READING THE PTRANS FILE CONTAINING THE MTRANS VERBS
		    FileInputStream fstream = new FileInputStream(inFile);
		    DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int i=0;
			try 
			{
				while ((strLine = br.readLine()) != null)   
				{
					StringTokenizer stt = new StringTokenizer(strLine," "); //TOKENIZING EACH LINE OF THE FILE READ
					ptrans_verbs[i] = new ptrans();
					int j=0;
					int to_be_changed=0; //BASED ON THE ACTION, THE STATES OF THE ACTOR / OBJECT MAY UNDERGO CHANGE. THIS VARIABLE TELLS WHETHER THE OBJECT OR THE ACTOR IS UNDERGOING THE STATE CHANGE
					String field_to_be_changed = new String(); //GIVES THE PARTICULAR STATE OF THE ENTITY BEING CHANGED. LIKE PHYSICAL STATE, MENTAL STATE.
			        while (stt.hasMoreTokens()){
			            String token = stt.nextToken(); //GET NEXT TOKEN IN THE LINE READ
			            if(token.substring(0,1).equals("$"))
			            	ptrans_verbs[i].conceptual_tense = token.substring(1); //GET TENSE OF THE VERB
			            //System.out.println(token);
			           if(j==0)
			           {
			        	   ptrans_verbs[i].name = token; //GET NAME
			        	   ptrans_verbs[i].token = token; //GET IDENTIFER
			           }
			           if(j==3)
			           {
			        	   to_be_changed = token.charAt(token.length()-1) - '0';//GET THE ENTITY THAT UDNERGOES CHANGE (1 -> ACTOR , 2 -> OBJECT)
			        	   //System.out.println(to_be_changed);
			           }
			           if(j==4)
			           {
			        	   field_to_be_changed = token;
			        	  // System.out.println(field_to_be_changed);
			           }
			           if(j==5)
			           {
			        	   if(to_be_changed==1) //ACTOR TO BE CHANGED
			        	   {
			        		   //CHANGE THE CORRESPONDING STATES OF THE ACTOR.
			        		  // System.out.println(field_to_be_changed);
			        		   if(field_to_be_changed.equals( "mental_state"))
			        		   {
			        			   //System.out.println("check");
			        			   ptrans_verbs[i].source.mental_state = Integer.parseInt(token);
			        			   //System.out.println(Integer.parseInt(token));
			        		   }
			        		   if(field_to_be_changed .equals("physical_state"))
			        			   ptrans_verbs[i].source.physical_state = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("health"))
			        			   ptrans_verbs[i].source.health = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("anger"))
			        			   ptrans_verbs[i].source.anger = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("fear") )
			        			   ptrans_verbs[i].source.fear = Integer.parseInt(token);
			        	   }
			        	   if(to_be_changed==2) //DESTINATION TO BE CHANGED
			        	   {
			        		 //CHANGE THE CORRESPONDING STATES OF THE DESTINATION.
			        		   //System.out.println(field_to_be_changed);
			        		   if(field_to_be_changed.equals("mental_state"))
			        		   {
			        			   //System.out.println("check");
			        			   ptrans_verbs[i].dest.mental_state = Integer.parseInt(token);
			        			   //System.out.println(Integer.parseInt(token));
			        		   }
			        		   if(field_to_be_changed .equals("physical_state"))
			        			   ptrans_verbs[i].dest.physical_state = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("health"))
			        			   ptrans_verbs[i].dest.health = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("anger"))
			        			   ptrans_verbs[i].dest.anger = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("fear") )
			        			   ptrans_verbs[i].dest.fear = Integer.parseInt(token);
			        	   }
			        	   
			           }
			           j++; //GET NEXT TOKEN IN THE LINE READN
			        }
			        ptrans_verbs[i].part_of_speech = "verb";
			        ptrans_verbs[i].cd_form = "(PTRANS"
																					+"(FROM VAR1)"
																					+"(TO VAR2)"
																					+"(OBJECT VAR3)"
																					+"(ACTOR VAR4)"
																					+"(CONC_TENSE VAR5)"
																					+"(USING VAR6)"
																					+"(ADVERB VAR7)"
																					+")";
					i++; //GET NEXT TOKEN FROM NEXT LINE
				}
				int k;
				//UNCOMMENT THE FOLLOWING 2 PRINT STATEMENTS TO CHECK THE OUTPUT OF STORING THE DATA STRUCTURES.
				for(k=0;k<i;k++)
				{
				//	System.out.println(ptrans_verbs[k].name + " "+ ptrans_verbs[k].conceptual_tense);
				}
					br.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		} 
		catch (FileNotFoundException fe)
		{
		    fe.printStackTrace();
		}
		
	}
	public void atrans()
	{
		try 
		{
			File inFile = new File("ATRANS"); //READING THE MTRANS FILE CONTAINING THE ATRANS VERBS
		    FileInputStream fstream = new FileInputStream(inFile);
		    DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int i=0;
			try 
			{
				while ((strLine = br.readLine()) != null)   
				{
					StringTokenizer stt = new StringTokenizer(strLine," "); //TOKENIZING EACH LINE OF THE FILE READ
					atrans_verbs[i] = new atrans();
					int j=0;
					int to_be_changed=0; //BASED ON THE ACTION, THE STATES OF THE ACTOR / OBJECT MAY UNDERGO CHANGE. THIS VARIABLE TELLS WHETHER THE OBJECT OR THE ACTOR IS UNDERGOING THE STATE CHANGE
					String field_to_be_changed = new String(); //GIVES THE PARTICULAR STATE OF THE ENTITY BEING CHANGED. LIKE PHYSICAL STATE, MENTAL STATE.
			        while (stt.hasMoreTokens()){
			            String token = stt.nextToken();//GET NEXT TOKEN IN THE LINE READ
			            if(token.substring(0,1).equals("$"))
			            	atrans_verbs[i].conceptual_tense = token.substring(1); //GET TENSE OF THE VERB
			            //System.out.println(token);
			           if(j==0)
			           {
			        	   atrans_verbs[i].name = token;
			        	   atrans_verbs[i].token = token;
			           }
			           if(j==3)
			           {
			        	   to_be_changed = token.charAt(token.length()-1) - '0'; //GET THE ENTITY THAT UDNERGOES CHANGE (1 -> ACTOR , 2 -> OBJECT)
			        	   //System.out.println(to_be_changed);
			           }
			           if(j==4)
			           {
			        	   field_to_be_changed = token; //GET THE FEILD TO BE CHANGED. LIKE MENTAL_STATE, PHYSICAL_STATE.
			        	  // System.out.println(field_to_be_changed);
			           }
			           if(j==5)
			           {
			        	   if(to_be_changed==1) //ACTOR TO BE CHANGED
			        	   {
			        		   //CHANGE THE APPROPRATE STATE OF THE ACTOR
			        		  // System.out.println(field_to_be_changed);
			        		   if(field_to_be_changed.equals( "mental_state"))
			        		   {
			        			   //System.out.println("check");
			        			   atrans_verbs[i].source.mental_state = Integer.parseInt(token);
			        			   //System.out.println(Integer.parseInt(token));
			        		   }
			        		   if(field_to_be_changed .equals("physical_state"))
			        			   atrans_verbs[i].source.physical_state = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("health"))
			        			   atrans_verbs[i].source.health = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("anger"))
			        			   atrans_verbs[i].source.anger = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("fear") )
			        			   atrans_verbs[i].source.fear = Integer.parseInt(token);
			        	   } 
			        	   if(to_be_changed==2) //CHANGE STATES OF THE DESTINATION OF THE ACTION
			        	   {
			        		   //CHANGE THE STATES OF THE DESTINATION APPROPRATELY.
			        		   //System.out.println(field_to_be_changed);
			        		   if(field_to_be_changed.equals("mental_state"))
			        		   {
			        			   //System.out.println("check");
			        			   atrans_verbs[i].dest.mental_state = Integer.parseInt(token);
			        			   //System.out.println(Integer.parseInt(token));
			        		   }
			        		   if(field_to_be_changed .equals("physical_state"))
			        			   atrans_verbs[i].dest.physical_state = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("health"))
			        			   atrans_verbs[i].dest.health = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("anger"))
			        			   atrans_verbs[i].dest.anger = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("fear") )
			        			   atrans_verbs[i].dest.fear = Integer.parseInt(token);
			        	   }
			        	   
			           }
			           if(j==6)
			        	   atrans_verbs[i].conceptual_tense = token;
			           j++; //GET NEXT TOKEN IN THE LINE READ
			        }
			        //GET CD FORM
			        atrans_verbs[i].part_of_speech = "verb";
			        atrans_verbs[i].cd_form = "(ATRANS"
																				+"(FROM VAR1)"
																				+"(TO VAR2)"
																				+"(OBJECT VAR3)"
																				+"(ACTOR VAR4)"
																				+"(CONC_TENSE VAR5)"
																				+"(USING VAR6)"
																				+"(ADVERB VAR7)"
																				+")";

					i++; //GET NEXT TOEKN
				}
				int k;
				//UNCOMMENT THE FOLLOWING 2 PRINT STATEMENTS TO CHECK THE OUTPUT OF STORING THE DATA STRUCTURES.
				for(k=0;k<i;k++)
				{
				//	System.out.println(atrans_verbs[k].name + " "+ atrans_verbs[k].conceptual_tense);
				}
				br.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		} 
		catch (FileNotFoundException fe)
		{
		    fe.printStackTrace();
		}
		
	}
	public void propel()
	{
		try 
		{
			//THE PROCEDURE FOLLOWED FOR THE ABOVE VERBS IS REPEATED FOR OTHER VERBS HERE.
			File inFile = new File("PROPEL");  //READ FROM PROPEL FILE THE PROPEL VERBS
		    FileInputStream fstream = new FileInputStream(inFile);
		    DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int i=0;
			try 
			{
				while ((strLine = br.readLine()) != null)   
				{
					StringTokenizer stt = new StringTokenizer(strLine," "); //READ HTE FILE LINE BY LINE AND TOKENIZE THE LINE
					propel_verbs[i] = new propel();
					int j=0;
					int to_be_changed=0;
					String field_to_be_changed = new String();
			        while (stt.hasMoreTokens())
			        {
			            String token = stt.nextToken();
			            if(token.substring(0,1).equals("$"))
			            	propel_verbs[i].conceptual_tense = token.substring(1); //GET TENSE
			            //System.out.println(token);
			           if(j==0)
			           {
			        	   propel_verbs[i].name = token;
			        	   propel_verbs[i].token = token;
			           }
			           if(j==3)
			           {
			        	   to_be_changed = token.charAt(token.length()-1) - '0'; //GET THE ONE TO BE CAHGNED
			        	   //System.out.println(to_be_changed); 
			           }
			           if(j==4)
			           {
			        	   field_to_be_changed = token; //GET THE FEILD TO BE CHAGNED
			        	  // System.out.println(field_to_be_changed);
			           }
			           if(j==5)
			           {
			        	   if(to_be_changed==1)
			        	   {
			        		   	//CHANGE THE FEILDS APPROPRIATELY OF THE SOURCE
			        		  // System.out.println(field_to_be_changed);
			        		   if(field_to_be_changed.equals( "mental_state"))
			        		   {
			        			   //System.out.println("check");
			        			   propel_verbs[i].source.mental_state = Integer.parseInt(token);
			        			   //System.out.println(Integer.parseInt(token));
			        		   }
			        		   if(field_to_be_changed .equals("physical_state"))
			        			   propel_verbs[i].source.physical_state = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("health"))
			        			   propel_verbs[i].source.health = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("anger"))
			        			   propel_verbs[i].source.anger = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("fear") )
			        			   propel_verbs[i].source.fear = Integer.parseInt(token);
			        	   }
			        	   if(to_be_changed==2)
			        	   {
			        		   //System.out.println(field_to_be_changed);
			        		   //CHANGE THE FIELDS APPROPRIATELY OF THE DESTINATION
			        		   if(field_to_be_changed.equals("mental_state"))
			        		   {
			        			   //System.out.println("check");
			        			   propel_verbs[i].dest.mental_state = Integer.parseInt(token);
			        			   //System.out.println(Integer.parseInt(token));
			        		   }
			        		   if(field_to_be_changed .equals("physical_state"))
			        		   {
			        			  // System.out.println(Integer.parseInt(token));
			        			   propel_verbs[i].dest.physical_state = Integer.parseInt(token);
			        		   }
			        		   if(field_to_be_changed.equals("health"))
			        			   propel_verbs[i].dest.health = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("anger"))
			        			   propel_verbs[i].dest.anger = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("fear") )
			        			   propel_verbs[i].dest.fear = Integer.parseInt(token);
			        	   }
			        	   
			           }
			           //GET THE RESULT OF THE ACTION
			           if(j==8)
			           {
			        	   to_be_changed = token.charAt(token.length()-1) - '0'; //GET THE RESULTANT
			        	   //System.out.println(to_be_changed);
			           }
			           if(j==9)
			           {
			        	   field_to_be_changed = token; //GET THE STATE TO BE CANGED OF THE RESULTANT
			        	  // System.out.println(field_to_be_changed);
			           }
			           if(j==10)
			           {
			        	   if(to_be_changed==1)
			        	   {
			        		  // System.out.println(field_to_be_changed);
			        		   //CHANGE APPROPRIATELY THE STATE OF THE SOURCE
			        		   if(field_to_be_changed.equals( "mental_state"))
			        		   {
			        			   //System.out.println("check");
			        			   propel_verbs[i].source.mental_state = Integer.parseInt(token);
			        			   //System.out.println(Integer.parseInt(token));
			        		   }
			        		   if(field_to_be_changed .equals("physical_state"))
			        		   {
			        		//	   System.out.println(Integer.parseInt(token));
			        			   propel_verbs[i].source.physical_state = Integer.parseInt(token);
			        		   }
			        		   if(field_to_be_changed.equals("health"))
			        			   propel_verbs[i].source.health = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("anger"))
			        			   propel_verbs[i].source.anger = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("fear") )
			        			   propel_verbs[i].source.fear = Integer.parseInt(token);
			        	   }
			        	   if(to_be_changed==2)
			        	   {
			        		   //System.out.println(field_to_be_changed);
			        		   //CHANGE THE STAE OF THE DESTINATION
			        		   if(field_to_be_changed.equals("mental_state"))
			        		   {
			        			   //System.out.println("check");
			        			   propel_verbs[i].dest.mental_state = Integer.parseInt(token);
			        			   //System.out.println(Integer.parseInt(token));
			        		   }
			        		   if(field_to_be_changed .equals("physical_state"))
			        			   propel_verbs[i].dest.physical_state = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("health"))
			        			   propel_verbs[i].dest.health = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("anger"))
			        			   propel_verbs[i].dest.anger = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("fear") )
			        			   propel_verbs[i].dest.fear = Integer.parseInt(token);
			        	   }
			        	   
			           }
			           j++; //READ NEXT TOEKNN FROM THE LINE
			        }
			        propel_verbs[i].part_of_speech = "verb";
			        propel_verbs[i].cd_form = "(PROPEL"
																					+"(FROM VAR1)"
																					+"(TO VAR2)"
																					+"(OBJECT VAR3)"
																					+"(PHYSCONT VAR4 VAR5)"
																					+"(CONC_TENSE VAR6)"
																					+"(USING VAR7)"
																					+"(ADVERB VAR7)"
																					+")";
					i++; //READ NEXT LINE
				}
				int k;
				//UCOMMENT TO SEE OUTPUT
				for(k=0;k<i;k++)
				{
				//	System.out.println(propel_verbs[k].name + " "+ propel_verbs[k].conceptual_tense);
				}
					br.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		} 
		catch (FileNotFoundException fe)
		{
		    fe.printStackTrace();
		}
		
	}
	public void mbuild()
	{
		try 
		{
			File inFile = new File("MBUILD"); //READ MBUILD FILE TO GE THE EVERBS
		    FileInputStream fstream = new FileInputStream(inFile);
		    DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int i=0;
			try 
			{
				while ((strLine = br.readLine()) != null)   
				{
					StringTokenizer stt = new StringTokenizer(strLine," "); //READ IT LINE BY LINE AND GENERATE THE TOKENS
					int j=0;
					 while (stt.hasMoreTokens())
				        {
				            String token = stt.nextToken();
				            if(token.substring(0,1).equals("$"))
				            	mbuild_verbs[i].conceptual_tense = token.substring(1); //GET THE TENSE
				            if(j==0)
				            {
								mbuild_verbs[i] = new mbuild();
								mbuild_verbs[i].name = token;
								mbuild_verbs[i].token = token;
								mbuild_verbs[i].part_of_speech = "verb";
								mbuild_verbs[i].cd_form = "(MBUILD"
																					+"(ACTOR VAR1)"
																					+"(INITIAL VAR2)"
																					+"(FINAL VAR3)"
																					+"(CONC_TENSE VAR4)"
																					+"(USING VAR5)"
																					+"(ADVERB VAR6)"
																					+")";
				            }
				            j++; //GET NEXT TOEKN NI THE LINE
				        }
					i++; //GET NEXT LINE
				}
				int k;
				//UNCOMMENT TO SEE OUTPUT
				for(k=0;k<i;k++)
				{
				//	System.out.println(mbuild_verbs[k].name + " "+ mbuild_verbs[k].conceptual_tense);
				}
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		} 
			catch (FileNotFoundException fe)
			{
			    fe.printStackTrace();
			}
	}
	public void do_something()
	{
		try 
		{
			File inFile = new File("DO_SOMETHING"); //READ FROM DO_SOMETHING FILE TO GET THE DO_SOMEING VERBS
		    FileInputStream fstream = new FileInputStream(inFile);
		    DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int i=0;
			int k=0;
			String name = new String();
			try 
			{
				while ((strLine = br.readLine()) != null)   
				{
					StringTokenizer stt = new StringTokenizer(strLine," "); //TOKENIZE EACH LINE
					int j=0;
					int to_be_changed=0;
					int flag=0;
					String field_to_be_changed = new String();
			        while (stt.hasMoreTokens())
			        {
			            String token = stt.nextToken();
			            
			            //System.out.println(token);
			           if(j==0)
			           {
			        	   name = token;
			           }
			           if(j==1 && token.equals("person")) //DO_SOMETHING1
			           {
			        	   do_something_1_verbs[i] = new do_something1();
			        	   do_something_1_verbs[i].name = name;
			        	   do_something_1_verbs[i].token = name;
			        	   do_something_1_verbs[i].part_of_speech = "verb";
			        	  // System.out.println(token);
			        	   flag=1; //DO_SOMETHING1
			           }
			           if(j==1 && token.equals("object")) //DO_SOMETHING2
			           {
			        	   do_something_2_verbs[k] = new do_something2();
			        	   do_something_2_verbs[k].name = name;
			        	   do_something_2_verbs[k].token = name;
			        	   do_something_2_verbs[k].part_of_speech = "verb";
			        	   flag=2;//DO_SOMTHING2
			           }
			           if(j==2 && token.equals("abstract")) //CHECKING FOR ABSTRACT INSTRUMENTS OF ACTIONS
			           {
			        	   if(flag==1)
			        		   do_something_1_verbs[i].using_instrument.part_of_speech = "ABSTRACT-NOUN-PHRASE";
			        	   else
			        		   do_something_2_verbs[k].using_instrument.part_of_speech = "ABSTRACT-NOUN-PHRASE";
			        		   
			           }
			           if(j==5)
			           {
			        	   to_be_changed = token.charAt(token.length()-1) - '0'; //GET THE ONE TO BE MODIFIED
			        	   //System.out.println(to_be_changed);
			           }
			           if(j==6)
			           {
			        	   field_to_be_changed = token; //GET THE STATE TO BE MODIFIED
			        	  // System.out.println(field_to_be_changed);
			           }
			           if(token.substring(0,1).equals("$"))
		        	   {
			        	   if(flag==1)
			        	   {
			        		   do_something_1_verbs[i].conceptual_tense = token.substring(1); //CHANGE TENSE
			        		 //  System.out.println("check "+do_something_1_verbs[i] 
			            	//		+ " " + do_something_1_verbs[i].conceptual_tense);
			        		   i++;
			        	   }
			        	   if(flag==2)
			        	   {
			        		//   do_something_2_verbs[k].conceptual_tense = token.substring(1);
			        		//   System.out.println("check "+do_something_2_verbs[k] 
			            	//		+ " " + do_something_2_verbs[k].conceptual_tense);
			        		   k++;
			        	   }
			            	break;
		        	   }
			           if(j==7)
			           {
			        	   if(flag==1) //DO_SOMETHING1
			        	   {
			        		   do_something_1_verbs[i].name=name;
			        		   do_something_1_verbs[i].token = name;
			        		   if(to_be_changed==1) //actor to be changed
			        		   {
			        			   //CHANGE THE ACTOR APPROPRIATELY
			        			   if(field_to_be_changed.equals( "mental_state"))
				        		   {
				        			   //System.out.println("check");
				        			   do_something_1_verbs[i].actor.mental_state = Integer.parseInt(token);
				        			   //System.out.println(Integer.parseInt(token));
				        		   }
				        		   if(field_to_be_changed .equals("physical_state"))
				        		   {
				        			//   System.out.println(Integer.parseInt(token));
				        			   do_something_1_verbs[i].actor.physical_state = Integer.parseInt(token);
				        		   }
				        		   if(field_to_be_changed.equals("health"))
				        			   do_something_1_verbs[i].actor.health = Integer.parseInt(token);
				        		   if(field_to_be_changed.equals("anger"))
				        			   do_something_1_verbs[i].actor.anger = Integer.parseInt(token);
				        		   if(field_to_be_changed.equals("fear") )
				        			   do_something_1_verbs[i].actor.fear = Integer.parseInt(token);
			        		   }
			        		   if(to_be_changed==2) //actor to be changed
			        		   {
			        			   //CHANGE THE OBJECT APPROPRIATELY
			        			   if(field_to_be_changed.equals( "mental_state"))
				        		   {
				        			   //System.out.println("check");
				        			   do_something_1_verbs[i].object.mental_state = Integer.parseInt(token);
				        			   //System.out.println(Integer.parseInt(token));
				        		   }
				        		   if(field_to_be_changed .equals("physical_state"))
				        		   {
				        			 //  System.out.println(Integer.parseInt(token));
				        			   do_something_1_verbs[i].object.physical_state = Integer.parseInt(token);
				        		   }
				        		   if(field_to_be_changed.equals("health"))
				        			   do_something_1_verbs[i].object.health = Integer.parseInt(token);
				        		   if(field_to_be_changed.equals("anger"))
				        			   do_something_1_verbs[i].object.anger = Integer.parseInt(token);
				        		   if(field_to_be_changed.equals("fear") )
				        			   do_something_1_verbs[i].object.fear = Integer.parseInt(token);
				        		   do_something_1_verbs[i].cd_form = "(DO SOMETHING"
																												+"(ACTOR VAR1)"
																												+"(OBJECT VAR2)"
																												+"(CONC_TENSE VAR3)"
																												+"(USING VAR4)"
																												+"(ADVERB VAR5)"
																												+")";
				        		   if(field_to_be_changed.equals("NOT"))
				        		   {
				        			   do_something_1_verbs[i].cd_form = "(DO SOMETHING"
																														+"(ACTOR VAR1)"
																														+"(OBJECT VAR2)"
																														+"(CONC_TENSE VAR3)"
																														+"(USING VAR4)"
																														+"(ADVERB VAR5)"
																														+")";
				        		   }
			        		   }
			        	   }
			        	   if(flag==2) //DO_SOMRHIG 2
			        	   {
			        		   do_something_2_verbs[k].name=name;
			        		   do_something_2_verbs[k].token = name;
			        		   if(to_be_changed==1) //actor to be changed
			        		   {
			        			   //CHANGE THE ACTOR APPROPRIATELY
			        			   if(field_to_be_changed.equals( "mental_state"))
				        		   {
				        			   //System.out.println("check");
				        			   do_something_2_verbs[k].actor.mental_state = Integer.parseInt(token);
				        			   //System.out.println(Integer.parseInt(token));
				        		   }
				        		   if(field_to_be_changed .equals("physical_state"))
				        		   {
				        			   //System.out.println(Integer.parseInt(token));
				        			   do_something_2_verbs[k].actor.physical_state = Integer.parseInt(token);
				        		   }
				        		   if(field_to_be_changed.equals("health"))
				        			   do_something_2_verbs[k].actor.health = Integer.parseInt(token);
				        		   if(field_to_be_changed.equals("anger"))
				        			   do_something_2_verbs[k].actor.anger = Integer.parseInt(token);
				        		   if(field_to_be_changed.equals("fear") )
				        			   do_something_2_verbs[k].actor.fear = Integer.parseInt(token);
			        		   }
			        		   if(to_be_changed==2) //actor to be changed
			        		   {
			        			  //CHAGNE THE OBJECT APPROPRIATELY
				        		   if(field_to_be_changed .equals("physical_state"))
				        		   {
				        			   //System.out.println(Integer.parseInt(token));
				        			   do_something_2_verbs[k].object.physical_state = Integer.parseInt(token);
				        		   }
				        		   if(field_to_be_changed.equals("size"))
				        			   do_something_2_verbs[k].object.size = Integer.parseInt(token);
				        		   if(field_to_be_changed.equals("quality"))
				        			   do_something_2_verbs[k].object.quality = Integer.parseInt(token);
			        		   }
			        		   do_something_2_verbs[k].cd_form = "(DO SOMETHING"
																														+"(ACTOR VAR1)"
																														+"(OBJECT VAR2)"
																														+"(CONC_TENSE VAR3)"
																														+"(USING VAR4)"
																														+"(ADVERB VAR5)"
																														+")";
				        	   if(token.substring(0,1).equals("$"))
				        	   {
					            	do_something_2_verbs[k].conceptual_tense = token.substring(1); //GET TENSE
					            //	System.out.println("check "+do_something_2_verbs[k].conceptual_tense);
				        	   }
			        	   }
			           }
			           //System.out.println("i is " + i + " check " + do_something_1_verbs[i].name);
			           
			           
			           j++; //GET NEXT TOKEN
			        }
				}
				int l;
				for(l=0;l<i;l++)
				{
				//		System.out.println(do_something_1_verbs[l].name + " "+ do_something_1_verbs[l].conceptual_tense);
				}
				for(l=0;l<k;l++)
				{
				//	System.out.println(do_something_2_verbs[l].name + " "+ do_something_2_verbs[l].conceptual_tense);
				}
					br.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		} 
		catch (FileNotFoundException fe)
		{
		    fe.printStackTrace();
		}
		
	}
	public void expelmethod()
	{
		try 
		{
			File inFile = new File("EXPEL"); //READ EXPEL FILE TO GET THE VERBS LINE BY LINE
		    FileInputStream fstream = new FileInputStream(inFile);
		    DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int i=0;
			try 
			{
				while ((strLine = br.readLine()) != null)   
				{
					StringTokenizer stt = new StringTokenizer(strLine," "); //TOKENIZE EACH LINE
					expel_verbs[i] = new expel();
					int j=0;
					int to_be_changed=0;
					String field_to_be_changed = new String();
			        while (stt.hasMoreTokens()){
			            String token = stt.nextToken();
			            if(token.substring(0,1).equals("$"))
			            	expel_verbs[i].conceptual_tense = token.substring(1); //GET TENSE
			           if(j==0) //GET CD FORM
			           {
			        	   expel_verbs[i].name = token;
			        	   expel_verbs[i].token = token;
			        	   expel_verbs[i].cd_form = "(EXPEL"
																														+"(FROM VAR1)"
																														+"(OBJECT VAR2)"
																														+"(CONC_TENSE VAR3)"
																														+"(USING VAR4)"
																														+"(ADVERB VAR5)"
																														+")";
			           }
			           if(j==4)
			           {
			        	   field_to_be_changed = token;
			           }
			           if(j==5)
			           {
			        	 //CHANGE THE STATES OF THE ACTOR APPROPRIATELY
			        		   if(field_to_be_changed.equals("mental_state"))
			        			   expel_verbs[i].actor.mental_state = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("physical_state"))
			        			   expel_verbs[i].actor.physical_state = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("health"))
			        			   expel_verbs[i].actor.health = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("anger"))
			        			   expel_verbs[i].actor.anger = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("fear"))
			        			   expel_verbs[i].actor.fear = Integer.parseInt(token);
			        	   
			
			        	   
			           }
			           j++; //GET NEXT TOKEN IN THE LINE READ
			        }
					i++; //GET NEXT LINE
				}
				int k;
				for(k=0;k<i;k++)
				{
				//	System.out.println(expel_verbs[k].name + " "+ expel_verbs[k].conceptual_tense);
				}
				br.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			expelcount = i;
		} 
		catch (FileNotFoundException fe)
		{
		    fe.printStackTrace();
		}
		//UNCOMMENT TO SEE THE OUTPUT
		for(k=0;k<expelcount;k++)
		{
			//System.out.println(expel_verbs[k].name + expel_verbs[k].cd_form);
		}
	}
	public void graspmethod()
	{
		try 
		{
			File inFile = new File("GRASP"); //READ FROM GRASP FILE LINE BY LINE TO GET THE VERBS
		    FileInputStream fstream = new FileInputStream(inFile);
		    DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int i=0;
			try 
			{
				while ((strLine = br.readLine()) != null)   
				{
					StringTokenizer stt = new StringTokenizer(strLine," "); //GENERATE TOKENS FROM LINE READ
					grasp_verbs[i] = new grasp();
					int j=0;
					int to_be_changed=0;
					String field_to_be_changed = new String();
			        while (stt.hasMoreTokens()){
			            String token = stt.nextToken();
			            if(token.substring(0,1).equals("$"))
			            	grasp_verbs[i].conceptual_tense = token.substring(1); //GET TENES
			           if(j==0) //GET CD FORM
			           {
			        	   grasp_verbs[i].name = token;
			        	   grasp_verbs[i].token = token;
			        	   grasp_verbs[i].cd_form = "(GRASP"
																														+"(ACTOR VAR1)"
																														+"(OBJECT VAR2)"
																														+"(CONC_TENSE VAR3)"
																														+"(USING VAR4)"
																														+"(ADVERB VAR5)"
																														+") ";
			           }
			           if(j==3)
			        	   
			           j++;
			        }
					i++;
				}
				int k;
				for(k=0;k<i;k++)
				{
				//	System.out.println(grasp_verbs[k].name + " "+ grasp_verbs[k].conceptual_tense);
				}
				br.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			graspcount = i;
		} 
		catch (FileNotFoundException fe)
		{
		    fe.printStackTrace();
		}
		//UNCOMMENT TO SEE OUTPUT
		for(k=0;k<graspcount;k++)
		{
			//System.out.println(grasp_verbs[k].name + grasp_verbs[k].cd_form);
		}
		
	}
	public void ingestmethod()
	{
		try 
		{
			File inFile = new File("INGEST");
		    FileInputStream fstream = new FileInputStream(inFile);
		    DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int i=0;
			try 
			{
				while ((strLine = br.readLine()) != null)   
				{
					StringTokenizer stt = new StringTokenizer(strLine," "); //READ LINE BY LINE EVERY VERB
					ingest_verbs[i] = new ingest();
					int j=0;
					int to_be_changed=0;
					String field_to_be_changed = new String();
			        while (stt.hasMoreTokens()){
			            String token = stt.nextToken();
			            if(token.substring(0,1).equals("$"))
			            	ingest_verbs[i].conceptual_tense = token.substring(1); //GET CONCEPTIAL TENSE
			           if(j==0) //GET CD FORM
			           {
			        	   ingest_verbs[i].name = token;
			        	   ingest_verbs[i].token = token;
			        	   ingest_verbs[i].cd_form = "(INGEST"
																														+"(ACTOR VAR1)"
																														+"(OBJECT VAR2)"
																														+"(CONC_TENSE VAR3)"
																														+"(USING VAR4)"
																														+"(ADVERB VAR5)"
																														+") ";
			           }
			           j++;//GET NEXT TOKEN FROM THE LINE READ
			        }
					i++; //GET NEXT VERB FROM NEXT LINE
				}
				int k;
				for(k=0;k<i;k++)
				{
				//	System.out.println(ingest_verbs[k].name + " "+ ingest_verbs[k].conceptual_tense);
				}
				br.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			ingestcount = i;
		} 
		catch (FileNotFoundException fe)
		{
		    fe.printStackTrace();
		}
		//UNCOMMENT TO PRINT THE OUTPUT
		for(k=0;k<ingestcount;k++)
		{
			//System.out.println(ingest_verbs[k].name + ingest_verbs[k].cd_form);
		}
	}
	public void attendmethod()
	{
		try 
		{
			File inFile = new File("ATTEND"); //READ FILE ATTEND TO GET THE ATTEND VERBS
		    FileInputStream fstream = new FileInputStream(inFile);
		    DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int i=0;
			try 
			{
				while ((strLine = br.readLine()) != null)   
				{
					StringTokenizer stt = new StringTokenizer(strLine," "); //GET NEXT TOKEN FROM NEXT LINE
					attend_verbs[i] = new attend();
					int j=0;
					int to_be_changed=0;
					String field_to_be_changed = new String();
			        while (stt.hasMoreTokens()){
			            String token = stt.nextToken();
			            if(token.substring(0,1).equals("$"))
			            	attend_verbs[i].conceptual_tense = token.substring(1); //GET TENSE
			           if(j==0) 
			           {
			        	   attend_verbs[i].name = token;
			        	   attend_verbs[i].token = token;
			        	   attend_verbs[i].cd_form = " ATTEND((ACTOR ?VAR1)(ORGAN ?VAR2)(OBJECT ?VAR3)" +
			        	   		"(USING ?VAR4)(ADVERB ?VAR5)*(CONC_TENSE ?VAR6)) ";
			           }
			           j++; //NEXT LINE OF THE INPUT FILE
			        }
					i++;//NEXT TOKEN
				}
				int k;
				for(k=0;k<i;k++)
				{
				//	System.out.println(attend_verbs[k].name + " "+ attend_verbs[k].conceptual_tense);
				}
				br.close();
			} 
			//EXCEPTION
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			attendcount = i;
		} 
		catch (FileNotFoundException fe)
		{
		    fe.printStackTrace();
		}
		//UNCOMMENT TO GET PRINT THE OUTPTU
		for(k=0;k<attendcount;k++)
		{
			//System.out.println(attend_verbs[k].name + attend_verbs[k].cd_form);
		}
	}
	public void statementhod()
	{
		try 
		{
			File inFile = new File("STATE"); //READ FROM THE STATE FILE
		    FileInputStream fstream = new FileInputStream(inFile);
		    DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int i=0;
			try 
			{
				while ((strLine = br.readLine()) != null)   
				{
					StringTokenizer stt = new StringTokenizer(strLine," "); //GET THE NEXT LINE AND THE NEXT VERB
					state_verbs[i] = new state();
					int j=0;
					int to_be_changed=0;
					String field_to_be_changed = new String();
			        while (stt.hasMoreTokens()){
			            String token = stt.nextToken();
			            if(token.substring(0,1).equals("$"))
			            {
			            	state_verbs[i].conceptual_tense = token.substring(1); //GET TENSE
			            	break;
			            }
			           if(j==0)
			           {
			        	   state_verbs[i].name = token;
			        	   state_verbs[i].token = token;
			           }
			           if(j==1)
			           {
			        	  // System.out.println("check "+token.substring(0,3));
			        	   if(token.substring(0,3).equals("NOT"))
			        	   {
			        		   state_verbs[i].conceptual_tense="/"; //IF THE TOKEN IS NOT, LIKE THE VERB IS DENY, THEN CONCEPTUAL TENSE IS /
			        		   state_verbs[i].poss_loc = token; //IT GIVES THE LOCATION OR ANY INFORMATION ABOUT POSSESSION . LIKE OWN IS A POSS TYPE.
			        		   state_verbs[i].cd_form = "(" +token.substring(3)+ "ACTOR ?VAR1)(OBJECT ?VAR2)(USING ?VAR3)(ADVERB ?VAR4)*(CONC_TENSE ?VAR5)))";
			        	   }
			        	   else 
			        	   {
			        		   state_verbs[i].poss_loc = token;
			        		   state_verbs[i].cd_form = "("+ token + "?VAR1)(OBJECT ?VAR2))(USING ?VAR3)(ADVERB ?VAR4)*(CONC_TENSE ?VAR5)";
			        	   }
			           }
			           if(j==3) //GET THE FEILD TO BE CHANEGED LIKE MENTAL STATE ETC
			        	   field_to_be_changed = token;
			           if(j==4) //CHANGE THE ACTOR'S STATES
			           {
			        	 
			        		   if(field_to_be_changed.equals("mental_state"))
			        			   state_verbs[i].actor.mental_state = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("physical_state"))
			        			   state_verbs[i].actor.physical_state = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("health"))
			        			   state_verbs[i].actor.health = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("anger"))
			        			   state_verbs[i].actor.anger = Integer.parseInt(token);
			        		   if(field_to_be_changed.equals("fear"))
			        			   state_verbs[i].actor.fear = Integer.parseInt(token);
			           }
			        	   
			           j++;
			        }
					i++;
				}
				int k;
				for(k=0;k<i;k++)
				{
				//	System.out.println(state_verbs[k].name + " "+ state_verbs[k].actor.mental_state);
				}
				br.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			statecount = i;
		} 
		//EXCEPTIPON
		catch (FileNotFoundException fe)
		{
		    fe.printStackTrace();
		}
		//UNCOMMENT TO GET OUTPTU
		for(k=0;k<statecount;k++)
		{
			//System.out.println(state_verbs[k].name + state_verbs[k].cd_form);
			
		}
	}
}
