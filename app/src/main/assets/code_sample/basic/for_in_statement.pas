type
    MonthType = (January, February, March, April,
    May, June, July, August, September,
    October, November, December);
var
    a: array[1..3] of integer = (1, 2 , 3);
    b: set of char = ['a','c','d'];
    c: char;
    i: integer;
    m: MonthType;

begin
    //for each array
    for i in a do write(i, ' '); //1 2 3
    writeln;

    //for each set
    for c in b do write(c, ' '); //a c d
    writeln;

    //for each enum
    for m := January to December do write(m, ' ');
    readln;
end.