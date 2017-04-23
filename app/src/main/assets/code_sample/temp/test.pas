type
    strlen = record
        s: string;
        l: integer;
        a: array of integer;
    end;


procedure printdouble(x: real);
begin
    writeln('double'#10);
end;

procedure printdouble(x: integer);
begin
    writeln('int'#10,x,'lol'#10);
end;

var
    doobie: strlen; i: integer;
begin
    doobie.s := 'hello';
    doobie.l := 5;
    setlength(doobie.a,doobie.l);
    for i:=0 to 3 do begin
        doobie.a[i] := i;
    end;
    for i:=0 to 4 do begin
        writeln(doobie.a[i]);
    end;
    printdouble(doobie.l);
end.
