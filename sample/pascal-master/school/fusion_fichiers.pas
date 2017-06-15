type
  fi = file of Integer;
var
  f1, f2, f3 : fi;
  e : Integer;
procedure Fusion(var f1, f2, f3 : fi);
var
  e1, e2 : Integer;
begin
  reset(f1);
  reset(f2);
  rewrite(f3);
  while (not(EOF(f1)) and not(EOF(f2)))  do begin
    read(f1, e1);
    read(f2, e2);
    if (e1<e2) then begin
      write(f3, e1);
      Seek(f2, filepos(f2)-1);
    end
    else begin
      write(f3, e2);
      Seek(f1, filepos(f1)-1);
    end
  end;

  while (not(EOF(f1))) do begin
    read(f1, e1);
    write(f3, e1);
  end;
  while (not(EOF(f2))) do begin
    read(f2, e1);
    write(f3, e1);
  end;
end;
procedure saisie(var f : fi);
var
 i,  n, e : Integer;
begin
  readln(n) ;
  rewrite(f);
  for i := 1 to n do begin
    write(i, ' ?');
    readln(e);
    write(f, e);
  end;
end;

BEGIN
  assign(f1, 'k.dat');  
  saisie(f1);
  assign(f2, 'r.dat');  
  saisie(f2);
  assign(f3, 's.dat');
  Fusion(f1, f2, f3);
  reset(f3);
  while not(EOF(f3)) do begin
    read(f3, e);
    writeln(e);
  end;
END.
