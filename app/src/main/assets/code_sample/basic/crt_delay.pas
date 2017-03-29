Program CRT_DELAY;
var
  i : integer;
begin
  WriteLn('Counting Down');
  for i:=10 downto 1 do
   begin
     WriteLn(i);
     Delay(1000);
   end;
  WriteLn('BOOM!!!');
    readln;
end.

{if you want to improve this code, please send code to me
tranleduy1233@gmail.com}