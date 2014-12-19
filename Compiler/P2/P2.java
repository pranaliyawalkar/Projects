import syntaxtree.*;
import visitor.*;

public class P2 {
   public static void main(String [] args) {
      try {
         Node root = new MiniJavaParser(System.in).Goal();
         root.accept(new P2_visitor()); // Your assignment part is invoked here.
      }
      catch (ParseException e) {
         System.out.println("Type error");
      }
   }
}
