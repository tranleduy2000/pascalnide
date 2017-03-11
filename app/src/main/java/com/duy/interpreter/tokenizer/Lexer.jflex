package com.js.com.js.interpreter.tokenizer;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.List;
import java.util.Stack;

import com.js.com.duy.interpreter.exceptions.grouping.EnumeratedGroupingException;
import com.js.com.duy.interpreter.exceptions.grouping.EnumeratedGroupingException.grouping_exception_types;
import com.js.com.duy.interpreter.exceptions.grouping.StrayCharacterException;
import com.js.com.duy.interpreter.linenumber.LineInfo;
import com.js.com.js.interpreter.startup.ScriptSource;
import com.js.com.duy.interpreter.tokens.*;
import com.js.com.duy.interpreter.tokens.basic.*;
import com.js.com.duy.interpreter.tokens.closing.*;
import com.js.com.duy.interpreter.tokens.grouping.*;
import com.js.com.duy.interpreter.tokens.value.*;

%%
%unicode
%line
%column
%ignorecase

%class Lexer
%{
	List<ScriptSource> searchDirectories;
	Stack<String> sourcenames;
	
	StringBuilder literal=new StringBuilder();

	private String tmpname;
	private Reader tmpreader;
	void addInclude(String filePath) throws FileNotFoundException {
		for (ScriptSource s : searchDirectories) {
			Reader r = s.read(filePath);
			if (r != null) {
				this.tmpreader=r;
				this.tmpname=filePath;
				return;
			}
		}
		throw new FileNotFoundException("Cannot find the $INCLUDE file " + filePath);
	}
	
	void commitInclude() {
		sourcenames.push(tmpname);
		yypushStream(tmpreader);
	}
	
	LineInfo getLine() {
		return new LineInfo(yyline,yycolumn,sourcenames.peek());
	}
%}

%ctorarg String sourcename
%ctorarg List<ScriptSource> searchDirectories
%init{
		sourcenames=new Stack<String>();
		sourcenames.push(sourcename);
		this.searchDirectories = searchDirectories;
%init}

%type Token

%eofval{
	if (!yymoreStreams()) {
		return new EOF_Token(getLine());
	}
	sourcenames.pop();
	yypopStream();
%eofval}

Identifier = [a-zA-Z_] [a-zA-Z_0-9]*
Digit = [0-9]
Integer = {Digit}+
Float	= {Digit}+ "." {Digit}+
WhiteSpace = ([ \t] | {LineTerminator})+

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r|\n|]

Comment = {TraditionalComment} | {EndOfLineComment}  | {BracesComment}

CommentStarter		 =  "(*" | "{"
CommentEnder		 =   "*)" | "}"

BracesComment		 = {CommentStarter} {RestOfComment}

RestOfComment		 = ([^*] | \*[^)}])* "}"
 
TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}


IncludeStatement = {CommentStarter}\$(("i" [ \t]) |"include")
CompilerDirective = {CommentStarter}\$ {RestOfComment}

%state STRING
%state STRINGDONE
%state STRINGPOUND
%state INCLUDE
%state INCLUDE_DBL_QUOTE
%state INCLUDE_SNGL_QUOTE
%state END_INCLUDE
%%

<YYINITIAL> {
	{WhiteSpace} {}
	{IncludeStatement} {yybegin(INCLUDE);}
	{CompilerDirective} {return new WarningToken(getLine(),"Warning! Unrecognized Compiler Directive!"); }
	
	{Comment} {}


	{Float} {return new DoubleToken(getLine(),Double.parseDouble(yytext()));}
	{Integer} {return new IntegerToken(getLine(),Integer.parseInt(yytext()));}
	
	"and" {return new OperatorToken(getLine(),OperatorTypes.AND); }
	"not" {return new OperatorToken(getLine(),OperatorTypes.NOT); }
	"or" {return new OperatorToken(getLine(),OperatorTypes.OR); }
	"xor" {return new OperatorToken(getLine(),OperatorTypes.XOR); }
	"shl" {return new OperatorToken(getLine(),OperatorTypes.SHIFTLEFT); }
	"shr" {return new OperatorToken(getLine(),OperatorTypes.SHIFTRIGHT); }
	"div" {return new OperatorToken(getLine(),OperatorTypes.DIV); }
	"mod" {return new OperatorToken(getLine(),OperatorTypes.MOD); }
	"=" {return new OperatorToken(getLine(),OperatorTypes.EQUALS); }
	"/" {return new OperatorToken(getLine(),OperatorTypes.DIVIDE); }
	"*" {return new OperatorToken(getLine(),OperatorTypes.MULTIPLY); }
	"+" {return new OperatorToken(getLine(),OperatorTypes.PLUS); }
	"-" {return new OperatorToken(getLine(),OperatorTypes.MINUS); }
	"<>" {return new OperatorToken(getLine(),OperatorTypes.NOTEQUAL); }
	"<=" {return new OperatorToken(getLine(),OperatorTypes.LESSEQ); }
	">=" {return new OperatorToken(getLine(),OperatorTypes.GREATEREQ); }
	">" {return new OperatorToken(getLine(),OperatorTypes.GREATERTHAN); }
	"<" {return new OperatorToken(getLine(),OperatorTypes.LESSTHAN); }
	
	"if" {return new IfToken(getLine()); }
	"then" {return new ThenToken(getLine()); }
	"while" {return new WhileToken(getLine()); }
	"do" {return new DoToken(getLine()); }
	"var" {return new VarToken(getLine()); }
	"type" {return new TypeToken(getLine()); }
	"procedure" {return new ProcedureToken(getLine()); }
	"function" {return new FunctionToken(getLine()); }
	"program" {return new ProgramToken(getLine()); }
	"else" {return new ElseToken(getLine()); }
	"for" {return new ForToken(getLine()); }
	"to" {return new ToToken(getLine()); }
	"downto" {return new DowntoToken(getLine()); }
	"repeat" {return new RepeatToken(getLine()); }
	"of" {return new OfToken(getLine()); }
	"const" {return new ConstToken(getLine()); }
	"false" {return new BooleanToken(getLine(),false); }
	"true" {return new BooleanToken(getLine(),true); }
	"forward" {return new ForwardToken(getLine()); }
	"array" {return new ArrayToken(getLine()); }
	"until" {return new UntilToken(getLine()); }
	":=" {return new AssignmentToken(getLine()); }
	"," {return new CommaToken(getLine()); }
	":" {return new ColonToken(getLine()); }
	".." {return new DotDotToken(getLine());}
	"." {return new PeriodToken(getLine()); }
	";" {return new SemicolonToken(getLine()); }
	"begin" {return new BeginEndToken(getLine());}
	"record" {return new RecordToken(getLine());}
	"case" {return new CaseToken(getLine());}
	"(" {return new ParenthesizedToken(getLine());}
	"[" {return new BracketedToken(getLine());}
	"end" {return new EndToken(getLine());}
	")" {return new EndParenToken(getLine());}
	"]" {return new EndBracketToken(getLine());}
	"'" {
		literal.setLength(0);
		yybegin(STRING);
	}
	
	{Identifier} {return new WordToken(getLine(),yytext().toLowerCase()); }

}
<STRING> {
	"''"	{literal.append('\'');}
	"'"		{yybegin(STRINGDONE);}
	[^'\n\r]* {literal.append(yytext());}
	[\n\r]	{return new GroupingExceptionToken(getLine(),grouping_exception_types.NEWLINE_IN_QUOTES);}
}
<STRINGPOUND> {
	{Integer} {literal.append((char)Integer.parseInt(yytext())); yybegin(STRINGDONE);}
	.|\n      { return new GroupingExceptionToken(getLine(),grouping_exception_types.INCOMPLETE_CHAR);}
	
}
<STRINGDONE> {
	{WhiteSpace} {}
	{Comment} {}
	"'" {yybegin(STRING);}
	"#" {yybegin(STRINGPOUND);}
	.|\n {
			yypushback(1);
			yybegin(YYINITIAL); 
			if(literal.length()==1) {
				return new CharacterToken(getLine(),literal.toString().charAt(0));
			} else {
				return new StringToken(getLine(),literal.toString());
			}
		}
}

<INCLUDE> { 
	{WhiteSpace} {} 
	"'" {literal.setLength(0); yybegin(INCLUDE_SNGL_QUOTE);}
    "\"" {literal.setLength(0); yybegin(INCLUDE_DBL_QUOTE);}
    [^ \r\n*)}]+ {
    	try {
    		addInclude(yytext());
    	}catch( FileNotFoundException e) {
    		EnumeratedGroupingException t = new EnumeratedGroupingException(getLine(),grouping_exception_types.IO_EXCEPTION);
			t.caused = e;
			return new GroupingExceptionToken(t);
    	}
    	yybegin(END_INCLUDE);
    }
    .|\n {return new GroupingExceptionToken(getLine(),grouping_exception_types.MISSING_INCLUDE);}
}

<INCLUDE_SNGL_QUOTE> {
	"''"	{literal.append('\'');}
	"'"		{
    	try {
    		addInclude(yytext());
    	}catch( FileNotFoundException e) {
    		EnumeratedGroupingException t = new EnumeratedGroupingException(getLine(),grouping_exception_types.IO_EXCEPTION);
			t.caused = e;
			return new GroupingExceptionToken(t);
    	} 
    	yybegin(END_INCLUDE);
    }
	[^\n\r]+ {literal.append(yytext());}
	[\n\r]	{return new GroupingExceptionToken(getLine(),grouping_exception_types.NEWLINE_IN_QUOTES);}
}

<INCLUDE_DBL_QUOTE> {
	"\"\""	{literal.append('\"');}
	"\""		{
    	try {
    		addInclude(yytext());
    	}catch( FileNotFoundException e) {
    		EnumeratedGroupingException t = new EnumeratedGroupingException(getLine(),grouping_exception_types.IO_EXCEPTION);
			t.caused = e;
			return new GroupingExceptionToken(t);
    	} 
    	yybegin(END_INCLUDE);
    	}
	[^\n\r]+ {literal.append(yytext());}
	[\n\r]	{return new GroupingExceptionToken(getLine(),grouping_exception_types.IO_EXCEPTION);}
}

<END_INCLUDE> {
	{RestOfComment}	{yybegin(YYINITIAL); commitInclude(); }
	.|\n {return new GroupingExceptionToken(getLine(),
				grouping_exception_types.MISMATCHED_BRACKETS);}
}

/* error fallback */
.|\n  { return new GroupingExceptionToken(new StrayCharacterException(getLine(),yytext().charAt(0))); }

