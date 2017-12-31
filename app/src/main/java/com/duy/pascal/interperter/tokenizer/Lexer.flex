package com.duy.pascal.backend.tokenizer;

import com.duy.pascal.interperter.parse_exception.grouping.GroupingException;
import com.duy.pascal.interperter.parse_exception.grouping.StrayCharacterException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.tokens.EOFToken;
import com.duy.pascal.interperter.tokens.ignore.GroupingExceptionToken;
import com.duy.pascal.interperter.tokens.OperatorToken;
import com.duy.pascal.interperter.declaration.lang.types.OperatorTypes;
import com.duy.pascal.interperter.source.ScriptSource;

import com.duy.pascal.interperter.tokens.*;
import com.duy.pascal.interperter.tokens.basic.*;
import com.duy.pascal.interperter.tokens.closing.*;
import com.duy.pascal.interperter.tokens.grouping.*;
import com.duy.pascal.interperter.tokens.value.*;
import com.duy.pascal.interperter.tokens.visibility.*;
import com.duy.pascal.interperter.tokens.ignore.*;

import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.List;
import java.util.Stack;

%%
%unicode
%line
%column
%ignorecase

%public
%class Lexer
%{
	List<ScriptSource> searchDirectories;
	Stack<String> sourcenames;
	
	StringBuilder literal=new StringBuilder();
    private Stack zzStreams = new Stack();
	private String tmpname;
	private Reader tmpreader;
		void addInclude(String name) throws FileNotFoundException {
    		for (ScriptSource s : searchDirectories) {
    			Reader r = s.read(name);
    			if (r != null) {
    				this.tmpreader=r;
    				this.tmpname=name;
    				return;
    			}
    		}
    		throw new FileNotFoundException("Cannot find the $INCLUDE file " + name);
    	}

	void commitInclude() {
		sourcenames.push(tmpname);
		yypushStream(tmpreader);
	}

    public final boolean yymoreStreams() {
        return !zzStreams.isEmpty();
    }

	public final void yypopStream() throws java.io.IOException {
            zzReader.close();
            ZzFlexStreamInfo s = (ZzFlexStreamInfo) zzStreams.pop();
            zzBuffer = s.zzBuffer;
            zzReader = s.zzReader;
            zzEndRead = s.zzEndRead;
            zzStartRead = s.zzStartRead;
            zzCurrentPos = s.zzCurrentPos;
            zzMarkedPos = s.zzMarkedPos;
            zzAtEOF = s.zzAtEOF;
            zzEOFDone = s.zzEOFDone;
            yyline = s.yyline;
            yycolumn = s.yycolumn;
    }

    public final void yypushStream(Reader reader) {
            zzStreams.push(
                    new ZzFlexStreamInfo(zzReader, zzEndRead, zzStartRead, zzCurrentPos,
                            zzMarkedPos, zzBuffer, zzAtEOF,
                            yyline, yycolumn)
            );
            zzAtEOF = false;
            zzBuffer = new char[ZZ_BUFFERSIZE];
            zzReader = reader;
            zzEndRead = zzStartRead = 0;
            zzCurrentPos = zzMarkedPos = 0;
            yyline = yycolumn = 0;
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
		return new EOFToken(getLine());
	}
	sourcenames.pop();
	yypopStream();
%eofval}

Digit      = [0-9]
Identifier = [a-zA-Z_] [a-zA-Z_0-9]*
Char = "#" {Digit}+
WhiteSpace = ([ \t] | {LineTerminator})+

LineTerminator       = \r|\n|\r\n
InputCharacter       = [^\r|\n|]

Integer              = {Digit} {1, 20}
Exp                  = [Ee] [+-]? {Digit}+
NumberExp            = {NumberDecimal} {Exp} | {Digit}+ {Exp}
NumberDecimal        = {Digit}+ "." {Digit}+
Float                = {NumberExp} | {NumberDecimal} | {Digit}+
Hex                  = "$" [0-9a-fA-F]+
Binary               = ("%" [01]+) | ({Digit}[bB])
Octal                = "&" [0-7]+

Comment = {TraditionalComment} | {EndOfLineComment}  | {PascalComment}

CommentStarter		 =  "(*" | "{"
PascalComment        = ("(*" !([^]* "*)" [^]*) ("*)")?) | ( "{" !([^]* "}" [^]*) ("}")?)

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
	{IncludeStatement} {yybegin(INCLUDE);}
	{CompilerDirective} {return new CompileDirectiveToken(getLine(), yytext()); }
	
	{Comment} {return new CommentToken(getLine(), yytext());}

    {Char} {
         LineInfo lineInfo = getLine();
         String text = yytext();
         lineInfo.setColumn(lineInfo.getColumn() - text.length() - 1);
         return new CharacterToken(lineInfo, text);
    }
	{Integer} {return new IntegerToken(getLine(), (yytext()));}
	{Float} {return new DoubleToken(getLine(),(yytext()));}
	{Hex} {return new HexToken(getLine(),(yytext()));}
	{Octal} {return new OctalToken(getLine(),(yytext()));}
	{Binary} {return new BinaryToken(getLine(),(yytext()));}

	"and" {return new OperatorToken(getLine(),OperatorTypes.AND); }
	"not" {return new OperatorToken(getLine(),OperatorTypes.NOT); }
	"or" {return new OperatorToken(getLine(),OperatorTypes.OR); }
	"xor" {return new OperatorToken(getLine(),OperatorTypes.XOR); }

    //shift left operator
	"shl" {return new OperatorToken(getLine(),OperatorTypes.SHIFTLEFT); }
	"<<" {return new OperatorToken(getLine(),OperatorTypes.SHIFTLEFT); }

	//shift right operator
	"shr" {return new OperatorToken(getLine(),OperatorTypes.SHIFTRIGHT); }
	">>" {return new OperatorToken(getLine(),OperatorTypes.SHIFTRIGHT); }

	"div" {return new OperatorToken(getLine(),OperatorTypes.DIV); }
	"mod" {return new OperatorToken(getLine(),OperatorTypes.MOD); }
	"in" {return new OperatorToken(getLine(),OperatorTypes.IN); }

	"<>" {return new OperatorToken(getLine(),OperatorTypes.NOTEQUAL); }
	"><" {return new OperatorToken(getLine(),OperatorTypes.DIFFERENT); }
	"<=" {return new OperatorToken(getLine(),OperatorTypes.LESSEQ); }
	">=" {return new OperatorToken(getLine(),OperatorTypes.GREATEREQ); }
	">" {return new OperatorToken(getLine(),OperatorTypes.GREATERTHAN); }
	"<" {return new OperatorToken(getLine(),OperatorTypes.LESSTHAN); }
	"@" {return new OperatorToken(getLine(),OperatorTypes.ADDRESS); }
	"^" {return new OperatorToken(getLine(),OperatorTypes.DEREF); }

	"+=" {return new PlusAssignToken(getLine()); }
    "/=" {return new DivAssignToken(getLine()); }
    "*=" {return new MultiplyAssignToken(getLine()); }
    "-=" {return new MinusAssignToken(getLine()); }

	"=" {return new OperatorToken(getLine(),OperatorTypes.EQUALS); }
	"/" {return new OperatorToken(getLine(),OperatorTypes.DIVIDE); }
	"*" {return new OperatorToken(getLine(),OperatorTypes.MULTIPLY); }
	"+" {return new OperatorToken(getLine(),OperatorTypes.PLUS); }
	"-" {return new OperatorToken(getLine(),OperatorTypes.MINUS); }


	":=" {return new AssignmentToken(getLine()); }
	"," {return new CommaToken(getLine()); }
	":" {return new ColonToken(getLine()); }
	".." {return new DotDotToken(getLine());}
	"." {return new PeriodToken(getLine()); }
	";" {return new SemicolonToken(getLine()); }
	"(" {return new ParenthesizedToken(getLine());}
	"[" {return new BracketedToken(getLine());}
	"end" {return new EndToken(getLine());}
	")" {return new EndParenToken(getLine());}
	"]" {return new EndBracketToken(getLine());}

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
	"uses" {return new UsesToken(getLine()); }
	"begin" {return new BeginEndToken(getLine());}
	"record" {return new RecordToken(getLine());}
	"case" {return new CaseToken(getLine());}
	"break" {return new BreakToken(getLine());}
	"continue" {return new ContinueToken(getLine());}
	"with" {return new WithToken(getLine());}
	"implementation" {return new ImplementationToken(getLine());}
	"initialization" {return new InitializationToken(getLine());}
	"finalization" {return new FinalizationToken(getLine());}
	"interface" {return new InterfaceToken(getLine());}
	"unit" {return new UnitToken(getLine());}
	"set" {return new SetToken(getLine());}
	"goto" {return new GotoToken(getLine());}
	"lable" {return new LabelToken(getLine());}
	"private" {return new PrivateToken(getLine());}
	"public" {return new PublicToken(getLine());}
	"protected" {return new ProtectedToken(getLine());}
	"published" {return new PublishedToken(getLine());}
	"constructor" {return new ConstructorToken(getLine());}
	"destructors" {return new DestructorToken(getLine());}
	"class" {return new ClassToken(getLine());}

	"'" {
		literal.setLength(0);
		yybegin(STRING);
	}
	{WhiteSpace} {/*return new SpaceToken(getLine(), yytext());*/}
	{Identifier} {return new WordToken(getLine(),yytext()); }
}
<STRING> {
	"''"	{literal.append('\'');}
	"'"		{yybegin(STRINGDONE);}
	[^'\n\r]* {literal.append(yytext());}
	[\n\r]	{return new GroupingExceptionToken(getLine(), GroupingException.Type.NEWLINE_IN_QUOTES);}
}
<STRINGPOUND> {
	{Integer} {literal.append((char)Integer.parseInt(yytext())); yybegin(STRINGDONE);}
	.|\n      { return new GroupingExceptionToken(getLine(), GroupingException.Type.INCOMPLETE_CHAR);}
	
}
<STRINGDONE> {
	{WhiteSpace} {}
	{Comment} {return new CommentToken(getLine(), yytext());}
	"'" {yybegin(STRING);}
	//"#" {yybegin(STRINGPOUND);}
	.|\n {
			yypushback(1);
			yybegin(YYINITIAL); 
			if(literal.length()==1) {
			    LineInfo lineInfo = getLine();
                lineInfo.setColumn(lineInfo.getColumn() - 3);
				return new CharacterToken(lineInfo,literal.toString().charAt(0));
			} else {
			    LineInfo lineInfo = getLine();
                lineInfo.setColumn(lineInfo.getColumn() - literal.length() - 2);  //-2 by two quote
                return new StringToken(lineInfo, literal.toString());
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
    		GroupingException t = new GroupingException(getLine(),
    		     GroupingException.Type.IO_EXCEPTION);
			t.setCaused(e);
			return new GroupingExceptionToken(t);
    	}
    	yybegin(END_INCLUDE);
    }
    .|\n {return new GroupingExceptionToken(getLine(),
                GroupingException.Type.MISSING_INCLUDE);}
}

<INCLUDE_SNGL_QUOTE> {
	"''"	{literal.append('\'');}
	"'"		{
    	try {
    		addInclude(yytext());
    	}catch( FileNotFoundException e) {
    		GroupingException t = new GroupingException(getLine(),
    		    GroupingException.Type.IO_EXCEPTION);
			t.setCaused(e);
			return new GroupingExceptionToken(t);
    	}
    	yybegin(END_INCLUDE);
    }
	[^\n\r]+ {literal.append(yytext());}
	[\n\r]	{return new GroupingExceptionToken(getLine(),
	        GroupingException.Type.NEWLINE_IN_QUOTES);}
}
<INCLUDE_DBL_QUOTE> {
	"\"\""	{literal.append('\"');}
	"\""		{
    	try {
    		addInclude(yytext());
    	}catch( FileNotFoundException e) {
    		GroupingException t = new GroupingException(getLine(),
    		        GroupingException.Type.IO_EXCEPTION);
			t.setCaused(e);
			return new GroupingExceptionToken(t);
    	}
    	yybegin(END_INCLUDE);
    	}
	[^\n\r]+ {literal.append(yytext());}
	[\n\r]	{return new GroupingExceptionToken(getLine(),
	        GroupingException.Type.IO_EXCEPTION);}
}

<END_INCLUDE> {
	{RestOfComment}	{yybegin(YYINITIAL); commitInclude(); }
	.|\n {return new GroupingExceptionToken(getLine(),
				GroupingException.Type.MISMATCHED_BRACKETS);}
}

/* error fallback */
.|\n  { return new GroupingExceptionToken(new StrayCharacterException(getLine(),yytext().charAt(0))); }

