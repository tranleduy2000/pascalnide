program test_random;
var
    i: integer;
begin
    randomize;
    for i := 1 to 1000 do
        writeln(random(1000));
    readln;
end.

{if you want to improve this code, please send code to me
tranleduy1233@gmail.com}