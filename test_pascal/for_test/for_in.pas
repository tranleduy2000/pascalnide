var
    a: array[1..3] of integer = (1, 2 , 3);
    b: set of char = ['a','c','d'];
    c: char;
    i: integer;
begin
    for i in a do write(i, ' ');
    for c in b do write(c, ' ');
    for i in i do write(i); //error
    readln;
end.