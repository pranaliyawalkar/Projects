import java.awt.GradientPaint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Process_indexing 
{
	HashMap<Integer, Trajectory> trajectories = new HashMap<Integer, Trajectory>();
	ArrayList<K_Clusters> all_chunk_k_clusters = new ArrayList<K_Clusters>();
	long min_start, max_end;
	void processing()
	{
		Process process = new Process();
		process.read_trajectories();
		process.interpolate();
		trajectories.putAll(process.trajectories);
		min_start = process.min_start;
		max_end = process.max_end;
	}
	
	void indexing(long granularity, long k)
	{
		//810
		//9699
		long gap = granularity*5;
		
		for(long i = min_start ; i< max_end; i = i + gap)
		{
			ArrayList<Cluster> clusters = new ArrayList<Cluster>();
			for(long j = 0; j < k ;j++)
				clusters.add(new Cluster());
			K_Clusters k_clusters = new K_Clusters();
			k_clusters.k_clusters.addAll(clusters);
			all_chunk_k_clusters.add(k_clusters);
		}
		
		
		
		for(int t = 0 ; t < trajectories.size(); t++)
		{
			//greatest number such that number - 810 divisible by 25
			long chunk_number = ( (trajectories.get(t).trajectory.get(0).time - min_start) /gap);
			
			
			
			long new_start = (chunk_number+1)*gap + min_start;
			
			add_to_clusters(chunk_number, trajectories.get(t).id, trajectories.get(t).trajectory.get(0).time,
			new_start - trajectories.get(t).trajectory.get(0).time, k, granularity);
			
			for(long i = new_start ; ; i = i + gap)
			{
				if(i > trajectories.get(t).trajectory.get(trajectories.get(t).trajectory.size()-1).time)
				{
					i = i - gap;
					chunk_number = chunk_number + 1;
					add_to_clusters(chunk_number, trajectories.get(t).id, i,
							trajectories.get(t).trajectory.get(trajectories.get(t).trajectory.size()-1).time - i 
							,  k, granularity);
				}
				chunk_number = (i - min_start) / gap;
				add_to_clusters(chunk_number, trajectories.get(t).id, i, gap, k, granularity);
				
 			}
			
		}
	}
	
	void add_to_clusters(long chunk_number, int taxi_id, long init_time, long gap, long k, long granularity)
	{
		//ArrayList<Cluster> clusters = all_chunk_k_clusters.get((int)chunk_number).k_clusters;
		K_Clusters k_clusters = all_chunk_k_clusters.get( (int) chunk_number);
		Cluster_element element = new Cluster_element();
		
		int t = 0;
		while(trajectories.get(taxi_id).trajectory.get(t).time < init_time)
			t++;
		int number_of_points = (int)( gap / 5 );
		element.traj.id = taxi_id;
		for(int i = t ; i< number_of_points; i++)
			element.traj.trajectory.add(trajectories.get(taxi_id).trajectory.get(i));
		k_clusters.bulk.add(element);
	}
	
	void clustering(int k, int granularity)
	{
		for(int i = 0; i < all_chunk_k_clusters.size() ; i++)
		{
			K_Clusters k_clusters = all_chunk_k_clusters.get(i);
			
			//init
			int t ;
			for(t = 0; t < k ; t++)
			{
				k_clusters.k_clusters.get(t).centroid = k_clusters.bulk.get(t);
			}
			
			int count = 0;
			
			while(true)
			{
				for(int j = t; j < k_clusters.bulk.size() ; j++)
				{
					int closest = 0 ;
					double max_sim = 0.0;
					for(int l = 0; l< k ; l++)
					{
						double sim = similarity(k_clusters.bulk.get(j).traj, k_clusters.k_clusters.get(l).centroid.traj);
						if(sim > max_sim)
						{
							max_sim = sim;
							closest = l;
						}
					}
					
					
					//find similarities of each point with centroids
					//find the closest one.
					if(closest != k_clusters.assignment.get(j))
						count++;
					
					k_clusters.k_clusters.get(closest).elements.add(k_clusters.bulk.get(j));
					k_clusters.assignment.set(j, closest);
				}
				//recompute
				for( t = 0; t< k ; t++)
				{
					Cluster_element new_centroid = new Cluster_element();
					
					int chunk_number = i;
					for(int p = 0; p< granularity; p++)
					{
						Point new_point = new Point();
						new_point.lati = 0;
						new_point.longi = 0;
						new_point.time = (chunk_number * granularity + p) * 5 + min_start;
						new_centroid.traj.trajectory.add(new_point);
					}
					
					ArrayList<Integer> indices = new ArrayList<Integer>();
					for(int p = 0; p< k_clusters.k_clusters.get(t).elements.size(); p++)
						indices.add(0);
					for(int centroid_index = 0 ; centroid_index < granularity; centroid_index++)
					{
						int size = k_clusters.k_clusters.get(t).elements.size();
						for(int j = 0 ; j < k_clusters.k_clusters.get(t).elements.size(); j++)
						{
							if(k_clusters.k_clusters.get(t).elements.get(j).traj.trajectory.get(indices.get(j)).time 
									== k_clusters.k_clusters.get(t).centroid.traj.trajectory.get(centroid_index).time);
							{
								k_clusters.k_clusters.get(t).centroid.traj.trajectory.get(centroid_index).lati += 
									k_clusters.k_clusters.get(t).elements.get(j).traj.trajectory.get(indices.get(j)).lati;
								k_clusters.k_clusters.get(t).centroid.traj.trajectory.get(centroid_index).longi += 
									k_clusters.k_clusters.get(t).elements.get(j).traj.trajectory.get(indices.get(j)).longi;
								indices.set(j, indices.get(j) + 1);
							}
						}
						//mean
						k_clusters.k_clusters.get(t).centroid.traj.trajectory.get(centroid_index).lati = 
							k_clusters.k_clusters.get(t).centroid.traj.trajectory.get(centroid_index).lati / size;
						
						k_clusters.k_clusters.get(t).centroid.traj.trajectory.get(centroid_index).longi = 
							k_clusters.k_clusters.get(t).centroid.traj.trajectory.get(centroid_index).longi / size;
						
						
					}
					
				}
				//clear cluster
				k_clusters.k_clusters.clear();
				t = 0;
				
			}
			
		}
	}
	
	public double similarity(Trajectory query1, Trajectory query2)
	{
		double var = 0.0;
		double sim = 0.0;
		
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
			while(query1.trajectory.get(start_point_1).time < max_start)
				start_point_1++;
			
		}
		else 
		{
			max_start = start1;
			//offset traj 2
			start_point_1 = 0;
			start_point_2 = 0;
			while(query2.trajectory.get(start_point_2).time < max_start)
				start_point_2++;
			
			
		}
		if(end1 < end2)
		{
			min_end = end1;
			end_point_2 = size2 -1 ;
			end_point_1 = size1 - 1;
			while(query2.trajectory.get(end_point_2).time > min_end)
				end_point_2--;
		}
		else 
		{
			end_point_2 = size2 -1 ;
			end_point_1 = size1 - 1;
			min_end = end2;
			while(query1.trajectory.get(end_point_1).time > min_end)
				end_point_1--;
		}
		
		long total_points =  ( max_end - min_start ) / 5;
		ArrayList<Double> sim_values = new ArrayList<Double>();
		double l2 = 0.0;
		int index1 = start_point_1;
		int index2 = start_point_2;
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
		for(int j = 0; j< sim_values.size(); j++)
		{
			var += Math.pow(sim_values.get(j) - mean, 2);
		}
		var = var / sim_values.size();
		if(!(Math.abs(var - 0.0) < 0.00001))
			sim = 1/var;
		return sim;
	}
	
	public double L2(Point point1, Point point2)
	{
		double ans = 0.0;
		ans = Math.pow((point1.lati - point2.lati), 2) + Math.pow((point1.longi - point2.longi), 2);
		ans = Math.sqrt(ans);
		return ans;
	}
	
}
