Program Example15;
uses Crt;

{ Program to demonstrate the Delay function. }
var
    i : longint;
begin
    WriteLn('Counting Down');
    for i:=10 downto 1 do
    begin
        WriteLn(i);
        {Wait one second}
        Delay(1000);
    end;
    WriteLn('BOOM!!!');
end.
{http://www.freepascal.org/docs-html/rtl/crt/delay.html}