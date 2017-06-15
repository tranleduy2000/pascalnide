{medazizknani@gmail.com}

program DevoirTP;
Uses SysUtils, crt;
var
  f : text;
  p, q : LongInt;

procedure Saisie(var p, q : LongInt);
begin
    Readln(p);
    Readln(q);
end;

procedure Traitement(var f : text; p, q : LongInt);
var
  i : LongInt;
  st : string;
  c, n : LongInt;
begin
  Rewrite(f)  ;
  Writeln(f, p, ' ', q);
  for i:=p to q do begin
    n := i;
    st := '';
    c := 2;
    repeat
      while (n mod c = 0) do  begin
        st := st + '.' + IntTostr(c);
        n := n div c;
      end;
      c := c+1;
    until (n=1);
    Delete(st, 1, 1);
    st := IntTostr(i) + '=' + st;
    Writeln(f, st);
  end;
end;

procedure Affichage(var f : text);
var
  st : string;
begin
  reset(f) ;
  while (not(EOF(f))) do begin
    Readln(f, st);
    Writeln(st);
  end;

end;

BEGIN
  assign(f, 'decomp.txt');
  Saisie(p , q)     ;
  Traitement(f, p, q);
  Affichage(f);
END.

