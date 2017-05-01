uses crt;
var
    p : ^integer;
    a, s : integer;
BEGIN
    clrscr;
    write('Nhap vao mot so (kieu con tro) p^ = ');
    new(p);
    readln(p^);
    write('Nhap vao mot so (kieu so nguyen) a =  ');
    readln(a);
    s := 100 * p^ + a;
    writeln('100p^ + a = : ', s);
    readln;
END.