Program Example6;
uses Crt;
{ Program to demonstrate the GotoXY function. }
begin
    ClrScr;
    GotoXY(10,10);
    Write('10,10');
    GotoXY(20,20);
    Write('20,20');
    GotoXY(1,22);
    readln;
end.
{http://www.freepascal.org/docs-html/rtl/crt/gotoxy.html}