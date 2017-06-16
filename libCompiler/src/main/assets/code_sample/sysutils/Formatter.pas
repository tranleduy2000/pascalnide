{https://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html}
uses
   sysutils;

var
   s : string;
begin
   // Explicit argument indices may be used to re-order output.
   s := format('%4$2s %3$2s %2$2s %1$2s', 'a', 'b', 'c', 'd');
   // -> ' d  c  b  a'
   WriteLn(s);
   
   s := format('pi = %+10.4f', pi);
   // -> 'e =    +2,7183'
   WriteLn(s);
   
   // The '(' numeric flag may be used to format negative numbers with
   // parentheses rather than a minus sign.  Group separators are
   // automatically inserted.
   s := format('Amount gained or lost since last statement: $ %(,.2f',
                 balanceDelta);
   // -> 'Amount gained or lost since last statement: $ (6,217.58)'
   writeln(s);
end.
