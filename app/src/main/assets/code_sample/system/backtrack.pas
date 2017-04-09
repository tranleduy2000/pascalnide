program loop;

function factorial(i: integer): integer;
begin
    if i = 1 then
        factorial := 1
    else
        factorial := factorial(i - 1) * i;
end;

begin
    write('10! = ', factorial(10));
    readln;
end.

{if you want to improve this code, please send code to me
tranleduy1233@gmail.com}