import syntaxtree.*;
import visitor.*;
import java.util.*;
import javax.lang.model.type.NullType;
public class P5 {
   public static void main(String [] args) {
      try {
         Node root = new microIRParser(System.in).Goal();
         //System.out.println("Program parsed successfully");
         final_ra_visitor new_ra_visitor = new final_ra_visitor();
         LinkedHashMap<String,locals> intervals = new_ra_visitor.return_temp_info();
         root.accept(new_ra_visitor); // Your assignment part is invoked here.
         
         
         
         
        final_linear_scan new_ra_visitor1 = new final_linear_scan();
         new_ra_visitor1.get_temp_info(intervals);
         new_ra_visitor1.linear_scan_function();
         LinkedHashMap<String,locals> intervals1 = new_ra_visitor1.return_temp_info();
         
        
         
         final_ra_visitor2 new_ra_visitor2 = new final_ra_visitor2();
         new_ra_visitor2.get_temp_info(intervals1);
        root.accept(new_ra_visitor2);
        // System.out.println(intervals.size()); 
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
} 



