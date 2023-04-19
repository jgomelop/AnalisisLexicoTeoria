package codigo;
import static codigo.Tokens.*;
%%
%class Lexer
%type Tokens
D=[0-9]+
espacio=[ ,\t,\r,\n]+
%{
    public String lexeme;
%}
%%
{espacio} {/*Ignore*/}
"=" {return Igual;}
"+" {return Suma;}
"-" {return Resta;}
"*" {return Multiplicacion;}
"/" {return Division;}
"(" {return ParentesisApertura;}
")" {return ParentesisCierre;}
("(-"{D}+")")|
{D}+ {lexeme=yytext(); return Entero;}
("(-"{D}+"."{D}+")")|
({D}+"."{D}+)|
({D}+".")|
("."{D}+) {lexeme=yytext(); return Float;}
 . {return ERROR;}

