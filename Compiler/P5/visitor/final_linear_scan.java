package visitor;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

public class final_linear_scan {
	private LinkedHashMap<String,locals > proc_temp_info=new LinkedHashMap<String,locals>();
	private LinkedHashMap<String,locals > proc_temp_info1=new LinkedHashMap<String,locals>();
	private LinkedHashMap<String,locals > proc_temp_info2=new LinkedHashMap<String,locals>();
	private Vector<interval_scan> all_intervals = new Vector<interval_scan>();
	private Vector<interval_scan> active= new Vector<interval_scan>();
	private Vector<String> free= new Vector<String>();
	private interval_scan current_interval = new interval_scan();
	int proc_start=0;
	int proc_end=0;
	String proc_name = new String();
	int current_start=0;
	int spill_loc=30;
	int current_end=0;
	int number_of_args=0;
	int max_active=0;
	public void get_temp_info(LinkedHashMap<String,locals> intervals)
	{
		  proc_temp_info = intervals;
	}
	
	public void linear_scan_function()
	{

		Iterator it = proc_temp_info.entrySet().iterator();
	    while(it.hasNext())
	    {
	           Map.Entry pairs1=(Map.Entry)it.next();
	           proc_name = (pairs1.getKey()).toString();
	           locals v = proc_temp_info.get(pairs1.getKey());
	           locals new_local = new locals();
	    	   new_local.args1 = v.args1;
	    	   number_of_args = v.args1;
	    	   new_local.args2=v.args2;
	    	   new_local.args3=v.args3;
	    	   new_local.temp_info = new LinkedHashMap<String, interval>();
	    	   proc_temp_info2.put(proc_name, new_local);
	    	   all_intervals.clear();
	    	   all_intervals=new Vector<interval_scan>();
	           Iterator it2 = v.temp_info.entrySet().iterator();
	           while(it2.hasNext()){
	               Map.Entry pairs2 = (Map.Entry)it2.next();
	               interval r = v.temp_info.get(pairs2.getKey());
	               String temp = pairs2.getKey().toString();
	               interval_scan new_interval_scan = new interval_scan();
	               new_interval_scan.intervals= r;
	               new_interval_scan.proc_name = proc_name;
	               if((!temp.equals("TEMP 0")) && (!temp.equals("TEMP 1")) && (!temp.equals("TEMP 2")) && (!temp.equals("TEMP 3")) && (!temp.equals("TEMP 4")) && (!temp.equals("TEMP 5")) && (!temp.equals("TEMP 6")) && (!temp.equals("TEMP 7")) && (!temp.equals("TEMP 8")) && (!temp.equals("TEMP 9")) && (!temp.equals("TEMP 10")) && (!temp.equals("TEMP 11")) && (!temp.equals("TEMP 12")) && (!temp.equals("TEMP 13")) && (!temp.equals("TEMP 14")) && (!temp.equals("TEMP 15")))
	               {
	            		   all_intervals.add(new_interval_scan);
	               }
	               else
	               {
	            	   proc_temp_info2.get(proc_name).temp_info.put(temp, r);
	               }
	           }
	           linear_scan_reg_allocation();
	           int i=0;
	           for(i=0;i<all_intervals.size();i++)
	           {
	        	   String temp = all_intervals.get(i).intervals.temp;
	        	   proc_temp_info2.get(proc_name).temp_info.put(temp,all_intervals.get(i).intervals);
	           }
	    }
	    //print_function();
	    
	}
	
	
	void linear_scan_reg_allocation()
	{
		active.clear();
		active = new Vector<interval_scan>();
		sort_start();
		int i=0;
		for(i=7;i>=number_of_args;i--)
	    	free.add ("s"+i);
	    for(i=9;i>=0;i--)
	    	free.add("t"+i);
	    
	    max_active = free.size();
	    active.clear();
	    active = new Vector<interval_scan>();
	    spill_loc=30;
	    for(i=0;i<4;i++)
	    {
	    	if(proc_temp_info2.get(proc_name).temp_info.get("TEMP "+i)!=null)
	    		proc_temp_info2.get(proc_name).temp_info.get("TEMP "+i).regs="s"+i ;
	    }
	    int spill_loc_new=0;
	    for(i=4;i<16;i++)
	    {
	    	if(proc_temp_info2.get(proc_name).temp_info.get("TEMP "+i)!=null)
	    		proc_temp_info2.get(proc_name).temp_info.get("TEMP "+i).regs="SPILLEDARG "+spill_loc_new++ ;
	    }
	    
	    for(i=0;i<all_intervals.size();i++)
		{
			expire_old_intervals(i);
			//System.out.println(free);
			if(active.size()==max_active || free.size()==0)
				spill_at_interval(i);
			else
			{
				all_intervals.get(i).intervals.regs = free.lastElement();
				free.remove(free.size()-1);
				active.add(all_intervals.get(i));
				//String temp = all_intervals.get(i).intervals.temp;
				//proc_temp_info2.get(proc_name).temp_info.put(temp,all_intervals.get(i).intervals);
				sort_end();
			}
		}
	    free.clear();
	    //print_all_intervals();
		
	}
	
	
	void expire_old_intervals(int current_int)
	{
		int i=0;
		sort_end();
		for(i=0;i<active.size();)
		{
			
			if(active.get(i).intervals.end>= all_intervals.get(current_int).intervals.start)
					return;
			//System.out.println(active.get(i).intervals.regs);
			free.add(active.get(i).intervals.regs);
				//System.out.println("size before"+active.size());
			active.remove(i);
		}
		
	}
	
	void spill_at_interval(int current_int)
	{
		interval_scan spill = new interval_scan();
		spill = active.get(active.size()-1);
		if(spill.intervals.end> all_intervals.get(current_int).intervals.end)
		{
			all_intervals.get(current_int).intervals.regs = spill.intervals.regs;
			//String temp = all_intervals.get(current_int).intervals.temp;
			//proc_temp_info2.get(proc_name).temp_info.put(temp,all_intervals.get(current_int).intervals);
			spill.intervals.regs = "SPILLEDARG "+spill_loc++;
			active.remove(active.size()-1);
			active.add(all_intervals.get(current_int));
			sort_end();
		}
		else
		{
			all_intervals.get(current_int).intervals.regs = "SPILLEDARG "+spill_loc++;
			//String temp = all_intervals.get(current_int).intervals.temp;
			//proc_temp_info2.get(proc_name).temp_info.put(temp,all_intervals.get(current_int).intervals);
		}
	}
	
	void print_function()
	{
		Iterator it = proc_temp_info2.entrySet().iterator();
	    while(it.hasNext())
	    {
	    	
	           Map.Entry pairs1=(Map.Entry)it.next();
	           String proc_name = (pairs1.getKey()).toString();
	           System.out.println(proc_name+"  " +proc_temp_info2.get(proc_name).args1+"  "+proc_temp_info2.get(proc_name).args2+"  " +proc_temp_info2.get(proc_name).args3);
	           locals v = proc_temp_info2.get(pairs1.getKey());
	           Iterator it2 = v.temp_info.entrySet().iterator();
	           while(it2.hasNext()){
	               Map.Entry pairs2 = (Map.Entry)it2.next();
	               interval r = v.temp_info.get(pairs2.getKey());
	               String temp = pairs2.getKey().toString();
	               System.out.println("Start : "+r.start+" End : "+ r.end+" "+r.temp+" "+r.regs);
	               }
	           }
		
	}
	void print_all_intervals()
	{
		int i,j=0;
		System.out.println(proc_name);
		for(i=0;i<all_intervals.size();i++)
		{
			String temp = all_intervals.get(i).proc_name;
			interval r = all_intervals.get(i).intervals;
			System.out.println("Start : "+r.start+" End : "+ r.end+" "+r.temp+" "+r.regs);
		}
			
	}
	void sort_start()
	{
		int i,j;
		interval_scan min_interval_scan  = new interval_scan();
		for(i=0;i<all_intervals.size();i++)
		{
			min_interval_scan = all_intervals.get(i);
			for(j=i+1;j<all_intervals.size();j++)
			{
				if(all_intervals.get(j).intervals.start<min_interval_scan.intervals.start)
				min_interval_scan = all_intervals.get(j);
			}
			int start, end;
			String temp, regs = new String();
			String proc_name = new String();
			start  = min_interval_scan.intervals.start;
			end = min_interval_scan.intervals.end;
			temp = min_interval_scan.intervals.temp;
			regs  = min_interval_scan.intervals.regs;
			proc_name = min_interval_scan.proc_name;
			min_interval_scan.intervals.start = all_intervals.get(i).intervals.start;
			min_interval_scan.intervals.end = all_intervals.get(i).intervals.end;
			min_interval_scan.intervals.temp = all_intervals.get(i).intervals.temp;
			min_interval_scan.intervals.regs = all_intervals.get(i).intervals.regs;
			min_interval_scan.proc_name = all_intervals.get(i).proc_name;
			all_intervals.get(i).intervals.start = start;
			all_intervals.get(i).intervals.end = end;
			all_intervals.get(i).intervals.temp = temp;
			all_intervals.get(i).intervals.regs = regs;
			all_intervals.get(i).proc_name = proc_name;
		
		}
		//print_all_intervals();
	}
	void sort_end()
	{
		int i,j;
		interval_scan min_interval_scan = new interval_scan();
		for(i=0;i<active.size();i++)
		{
			min_interval_scan = active.get(i);
			for(j=0;j<active.size();j++)
			{
				if(active.get(j).intervals.end<min_interval_scan.intervals.end)
					min_interval_scan = active.get(j);
			}
			int start, end;
			String temp, regs = new String();
			String proc_name = new String();
			start  = min_interval_scan.intervals.start;
			end = min_interval_scan.intervals.end;
			temp = min_interval_scan.intervals.temp;
			regs  = min_interval_scan.intervals.regs;
			proc_name = min_interval_scan.proc_name;
			min_interval_scan.intervals.start = active.get(i).intervals.start;
			min_interval_scan.intervals.end = active.get(i).intervals.end;
			min_interval_scan.intervals.temp = active.get(i).intervals.temp;
			min_interval_scan.intervals.regs = active.get(i).intervals.regs;
			min_interval_scan.proc_name = active.get(i).proc_name;
			active.get(i).intervals.start = start;
			active.get(i).intervals.end = end;
			active.get(i).intervals.temp = temp;
			active.get(i).intervals.regs = regs;
			active.get(i).proc_name = proc_name;
		}
	}
	
	public LinkedHashMap<String,locals> return_temp_info()
	{
		//print_function();
		return proc_temp_info2;
	}
}
