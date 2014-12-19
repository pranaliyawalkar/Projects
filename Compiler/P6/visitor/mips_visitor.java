//
// Generated by JTB 1.3.2
//

package visitor;
import syntaxtree.*;
import java.util.*;

/**
 * Provides default methods which visit each node in the tree in depth-first
 * order.  Your visitors may extend this class.
 */
public class mips_visitor<R> implements GJNoArguVisitor<R> {
   //
   // Auto class visitors--probably don't need to be overridden.
   //
	int coming_from_label=0;
	int current_max_args =0 ;
	String current_label = new String();
	String mov_d = new String();
	int coming_from_op=0;
	String current_label_stmt_list = new String();
	public R visit(NodeList n) {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeListOptional n) {
      if ( n.present() ) {
         R _ret=null;
         int _count=0;
         for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
            e.nextElement().accept(this);
           
            _count++;
         }
         return _ret;
      }
      else
         return null;
   }

   public R visit(NodeOptional n) {
      if ( n.present() )
      {
    	  String s = (String)n.node.accept(this);
          	current_label_stmt_list = s;
         return (R)s;
      }
      else
         return null;
   }

   public R visit(NodeSequence n) {
      R _ret=null;
      int _count=0;
      for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
         e.nextElement().accept(this);
         _count++;
      }
      return _ret;
   }

   public R visit(NodeToken n) { return null; }

   //
   // User-generated visitor methods below
   //

   /**
    * f0 -> "MAIN"
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> "["
    * f5 -> IntegerLiteral()
    * f6 -> "]"
    * f7 -> "["
    * f8 -> IntegerLiteral()
    * f9 -> "]"
    * f10 -> StmtList()
    * f11 -> "END"
    * f12 -> ( Procedure() )*
    * f13 -> VariablePackingInfo()
    * f14 -> <EOF>
    */
   public R visit(Goal n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      String s2 =(String)n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      String s5 =(String)n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      String s8 =(String)n.f8.accept(this);
      System.out.println(".text");
      System.out.println(".globl           main");
      System.out.println("main:");
      System.out.println("move $fp, $sp");
      int args3 = Integer.parseInt(s8);
      int args2 = Integer.parseInt(s5);
      int temp=0;
      if(args3>4)
    	  temp = 4*((args3-4)+args2+1);
      else
    	  temp = (4*(args2+1));
      System.out.println("subu $sp, $sp," + temp);
      System.out.println("sw $ra, -4($fp)");
      n.f9.accept(this);
      n.f10.accept(this);
      n.f11.accept(this);
      System.out.println("lw $ra, -4($fp)");
      System.out.println("addu $sp, $sp,"+temp);
      System.out.println("jr $ra");
      n.f12.accept(this);
      n.f13.accept(this);
      
      System.out.println(" .text \n .globl _halloc \n _halloc: \n li $v0, 9 \n  syscall \n  jr $ra");
      System.out.println(".text \n .globl _print \n _print: \n li $v0, 1 \n syscall \n la $a0, newl \n  li $v0, 4 \n syscall \n  jr $ra");
      System.out.println(".data \n .align   0 \n newl:    .asciiz \" \\n \" \n .data \n .align   0 \n ");
      System.out.println("str_er:  .asciiz \" ERROR: abnormal termination\\n\" ");
      
      
      n.f14.accept(this);
      return _ret;
   }

   /**
    * f0 -> ( ( Label() )? Stmt() )*
    */
   public R visit(StmtList n) {
      R _ret=null;
      n.f0.accept(this);
     
     //if(current_label_stmt_list!=null && (!current_label_stmt_list.equals("pranali")))
    	//System.out.println(current_label_stmt_list ); 
      return _ret;
   }

   /**
    * f0 -> Label()
    * f1 -> "["
    * f2 -> IntegerLiteral()
    * f3 -> "]"
    * f4 -> "["
    * f5 -> IntegerLiteral()
    * f6 -> "]"
    * f7 -> "["
    * f8 -> IntegerLiteral()
    * f9 -> "]"
    * f10 -> StmtList()
    * f11 -> "END"
    */
   public R visit(Procedure n) {
      R _ret=null;
      String s0 = (String)n.f0.accept(this);
      n.f1.accept(this);
      String s2 = (String)n.f2.accept(this);
      n.f3.accept(this);
      n.f4.accept(this);
      String s5 = (String)n.f5.accept(this);
      n.f6.accept(this);
      n.f7.accept(this);
      String s8 = (String)n.f8.accept(this);
      n.f9.accept(this);
      
      System.out.println(".text");
      System.out.println(".globl           "+s0);
      System.out.println(s0+":");

      System.out.println("sw $fp, -8($sp)");
      System.out.println("move $fp, $sp");
      int temp =0;
      int args3 = Integer.parseInt(s8);
      int args2 = Integer.parseInt(s5);
      int args1 =Integer.parseInt(s2);
      current_max_args=args1;
      if(args3 >4 )
    	  temp = 4*((args3-4)+args2+2);
      else
    	  temp = 4*(args2+2);
      System.out.println("subu $sp,$sp,"+temp);
      System.out.println("sw $ra, -4($fp)");
      n.f10.accept(this);
      n.f11.accept(this);
      System.out.println("lw $ra, -4($fp)");
      if(args3>4)
      System.out.println("lw $fp, "+(4 * ((args3 - 4) + args2 + 2) - 8)+"($sp)");
      else
    	  System.out.println("lw $fp, "+(4 * (args2 + 2) - 8)+"($sp)");
      System.out.println("addu $sp, $sp,"+temp);
      System.out.println("jr $ra");
      /*
       *  lw $ra, -4($fp)
         lw $fp, 8($sp)
         addu $sp, $sp, 16
         j $ra
       */
      return _ret;
   }

   /**
    * f0 -> NoOpStmt()
    *       | ErrorStmt()
    *       | CJumpStmt()
    *       | JumpStmt()
    *       | HStoreStmt()
    *       | HLoadStmt()
    *       | MoveStmt()
    *       | PrintStmt()
    *       | ALoadStmt()
    *       | AStoreStmt()
    *       | PassArgStmt()
    *       | CallStmt()
    */
   public R visit(Stmt n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "NOOP"
    */
   public R visit(NoOpStmt n) {
      R _ret=null;
      n.f0.accept(this);
      System.out.println(current_label+" : nop");
      return _ret;
   }

   /**
    * f0 -> "ERROR"
    */
   public R visit(ErrorStmt n) {
      R _ret=null;
      n.f0.accept(this);
      return _ret;
   }

   /**
    * f0 -> "CJUMP"
    * f1 -> Reg()
    * f2 -> Label()
    */
   public R visit(CJumpStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String s1 = (String)n.f1.accept(this);
      String s2 = (String)n.f2.accept(this);
      System.out.println("beqz " + s1+" "+s2);
      return _ret;
   }

   /**
    * f0 -> "JUMP"
    * f1 -> Label()
    */
   public R visit(JumpStmt n) {
      R _ret=null;
      n.f0.accept(this);
     String s1 = (String) n.f1.accept(this);
     System.out.println("b "+ s1);
      return _ret;
   }

   /**
    * f0 -> "HSTORE"
    * f1 -> Reg()
    * f2 -> IntegerLiteral()
    * f3 -> Reg()
    */
   public R visit(HStoreStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String s1 = (String)n.f1.accept(this);
      String s2 = (String)n.f2.accept(this);
      String s3 = (String) n.f3.accept(this);
      //HSTORE t0 0 t2
      //sw $t2, 0($t0)
      System.out.println("sw "+s3+" , "+s2+"("+s1+")");
      return _ret;
   }

   /**
    * f0 -> "HLOAD"
    * f1 -> Reg()
    * f2 -> Reg()
    * f3 -> IntegerLiteral()
    */
   public R visit(HLoadStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String s1 = (String)n.f1.accept(this);
      String s2 = (String)n.f2.accept(this);
      String s3 = (String)n.f3.accept(this);
      // lw $t1 0($t0)
      //HLOAD t1 t0 0
      System.out.println("lw "+s1+" "+s3+"("+s2+")");
      return _ret;
   }

   /**
    * f0 -> "MOVE"
    * f1 -> Reg()
    * f2 -> Exp()
    */
   public R visit(MoveStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String s1 = (String)n.f1.accept(this);
      coming_from_label=0;
       mov_d = s1;
      //System.out.println("check "+s1);
      String s2 = (String)n.f2.accept(this);
      String first = new String();
      if(s2!=null)
    	  first = (String) s2.subSequence(0, 1);
      
      if(coming_from_op==0)
      {
	      if(coming_from_label==0)
	      {
	    	  if((!first.startsWith("0")) && (!first.startsWith("1")) && (!first.startsWith("2")) && (!first.startsWith("3")) && (!first.startsWith("4")) && (!first.startsWith("5")) && (!first.startsWith("6")) && (!first.startsWith("7")) && (!first.startsWith("8")) && (!first.startsWith("9")))
	    	  	System.out.println("move "+s1+" "+s2);
	    	  else
	    		  System.out.println("li "+s1+" "+s2);
	      }
	      else if(coming_from_label==1)
	      {
	    	  coming_from_label=1;
	    	  System.out.println("la "+s1+" "+s2);
	      }
      }
      coming_from_op=0;
      return _ret;
   }

   /**
    * f0 -> "PRINT"
    * f1 -> SimpleExp()
    */
   public R visit(PrintStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String s1 = (String)n.f1.accept(this);
      System.out.println("move $a0 "+s1);
      System.out.println("jal _print");
      return _ret;
   }

   /**
    * f0 -> "ALOAD"
    * f1 -> Reg()
    * f2 -> SpilledArg()
    */
   public R visit(ALoadStmt n) {
      R _ret=null;
      //ALOAD s0 SPILLEDARG 0
      //lw $s0, 0($sp)
      n.f0.accept(this);
      String s1  = (String)n.f1.accept(this);
      String s2  = (String)n.f2.accept(this);
      String tokens[] = s2.split(" ");
      
    	  
      if(current_max_args>4 && Integer.parseInt(tokens[1])< current_max_args-4)
      {
    	  //System.out.println("--------");
    	  System.out.println("lw "+s1+" , "+(4*Integer.parseInt((tokens[1])))+"($fp)");
      }
      else
    	  System.out.println("lw "+s1+" , "+(4*Integer.parseInt((tokens[1])))+"($sp)");
    	  return _ret;
   }

   /**
    * f0 -> "ASTORE"
    * f1 -> SpilledArg()
    * f2 -> Reg()
    */
   public R visit(AStoreStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String s1= (String)n.f1.accept(this);
      String s2= (String)n.f2.accept(this);
      //ASTORE SPILLEDARG 0 s0
      // sw $s0, 0($sp)
      String tokens[] = s1.split(" ");
      System.out.println("sw "+s2+" , "+(4*Integer.parseInt((tokens[1])))+"("+"$sp"+")");
      return _ret;
   }

   /**
    * f0 -> "PASSARG"
    * f1 -> IntegerLiteral()
    * f2 -> Reg()
    */
   public R visit(PassArgStmt n) {
      R _ret=null;
      //PASSARG 1 t5
      //sw $t5, 0($sp)
      n.f0.accept(this);
      String s1 = (String)n.f1.accept(this);
      String s2 = (String)n.f2.accept(this);
      int temp = Integer.parseInt(s1);
      System.out.println("sw "+s2+" , "+(4*(temp-1))+"($sp)");
      return _ret;
   }

   /**
    * f0 -> "CALL"
    * f1 -> SimpleExp()
    */
   public R visit(CallStmt n) {
      R _ret=null;
      n.f0.accept(this);
      String s1 = (String)n.f1.accept(this);
      System.out.println("jalr "+s1);
      return _ret;
   }

   /**
    * f0 -> HAllocate()
    *       | BinOp()
    *       | SimpleExp()
    */
   public R visit(Exp n) {
      R _ret=null;
      String s0 = (String)n.f0.accept(this);
      //System.out.println("check3"+mov_d);
      return (R)s0;
   }

   /**
    * f0 -> "HALLOCATE"
    * f1 -> SimpleExp()
    */
   public R visit(HAllocate n) {
      R _ret=null;
      n.f0.accept(this);
      String s1 = (String)n.f1.accept(this);
      if(s1.startsWith("0")||s1.startsWith("1")||s1.startsWith("2")||s1.startsWith("3")||s1.startsWith("4")||s1.startsWith("5")||s1.startsWith("6")||s1.startsWith("7")||s1.startsWith("8")||s1.startsWith("9"))
      {
      int temp = Integer.parseInt(s1);
      System.out.println("li $a0 "+temp);
      }
      else
      {
    	  System.out.println("move $a0 "+s1);
      }
      System.out.println("jal _halloc");
      return (R)"$v0";
   }

   /**
    * f0 -> Operator()
    * f1 -> Reg()
    * f2 -> SimpleExp()
    */
   public R visit(BinOp n) {
      R _ret=null;
      //System.out.println("check3"+mov_d);
      String s1 = (String)n.f0.accept(this);
      String s2 = (String)n.f1.accept(this);
      String s3 = (String)n.f2.accept(this);
      //MOVE t4 LT t2 t3
      //slt $t4, $t2, $t3
      if(s1.equals("LT"))
    	  s1 = "slt";
      else if(s1.equals("PLUS"))
    	  s1="add";
      else if(s1.equals("MINUS"))
    	  s1 = "sub";
      else if(s1.equals("TIMES"))
    	  s1 = "mul";
      System.out.println(s1+" "+mov_d+" , "+s2+" , "+s3);
      //MOVE t3 PLUS t1 t2
      //add $t3, $t1, $t2
      coming_from_op=1;
      return _ret;
   }

   /**
    * f0 -> "LT"
    *       | "PLUS"
    *       | "MINUS"
    *       | "TIMES"
    *       | "BITOR"
    *       | "BITAND"
    *       | "LSHIFT"
    *       | "RSHIFT"
    *       | "BITXOR"
    */
   public R visit(Operator n) {
      R _ret=null;
      int which = n.f0.which;
      String s = " ";
      switch (which)
      {
      case 0 : {s="LT"; break;}
      case 1 : {s= "PLUS"; break;}
      case 2 : {s= "MINUS";break;}
      case 3 : {s= "TIMES"; break;}
      case 4 : {s= "BITOR"; break;}
      case 5 : {s= "BITAND"; break;}
      case 6 : {s= "LSHIFT"; break;}
      case 7 : {s= "RSHIFT"; break;}
      case 8 : {s= "BITXOR"; break;}
      }
      
      return (R)s;
   }

   /**
    * f0 -> "SPILLEDARG"
    * f1 -> IntegerLiteral()
    */
   public R visit(SpilledArg n) {
      R _ret=null;
      n.f0.accept(this);
      String s1 = (String)n.f1.accept(this);
      return (R)("SPILLEDARG "+s1);
   }

   /**
    * f0 -> Reg()
    *       | IntegerLiteral()
    *       | Label()
    */
   public R visit(SimpleExp n) {
      R _ret=null;
      String s0 = (String)n.f0.accept(this);
      return (R)s0;
   }

   /**
    * f0 -> "a0"
    *       | "a1"
    *       | "a2"
    *       | "a3"
    *       | "t0"
    *       | "t1"
    *       | "t2"
    *       | "t3"
    *       | "t4"
    *       | "t5"
    *       | "t6"
    *       | "t7"
    *       | "s0"
    *       | "s1"
    *       | "s2"
    *       | "s3"
    *       | "s4"
    *       | "s5"
    *       | "s6"
    *       | "s7"
    *       | "t8"
    *       | "t9"
    *       | "v0"
    *       | "v1"
    */
   public R visit(Reg n) {
      R _ret=null;
      int which = n.f0.which;
      String s = " ";
      switch (which)
      {
      case 0 : {s="a0"; break;}
      case 1 : {s= "a1"; break;}
      case 2 : {s= "a2";break;}
      case 3 : {s= "a3"; break;}
      case 4 : {s= "t0"; break;}
      case 5 : {s= "t1"; break;}
      case 6 : {s= "t2"; break;}
      case 7 : {s= "t3"; break;}
      case 8 : {s= "t4"; break;}
      case 9 : {s= "t5"; break;}
      case 10 : {s= "t6"; break;}
      case 11 : {s= "t7"; break;}
      case 12 : {s= "s0"; break;}
      case 13 : {s= "s1"; break;}
      case 14 : {s= "s2"; break;}
      case 15 : {s= "s3"; break;}
      case 16 : {s= "s4"; break;}
      case 17 : {s= "s5"; break;}
      case 18 : {s= "s6"; break;}
      case 19 : {s= "s7"; break;}
      case 20 : {s= "t8"; break;}
      case 21 : {s= "t9"; break;}
      case 22 : {s= "v0"; break;}
      case 23 : {s= "v1"; break;}
      
      }
      return (R)("$"+s);
   }

   /**
    * f0 -> <INTEGER_LITERAL>
    */
   public R visit(IntegerLiteral n) {
      R _ret=null;
      Integer my_int = Integer.parseInt(n.f0.tokenImage);
      String s = (String)n.f0.toString();
      return (R)s;
   }

   /**
    * f0 -> <IDENTIFIER>
    */
   public R visit(Label n) {
      R _ret=null;
      String s0 = (String)n.f0.toString();
      coming_from_label=1;
      current_label = s0;
      return (R)s0;
   }

   /**
    * f0 -> "// Number of  vars after packing ="
    * f1 -> IntegerLiteral()
    * f2 -> "; Number of Spilled vars ="
    * f3 -> IntegerLiteral()
    */
   public R visit(VariablePackingInfo n) {
      R _ret=null;
      n.f0.accept(this);
      n.f1.accept(this);
      n.f2.accept(this);
      n.f3.accept(this);
      return _ret;
   }

}
