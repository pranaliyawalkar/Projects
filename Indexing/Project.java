import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


public class Project {

	public static void main(String[] args)
	{
		Online_processing olProcessing = new Online_processing();
		HashMap<Integer, Trajectory> cross_valiation = new HashMap<Integer, Trajectory>();
		long startTime = System.currentTimeMillis();
		Offline_indexing offIndex = new Offline_indexing();
		offIndex.processing();
		long endTime = System.currentTimeMillis();
		System.out.println("Preprocessing time : " + (endTime -  startTime));
		cross_valiation.putAll(offIndex.cross_validation);
		
		double max_avg_accuracy = 0.0;
		int max_g_accuracy = 0;
		int max_k_accuracy = 0;
		
		double min_avg_time = 9999.0;
		int min_g_time = 0;
		int min_k_time = 0;
		
		double min_k_value = 999999999.0;
		double min_g_value = 99999999.0;
		int optimal_g = 0;
		int optimal_k = 0;
		/**********************************************************/
		for(int g = 20; g > 10; g = g - 10)
		{
		
			Iterator it = cross_valiation.entrySet().iterator();
			double avg_acc = 0.0;
			double avg_query_time = 0.0;
			int query_count = 0;
			while (it.hasNext()) 
		    {
		        Map.Entry pairs = (Map.Entry)it.next();
		        Trajectory query = new Trajectory();
		        query = cross_valiation.get(Integer.parseInt(pairs.getKey().toString()));
		        Trajectory query_del = new Trajectory();
		        //time taken
		        startTime = System.currentTimeMillis();
		        offIndex.process.final_val.clear();
		        offIndex.process.final_vars.clear();
		        offIndex.process.similarity(query, g);
		        offIndex.process.similarity1(query, 15);
				endTime = System.currentTimeMillis();
				System.out.println("Taxi " +query.id+ " Time taken for query : " + query_count + " is : " + (endTime - startTime));
				
				HashMap<Integer, Integer> final_values = offIndex.process.final_val;
				avg_query_time += (endTime - startTime);
				
				//accuracy
				//best accuracy = length / position = length / 1
				double accuracy = 0.0;
				Iterator it1 = final_values.entrySet().iterator();
				int count = 1 ;
				//rank correlation with baseline
				System.out.println("Query : " + query_count + " Top 10 rankers : for g = "+g );
				while(it1.hasNext())
				{
					Map.Entry pairs1 = (Map.Entry)it1.next();
					if(Integer.parseInt(pairs1.getKey().toString()) == query.id)
					{
						accuracy = count / final_values.get(Integer.parseInt(pairs1.getKey().toString()));
						break;
					}
					System.out.println(pairs1.getKey().toString() + "  " + pairs1.getValue().toString() + " " 
							+ offIndex.process.final_vars.get(Integer.parseInt(pairs1.getKey().toString())) +  " ; ");
					count++;
				}
				//System.out.println();
				System.out.println("Accuracy : " + accuracy + " Actual length of query : " + query.trajectory.size());
				System.out.println();
				avg_acc += accuracy;
				query_count++;
		    }
			//remembering the best k and g that give the highest average accuracy
			avg_query_time = avg_query_time/query_count;
			if(avg_acc > max_avg_accuracy)
			{
				max_avg_accuracy = avg_acc;
				max_g_accuracy = g;
			}
			if(avg_query_time < min_avg_time)
			{
				min_avg_time = avg_query_time;
				min_g_time = g;
			}
			System.out.println("Average accuracy for g = " + g + " is : "+ max_avg_accuracy);
			System.out.println("Average time taken for g = " + g + " is : "+ avg_query_time);
		}
		System.out.println("Best (accuracy wise) g and k from cross validation : " + max_g_accuracy );
		System.out.println("Best (time wise) g and k from cross validation : " + min_g_time );
		
		
		/********************************************************/
		
		for(int g = 10; g <= 50; g = g + 10)
		{
			double temp2 = 0.0;
			for(int k = 5; k <= 25; k = k +5)
			{
				double temp = 0.0;
				System.out.println("Gran : " + g + " k : "  + k);
				startTime = System.currentTimeMillis();
				offIndex.all_chunk_k_clusters.clear();
				offIndex.indexing(g, k);
				offIndex.clustering(k, g);
				endTime = System.currentTimeMillis();
				System.out.println("Index construction time for gran : " + g + " k : " + k + " is " + (endTime- startTime));
				
				Iterator it = cross_valiation.entrySet().iterator();
				double avg_query_time = 0.0;
				double avg_sim = 0.0;
				int query_count = 0;
				while (it.hasNext()) 
			    {
			        Map.Entry pairs = (Map.Entry)it.next();
			        Trajectory query = new Trajectory();
			        query = cross_valiation.get(Integer.parseInt(pairs.getKey().toString()));
			        
			        
			        //time taken
			        startTime = System.currentTimeMillis();
			        olProcessing.initialise(offIndex.all_chunk_k_clusters, offIndex.min_start, offIndex.max_end,
							g, query , 10); //l is constant
			        LinkedHashMap<Integer, Trajectory_Similarity> final_values = olProcessing.processing();
					endTime = System.currentTimeMillis();
					System.out.println("Time taken for query : " + query.id + " for gran : " + g + " k : " + k  + " is : " + (endTime - startTime));
					avg_query_time += (endTime - startTime);
					
					olProcessing.saved_computation = 0;
					
					olProcessing.all_chunk_k_clusters.clear();
					olProcessing.traj_max = new HashMap<Integer, Maximum>();
					//accuracy
					//best accuracy = length / position = length / 1
					double accuracy = 0.0;
					Iterator it1 = final_values.entrySet().iterator();
					int count = 1 ;
					//rank correlation with baseline
					gen top_1 = new gen();
					System.out.println("Query : " + query.id + " Top 10 rankers : ");
					while(it1.hasNext())
					{
						Map.Entry pairs1 = (Map.Entry)it1.next();
						/*if(Integer.parseInt(pairs1.getKey().toString()) == query.id)
						{
							accuracy = count / final_values.get(Integer.parseInt(pairs1.getKey().toString()));
							break;
						}*/
						if(count == 1)
						{
							int index = Integer.parseInt(pairs1.getKey().toString());
							System.out.print(index + " : " + final_values.get(index).traj_id + "  " + "( " + final_values.get(index).var + " )  ");
							avg_sim += final_values.get(index).var;
							top_1.traj_id =index ;
							top_1.var = final_values.get(index).var;
							top_1.length = final_values.get(index).traj_id;
						}
						else
						{
							int index = Integer.parseInt(pairs1.getKey().toString());
							System.out.print(index + " : " + final_values.get(index).traj_id+ " ");
							if(top_1.traj_id == final_values.get(index).traj_id)
							{
								if(top_1.var > final_values.get(index).var)
								{
									top_1.traj_id =index ;
									top_1.var = final_values.get(index).var;
									top_1.length = final_values.get(index).traj_id;
								}
							}
						}
						count++;
					}
					System.out.println();
					System.out.println("Best : " + top_1.traj_id + "  " + top_1.length + "  " + top_1.var +"  " + top_1.var/ (top_1.length* top_1.length *top_1.length ) );
					temp+= ((endTime - startTime) * top_1.var ) / (top_1.length * top_1.length);
					temp2 += (top_1.var ) / (top_1.length * top_1.length);
					//System.out.println("Accuracy : " + accuracy + " Actual length of query : " + query.trajectory.size());
					//avg_acc += accuracy;
					query_count++;
			    }
				//remembering the best k and g that give the highest average accuracy
				/*if(avg_acc > max_avg_accuracy)
				{
					max_avg_accuracy = avg_acc;
					max_g_accuracy = g;
					max_k_accuracy = k;
				} */
				
				if(temp < min_k_value)
				{
					min_k_value = temp;
					optimal_k = k;
				}
				
				//System.out.println("Average accuracy for g = " + g + " k =  " + k + " is : "+ max_avg_accuracy);
				System.out.println("Average time taken for g = " + g + " k =  " + k + " is : "+ avg_query_time/300);
				System.out.println("Average sim for g = " + g + " k =  " + k + " is : "+ avg_sim/300);
			}
			if(temp2 < min_g_value)
			{
				min_g_value = temp2;
				optimal_g = g;
			}
		}
		System.out.println("Best (accuracy wise) g and k from cross validation : " + optimal_g + "  " + optimal_k);
	}
}
