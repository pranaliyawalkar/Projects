import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class get_domain
{
	public Actor_Place_structure[] politicians = new Actor_Place_structure[200]; //86
	public Actor_Place_structure[]places = new Actor_Place_structure[600]; //543
	public Actor_Place_structure[] pronouns = new Actor_Place_structure[100]; //55
	public IObject_structure[] objects = new IObject_structure[150]; //104
	public Adverb_structure[] adverb = new Adverb_structure[100] ; //33
	public Adjective_structure[] adjective = new Adjective_structure[100]; //30
	/***********************POLITICIANS*****************************/
	public void function_politicians()
	{
		try 
		{
			File inFile = new File("POLITICIANS_NAMES");
		    FileInputStream fstream = new FileInputStream(inFile);
		    DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int i=0;
		//	System.out.println("--------------------------------------------------------------------");
		//	System.out.println("Politicians");
			try 
			{
				while ((strLine = br.readLine()) != null)   
				{
					politicians[i] = new Actor_Place_structure();
					//System.out.println("check");
					politicians[i].name = strLine;
			//		System.out.println("Name "+ strLine);
					politicians[i].token = strLine;
			//		System.out.println("Token Name "+ strLine);
					politicians[i].cd_form = "(PERSON(NAME "+strLine+"))";// (ISA(ADJECTIVE))*)";
			//		System.out.println("CD form "+ "(PERSON(NAME "+strLine+"))");
					politicians[i].part_of_speech="NOUN-PHRASE";
			//		System.out.println("Part of Speech "+ "NOUN-PHRASE" );
					politicians[i].type = "person";
			//		System.out.println("Type Person");
					politicians[i].specific ="YES";
					i++;
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
	/***********************PLACES*****************************/
	public void function_places()
	{
		try 
		{
			File inFile = new File("PLACES_NAMES");
		    FileInputStream fstream = new FileInputStream(inFile);
		    DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int i=0;
	//		System.out.println("--------------------------------------------------------------------");
	//		System.out.println("Places");
			try
			{
				while ((strLine = br.readLine()) != null)   
				{
					places[i] = new Actor_Place_structure();
					places[i].name = strLine;
					//System.out.println("Name "+ strLine);
					places[i].token = strLine;
					//System.out.println("Token "+ strLine);
					places[i].cd_form = "(PLACE(NAME "+strLine+"))";
					//System.out.println("CD form "+ "(PLACE(NAME "+strLine+")(ISA(ADJECTIVE))*)" );
					places[i].part_of_speech="NOUN-PHRASE";
					//System.out.println("Part of Speech "+ "NOUN-PHRASE" );
					places[i].type = "object";
					//System.out.println("Type "+ "Object");
					i++;
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
	/***********************PRONOUN*****************************/
	public void function_pronoun()
	{
		try 
		{
			File inFile = new File("PRONOUN_NAMES");
		    FileInputStream fstream = new FileInputStream(inFile);
		    DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
		//	System.out.println("--------------------------------------------------------------------");
		//	System.out.println("Pronouns");
			int i=0;
			try 
			{
				while ((strLine = br.readLine()) != null)   
				{
					pronouns[i] = new Actor_Place_structure();
					pronouns[i].name = strLine;
				//	System.out.println("Name "+ strLine );
					pronouns[i].token = strLine;
				//	System.out.println("Token "+ strLine );
					strLine = br.readLine();
					pronouns[i].cd_form = strLine;
				//	System.out.println("CD form "+ strLine );
					int k=0;
					int flag1=0;
					int flag2=0;
					for(k=0;k<strLine.length()-6;k++)
					{
						if(strLine.charAt(k)=='O' && strLine.charAt(k+1)=='B' && strLine.charAt(k+2)=='J' && strLine.charAt(k+3)=='E' && strLine.charAt(k+4)=='C' && strLine.charAt(k+5)=='T')
							flag1=1;
						if(strLine.charAt(k)=='P' && strLine.charAt(k+1)=='E' && strLine.charAt(k+2)=='R' && strLine.charAt(k+3)=='S' && strLine.charAt(k+4)=='O' && strLine.charAt(k+5)=='N')
							flag2=1;
					}
					pronouns[i].part_of_speech="NOUN-PHRASE";
					if(flag1==1 && flag2==0)
						pronouns[i].type = "object";
					if(flag1==1 && flag2==1)
						pronouns[i].type = "both";
					if(flag1==0 && flag2==1)
						pronouns[i].type = "person";
				//	System.out.println("Type used for "+ pronouns[i].type );
					strLine = br.readLine();
					pronouns[i].specific = strLine;
					i++;
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
	/***********************OBJECT*****************************/
	public void function_object()
	{
		try {
			File inFile = new File("OBJECT_NAMES");
		    FileInputStream fstream = new FileInputStream(inFile);
		    DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int i=0;
		//	System.out.println("--------------------------------------------------------------------");
		//	System.out.println("Object Names");
			try 
			{
				while ((strLine = br.readLine()) != null)   
				{
					objects[i] = new IObject_structure();
				//	System.out.println(i);
					if(strLine.charAt(strLine.length()-1) =='*')
					{
						strLine = strLine.substring(0, strLine.length()-1);
						objects[i].name = strLine;
						
						objects[i].token = strLine;
						
						objects[i].cd_form = "(OBJECT(NAME "+strLine+"))";//;(ISA(ADJECTIVE))*)";
						
						objects[i].part_of_speech="ABSTRACT-NOUN-PHRASE";
						
						objects[i].type = "object";
						
					}
					else
					{
						objects[i].name = strLine;
						objects[i].token = strLine;
						objects[i].cd_form = "(OBJECT(NAME "+strLine+"))";
						objects[i].part_of_speech="NOUN-PHRASE";
						objects[i].type = "object";
					}
			//		System.out.println("Name " + objects[i].name);
				//	System.out.println("Token " + objects[i].token);
				//	System.out.println("CD form " + objects[i].cd_form);
				//	System.out.println("Part of Speech " + objects[i].part_of_speech);
					//System.out.println("type " + objects[i].type);
					i++;
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
	/***********************ADVERB*****************************/
	public void function_adverb()
	{
		try 
		{
			File inFile = new File("ADVERB_NAMES");
		    FileInputStream fstream = new FileInputStream(inFile);
		    DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			int i=0;
		//	System.out.println("--------------------------------------------------------------------");
		//	System.out.println("Adverbs/Action Aiders");
			try 
			{
				while ((strLine = br.readLine()) != null)   
				{
					adverb[i] = new Adverb_structure();
					adverb[i].name = strLine;
			//		System.out.println("Name " + adverb[i].name);
					adverb[i].token = strLine;
			//		System.out.println("Token " + adverb[i].token);
					adverb[i].cd_form = "(ADVERB "+strLine+")";
			//		System.out.println("CD form " + adverb[i].cd_form);
					adverb[i].part_of_speech="ADVERB-PHRASE";
			//		System.out.println("Part of Speech " + adverb[i].part_of_speech);
					adverb[i].type = "adverb";
			//		System.out.println("Type " + adverb[i].type);
					i++;
				}
				br.close();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
	
		} 
		catch (FileNotFoundException fe)
		{
		    fe.printStackTrace();
		}
	}
	/***********************ADJECTIVE*****************************/
	public void function_adjective()
	{
		try 
		{
			File inFile = new File("ADJECTIVE_NAMES");
		    FileInputStream fstream = new FileInputStream(inFile);
		    DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
		//	System.out.println("--------------------------------------------------------------------");
		//	System.out.println("Adjectives/Picture Aiders");
			int i=0;
			try 
			{
				while ((strLine = br.readLine()) != null)   
				{
					adjective[i] = new Adjective_structure();
					adjective[i].name = strLine;
			//		System.out.println("Name " + adjective[i].name);
					adjective[i].token = strLine;
			//		System.out.println("Token " + adjective[i].token);
					adjective[i].cd_form = "(ADJECTIVE "+strLine+")";
			//		System.out.println("CD form " + adjective[i].cd_form);
					adjective[i].part_of_speech="ADJECTIVE-PHRASE";
			///		System.out.println("Part of speech " + adjective[i].part_of_speech);
					adjective[i].type = "adjective";
			//		System.out.println("Type " + adjective[i].type);
					i++;
				}
				br.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
	
		} catch (FileNotFoundException fe)
		{
		    fe.printStackTrace();
		}
	}
}
