program array_example;
var
    simpleArray: array[1..3] of integer;
    complexArray: array[1..3, 1..3] of integer;
    arrayOfString: array[-3..-1] of string;
    i, j: integer;
begin
    {set value for simpleArray}
    for i:=2 to 3 do simpleArray[i] := i * i;
    {write to console}
    for i:=1 to 3 do writeln(simpleArray[i]);
    {set text for complexArray}
    for i:=2 to 3 do
        for j:=1 to 2 do complexArray[i,j] := i * j;
    writeln('complex array:');
    {write to console}
    for i:=1 to 3 do for j:=1 to 3 do writeln('[',i,',',j,']=',complexArray[i,j]);

    arrayOfString[-2] := 'hello pascal';
    writeln(arrayOfString[-3],arrayOfString[-2]);
    readln;
end.

{if you want to improve this code, please send code to me
tranleduy1233@gmail.com}