import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class Main 
{

	private static String USER_AGENT = "Mozilla/5.0";
	public static void main(String[] args) 
	{
		try 
		{
			
			File file1 = new File("/home/pranali/workspace/ProjectNLP/src/stopwords");
			FileInputStream  fstream1 = new FileInputStream(file1);
			DataInputStream in1 = new DataInputStream(fstream1);
			BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));
			ArrayList<String> stopwords = new ArrayList<String>();
			String new_wor = new String();
			while((new_wor = br1.readLine()) != null)
				stopwords.add(new_wor);
			File inFile = new File("/home/pranali/workspace/ProjectNLP/src/test.txt");
			fstream1 = new FileInputStream(inFile);
			in1 = new DataInputStream(fstream1);
			br1 = new BufferedReader(new InputStreamReader(in1));
			String strLine;
			while ((strLine = br1.readLine()) != null)   
			{
				/************** NER ***************/
				long startTime = System.currentTimeMillis();
				
				Hashtable<String, Integer> position = new Hashtable<String, Integer>();
				File file = new File("/home/pranali/workspace/ProjectNLP/src/example.txt");
				BufferedWriter output = new BufferedWriter(new FileWriter(file));
		        output.write(strLine);
		        output.close();
				Process p1 = Runtime.getRuntime().exec("/home/pranali/workspace/ProjectNLP/src/NER/./ner.sh /home/pranali/workspace/ProjectNLP/src/example.txt");
				p1.waitFor();
				BufferedReader reader1 = new BufferedReader(new InputStreamReader(p1.getInputStream())); 
				String NER = reader1.readLine(); 
				System.out.println(NER);
				LinkedHashMap<String, String> NERS = new LinkedHashMap<String, String>();
				LinkedHashMap<String, String> all_ner = new LinkedHashMap<String, String>();
				StringTokenizer stt = new StringTokenizer(NER," ");
				int count = 0;
				while(stt.hasMoreTokens())
				{
					String temp = stt.nextToken();
					StringTokenizer stt2 = new StringTokenizer(temp,"/");
					String token1 = stt2.nextToken();
					String token2 = stt2.nextToken();
					position.put(token1, count++);
					if(token2.equals("TIME") || token2.equals("LOCATION") || token2.equals("ORGANIZATION") || token2.equals("PERSON") || token2.equals("MONEY") | token2.equals("PERCENT") || token2.equals("DATE"))
					{
						all_ner.put(token1, token2);
						Iterator it = NERS.entrySet().iterator();
						String prev_ner_pos = "";
						String prev = "";
					    while (it.hasNext()) 
					    {
					        Map.Entry pairs = (Map.Entry)it.next();
					        prev = pairs.getKey().toString();
					        prev_ner_pos = NERS.get(pairs.getKey().toString());
					    }
					    
					    if(prev_ner_pos.equals(token2) && !prev_ner_pos.equals(""))
					    {
					    	NERS.remove(prev);
					    	NERS.put(prev + "%20" + token1, token2);
					    }
					    else if (prev_ner_pos.equals(""))
					    {
					    	NERS.put(token1, token2);
					    }
					}
					count++;
				}
				long endTime = System.currentTimeMillis();
				System.out.print("NER Time : " + (endTime-startTime) + "  ");
				//System.out.println(NERS);
				
				/************* Dependency Parser ******************/
				startTime = System.currentTimeMillis();
				
				String relation = "";
				Process p2 = Runtime.getRuntime().exec("/home/pranali/workspace/ProjectNLP/src/parser/./lexparser.sh /home/pranali/workspace/ProjectNLP/src/example.txt"); 
				p2.waitFor(); 
				BufferedReader reader2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
				
				String line = reader2.readLine(); 
				ArrayList<String> all_dependencies = new ArrayList<String>();
				
				while(line!=null) 
				{ 
					
					if(line.length() > 0 && line.charAt(0)!=' ' && line.charAt(0)!='(')
					{
						//System.out.println(line);
						all_dependencies.add(line);
						StringTokenizer stt2 = new StringTokenizer(line," ( ) , ");
						String token1 = stt2.nextToken();
						String token2 = stt2.nextToken();
						String token3 = stt2.nextToken();
	
						stt2 = new StringTokenizer(token2,"-");
						token2 = stt2.nextToken();
						
						stt2 = new StringTokenizer(token3,"-");
						token3 = stt2.nextToken();
						
						if(token1.equalsIgnoreCase("root") || token2.equalsIgnoreCase("root"))
						{
							line=reader2.readLine(); 
							continue;
						}
						else
						{
							if(all_ner.containsKey(token2) && !all_ner.containsKey(token3))
							{
								relation = token3;
							}
							else if(!all_ner.containsKey(token2) && all_ner.containsKey(token3))
							{
								relation = token2;
							}
						}
					}
					line=reader2.readLine(); 
				}
				String relation1 = "";
				for(int i = 0 ; i < all_dependencies.size(); i++)
				{
					String line1 = all_dependencies.get(i);
					StringTokenizer stt2 = new StringTokenizer(line1," ( ) , ");
					String token1 = stt2.nextToken();
					String token2 = stt2.nextToken();
					String token3 = stt2.nextToken();

					stt2 = new StringTokenizer(token2,"-");
					token2 = stt2.nextToken();
					
					stt2 = new StringTokenizer(token3,"-");
					token3 = stt2.nextToken();
					
					if(token1.equalsIgnoreCase("root") || token2.equalsIgnoreCase("root"))
					{
						continue;
					}
					else
					{
						if(token2.equals(relation) && !all_ner.containsKey(token3) && !stopwords.contains(token3))
						{
							relation1 = token3;
						}
						else if(!all_ner.containsKey(token2) && token3.equals(relation) && !stopwords.contains(token2))
						{
							relation1 = token2;
						}
					}
				}
				
				if(!relation1.equals("") &&  position.get(relation) < position.get(relation1))
				{
					if(relation1.equalsIgnoreCase("who"))
						relation = relation + " " + "person";
					if(relation1.equalsIgnoreCase("where"))
						relation = relation + " " + "place";
					if(relation1.equalsIgnoreCase("when"))
						relation = relation + " " + "time";
					else
						relation = relation + " " + relation1;
				}
				else if(!relation1.equals(""))
				{
					if(relation1.equalsIgnoreCase("who"))
						relation = "person" + " "  + relation;
					if(relation1.equalsIgnoreCase("where"))
						relation = "place" + " "  + relation;
					if(relation1.equalsIgnoreCase("when"))
						relation = "time" + " "  + relation;
					else
						relation = relation1 + " " + relation;
				}
				
				endTime = System.currentTimeMillis();
				System.out.print("RE time : " + (endTime-startTime) + "  ");
				
				/************** WIKI Infobox ***********************/
				Iterator it = NERS.entrySet().iterator();
				String Main_NER = new String();
				ArrayList<String> attributes = new ArrayList<String>();
				Hashtable<String, String> attribute_values = new Hashtable<String, String>();
				Hashtable<String, Double> attribute_scores = new Hashtable<String, Double>();
				if(it.hasNext())
				{
					Map.Entry pairs = (Map.Entry)it.next();
					Main_NER = pairs.getKey().toString();
			        String url = "http://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&format=php&titles=" + Main_NER + "&rvsection=0";
					
					URL obj = new URL(url);
					HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			 
					// optional default is GET
					con.setRequestMethod("GET");
					
					//add request header
					con.setRequestProperty("User-Agent", USER_AGENT);
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					
					String inputLine;
					StringBuffer response = new StringBuffer();
					boolean flag = false;
					count = 0;
					while ((inputLine = in.readLine()) != null) {
						if(inputLine.startsWith("{{Infobox")){
							flag = true;
						}
						if(flag){
							if(inputLine.contains("{{"))
								count++;
							if(inputLine.contains("}}"))
								count--;
							response.append(inputLine + "\n");
							if(count == 0)
								break;
						}
					}
					in.close();
			 
					//print result
					//System.out.println(response.toString());
					/******** Parsing output ********/
					int check = 0;
					String token_left = "";
					String token_right = "";
					
					stt = new StringTokenizer(response.toString(),"\n");
					String next_token = "";
					while(stt.hasMoreTokens())
					{
						if(next_token.equals(""))
						{
							next_token = stt.nextToken();
						}
						String token = new String();
						token = next_token;
						if(stt.hasMoreTokens())
						{
							next_token = stt.nextToken();
							int p = 0;
							while(p < next_token.length() && (next_token.charAt(p)==' '))
								p++;
							next_token = next_token.substring(p);
							while(!next_token.startsWith("|"))
							{
								token = token + next_token;
								if(stt.hasMoreTokens())
									next_token = stt.nextToken();
								else
									break;
							}
						}
						int p = 0;
						while(p < token.length() && (token.charAt(p)==' '|| token.charAt(p)=='|'))
							p++;
						token = token.substring(p);
						
						if(token.startsWith("image") || token.startsWith("logo") || token.startsWith("signature") || token.startsWith("alt") || token.startsWith("caption"))
							continue;
						if(token.startsWith("{{Infobox"))
							continue;
						if(token.startsWith("}}"))
							continue;

						
						if(token.startsWith("birth_date") || token.startsWith("death_date"))
						{
							String answer = "";
							StringTokenizer stt4 =  new StringTokenizer(token,"|");
							int count1 = 0;
							
							while(stt4.hasMoreTokens() && count1 < 3)
							{
								String token4 = stt4.nextToken();
								int flag1 = 0;
								for (char c : token4.toCharArray())
							    {
							        if (!Character.isDigit(c))
							        {
							        	flag1 = 1;
							        	break;
							        }
							    }
								if(flag1 ==1 )
									continue;
								count1++;
								if(!token4.equalsIgnoreCase("Plainlist") && !token4.equalsIgnoreCase("unbulleted list") && !token4.equalsIgnoreCase("bulleted list"))
								{
									if(!token4.endsWith("}}"))
										answer = answer + token4 + " ";
									else
										answer = answer + token4.substring(0, token4.length()-2) +" ";
								}
							}
							
							//System.out.println(token.substring(0, 10) + "  " + answer);
							StringTokenizer stt6 = new StringTokenizer(token.substring(0, 10), "_");
							String att = "";
							while(stt6.hasMoreTokens())
							{
								att = att+stt6.nextToken() + " ";
							}
							attributes.add(att.substring(0,att.length()-1));
							attribute_values.put(att.substring(0,att.length()-1), answer);
							continue;
						}
						
						else
						{
							if(token.startsWith("label") || token.startsWith("lbl")){
								check = 1;
								token_left = "";
								token_right = "";
							}
							if(token.startsWith("data"))
							{
								check = 2;
							}
							//remove refs
							//System.out.println(token);
							int flag2 = 0;
							for(int i = 0; i< token.length(); i++)
							{
								if(token.charAt(i)=='<' && token.charAt(i+1)=='r' && token.charAt(i+2)=='e' && token.charAt(i+3)=='f' )
								{
									while(i+4 < token.length() && !( (token.charAt(i)=='/' && token.charAt(i+1)=='r' && token.charAt(i+2)=='e' && token.charAt(i+3)=='f' && token.charAt(i+4)=='>' ) 
											|| (token.charAt(i+1)=='/' && token.charAt(i+2)=='>')) )
									{
										token = token.substring(0,i)+'*'+token.substring(i+1);
										i++;
									}
									token = token.substring(0,i)+'*'+token.substring(i+1);
									if((token.charAt(i)=='*' && token.charAt(i+1)=='r' && token.charAt(i+2)=='e' && token.charAt(i+3)=='f' && token.charAt(i+4)=='>' ))
									{
										token = token.substring(0,i+1)+"****"+token.substring(i+5);
									}
									else if((token.charAt(i+1)=='/' && token.charAt(i+2)=='>'))
									{
										token = token.substring(0,i+1)+"**"+token.substring(i+3);
									}
								}
							}
							
							//System.out.println(token);
							String new_token = ""; 
								
							for(int i = 0; i< token.length(); i++)
							{
								if(token.charAt(i)!='*')
								{
									new_token = new_token + token.charAt(i); 
								}
							}
							token = "";
							token = new_token;
							
							//System.out.println("new " + new_token);
							
							for(int i = 0; i< token.length(); i++)
							{
								if(token.charAt(i)=='<' && token.charAt(i+1)=='s' && token.charAt(i+2)=='m' && token.charAt(i+3)=='a' && token.charAt(i+4)=='l' && token.charAt(i+5)=='l'  )
								{
									while( i+6 < token.length() && !( (token.charAt(i)=='/' && token.charAt(i+1)=='s' && token.charAt(i+2)=='m' && token.charAt(i+3)=='a' && token.charAt(i+4)=='l' && token.charAt(i+5)=='l' && token.charAt(i+6)=='>' ) 
											|| (token.charAt(i+1)=='/' && token.charAt(i+2)=='>')) )
									{
										token = token.substring(0,i)+'*'+token.substring(i+1);
										i++;
									}
									token = token.substring(0,i)+'*'+token.substring(i+1);
									if((token.charAt(i)=='*' && token.charAt(i+1)=='s' && token.charAt(i+2)=='m' && token.charAt(i+3)=='a' && token.charAt(i+4)=='l' && token.charAt(i+5)=='l'  && token.charAt(i+6)=='>'))
									{
										token = token.substring(0,i+1)+"*****"+token.substring(i+7);
									}
									else if((token.charAt(i+1)=='/' && token.charAt(i+2)=='>'))
									{
										token = token.substring(0,i+1)+"**"+token.substring(i+3);
									}
								}
							}
							
							//System.out.println(token);
							new_token = ""; 
								
							for(int i = 0; i< token.length(); i++)
							{
								if(token.charAt(i)!='*')
								{
									new_token = new_token + token.charAt(i); 
								}
							}
							
							//System.out.println("newer " + new_token);
							token = "";
							token = new_token;
							
							
							
							
							
							for(int i = 0; i< token.length(); i++)
							{
								if(token.charAt(i)=='<' && token.charAt(i+1)=='!' && token.charAt(i+2)=='-' && token.charAt(i+3)=='-' )
								{
									while( i+3 < token.length() && !(token.charAt(i+1)=='-'  && token.charAt(i+2)=='-' && token.charAt(i+3)=='>')) 
									{
										token = token.substring(0,i)+'*'+token.substring(i+1);
										i++;
									}
									token = token.substring(0,i)+'*'+token.substring(i+1);
									if((token.charAt(i)=='*' && token.charAt(i+1)=='-'  && token.charAt(i+2)=='-' && token.charAt(i+3)=='>' ))
									{
										token = token.substring(0,i+1)+"***"+token.substring(i+3);
									}
								}
							}
							
							//System.out.println(token);
							new_token = ""; 
								
							for(int i = 0; i< token.length(); i++)
							{
								if(token.charAt(i)!='*')
								{
									new_token = new_token + token.charAt(i); 
								}
							}
							token = "";
							token = new_token;
							
							
							
							//System.out.println("newest " + new_token);
							
							
							
							
							
							
							
							StringTokenizer stt3 =  new StringTokenizer(token,"=");
							if(!stt3.hasMoreTokens())
								continue;
							String token1 = stt3.nextToken();
							String answer = "";
							if(!stt3.hasMoreTokens())
								continue;
							String token_ans = stt3.nextToken();
							//<br> <br />
							//StringTokenizer stt2 =  new StringTokenizer(token_ans,"{{,}}");
							
							String[] stt2_tokens = token_ans.split("\\{\\{|\\}\\}");
							for(int i = 0; i < stt2_tokens.length; i++)
							{
								String token2 = stt2_tokens[i];
								String[] stt3_tokens = token2.split("<br>|<br />");
								for(int j = 0; j< stt3_tokens.length; j++)
								{
									String token4 = stt3_tokens[j];
									String[] stt4_tokens = token4.split("\\[\\[|\\]\\]|\\'\\'");
									for(int k = 0; k < stt4_tokens.length; k++)
									{
										String token5 = stt4_tokens[k];
										StringTokenizer stt5 = new StringTokenizer(token5,"|");
										String token6 = "";
										while(stt5.hasMoreTokens())
										{
											token6 = stt5.nextToken();
										}
										if(!token6.equalsIgnoreCase("Plainlist") && !token6.equalsIgnoreCase("unbulleted list") && !token6.equalsIgnoreCase("bulleted list"))
											answer += token6 + " ";
										
									}
									answer = answer + ", ";
								}
								
							}
							StringTokenizer stt6 = new StringTokenizer(token1, "_");
							String att = "";
							while(stt6.hasMoreTokens())
							{
								att = att+stt6.nextToken() + " ";
							}
							if(!att.equals(""))
							{
								if(check == 1){
									token_left = answer;
								}
								if(check == 2){
									token_right = answer;
									attributes.add(token_left);
									attribute_values.put(token_left, token_right);
									check = 0;
								}
								else{
									attributes.add(att.substring(0,att.length()-1));
									attribute_values.put(att.substring(0,att.length()-1), answer);
								}
							}
							//System.out.println(token1 + "  " + answer);
						}
						
					}
			
				}
				
				/******************** Wordnet similarity ***************************/
				String url = "http://swoogle.umbc.edu/SimService/GetSimilarity?operation=api";
				String op1 = "&phrase1=";
				String op2 = "&phrase2=";
				
				
				StringTokenizer stt2 = new StringTokenizer(relation," ");
				while(stt2.hasMoreTokens())
				{
					op1 = op1 + stt2.nextToken()+"%20";
				}
				op1 = op1.substring(0, op1.length() - 3);
				//System.out.println(op1);
				double max_score = -0.11;
				String max_attribute = new String();
				startTime = System.currentTimeMillis();
				for(int i = 0; i< attributes.size(); i++)
				{			
					//URL yahoo = new URL("http://swoogle.umbc.edu/SimService/GetSimilarity?operation=api&phrase1=car&phrase2=defence%20minister");
			        
					stt2 = new StringTokenizer(attributes.get(i)," ");
					op2 = "&phrase2=";
					int flag = 0;
					while(stt2.hasMoreTokens())
					{
						String token = stt2.nextToken();
						if(token.contains("0") || token.contains("1") || token.contains("2") || token.contains("3") || token.contains("4") || token.contains("5") || token.contains("6") || token.contains("7") || token.contains("8") || token.contains("9"))
						{
							flag = 1;
							break;
						}
						op2 = op2 + token+"%20";
					}
					if(flag ==1)
						continue;
					op2 = op2.substring(0, op2.length() - 3);
					//System.out.print(attributes.get(i) + " ");
					
					URL yahoo = new URL(url+op1+op2);
					URLConnection yc = yahoo.openConnection();
			        BufferedReader in = new BufferedReader(
			                                new InputStreamReader(
			                                yc.getInputStream()));
			        String inputLine = in.readLine();
			        attribute_scores.put(attributes.get(i), Double.parseDouble(inputLine));
			        if(Double.parseDouble(inputLine) > max_score &&  Double.parseDouble(inputLine) < 10000)
			        {
			        	max_score = Double.parseDouble(inputLine);
			        	max_attribute = attributes.get(i);
			        }
			        //System.out.println(inputLine);
			        in.close();
				}
				
				endTime = System.currentTimeMillis();
				System.out.println(" Best match time : " + (endTime-startTime));
				System.out.println(" relation : " + relation);
				System.out.println("Final answer : " + attribute_values.get(max_attribute));
				NER = reader1.readLine(); 
			}
		}
		catch(Exception e) 
		{
			System.out.println(e);
		} 
	}
	
}
