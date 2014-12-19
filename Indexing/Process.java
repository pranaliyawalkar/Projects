import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.Map.Entry;


public class Process 
{
	public HashMap<Integer, Trajectory> trajectories = new HashMap<Integer, Trajectory>();
	public HashMap<Integer, Trajectory> cross_validation = new HashMap<Integer, Trajectory>();
	public LinkedHashMap<Integer, Integer> final_val = new LinkedHashMap<Integer, Integer>();
	public LinkedHashMap<Integer, Double> final_vars = new LinkedHashMap<Integer, Double>();
	public long min_start = 999999;
	public long max_end = 0;
	ArrayList<Integer> print = new ArrayList<Integer>();
	
	public void read_trajectories()
	{
		
		String address = "/home/pranali/workspace/indexing/trajectories";
		{
			double min_lat = 99999;
			double min_long = 99999;
			double max_lat = 0;
			double max_long = 0;
			int id = 0;
			File folder = new File(address);
			File[] listOfFiles = folder.listFiles();
			int taxi_id = 1;
			int count = 1;
			for(int t=0; t<10; t++){
				print.add(0);
			}
		//	System.out.println("List of files ********************************: ");
			for (File file : listOfFiles) 
			{
				ArrayList<Integer> num_days = new ArrayList<Integer>();
				try
				{
					FileInputStream fstream = new FileInputStream(file);
					DataInputStream in = new DataInputStream(fstream);
					BufferedReader br = new BufferedReader(new InputStreamReader(in));
					String strLine = "";
					int j = 0;
					int flag = 0;
					for ( j= 0; j< file.getName().toString().length();j++)
					{
						if(file.getName().toString().charAt(j) == '.')
							break;
					}
					taxi_id = Integer.parseInt(file.getName().substring(0, j));
					
					//Random rand = new Random();
					int random_numb;
					///random_numb = ( taxi_id % 10 ) * 50;
					random_numb = 0;
					//print.set(taxi_id%10, print.get(taxi_id%10)+1);
					
					while ((strLine = br.readLine()) != null)   
					{
						flag = 1;
						Point new_point = new Point();
						StringTokenizer stt = new StringTokenizer(strLine,",");
						int temp = Integer.parseInt(stt.nextToken().toString());
						String date_time = stt.nextToken().toString();
						
						StringTokenizer stt1 = new StringTokenizer(date_time, " ");
						String date = stt1.nextToken();
						int int_date = Integer.parseInt(date.substring(8, 10));
						if(!num_days.contains(int_date))
						{
							num_days.add(int_date);
						}
						String time = stt1.nextToken();
						StringTokenizer stt2 = new StringTokenizer(time, ":");
						int hour = Integer.parseInt(stt2.nextToken());
						int mins = Integer.parseInt(stt2.nextToken());
						
						new_point.time = (int_date - 2)*24*60 + hour*60 + mins;
						new_point.lati = Double.parseDouble(stt.nextToken().toString()) + random_numb;
						new_point.longi = Double.parseDouble(stt.nextToken().toString()) + random_numb;
						
						if( new_point.lati > max_lat)
						{
							max_lat = new_point.lati;
						}
						if( new_point.lati < min_lat)
						{
							min_lat = new_point.lati;
						}
						if( new_point.longi > max_long)
						{
							max_long = new_point.longi;
						}
						if( new_point.longi < min_long)
						{
							min_long = new_point.longi;
						}
						
						if( trajectories.get(taxi_id)!=null)
						{
							trajectories.get(taxi_id).trajectory.add(new_point);
							if(new_point.time > max_end) 
								max_end = new_point.time;
						}
						else
						{
							Trajectory new_trajectory = new Trajectory();
							new_trajectory.id = id++;
							new_trajectory.trajectory.add(new_point);
							//System.out.println("adding trajectory");
							if(new_point.time < min_start)
								min_start = new_point.time;
							trajectories.put(taxi_id, new_trajectory);
							//System.out.println(taxi_id);
						}
						
					}
					if(flag == 0 )
						continue;
					
				}
				catch(Exception e)
				{
					System.out.println(e + "  "+ taxi_id);
				}
				
				if(num_days.size() !=7)
				{
					trajectories.remove(taxi_id);
					continue;
				}
				//System.out.println(taxi_id);
				//System.out.println("Taxi : " + taxi_id + " No. of points : " + trajectories.get(taxi_id).trajectory.size());
				if(count > 4000)
					break;
				count++;
			}
		//	for(int t = 0; t < 10; t++)
		//		System.out.println("T = " + t + " Value = "+print.get(t));
			/*System.out.println("Taxi id : " + taxi_id);
			for(int i = 2; i<=8 ;i++)
			{
				System.out.println("Date : " + i);
				for(int j = 0; j < trajectories.get(taxi_id).trajectory.size(); j++)
				{
					Point point = new Point();
					point = trajectories.get(taxi_id).trajectory.get(j);
					System.out.println(point.time + "  " + point.lati + "  " + point.longi);
				}
			}*/
			
		//	System.out.println("***************************************");
			//System.out.println(trajectories.size());
		//	System.out.println(min_start);
		//	System.out.println(max_end);
		
			
			Iterator it = trajectories.entrySet().iterator();
			while (it.hasNext()) 
			{
				Map.Entry pairs = (Map.Entry)it.next();
				int t = Integer.parseInt(pairs.getKey().toString());
				for(int j = 0 ; j < trajectories.get(t).trajectory.size(); j++)
				{
					trajectories.get(t).trajectory.get(j).lati = 500 + 500* (trajectories.get(t).trajectory.get(j).lati - min_lat) /  (max_lat - min_lat );
					trajectories.get(t).trajectory.get(j).longi = 500 + 500* (trajectories.get(t).trajectory.get(j).longi - min_long) /  (max_long - min_long );
				}
				
			}
			
			/*for(int i = 0; i < trajectories.size(); i++)
			{
				for(int j = 0 ; j < trajectories.get(i).trajectory.size(); j++)
				{
					trajectories.get(i).trajectory.get(j).lati = 500 + 500* (trajectories.get(i).trajectory.get(j).lati - min_lat) /  (max_lat - min_lat );
					trajectories.get(i).trajectory.get(j).longi = 500 + 500* (trajectories.get(i).trajectory.get(j).longi - min_long) /  (max_long - min_long );
				}
			}*/		
		}
	}
	
	public void read_cross_valid()
	{
		String address = "/home/pranali/workspace/indexing/cross";
		{
			double min_lat = 99999;
			double min_long = 99999;
			double max_lat = 0;
			double max_long = 0;
			int id = 0;
			File folder = new File(address);
			File[] listOfFiles = folder.listFiles();
			int taxi_id = 1;
			int count = 1;
			for(int t=0; t<10; t++){
				print.add(0);
			}
		//	System.out.println("List of files ********************************: ");
			for (File file : listOfFiles) 
			{
				ArrayList<Integer> num_days = new ArrayList<Integer>();
				try
				{
					FileInputStream fstream = new FileInputStream(file);
					DataInputStream in = new DataInputStream(fstream);
					BufferedReader br = new BufferedReader(new InputStreamReader(in));
					String strLine = "";
					int j = 0;
					int flag = 0;
					for ( j= 0; j< file.getName().toString().length();j++)
					{
						if(file.getName().toString().charAt(j) == '.')
							break;
					}
					taxi_id = Integer.parseInt(file.getName().substring(0, j));
					
					//Random rand = new Random();
					int random_numb;
					//random_numb = ( taxi_id % 10 ) * 50;
					random_numb = 0;
					
					//print.set(taxi_id%10, print.get(taxi_id%10)+1);
					while ((strLine = br.readLine()) != null)   
					{
						flag = 1;
						Point new_point = new Point();
						StringTokenizer stt = new StringTokenizer(strLine,",");
						int temp = Integer.parseInt(stt.nextToken().toString());
						String date_time = stt.nextToken().toString();
						
						StringTokenizer stt1 = new StringTokenizer(date_time, " ");
						String date = stt1.nextToken();
						int int_date = Integer.parseInt(date.substring(8, 10));
						if(!num_days.contains(int_date))
						{
							num_days.add(int_date);
						}
						String time = stt1.nextToken();
						StringTokenizer stt2 = new StringTokenizer(time, ":");
						int hour = Integer.parseInt(stt2.nextToken());
						int mins = Integer.parseInt(stt2.nextToken());
						
						new_point.time = (int_date - 2)*24*60 + hour*60 + mins;
						new_point.lati = Double.parseDouble(stt.nextToken().toString()) + random_numb;
						new_point.longi = Double.parseDouble(stt.nextToken().toString()) + random_numb;
						
						if( new_point.lati > max_lat)
						{
							max_lat = new_point.lati;
						}
						if( new_point.lati < min_lat)
						{
							min_lat = new_point.lati;
						}
						if( new_point.longi > max_long)
						{
							max_long = new_point.longi;
						}
						if( new_point.longi < min_long)
						{
							min_long = new_point.longi;
						}
						if(new_point.time >= 2010 && new_point.time < 8010){
							if( cross_validation.get(taxi_id) != null)
							{
								cross_validation.get(taxi_id).trajectory.add(new_point);
							}
							else
							{
								Trajectory new_trajectory = new Trajectory();
								new_trajectory.id = id++;
								new_trajectory.trajectory.add(new_point);
								cross_validation.put(taxi_id, new_trajectory);
							}
						}
					}
					if(flag == 0 )
						continue;
					
				}
				catch(Exception e)
				{
					System.out.println(e + "  "+ taxi_id);
				}
				
				if(num_days.size() !=7)
				{
					trajectories.remove(taxi_id);
					continue;
				}
				//System.out.println(taxi_id);
				//System.out.println("Taxi : " + taxi_id + " No. of points : " + trajectories.get(taxi_id).trajectory.size());
				if(count > 4000)
					break;
				count++;
			}
		//	for(int t = 0; t < 10; t++)
		//		System.out.println("T = " + t + " Value = "+print.get(t));
			/*System.out.println("Taxi id : " + taxi_id);
			for(int i = 2; i<=8 ;i++)
			{
				System.out.println("Date : " + i);
				for(int j = 0; j < trajectories.get(taxi_id).trajectory.size(); j++)
				{
					Point point = new Point();
					point = trajectories.get(taxi_id).trajectory.get(j);
					System.out.println(point.time + "  " + point.lati + "  " + point.longi);
				}
			}*/
			
		//	System.out.println("***************************************");
			//System.out.println(trajectories.size());
		//	System.out.println(min_start);
		//	System.out.println(max_end);
			Iterator it = cross_validation.entrySet().iterator();
			while (it.hasNext()) 
			{
				Map.Entry pairs = (Map.Entry)it.next();
				int t = Integer.parseInt(pairs.getKey().toString());
				for(int j = 0 ; j < cross_validation.get(t).trajectory.size(); j++)
				{
					cross_validation.get(t).trajectory.get(j).lati = 500 + 500* (cross_validation.get(t).trajectory.get(j).lati - min_lat) /  (max_lat - min_lat );
					cross_validation.get(t).trajectory.get(j).longi = 500 + 500* (cross_validation.get(t).trajectory.get(j).longi - min_long) /  (max_long - min_long );
				}
				
			}
		}
	}
	
	public void interpolate()
	{
		Iterator it = trajectories.entrySet().iterator();
		int count = 0;
		while (it.hasNext()) 
		{
			Map.Entry pairs = (Map.Entry)it.next();
			int i = Integer.parseInt(pairs.getKey().toString());
			ArrayList<Point> new_traj = new ArrayList<Point>();
			long first = 0;
			long last = 0;
			Point last_point = new Point();
			int size = trajectories.get(i).trajectory.size();
			first = trajectories.get(i).trajectory.get(0).time;
			last = trajectories.get(i).trajectory.get(size-1).time;
			if(first % 5 !=0) //not a multiple of 5
			{
				Point new_point = new Point();
				new_point = trajectories.get(i).trajectory.get(0);
				new_point.time = first - (first%5);
				first = first - (first%5);
				new_traj.add(new_point);
			}
			else
			{
				Point new_point = new Point();
				new_point = trajectories.get(i).trajectory.get(0);
				new_traj.add(new_point);
			}
			if(last % 5 != 0)
			{
				last_point = trajectories.get(i).trajectory.get(size-1);
				last_point.time = last + (5 - last%5);
				last = last + (5 - last%5);
			}
			else
			{
				last_point = trajectories.get(i).trajectory.get(size-1);
			}
			int elem1, elem2;
			elem1 = 0;
			elem2 = 1;
			for(long j = first + 5 ;j < last ; j = j+5)
			{
				if(	trajectories.get(i).trajectory.get(elem2).time != j)
				{
					Point new_point = new Point();
					new_point.time = j;
					long time1 = trajectories.get(i).trajectory.get(elem1).time;
					long time2 = trajectories.get(i).trajectory.get(elem2).time;
					while(time1 == time2)
					{
						elem1++;
						elem2++;
						time1 = trajectories.get(i).trajectory.get(elem1).time;
						time2 = trajectories.get(i).trajectory.get(elem2).time;
					}
					new_point.lati = ( (j - time1) * trajectories.get(i).trajectory.get(elem1).lati +
									(time2-j) * trajectories.get(i).trajectory.get(elem2).lati) / (time2-time1);

					new_point.longi = ( (j - time1) * trajectories.get(i).trajectory.get(elem1).longi +
							(time2-j) * trajectories.get(i).trajectory.get(elem2).longi) / (time2-time1);
					new_traj.add(new_point);
					while(j+5 > time2)
					{
						elem1++;
						elem2++;
						time1 = trajectories.get(i).trajectory.get(elem1).time;
						time2 = trajectories.get(i).trajectory.get(elem2).time;
					}

				}
				else
				{
					Point new_point = new Point();
					new_point = trajectories.get(i).trajectory.get(elem2);
					new_traj.add(new_point);
					elem1++;
					elem2++;
				}
			}
			new_traj.add(last_point);
			trajectories.get(i).trajectory.clear();
			trajectories.get(i).trajectory.addAll(new_traj);
			/*if(i == 8192 || i == 1)
			{
				System.out.println("Taxi : " + i);
				for(int j = 0; j < trajectories.get(i).trajectory.size(); j++)
				{
					Point point = new Point();
					point = trajectories.get(i).trajectory.get(j);
					System.out.println(point.time + "  " + point.lati + "  " + point.longi);
				}
			}*/
			count++;
		}
	}

	public void interpolate_cross_valid_set()
	{
		Iterator it = cross_validation.entrySet().iterator();
		int count = 0;
		while (it.hasNext()) 
		{
			Map.Entry pairs = (Map.Entry)it.next();
			int i = Integer.parseInt(pairs.getKey().toString());
			ArrayList<Point> new_traj = new ArrayList<Point>();
			long first = 0;
			long last = 0;
			Point last_point = new Point();
			int size = cross_validation.get(i).trajectory.size();
			first = cross_validation.get(i).trajectory.get(0).time;
			last = cross_validation.get(i).trajectory.get(size-1).time;
			if(first % 5 !=0) //not a multiple of 5
			{
				Point new_point = new Point();
				new_point = cross_validation.get(i).trajectory.get(0);
				new_point.time = first - (first%5);
				first = first - (first%5);
				new_traj.add(new_point);
			}
			else
			{
				Point new_point = new Point();
				new_point = cross_validation.get(i).trajectory.get(0);
				new_traj.add(new_point);
			}
			if(last % 5 != 0)
			{
				last_point = cross_validation.get(i).trajectory.get(size-1);
				last_point.time = last + (5 - last%5);
				last = last + (5 - last%5);
			}
			else
			{
				last_point = cross_validation.get(i).trajectory.get(size-1);
			}
			int elem1, elem2;
			elem1 = 0;
			elem2 = 1;
			for(long j = first + 5 ;j < last ; j = j+5)
			{
				if(	cross_validation.get(i).trajectory.get(elem2).time != j)
				{
					Point new_point = new Point();
					new_point.time = j;
					long time1 = cross_validation.get(i).trajectory.get(elem1).time;
					long time2 = cross_validation.get(i).trajectory.get(elem2).time;
					while(time1 == time2)
					{
						elem1++;
						elem2++;
						time1 = cross_validation.get(i).trajectory.get(elem1).time;
						time2 = cross_validation.get(i).trajectory.get(elem2).time;
					}
					new_point.lati = ( (j - time1) * cross_validation.get(i).trajectory.get(elem1).lati +
									(time2-j) * cross_validation.get(i).trajectory.get(elem2).lati) / (time2-time1);

					new_point.longi = ( (j - time1) * cross_validation.get(i).trajectory.get(elem1).longi +
							(time2-j) * cross_validation.get(i).trajectory.get(elem2).longi) / (time2-time1);
					new_traj.add(new_point);
					while(j+5 > time2)
					{
						elem1++;
						elem2++;
						time1 = cross_validation.get(i).trajectory.get(elem1).time;
						time2 = cross_validation.get(i).trajectory.get(elem2).time;
					}

				}
				else
				{
					Point new_point = new Point();
					new_point = cross_validation.get(i).trajectory.get(elem2);
					new_traj.add(new_point);
					elem1++;
					elem2++;
				}
			}
			new_traj.add(last_point);
			cross_validation.get(i).trajectory.clear();
			cross_validation.get(i).trajectory.addAll(new_traj);
			/*if(i == 8192 || i == 1)
			{
				System.out.println("Taxi : " + i);
				for(int j = 0; j < cross_validation.get(i).trajectory.size(); j++)
				{
					Point point = new Point();
					point = cross_validation.get(i).trajectory.get(j);
					System.out.println(point.time + "  " + point.lati + "  " + point.longi);
				}
			}*/
			count++;
		}
	}

	
	public void similarity(Trajectory query, int granularity)
	{
		Iterator it = trajectories.entrySet().iterator();
		
		ArrayList<HashMap<Integer, Double>> vars = new ArrayList<HashMap<Integer, Double>>(); 
		while (it.hasNext())
		{
			Map.Entry pairs = (Map.Entry)it.next();
			int i = Integer.parseInt(pairs.getKey().toString()); //get the taxi_id
			
			Double min_var = 99999.0;
			for(int size = granularity; size < query.trajectory.size(); size = size + granularity){
				int count = 0;
				int index = size/granularity - 1;
				if(index < vars.size()){
					
				}
				else{
					HashMap<Integer, Double> new_hash = new HashMap<Integer, Double>();
					vars.add(new_hash);
				}
				for(long start = query.trajectory.get(0).time;
						start < query.trajectory.get(query.trajectory.size()-1).time;
						start = start + size*5){
					long end = start + size*5;
					ArrayList<Double> sim_values = new ArrayList<Double>();
					int j = 0;
					for(j = 0; j < end; j++) 
					{
						if(j >= trajectories.get(i).trajectory.size())
						{
							j = trajectories.get(i).trajectory.size();
							break;
						}
						if(trajectories.get(i).trajectory.get(j).time == start)
							break;
					}
					if((j + size) < trajectories.get(i).trajectory.size())
					{
						//System.out.println("coming here????");
						double sim = 0.0;
						for(int l = count; l < count + size ; l++)
						{
							if(l < query.trajectory.size()){
							//	System.out.println("l = " + l );
								long x = query.trajectory.get(l).time;
								long y = trajectories.get(i).trajectory.get(j+l-count).time;
								sim = L2(query.trajectory.get(l), trajectories.get(i).trajectory.get(j+l-count));
								sim_values.add(sim);
							}
						}
						double mean = 0.0;
						double sum = 0.0;
						double var = 0.0;
						for(j = 0; j< sim_values.size(); j++)
						{
							sum += sim_values.get(j);
						}
						mean = sum / sim_values.size();
						for(j = 0; j< sim_values.size(); j++)
						{
							var += Math.pow(sim_values.get(j) - mean, 2);
						}
						var = var / sim_values.size();
						if(vars.get(index).containsKey(i) && 1000*var/(size*size) < vars.get(index).get(i))
							vars.get(index).put(i, 1000*var/(size*size));
						else if(!vars.get(index).containsKey(i))
							vars.get(index).put(i, 1000*var/(size*size));
						//System.out.println("Start time = " + start + " End time = " + end + "Var = " + var);
						//System.out.println(var);
						if(sim_values.size() == 0)
							System.out.println("size zero : "+ i);
					}
					else{
						Variance var_temp = new Variance();
						if(vars.get(index).containsKey(i) && (99999.0/(size*size)) < vars.get(index).get(i))
							vars.get(index).put(i, 1000*99999.0/(size*size));
						//System.out.println("Start time = " + start + " End time = " + end + "Var = 99999.0" );
					}
					count = count + size;
				}
			}
		}
		
		HashMap<Integer, Integer> final_values = new HashMap<Integer, Integer>();
		System.out.println("No of sizes " + vars.size() );
		HashMap<Integer, Double> final_ref = new HashMap<Integer, Double>();
		for(int s = 3; s < vars.size(); s++){
			HashMap<Integer, Double> temp = vars.get(s);
			Map<Integer, Double> Sorted_variances = new HashMap<Integer, Double>();
			Sorted_variances = sortByValues(temp);
			//if(s == 354)
			//	System.out.println("Size = "+ (s+1));
			Iterator it1 = Sorted_variances.entrySet().iterator();
			int count = 0;
		    while (it1.hasNext() && count < 15) 
		    {
		        Map.Entry pairs = (Map.Entry)it1.next();
		      //  if(s == 354)
		      //  	System.out.println(pairs.getKey().toString() + " = " + Sorted_variances.get(Integer.parseInt(pairs.getKey().toString())));
		        int taxi = Integer.parseInt(pairs.getKey().toString());
		        Double ref = Sorted_variances.get(Integer.parseInt(pairs.getKey().toString()));
		        if(final_values.containsKey(taxi)){
		        	if(ref < final_ref.get(taxi)){
		        		final_values.put(taxi, s+1);
		        		final_ref.put(taxi, ref);
		        	}
		        }
		        else{
		        	final_values.put(taxi, s+1);
		        	final_ref.put(taxi, ref);
		        }
		        count++;
		    }
		}
		final_vars.clear();
		final_val.clear();
		HashMap<Integer, Double> temp = final_ref;
		Map<Integer, Double> Sorted_variances = new HashMap<Integer, Double>();
		
		Sorted_variances = sortByValues(temp);
		Iterator it1 = Sorted_variances.entrySet().iterator();
		int count = 0;
		while (it1.hasNext() && count < 15) 
	    {
	        Map.Entry pairs = (Map.Entry)it1.next();
	        int taxi = Integer.parseInt(pairs.getKey().toString());
	        Double ref = Sorted_variances.get(Integer.parseInt(pairs.getKey().toString()));
	        final_vars.put(taxi, ref);
	        final_val.put(taxi, final_values.get(taxi));
	     //   System.out.println(taxi + " = " + final_values.get(taxi));
	        count++;
	    }
		
		
		/*if(variances.containsKey(8800)){
			HashMap<Integer, ArrayList<Variance>> var_print = variances.get(8800);
			for (Map.Entry<Integer, ArrayList<Variance>> entry : var_print.entrySet())
			{
			   int size = entry.getKey();
			   ArrayList<Variance> arr = entry.getValue();
			   System.out.println("Size = " + size + " List : ");
			   for(int z = 0; z < arr.size(); z++)
			   {
				   System.out.println(" Var " + arr.get(z).variance + " Start " +  arr.get(z).start);
			   }
			}
		}
		else
			System.out.println("cupper");*/
	}
	
	public void similarity1(Trajectory query, int k)
	{
		int size = query.trajectory.size();
		long start = query.trajectory.get(0).time;
		long end = query.trajectory.get(size-1).time;
		Iterator it = trajectories.entrySet().iterator();
		int count = 0;
		HashMap <Integer, Double> variances = new HashMap <Integer, Double>();
		while (it.hasNext()) 
		{
			Map.Entry pairs = (Map.Entry)it.next();
			int i = Integer.parseInt(pairs.getKey().toString()); //get the taxi_id
			ArrayList<Double> sim_values = new ArrayList<Double>();
			int j = 0;
			for(j = 0; j< trajectories.get(i).trajectory.size(); j++) //for all the points
			{
				if(trajectories.get(i).trajectory.get(j).time == start)
					break;
			}
			if((j+ size) < trajectories.get(i).trajectory.size())
			{
				double sim = 0.0;
				for(int l = 0; l < size ; l++)
				{
					sim = L2(query.trajectory.get(l), trajectories.get(i).trajectory.get(j+l));
					sim_values.add(sim);
				}
				double mean = 0.0;
				double sum = 0.0;
				double var = 0.0;
				for(j = 0; j< sim_values.size(); j++)
				{
					sum += sim_values.get(j);
				}
				mean = sum / sim_values.size();
				for(j = 0; j< sim_values.size(); j++)
				{
					var += Math.pow(sim_values.get(j) - mean, 2);
				}
				var = var / sim_values.size();
				//System.out.println(var);
				variances.put(i, var);
				if(sim_values.size() == 0)
					System.out.println("size zero : "+ i);
			}
		}
		
		/*Iterator it2 = variances.entrySet().iterator();
		System.out.println("Variances : ");
	    while (it2.hasNext()) 
	    {
	        Map.Entry pairs = (Map.Entry)it2.next();
	        System.out.println(pairs.getKey().toString() + " = " + variances.get(Integer.parseInt(pairs.getKey().toString())));
	    }
	    */
		
		
		Map<Integer, Double> Sorted_variances = new HashMap<Integer, Double>();
		Sorted_variances = sortByValues(variances);
		Iterator it1 = Sorted_variances.entrySet().iterator();
		System.out.println("Similar ones : "+ Sorted_variances.size());
	    while (it1.hasNext() && count < k) 
	    {
	        Map.Entry pairs = (Map.Entry)it1.next();
	        System.out.println(pairs.getKey().toString() + " = " + Sorted_variances.get(Integer.parseInt(pairs.getKey().toString())));
	        count++;
	    }
	}

	
	public double L2(Point point1, Point point2)
	{
		double ans = 0.0;
		ans = Math.pow((point1.lati - point2.lati), 2) + Math.pow((point1.longi - point2.longi), 2);
		ans = Math.sqrt(ans);
		return ans;
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
}