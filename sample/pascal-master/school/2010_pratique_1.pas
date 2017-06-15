
program Pratique1;
Uses crt;
type
  fi = file of Byte;
var
  f : fi;
  n : byte;

procedure saisie(var n : byte;
  var f: fi);
var
  i : Byte;
begin
  rewrite(f);
  repeat
    readln(n);
  until (n in [3..50]);
  randomize; 
  for i:=1 to n do begin
    write(f, Random(9)+1) ;
    {write(f, i) ;}
  end;
end;

procedure Permuter(var a, b : byte);
var
  c : byte;
begin
  c := a;
  a := b;
  b := c;
end;

procedure traitement(var f: fi;
  n : byte);
var
  t            : array [1..50] of Byte;
  i, j         : Byte;
  gn, pn       : String;
  r, rp, u, up : Integer;
  tr           : Boolean;
begin
  reset(f);
  for i := 1 to n do begin
    read(f, t[i]);
  end; 
  
  gn := ''; pn := '';
  { Tri a bulles mon preferes}
  for i := 1 to n do
  begin
    for j := 1 to n-1 do
    begin
      if (t[j]<=t[j+1]) then begin
        Permuter(t[j], t[j+1]);
      end;
    end;
    pn := pn + Chr(48+t[n-i+1]);
    gn := Chr(48+t[n-i+1])  + gn;
  end;
  Writeln('le plus grande composition : ', gn);
  Writeln('le plus petite composition : ', pn);
  {Est une suite de raison r ?}
  
  up := 0;
  r  := 0;
  rp := r;
  TR := True;
  i := 0;
  repeat
    Inc(i);
    u := t[i] - t[n-i+1];
    if (i<>1) then begin
      rp := r;
      r := u - up;
      { On peut conclure SSI on atteint le troisieme terme }
      if (r<>rp) and (i>=3) then TR := False;
    end;
    up := u;
  until (i> n div 2) or not(TR);
  if (TR) then begin
    Writeln('Ils forment une suite arithmetique de r=', r);
  end else
    Writeln('Ils ne forment pas unse suite arithmetique puisque r',i-2,'<>r',i-1)
end;

BEGIN
  assign(f, 'Nombres.dat') ;
  saisie(n, f);
  traitement(f, n) ;
  Close(f);
END.

