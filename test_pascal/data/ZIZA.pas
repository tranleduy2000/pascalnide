var 
	a: array[0..100,0..100] of longint;
    n: longint;
procedure process;
var 
	i,j,x,y:longint;
begin
    a[1,1]:=1;
    x:=1;
    y:=1;
    i:=2;
    while (i<=n*n) do
    begin
        if (i=n*n) then break;
        if (x=1) or (y=n) then
        begin
            if (y+1<=n) then inc(y)
            else inc(x);
            a[x,y]:=i;
            inc(i);
            while (y<>1) and (x+1<=n) do
            begin
                dec(y);
                inc(x);
                a[x,y]:=i;
                inc(i);
            end;
        end;
        if (y=1) or (x=n) then
        begin
            if (x+1<=n) then inc(x)
            else inc(y);
            a[x,y]:=i;
            inc(i);
            while (x<>1) and (y+1<=n) do
            begin
                dec(x);
                inc(y);
                a[x,y]:=i;
                inc(i);
            end;
        end;
    end;
    for i:=1 to n do
    begin
        for j:=1 to n do write(a[i,j],' ');
        writeln;
    end;
end;

begin
	readln(n);
	process;
end.
    
