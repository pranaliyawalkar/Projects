import java.awt.GradientPaint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;


public class Offline_indexing 
{
	HashMap<Integer, Trajectory> trajectories = new HashMap<Integer, Trajectory>();
	public HashMap<Integer, Trajectory> cross_validation = new HashMap<Integer, Trajectory>();
	public ArrayList<K_Clusters> all_chunk_k_clusters = new ArrayList<K_Clusters>();
	long min_start, max_end;
	public Process process = new Process();
	void processing()
	{
		
		process.read_trajectories();
		process.interpolate();
		process.read_cross_valid();
		process.interpolate_cross_valid_set();
		//process.similarity(query, 20);
		//trajectories.putAll(process.trajectories);
		cross_validation.putAll(process.cross_validation);
		//System.out.println("Done");
		Map tmp = new HashMap(process.trajectories);
		tmp.keySet().removeAll(trajectories.keySet());
		trajectories.putAll(tmp);
		min_start = process.min_start;
		max_end = process.max_end;
	}
	
	void indexing(long granularity, long k)
	{
		//810
		//9699
		long gap = granularity*5;
		
		for(long i = min_start ; i < max_end; i = i + gap)
		{
			ArrayList<Cluster> clusters = new ArrayList<Cluster>();
			for(long j = 0; j < k ;j++)
				clusters.add(new Cluster());
			K_Clusters k_clusters = new K_Clusters();
			k_clusters.k_clusters.addAll(clusters);
			all_chunk_k_clusters.add(k_clusters);
		}
		ArrayList<Cluster> clusters = new ArrayList<Cluster>();
		for(long j = 0; j < k ;j++)
			clusters.add(new Cluster());
		K_Clusters k_clusters = new K_Clusters();
		k_clusters.k_clusters.addAll(clusters);
		all_chunk_k_clusters.add(k_clusters);
		
		Iterator it = trajectories.entrySet().iterator();
		int count = 0;
		while (it.hasNext()) 
		{
			Map.Entry pairs = (Map.Entry)it.next();
			int t = Integer.parseInt(pairs.getKey().toString());
			long chunk_number = ( (trajectories.get(t).trajectory.get(0).time - min_start) /gap);

			long new_start = (chunk_number+1)*gap + min_start;
			
			add_to_clusters(chunk_number, t /*trajectories.get(t).id*/, trajectories.get(t).trajectory.get(0).time,
			new_start - trajectories.get(t).trajectory.get(0).time, k, granularity);
			
			for(long i = new_start ; ; i = i + gap)
			{
				if(i > trajectories.get(t).trajectory.get(trajectories.get(t).trajectory.size()-1).time)
				{
					i = i - gap;
					chunk_number = chunk_number + 1;
					//System.out.println("chunck "+chunk_number);
					add_to_clusters(chunk_number, t, i,
							trajectories.get(t).trajectory.get(trajectories.get(t).trajectory.size()-1).time - i 
							,  k, granularity);
					break;
				}
				chunk_number = (i - min_start) / gap;
				add_to_clusters(chunk_number, t, i, gap, k, granularity);
				
 			}
		}
		//for(int y = 0; y < all_chunk_k_clusters.size(); y++){
		//	System.out.println(" bulk size = " + all_chunk_k_clusters.get(y).bulk.size()); 
			
		//}
	}
	
	void add_to_clusters(long chunk_number, int taxi_id, long init_time, long gap, long k, long granularity)
	{
		//ArrayList<Cluster> clusters = all_chunk_k_clusters.get((int)chunk_number).k_clusters;
		K_Clusters k_clusters = all_chunk_k_clusters.get( (int) chunk_number);
		Cluster_element element = new Cluster_element();
		
		int t = 0;
		//System.out.println("cuppping: "+ trajectories.containsKey(taxi_id));
		while(trajectories.get(taxi_id).trajectory.get(t).time < init_time)
			t++;
		int number_of_points = (int)( gap / 5 );
		element.traj.id = taxi_id;
		for(int i = t ; i< t + number_of_points; i++)
		{
			if(i >= trajectories.get(taxi_id).trajectory.size())
				break;
			element.traj.trajectory.add(trajectories.get(taxi_id).trajectory.get(i));
		}
		if(element.traj.trajectory.size()!=0)
			all_chunk_k_clusters.get( (int) chunk_number).bulk.add(element);
	}
	
	void clustering(int k, int granularity)
	{
		for(int i = 0; i < all_chunk_k_clusters.size(); i++)
		{
		//	System.out.println("chunk: " + i);
			K_Clusters k_clusters = all_chunk_k_clusters.get(i);
			if(k_clusters.bulk.size() == 0)
				continue;
			//init
			int t ;
			HashSet<Integer> centroids  = new HashSet<Integer>();
			for(t = 0; t < k ; )
			{
				Random r = new Random();
				int p = r.nextInt(k_clusters.bulk.size()-1);
				if(!centroids.contains(p) /*&& ( k_clusters.bulk.get(p).traj.trajectory.size() ==granularity || 
						k_clusters.bulk.get(p).traj.trajectory.size() ==granularity+1 ||
						k_clusters.bulk.get(p).traj.trajectory.size() ==granularity-1 ) */)
				{
					k_clusters.k_clusters.get(t).centroid = k_clusters.bulk.get(p);
					t++;
					centroids.add(p);
				}
			}
			
			int count = 0;
			int terminate = 0;
			
			for(int p = 0 ; p < k_clusters.bulk.size(); p++)
			{
				k_clusters.assignment.add(-1);
			}
			
			while(true)
			{
				terminate++;
				count = 0 ;
				for(int j = 0; j < k_clusters.bulk.size() ; j++)
				{
					
					int closest = 0 ;
					double min_var = 9999.0;
					for(int l = 0; l< k ; l++)
					{
						double var = similarity(k_clusters.bulk.get(j).traj, k_clusters.k_clusters.get(l).centroid.traj);
						//System.out.println("Variance = " + var);
						if(var < min_var && ! (var < -0.5 ))
						{
							//System.out.println("Variance = " + var);
							min_var = var;
							closest = l;
						}
					}
					//System.out.println("Closest cluster = " + closest);
					//find similarities of each point with centroids
					//find the closest one.
					//System.out.println("Closest : " + closest);
					if(closest != k_clusters.assignment.get(j))
						count++;
					k_clusters.k_clusters.get(closest).elements.add(k_clusters.bulk.get(j));
					k_clusters.assignment.set(j, closest);
				}
				//recompute
				if(terminate < 6)
				{
					for( t = 0; t< k ; t++)
					{
						//compute similarity of each point with every other point
						int size = k_clusters.k_clusters.get(t).elements.size();
						double[][] vars = new double[size][size];
						double avg = 0.0;
						int temp = 0;
						for(int r = 0; r < size; r++)
						{
							for(int s = 0; s < size; s++)
							{
								double var = similarity(k_clusters.k_clusters.get(t).elements.get(r).traj, k_clusters.k_clusters.get(t).elements.get(s).traj);
								
								if(var > -0.5)
								{
									vars[r][s] = var;
									avg += var;
									temp++;
								}
							}
						}
						avg = avg / (temp);
						if(temp > 0)
						{
							HashMap<Integer, ArrayList<Integer>> avg_points = new HashMap<Integer, ArrayList<Integer>>();
							for(int r = 0; r < size; r++)
							{
								for(int s = 0; s < size; s++)
								{
									if(vars[r][s] <= avg && k_clusters.k_clusters.get(t).elements.get(s).traj.trajectory.size() == granularity)
									{
										if(avg_points.get(r)!=null)
										{
											if(!avg_points.get(r).contains(s))
												avg_points.get(r).add(s);
										}
										else
										{
											ArrayList<Integer> new_arr = new ArrayList<Integer>();
											new_arr.add(s);
											avg_points.put(r, new_arr);
										}
										
									}
								}
							}
							
							//intersection
							HashMap< Integer, Integer> frequency = new HashMap< Integer, Integer>();
							for(int r = 0 ; r < size; r++)
							{
								if(avg_points.get(r)!=null)
								{
									ArrayList<Integer> values = avg_points.get(r);
									for(int s = 0; s < values.size(); s++)
									{
										if(frequency.get( values.get(s)) != null)
											frequency.put(values.get(s), frequency.get(values.get(s)) + 1);
										else
											frequency.put(values.get(s), 1);
									}
								}
							}
							
							Iterator it = frequency.entrySet().iterator();
							int max_freq = 0;
							int max_num = 0;
							while (it.hasNext() ) 
						    {
						        Map.Entry pairs = (Map.Entry)it.next();
						        int num = Integer.parseInt(pairs.getKey().toString());
						        int val  = Integer.parseInt(pairs.getValue().toString());
						        if(val >  max_freq)
						        {
						        	max_freq = val;
						        	max_num = num;
						        }
						    }
							
							//new centroid  = max_num
							
							if(size!=0)
							{
								k_clusters.k_clusters.get(t).centroid.traj.trajectory.clear();
								k_clusters.k_clusters.get(t).centroid.traj.trajectory.addAll(k_clusters.k_clusters.get(t).elements.get(max_num).traj.trajectory);
							}
						}
					}
				}
				//clear cluster
				t = 0;
				if(terminate > 5) //Terminate is a constant
				{
					//if(i < 2)
					{
						//System.out.println("Chunk " + i + "  "+ k_clusters.bulk.size() + " iter : "+ terminate);
						//for(int x = 0; x < k_clusters.k_clusters.size(); x++) 
						{
							//System.out.println("Cluster " + x + " = " + k_clusters.k_clusters.get(x).elements.size());
						}
					}
					break;
				}
				/*if(i == 1)
				{
					for(t = 0; t < k ; t++)
					{
						System.out.println("cluster : " + t + " " + k_clusters.k_clusters.get(t).elements.size() );
						System.out.println("Centroid : ");
						for(int x = 0; x <  k_clusters.k_clusters.get(t).centroid.traj.trajectory.size(); x++)
						{
							System.out.print(k_clusters.k_clusters.get(t).centroid.traj.trajectory.get(x).lati + "  " + k_clusters.k_clusters.get(t).centroid.traj.trajectory.get(x).longi + " ; ") ;
						}
						System.out.println();
					}
				}*/
				for(int p = 0; p < k_clusters.k_clusters.size(); p++)
					k_clusters.k_clusters.get(p).elements.clear();
			}
			
		}
	}
	
	
	
	public double similarity(Trajectory query1, Trajectory query2)
	{
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
			while(start_point_1 < query1.trajectory.size() &&
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
			while(start_point_2 < query2.trajectory.size()
					&& query2.trajectory.get(start_point_2).time < max_start)
				start_point_2++;
			
			
		}
		if(end1 < end2)
		{
			min_end = end1;
			end_point_2 = size2 -1 ;
			end_point_1 = size1 - 1;
			while(end_point_2 >= 0 && end_point_2 < query2.trajectory.size() &&
					query2.trajectory.get(end_point_2).time > min_end)
				end_point_2--;
		}
		else 
		{
			end_point_2 = size2 -1 ;
			end_point_1 = size1 - 1;
			min_end = end2;
			while(end_point_1 >= 0 && end_point_1 < query1.trajectory.size() &&
					query1.trajectory.get(end_point_1).time > min_end)
				end_point_1--;
		}
		
		long total_points =  (( min_end - max_start ) / 5 ) +  1;
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
		double var = 0.0;
		for(int j = 0; j< sim_values.size(); j++)
		{
			sum += sim_values.get(j);
		}
		if(sim_values.size()==1) //one overlapping point
		{
			var = sim_values.get(0);
			return var;
		}
		mean = sum / sim_values.size();
		for(int j = 0; j< sim_values.size(); j++)
		{
			var += Math.pow(sim_values.get(j) - mean, 2);
		}
		if(sim_values.size() !=0)
			var = var / (sim_values.size());
		else
			var = -1;
		return var;
	}
	
	public double L2(Point point1, Point point2)
	{
		double ans = 0.0;
		ans = Math.pow((point1.lati - point2.lati), 2) + Math.pow((point1.longi - point2.longi), 2);
		ans = Math.sqrt(ans);
		return ans;
	}
	
}
