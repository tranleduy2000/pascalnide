program test_get_time_func;
uses dos,crt;
var
    a, b, c, d : integer;

function print(w: integer): string;
var
    s : string;
begin
    Str(w,s);
    if w < 10 then
        print := '0' + s
    else
        print := s;
end;

begin
    while not keyPressed do
    begin
        clrscr;
        GetTime(a,b,c,d);
        gotoXY(15,8);
        textColor(White);
        WriteLn('Current time');
        gotoXY(17,9);
        textColor(Yellow);

        WriteLn(print(a),':',print(b),':',print(c));
        delay(1000);
    end;
    readln;
end.
{if you want to improve this code, please send code to me
tranleduy1233@gmail.com}
