
public class Point 
{
	long time; //hours*60 + minutes
	double lati;
	double longi;
	Point()
	{
		
	}
	Point(long x, double y, double z)
	{
		time = x;
		lati = y;
		longi = z;
	}
}
