%{
  #include <stdio.h>
  #include <string.h>
  #include<stdlib.h>
  char def_identifiers[50][100]; //stores the names of the defines
  int arguments[50]; //stores the number of arguments for each define
  char action[50][1000]; //stores what each define does
  int total=0;
  char c='0';
  int params_total=0;
  char arg[50]="arg";
  int number;
  int position;
  int i1=0;
  char parameters[50][100]; //parameters for each call
  int t=0;
  int b;
  char temp_word[500];
  int k=0;
  const char s[2] = ",";
  const char s1[2] = " ";
   char *token;
  int number_of_params=0;
  int m,n;
  int flag=0;
  int flag1=0;
  char word[100];
  int j=0;
  int size;
  char gen[50];
  char temp[10000];
%}

%union {
  char *string;
}

%token<string> QUES CLASS PSVM STR OPAREN1 CPAREN1 SOPL OPAREN2 CPAREN2 EXT PUB RET INT1 BOOL INT2 IF ELSE WHILE AND LESS_THAN ADD SUB MUL DIV OPAREN3 
%token<string> TRE FASE THIS NEW EXCLM DEF SEMI EQUAL COMMA DOT ID CPAREN3 LEN NUMBER
%type<string> Goal MacroDefinition1 TypeDeclaration1 MainClass TypeDeclaration Type_Identifier_Semi1 MethodDeclaration1 MethodDeclaration 
%type<string> Type_Identifier_Comma_Type_Identifier1_1 Comma_Type_Identifier1 Statement1 Type Statement Expression_Comma_Expression1_1 Comma_Expression1
%type<string> Expression Identifier Identifier_Comma_Identifier1_1 Comma_Identifier1 MacroDefExpression MacroDefStatement MacroDefinition PrimaryExpression
%error-verbose

%% 

Goal: MacroDefinition1 MainClass TypeDeclaration1 {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s  %s  %s  ",$1,$2,$3);printf("%s\n",$$);free($1);free($2);free($3);};

MacroDefinition1: {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"\0");}
| MacroDefinition1 MacroDefinition  {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s",$1,$2);free($2);};

TypeDeclaration1: {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"\0");}
|  TypeDeclaration1 TypeDeclaration{$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s",$1,$2);};

MainClass: CLASS Identifier OPAREN2 PSVM OPAREN1 STR OPAREN3 CPAREN3 Identifier CPAREN1 OPAREN2 SOPL OPAREN1 Expression CPAREN1 SEMI CPAREN2 CPAREN2 {$$=(char*)malloc(sizeof(char)*100000);sprintf($$,"%s %s %s %s %s %s %s %s %s %s %s %s %s %s %s %s %s %s ",$1,$2,$3,$4,$5,$6,$7,$8,$9,$10,$11,$12,$13,$14,$15,$16,$17,$18);
free($2);free($9);free($14);};

TypeDeclaration: CLASS Identifier OPAREN2 Type_Identifier_Semi1 MethodDeclaration1 CPAREN2 {sprintf($$,"%s %s %s %s %s %s",$1,$2,$3,$4,$5,$6);/*free($2);free($4);free($5);*/}
| CLASS Identifier EXT Identifier OPAREN2 Type_Identifier_Semi1 MethodDeclaration1 CPAREN2 {sprintf($$,"%s %s %s %s %s %s %s %s",$1,$2,$3,$4,$5,$6,$7,$8);free($2);free($4);free($6);free($7);};

Type_Identifier_Semi1: {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"\0");}
|  Type_Identifier_Semi1 Type Identifier SEMI{$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s %s",$1,$2,$3,$4);free($2);};

MethodDeclaration1:{$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"\0");}
| MethodDeclaration1 MethodDeclaration {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s",$1,$2);free($2);};

MethodDeclaration: PUB Type Identifier OPAREN1 Type_Identifier_Comma_Type_Identifier1_1 CPAREN1 OPAREN2 Type_Identifier_Semi1 Statement1 RET Expression SEMI CPAREN2 {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s %s %s %s %s %s %s %s %s %s %s",$1,$2,$3,$4,$5,$6,$7,$8,$9,$10,$11,$12,$13);free($2);free($3);free($9);free($11);};

Type_Identifier_Comma_Type_Identifier1_1: {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"\0");}
| Type Identifier Comma_Type_Identifier1 {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s",$1,$2,$3);free($1);free($2);}; //0 or 1 times

Comma_Type_Identifier1: {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"\0");}
| Comma_Type_Identifier1  COMMA Type Identifier {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s %s",$1,$2,$3,$4);free($3);free($4);};

Statement1: {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"\0");}
|  Statement Statement1 {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s",$1,$2);free($2);};

Type: INT1 {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s",$1);}
| BOOL {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s",$1);}
| INT2 {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s",$1);}
| Identifier{$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s",$1);};

Statement: OPAREN2 Statement1 CPAREN2 {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s",$1,$2,$3);free($2);}
| SOPL OPAREN1 Expression CPAREN1 SEMI {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s %s %s",$1,$2,$3,$4,$5);free($3);}
| Identifier EQUAL Expression SEMI {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s %s",$1,$2,$3,$4);/*printf("kabhikabhi%s # %s # %s # %s\n",$1,$2,$3,$4);*/free($1);free($3);}
| Identifier OPAREN3 Expression CPAREN3 EQUAL Expression SEMI {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s %s %s %s %s",$1,$2,$3,$4,$5,$6,$7);free($1);free($3);free($6);}
| IF OPAREN1 Expression CPAREN1 Statement {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s %s %s",$1,$2,$3,$4,$5);free($3);free($5);}
| IF OPAREN1 Expression CPAREN1 Statement ELSE Statement {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s %s %s %s %s",$1,$2,$3,$4,$5,$6,$7);free($3);free($5);}
| WHILE OPAREN1 Expression CPAREN1 Statement {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s %s %s",$1,$2,$3,$4,$5);free($3);free($5);}
| Identifier OPAREN1 Expression_Comma_Expression1_1 CPAREN1 SEMI //calling thing
{for(i1=0;i1<total;i1++)
	{
		//printf("action %s\n",action[i1]);
		//printf("identifiers %s\n",def_identifiers[i1]);
		t=0;
		//printf("Mummy");
		params_total=0;
		if(strcmp($1,def_identifiers[i1])==0)  //found a string matching
		{
		strcpy(temp,action[i1]);
		//strcpy(parameters[0],"fart");
		//printf("length%d\n",strlen($3));
		for(j=0;j<strlen($3);j++)
		{
			if($3[j]!=',' )  //found a new parameter
			{
				if($3[j]!=' ')
				parameters[params_total][t++]=$3[j];
				
			}
			else
			{
				params_total++;
				t=0;
			}
		}
		if(params_total!=0)
		params_total++;
		else if(strlen($3)!=0)
		params_total++;
		//for(j=0;j<params_total;j++)
		    // printf("parameters %s\n",parameters[j]);
		      //	printf("fucky%d",params_total);
		     if(params_total!=0)
		      {
		       // 
		        position=0;
		        j=0;
		        size=strlen(temp);
		        k=0;
		        b=0;
		        position=0;
   			while( temp[k]!='\0' ) 
			{
				//printf("checkcheck\n");
				if(temp[k]!=' ')
				{
					if(flag1==0)
					{b=0;
					position=k;
					flag1=1;
					}
					word[b++]=temp[k++];
				}
				//printf("%s\n",token);
			        //strcpy(word,token);
			        
			       else
			       {flag1=0;
			       word[b]='\0';
			     //  printf("word %s\n  ",word);
			        b=0;
			       // printf("%s\n",word);
				if(word[0]=='a' && word[1]=='r' && word[2]=='g')
				{	
					flag=1;
					//printf("lol");
					for(j=3;j<strlen(word);j++)
						gen[j-3]=word[j];
					number=atoi(gen); //now to replace it with that parameter
					//printf("number%d\n",number);
					m=strlen(parameters[number-1]);
					n=strlen(word);
					//printf("%s %s check\n",parameters[number-1],word);
					//printf("Pranali"); 
				//	printf("word length %d %d\n",n,m);
					if(m==n)
					{
						//printf("mush");
						t=0;
						for(j=position;j<m+position;j++)
							temp[j]=parameters[number-1][t++];
						//position=position+n;
									
					}
					else if(m<n)
					{
						t=0;
						//printf("%s\n",temp);
						//printf("position%d\n",position);
						//printf("word%s %d\n",word,n);
						//printf("parameter %s %d\n",parameters[number-1],m);
						for(j=position;j<m+position;j++)
						{
							temp[j]=parameters[number-1][t++];
						}
						for(j=position+m;j<size-(n-m);j++)
						{
							temp[j]=temp[j+n-m];
						}
						temp[j]='\0';
						size=size-(n-m);
						t=0;
						//position=position+m;
					}	
					else if(m>n)
					{
						t=0;
						//printf("%s\n",temp);
						//printf("word%s %d\n",word,n);
						//printf("parameter %s %d\n",parameters[number-1],m);
						//printf("%s %d\n",word,position);
						for(j=position;j<n+position;j++)
							temp[j]=parameters[number-1][t++];
						for(j=size-1;j>=n+position;j--)
							temp[j+(m-n)]=temp[j];
						temp[size+m-n]='\0';
						for(j=n+position;j<position+m;j++)
							temp[j]=parameters[number-1][t++];
						t=0;
						size=size+m-n;
						//printf("%s\n",temp);
						//position=position+n;
				
					}
				}
				
				//k++;
				while(temp[k]==' ')
				{
				k++;
				}
			}
			}}
		//printf("%s\n",temp);
		$$=(char*)malloc(sizeof(char)*10000);
		sprintf($$,"%s ",temp);
		//printf("FINAL %s\n",$$);
		break;
			
		}
	}
}; //calling thing

Expression_Comma_Expression1_1: {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"\0");}
| Expression Comma_Expression1 {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s",$1,$2);free($1);}; //0 or 1 times

Comma_Expression1: {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"\0");}
|Comma_Expression1 COMMA Expression  {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s",$1,$2,$3);free($3);};

Expression: PrimaryExpression AND PrimaryExpression {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s",$1,$2,$3);free($1);free($3);}
| PrimaryExpression LESS_THAN PrimaryExpression {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s",$1,$2,$3);free($1);free($3);}
| PrimaryExpression ADD PrimaryExpression {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s",$1,$2,$3);free($1);free($3);}
| PrimaryExpression SUB PrimaryExpression {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s",$1,$2,$3);free($1);free($3);}
| PrimaryExpression MUL PrimaryExpression {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s",$1,$2,$3);free($1);free($3);}
| PrimaryExpression DIV PrimaryExpression {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s",$1,$2,$3);free($1);free($3);}
| PrimaryExpression OPAREN3 PrimaryExpression CPAREN3 {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s %s",$1,$2,$3,$4);free($1);free($3);}
| PrimaryExpression LEN{$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s",$1,$2);free($1);}
| PrimaryExpression{$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s",$1);free($1);}
| PrimaryExpression DOT Identifier OPAREN1 Expression_Comma_Expression1_1 CPAREN1 {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s %s %s %s ",$1,$2,$3,$4,$5,$6);free($1);free($3);}
| Identifier OPAREN1 Expression_Comma_Expression1_1 CPAREN1 {	
for(i1=0;i1<total;i1++)
	{
		//printf("action %s\n",action[i1]);
		//printf("identifiers %s\n",def_identifiers[i1]);
		t=0;
		params_total=0;
		//printf("Mummy1");
		if(strcmp($1,def_identifiers[i1])==0)  //found a string matching
		{
		strcpy(temp,action[i1]);
	//	strcpy(parameters[0],"fart");
		//printf("length%d\n",strlen($3));
		for(j=0;j<strlen($3);j++)
		{
			if($3[j]!=',')  //found a new parameter
			{
				if($3[j]!=' ')
				parameters[params_total][t++]=$3[j];
				
			}
			else
			{
				params_total++;
				t=0;
			}
		}
		if(params_total!=0)
		params_total++;
		else if(strlen($3)!=0)
		params_total++;
		//for(j=0;j<params_total;j++)
		  //   printf("PARAMETERS %d %s\n",i1,parameters[j]);
		      //	printf("fucky%d",params_total);
		     if(params_total!=0)
		      {
		        //printf("MEMEM\n");
		        position=0;
		        j=0;
		        size=strlen(temp);
		        k=0;
		        b=0;
		        position=0;
   			while( temp[k]!='\0' ) 
			{
				if(temp[k]!=' ')
				{
					
					if(flag1==0)
					{b=0;
					position=k;
					//printf("position %d\n",position);
					flag1=1;
					}
					word[b++]=temp[k++];
				}
				//printf("%s\n",token);
			        //strcpy(word,token);
			        
			       else
			       {flag1=0;
			       word[b]='\0';
			    // printf("word %s\n  ",word);
			        b=0;
			       // printf("%s\n",word);
				if(word[0]=='a' && word[1]=='r' && word[2]=='g')
				{	
					flag=1;
					for(j=3;j<strlen(word);j++)
						gen[j-3]=word[j];
					number=atoi(gen); //now to replace it with that parameter
					//printf("number%d\n",number);
					m=strlen(parameters[number-1]);
					n=strlen(word);
					//printf("Pranali"); 
				//	printf("word length %d %d\n",n,m);
					if(m==n)
					{
						//printf("mush");
						t=0;
						for(j=position;j<m+position;j++)
							temp[j]=parameters[number-1][t++];
						//position=position+n;
									
					}
					else if(m<n)
					{
						t=0;
						//printf("%s\n",temp);
						//printf("word%s %d\n",word,n);
						//printf("parameter %s %d\n",parameters[number-1],m);
						//printf("%s\n",temp);
						for(j=position;j<m+position;j++)
						{
							temp[j]=parameters[number-1][t++];
						}
						for(j=position+m;j<size-(n-m);j++)
						{
							temp[j]=temp[j+n-m];
						}
						temp[j]='\0';
						size=size-(n-m);
						t=0;
						//position=position+m;
					}	
					else if(m>n)
					{
						t=0;
						//printf("%s %d\n",word,position);
						//printf("%s\n",temp);
						//printf("word%s %d\n",word,n);
						//printf("parameter %s %d\n",parameters[number-1],m);
						for(j=position;j<n+position;j++)
							temp[j]=parameters[number-1][t++];
						for(j=size-1;j>=n+position;j--)
							temp[j+(m-n)]=temp[j];
						temp[size+m-n]='\0';
						for(j=n+position;j<position+m;j++)
							temp[j]=parameters[number-1][t++];
						t=0;
						size=size+m-n;
						//printf("%s\n",temp);
						//position=position+n;
				
					}
				}
				
				flag=0;
				//k++;
				while(temp[k]==' ')
				{
				k++;
				}
				
			}
			}}
			//else printf("pepe\n");
		//printf("%s\n",temp);
		$$=(char*)malloc(sizeof(char)*10000);
		sprintf($$,"%s ",temp);
		//printf("FINAL %s\n",$$);
		break;
			
		}
	}
};

PrimaryExpression: NUMBER {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s",$1);}
| TRE {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s",$1);}
| FASE {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s",$1);}
| Identifier {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s",$1);}
| THIS {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s",$1);}
| NEW INT2 OPAREN3 Expression CPAREN3 {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s %s %s",$1,$2,$3,$4,$5);free($4);}
| NEW Identifier OPAREN1 CPAREN1 {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s %s",$1,$2,$3,$4);free($2);}
| EXCLM Expression {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s%s",$1,$2);free($2);}
| OPAREN1 Expression CPAREN1 {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s",$1,$2,$3);free($2);};

MacroDefinition: MacroDefExpression {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s",$1);free($1);}
| MacroDefStatement {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s",$1);free($1);};

MacroDefStatement: DEF Identifier OPAREN1 Identifier_Comma_Identifier1_1 CPAREN1 OPAREN2 Statement1 CPAREN2 
{sprintf(def_identifiers[total],"%s",$2);sprintf(action[total],"%s %s %s",$6,$7,$8);
i1=0;
while($4[i1]!='\0')
{
	if($4[i1]==',')
		number_of_params++;
	i1++;
}
if(number_of_params!=0)
	number_of_params++;
arguments[total]=number_of_params;
i1=0;
number_of_params=0;
total++;
$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"\0");
/*$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s %s %s %s %s %s ",$1,$2,$3,$4,$5,$6,$7,$8);free($2);free($7);*/};

MacroDefExpression: DEF Identifier OPAREN1 Identifier_Comma_Identifier1_1 CPAREN1 OPAREN1 Expression CPAREN1
{sprintf(def_identifiers[total],"%s",$2);sprintf(action[total],"%s %s %s",$6,$7,$8);
i1=0;
while($4[i1]!='\0')
{
	if($4[i1]==',')
		number_of_params++;
		i1++;
}
if(number_of_params!=0)
	number_of_params++;
arguments[total]=number_of_params;
i1=0;
number_of_params=0;
total++;
$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"\0");
/*$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s %s %s %s %s %s",$1,$2,$3,$4,$5,$6,$7,$8);free($2);free($7);*/};

Comma_Identifier1: {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"\0");}
| Comma_Identifier1 COMMA Identifier {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s %s",$1,$2,$3);free($3);};

Identifier_Comma_Identifier1_1: {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"\0");}
| Identifier Comma_Identifier1 {$$=(char*)malloc(sizeof(char)*10000);sprintf($$,"%s %s",$1,$2);free($1);};

Identifier: ID
{
	$$=(char*)malloc(sizeof(char)*10000);
	sprintf($$,"%s",$1);
	
};
%%

main (int argc, char **argv) {

  /*extern int yydebug;
yydebug = 1;*/
  yyparse();
  
}

yyerror (char *s) {
  fprintf(stderr, "// Failed to parse macrojava code.\n");
}

