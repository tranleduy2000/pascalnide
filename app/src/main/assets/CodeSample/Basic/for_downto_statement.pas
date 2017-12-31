program for_downto_statement;
var
  i, n : integer;
begin
  writeln('Enter small number: ');
  readln(n);
  for i := n downto 1 do writeln('i = ', i);
  readln;
end.

{if you want to improve this code, please send code to me
tranleduy1233@gmail.com}