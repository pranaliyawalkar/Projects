import java.util.*;

import javax.lang.model.type.NullType;
import syntaxtree.*;
import visitor.*;

public class P3 {
   public static void main(String [] args) {
      try {
         Node root = new MiniJavaParser(System.in).Goal();
         symbol_table_visitor symbol_tab_vis = new symbol_table_visitor();
         LinkedHashMap<String,Class_info>  symbolTable_new = symbol_tab_vis.return_symbol_table();
         root.accept(symbol_tab_vis); // Your assignment part is invoked here.
         ir_visitor ir_visitor_new = new ir_visitor();
         ir_visitor_new.get_symbol_table(symbolTable_new) ;
         root.accept(ir_visitor_new);
         
      }
      catch (ParseException e) {
         System.out.println("Type error");
      }
   }
}
