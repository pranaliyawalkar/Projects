import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class test1 {

	public void fun()
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
		
		for(int g = 3; g < 15; g = g + 3)
		{
			for(int k = 5; k < 25; k = k +5)
			{
				startTime = System.currentTimeMillis();
				offIndex.indexing(g, k);
				offIndex.clustering(k, g);
				endTime = System.currentTimeMillis();
				System.out.println("Index construction time for gran : " + g + " k : " + k + " is " + (endTime- startTime));
				
				Iterator it = cross_valiation.entrySet().iterator();
				double avg_acc = 0.0;
				double avg_query_time = 0.0;
				while (it.hasNext()) 
			    {
			        Map.Entry pairs = (Map.Entry)it.next();
			        Trajectory query = new Trajectory();
			        query = cross_valiation.get(Integer.parseInt(pairs.toString()));
			        
			        //time taken
			        startTime = System.currentTimeMillis();
			        olProcessing.initialise(offIndex.all_chunk_k_clusters, offIndex.min_start, offIndex.max_end,
							g, query , 10); //l is constant
			        HashMap<Integer, Integer> final_values = olProcessing.processing();
					endTime = System.currentTimeMillis();
					System.out.println("Time taken for query : " + (endTime - startTime));
					avg_query_time += (endTime - startTime);
					
					
					
					//accuracy
					//best accuracy = length / position = length / 1
					double accuracy = 0.0;
					Iterator it1 = final_values.entrySet().iterator();
					int count = 1 ;
					while(it1.hasNext())
					{
						Map.Entry pairs1 = (Map.Entry)it1.next();
						if(Integer.parseInt(pairs1.getKey().toString()) == query.id)
						{
							accuracy = count / final_values.get(Integer.parseInt(pairs1.getKey().toString()));
							break;
						}
					}
					System.out.println("Accuracy : " + accuracy + " Actual length of query : " + query.trajectory.size());
					avg_acc += accuracy;
			    }
				//remembering the best k and g that give the highest average accuracy
				if(avg_acc > max_avg_accuracy)
				{
					max_avg_accuracy = avg_acc;
					max_g_accuracy = g;
					max_k_accuracy = k;
				}
				if(avg_query_time < min_avg_time)
				{
					min_avg_time = avg_query_time;
					min_g_time = g;
					min_k_time = k;
				}
				
				System.out.println("Average accuracy for g = " + g + " k =  " + k + " is : "+ max_avg_accuracy);
				System.out.println("Average time taken for g = " + g + " k =  " + k + " is : "+ avg_query_time);
			}
		}
		System.out.println("Best (accuracy wise) g and k from cross validation : " + max_g_accuracy + "  " + max_k_accuracy);
		System.out.println("Best (time wise) g and k from cross validation : " + min_g_time + "  " + min_k_time);
	}
}
