{https://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html}
uses
   sysutils;

var
   s : string;
begin
   // Explicit argument indices may be used to re-order output.
   s := format('%4$2s %3$2s %2$2s %1$2s', ['a', 'b', 'c', 'd']);
   // -> ' d  c  b  a'
   WriteLn(s);
   
   s := format('pi = %+10.4f', [pi]);
   // -> 'e =    +2,7183'
   WriteLn(s);
end.
