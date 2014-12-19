%{
#include "gen.tab.h"
int i=0;
%}

%%

"+"    {yylval.string="+"; return ADD; }
"-"    {yylval.string="-"; return SUB; }
"/"    {yylval.string="/"; return DIV; }
"*"    {yylval.string="*"; return MUL; }
"("    {yylval.string="("; return OPAREN1;}
")"    {yylval.string=")"; return CPAREN1;}
"{"    {yylval.string="{"; return OPAREN2;}
"}"    {yylval.string="}"; return CPAREN2;}
[1-9][0-9]*|0 { yylval.string=strdup(yytext); return NUMBER; }
[ \t]  { /* ignore whitespace */ }
"class" {yylval.string=strdup(yytext); return CLASS; }
"public static void main" {yylval.string=strdup(yytext); return PSVM; }
"String" {yylval.string=strdup(yytext); return STR; }
"extends" {yylval.string=strdup(yytext); return EXT; }
"public" {yylval.string=strdup(yytext);return PUB; }
"return" {yylval.string=strdup(yytext); return RET; }
"int[]" {yylval.string=strdup(yytext); return INT1; }
"boolean" {yylval.string=strdup(yytext); return BOOL; }
"int" {yylval.string=strdup(yytext); return INT2; }
"if" {yylval.string=strdup(yytext); return IF; }
"else" {yylval.string=strdup(yytext); return ELSE; }
"while" {yylval.string=strdup(yytext); return WHILE; }
"&" {yylval.string="&"; return AND; }
"<" {yylval.string="<"; return LESS_THAN; }
"[" {yylval.string="["; return OPAREN3; }
"]" {yylval.string="]"; return CPAREN3; }
".length" {yylval.string=strdup(yytext); return LEN; }
"true" {yylval.string=strdup(yytext); return TRE; }
"false" {yylval.string=strdup(yytext); return FASE; }
"this" {yylval.string=strdup(yytext); return THIS; }
"new" {yylval.string=strdup(yytext); return NEW; }
"!" {yylval.string="!"; return EXCLM; }
"#define" {yylval.string=strdup(yytext); return DEF; }
"System.out.println" {yylval.string=strdup(yytext); return SOPL; }
";" {yylval.string=";"; return SEMI; }
"=" {yylval.string="="; return EQUAL; }
"," {yylval.string=","; return COMMA; }
"?" {yylval.string="?"; return QUES; }
"." {yylval.string="."; return DOT; }
[_a-zA-Z][a-zA-Z0-9_]* {yylval.string=strdup(yytext); return ID;}
\/\*([^*]|[\r\n]|(\*+([^*\/]|[\r\n])))*\*+\/ {/*Multi-line comments, ignore*/}
\/\/([^\n\r])*[\n\r] {/*Single line comment, ignore*/ }
\n {}
<<EOF>> {return 0;}
. { printf("Mystery character %c\n", *yytext); }
%%


