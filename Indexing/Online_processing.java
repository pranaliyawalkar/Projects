import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class Online_processing 
{
	ArrayList<K_Clusters> all_chunk_k_clusters = new ArrayList<K_Clusters>();
	long min_start, max_end, granularity, gap, k;
	Chunk_top_l[] chunk_top_l; //top-k for every chunk
	HashMap <Integer, Maximum> traj_max = new HashMap<Integer, Maximum>();
	double average_variance = 0.0 ;
	long saved_computation = 0;
	int total_count = 0;
	int l;
	Trajectory query = new Trajectory();
	
	void initialise(ArrayList<K_Clusters> chunks_clusters, 
			long input_min_start, long input_max_end, long ip_granularity, Trajectory input_query, int input_l)
	{
		all_chunk_k_clusters.addAll(chunks_clusters);
		min_start = input_min_start;
		max_end = input_max_end;
		granularity = ip_granularity;
		gap = granularity*5;
		k = all_chunk_k_clusters.get(0).k_clusters.size();
		query = input_query;
		l = input_l;
		chunk_top_l = new Chunk_top_l[all_chunk_k_clusters.size()];
		for(int i = 0; i < all_chunk_k_clusters.size(); i++)
			chunk_top_l[i] = new Chunk_top_l();
	}
	
	
	public LinkedHashMap<Integer, Trajectory_Similarity> processing()
	{
			//greatest number such that number - 810 divisible by 25
			long chunk_number = ( (query.trajectory.get(0).time - min_start) /gap);
			long new_start = (chunk_number+1)*gap + min_start;
			chunk_top_l[(int)chunk_number].traj_sim.addAll(top_l_clusters(chunk_number, query.id, query.trajectory.get(0).time,
					new_start - query.trajectory.get(0).time, k, granularity, query.trajectory.get(0).time)) ;
			
			
			
			
			for(long i = new_start ; ; i = i + gap)
			{
				if( (i+gap) > query.trajectory.get(query.trajectory.size()-1).time)
				{
					i = i - gap;
					chunk_number = chunk_number + 1;
					chunk_top_l[(int)chunk_number].traj_sim.addAll(top_l_clusters(chunk_number, query.id, i,
							query.trajectory.get(query.trajectory.size()-1).time - i 
							,  k, granularity, query.trajectory.get(0).time))  ;
					break;
				}
				chunk_number = (i - min_start) / gap;
				chunk_top_l[(int)chunk_number].traj_sim.addAll(top_l_clusters(chunk_number, query.id, i, gap, k, granularity, query.trajectory.get(0).time))  ;
				
 			}
			
			average_variance = average_variance / total_count;
			
		//System.out.println("Chunks: ");
		//System.out.println(chunk_top_l);
		//System.out.println("*******************************");
		int j = 0;
		/*while (j <= (int)chunk_number) 
		{
			System.out.println("Chunk : " + j);
			System.out.println( "Top list: " );
			for(int i = 0 ;i < chunk_top_l[j].traj_sim.size() ; i++)
				System.out.println(chunk_top_l[j].traj_sim.get(i).traj_id + "  " + chunk_top_l[j].traj_sim.get(i).var );
			j++;
		}*/
		
		int total_chunks = (int)chunk_number + 1;
		System.out.println( "Average saved computation for the chunks : " + saved_computation/total_chunks);
		int max_len = 0;
		Map<Integer, Integer> values = new HashMap<Integer, Integer>();
		Map<Integer, Double> vars = new HashMap<Integer, Double>();
		LinkedHashMap<Integer, Trajectory_Similarity> return_values = new LinkedHashMap<Integer, Trajectory_Similarity>();
		double variance = 0.0;
		double min_variance = 99999999;
		for(int x = 0 ;x < total_chunks; x++)
		{
			for(int y = 0; y < 3 ; y++)
			{
				if(y < chunk_top_l[x].traj_sim.size())
				{
					ArrayList<Integer> sequence = new ArrayList<Integer>();
					variance = 0.0;
					int taxi_id = chunk_top_l[x].traj_sim.get(y).traj_id;
					for(int z = 0 ; z< total_chunks; z++)
					{
						for(int p = 0; p < chunk_top_l[z].traj_sim.size(); p++)
						{
							if(chunk_top_l[z].traj_sim.get(p).traj_id == taxi_id)
							{
								variance += chunk_top_l[z].traj_sim.get(p).var;
								if(chunk_top_l[z].traj_sim.get(p).var < 0.0)
								{
									int zp = 0;
								}
								sequence.add(z);
								break;
							}
						}
					}
					int len = longest_sum_subsequence(sequence);
					if(values.get(taxi_id) == null)
					{
						values.put(taxi_id, len);
						vars.put(taxi_id, variance);
					}
					else
					{
						if(values.get(taxi_id) < len)
						{
							values.remove(taxi_id);
							values.put(taxi_id, len);
							vars.remove(taxi_id);
							vars.put(taxi_id, variance);
						}
					}
				}
			}
		}
		Map<Integer, Integer> sorted_final_values = new HashMap<Integer, Integer>();
		sorted_final_values = sortByValues_decreasing(values);
		Iterator it = sorted_final_values.entrySet().iterator();
		int count = 0;
		while (it.hasNext() && count < 10) 
	    {
	        Map.Entry pairs = (Map.Entry)it.next();
	        Trajectory_Similarity new_sim = new Trajectory_Similarity();
	        new_sim.traj_id = values.get(Integer.parseInt(pairs.getKey().toString()));
	        new_sim.var = vars.get(Integer.parseInt(pairs.getKey().toString()));
	        return_values.put(Integer.parseInt(pairs.getKey().toString()), new_sim );
	        
	        count++;
	    }
		
		return return_values;
	}
	
	ArrayList<Trajectory_Similarity> top_l_clusters(long chunk_number, int taxi_id, long init_time, long gap,
			long k, long granularity, long query_start)
	{
		ArrayList<Trajectory_Similarity> top_l_ans = new ArrayList<Trajectory_Similarity>();
		K_Clusters k_clusters = all_chunk_k_clusters.get((int)chunk_number);
		
		Cluster_element query_chunk = new Cluster_element();
		
		//int start_point = ((int)chunk_number * (int)granularity) + ((int)granularity*5 -(int) gap)/ 5;
		int start_point = (int) (init_time-query_start)/ 5;
		for(int i = start_point ; i < gap / 5 + start_point ; i++)
		{
			if(i >= query.trajectory.size())
				break;
			query_chunk.traj.trajectory.add(query.trajectory.get(i));
		}
		
		HashMap<Integer, Double> top_l = new HashMap<Integer, Double>(); //taxi id vs similarity
		
		double min = 99999.0;
		int max_cluster = 0;
		
		for(int i = 0; i < k ; i++)
		{
			double var = similarity(query_chunk.traj, k_clusters.k_clusters.get(i).centroid.traj);
			if(var < min && ! (var < -0.5 ))
			{
				min = var;
				max_cluster = i;
				if(var < 0.0)
				{
					int zs = 0;
				}
			}
			
		}
		
		//finding top-l in that cluster 
		//System.out.println("Size of cluster = "+ k_clusters.k_clusters.get(max_cluster).elements.size());
		saved_computation += (long)k_clusters.bulk.size() - k_clusters.k_clusters.get(max_cluster).elements.size();
		for (int i = 0; i < k_clusters.k_clusters.get(max_cluster).elements.size(); i++)
		{
			//System.out.println(" searching in cluster " + max_cluster);
			double var = similarity(query_chunk.traj, k_clusters.k_clusters.get(max_cluster).elements.get(i).traj);
			if(! (var < -0.5 ))
			{
				top_l.put(k_clusters.k_clusters.get(max_cluster).elements.get(i).traj.id, var);
				if(var < 0.0)
				{
					int r = 0;
				}
			}
			
		}
		
		//sort in increasing order;
		Map<Integer, Double> sorted_var = new HashMap<Integer, Double>();
		sorted_var = sortByValues(top_l);
		
		Iterator it = sorted_var.entrySet().iterator();
		int count = 0;
		while (it.hasNext() && count < 50) 
	    {
	        Map.Entry pairs = (Map.Entry)it.next();
	        Trajectory_Similarity new_traj_sim_obj = new Trajectory_Similarity();
	        new_traj_sim_obj.traj_id = Integer.parseInt(pairs.getKey().toString());
	        new_traj_sim_obj.var = Double.parseDouble(pairs.getValue().toString());
	        if(! (new_traj_sim_obj.var < -0.5 ))
	        {
	        	top_l_ans.add(new_traj_sim_obj);
	        	average_variance += new_traj_sim_obj.var;
	        	count++;
	        	if(new_traj_sim_obj.var < 0.0)
	        	{
	        		int r = 0;
	        	}
	        }
	        total_count += count;
	    }
		
		return top_l_ans;
	}
	public static <K extends Comparable,V extends Comparable> Map<K,V> sortByValues_decreasing(Map<K,V> map){
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

	
	public static <K extends Comparable,V extends Comparable> Map<K,V> sortByValues(Map<K,V> map){
        List<Map.Entry<K,V>> entries = new LinkedList<Map.Entry<K,V>>(map.entrySet());
      
        Collections.sort(entries, new Comparator<Map.Entry<K,V>>() {

            @Override
            public int compare(Entry<K, V> o1, Entry<K, V> o2) {
            	return o1.getValue().compareTo(o2.getValue());
            }
        });
        Map<K,V> sortedMap = new LinkedHashMap<K,V>();
      
        for(Map.Entry<K,V> entry: entries){
            sortedMap.put(entry.getKey(), entry.getValue());
        }
      
        return sortedMap;
    }
	
	
	public double similarity(Trajectory query1, Trajectory query2)
	{
		double var = 0.0;
		double sim = 0.0;
		
		
		if(query1.trajectory.size() == 0 || query2.trajectory.size() == 0 )
			return 999999.0;
		long start1 = query1.trajectory.get(0).time;
		long start2 = query2.trajectory.get(0).time;
		int size1 = query1.trajectory.size();
		int size2 = query2.trajectory.size();
		long end1 = query1.trajectory.get(size1 - 1).time;
		long end2 = query2.trajectory.get(size2 - 1).time;
		
		int start_point_1, start_point_2, end_point_1, end_point_2; 
		
		long max_start , min_end;
		if(start1 < start2)
		{
			max_start = start2;
			
			//offset traj 1
			start_point_1 = 0;
			start_point_2 = 0;
			while(query1.trajectory.contains(start_point_1) &&
					query1.trajectory.get(start_point_1).time < max_start)
				start_point_1++;
			
		}
		else 
		{
			max_start = start1;
			//offset traj 2
			start_point_1 = 0;
			start_point_2 = 0;
		//	System.out.println("size : " + query2.trajectory.size() + " max start "+ max_start);
			while(query2.trajectory.contains(start_point_2) 
					&& query2.trajectory.get(start_point_2).time < max_start)
				start_point_2++;
			
			
		}
		if(end1 < end2)
		{
			min_end = end1;
			end_point_2 = size2 -1 ;
			end_point_1 = size1 - 1;
			while(end_point_2 >= 0 && query2.trajectory.contains(end_point_2) &&
					query2.trajectory.get(end_point_2).time > min_end)
				end_point_2--;
		}
		else 
		{
			end_point_2 = size2 -1 ;
			end_point_1 = size1 - 1;
			min_end = end2;
			while(end_point_1 >= 0 && query1.trajectory.contains(end_point_1) &&
					query1.trajectory.get(end_point_1).time > min_end)
				end_point_1--;
		}
		
		long total_points =  ( min_end - max_start ) / 5 + 1;
		ArrayList<Double> sim_values = new ArrayList<Double>();
		double l2 = 0.0;
		int index1 = start_point_1;
		int index2 = start_point_2;
		//System.out.println("index 1: "+index1 + " index 2: "+ index2+ " total " + total_points);
		for(int i = 0; i < total_points; i++)
		{
			
			l2 = L2(query1.trajectory.get(index1), query2.trajectory.get(index2));
			sim_values.add(l2);
			index1++;
			index2++;
		}
		
		double mean = 0.0;
		double sum = 0.0;
		for(int j = 0; j< sim_values.size(); j++)
		{
			sum += sim_values.get(j);
		}
		mean = sum / sim_values.size();
		if(sim_values.size()==1) //one overlapping point
		{
			var = sim_values.get(0);
			return var;
		}
		for(int j = 0; j< sim_values.size(); j++)
		{
			var += Math.pow(sim_values.get(j) - mean, 2);
		}
		if(sim_values.size() !=0)
			var = var / (sim_values.size());
		else
			var = -1;
		if(Math.abs(var - 0.0) < 0.000000000000000001)
		{
			//System.out.println("Double fucked");
		}
		return var;
	}
	
	
	public double L2(Point point1, Point point2)
	{
		double ans = 0.0;
		ans = Math.pow((point1.lati - point2.lati), 2) + Math.pow((point1.longi - point2.longi), 2);
		ans = Math.sqrt(ans);
		return ans;
	}
	
	public int longest_sum_subsequence(ArrayList<Integer> sequence)
	{
		//finding the longest sequence of consecutive numbers
		int max_length = 0;
		if(sequence.size() == 1)
			return 1;
		for(int i = 0; i < sequence.size() - 1;)
		{
			int len = 0;
			while(true)
			{
				len++;
				if(sequence.get(i)+1 != sequence.get(i+1))
				{
					i++;
					break;
				}
				i++;
				if(i == sequence.size()-1)
					break;
			}
			if(len > max_length)
				max_length = len;
		}
		return max_length;
	}
}
