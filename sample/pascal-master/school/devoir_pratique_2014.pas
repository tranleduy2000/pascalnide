type
  mat = array [1..20, 1..20] of Byte;
  tab = array [1..25] of Byte;
var
  f : text;
  m : mat;
  n : Byte;

  function premier(n : Integer):Boolean;
  var
    verif : Boolean;
    i : Integer;
  begin
    verif := True;
    for i:=2 to (n div 2) do begin
      if (n mod i = 0) then begin
      verif := False;
    end;
    end;

    premier := verif;
  end;
procedure remplir(var m : mat; var n:  byte);

var
  t :tab;
  c, i, j : Integer;
begin
  c:= 1;
  readln(n);
  for i:=2 to 99 do begin
    if premier(i) then begin
      t[c] := i;
      c := c+1; 
    end;
  end;
  for i := 1 to n do begin
    for j := 1 to n do begin
      m[i, j]:= t[random(25)+1];
    end;
  end;
end;

procedure affic(m : mat; n : byte);
var
  i, j : Byte;
begin
  for i := 1 to n do begin
    for j := 1 to n do begin
      write(m[i, j ], ' ')
    end;
    writeln;
  end;
  
end;

procedure Trait(n : Integer; m  : mat; var f : text);
  procedure ecrireln(var f : text; k : Integer);
  var
    j : Integer;
  begin
      write(f, 'L', k, '*');
      for j:=1 to n do begin
        write(f, m[k, j] );
        if (j<>n) then write(f, '-');
      end;
      writeln(f);
  end;
  procedure ecrirecol(var f : text; k : Integer);
  var
    j : Integer;
  begin
      write(f, 'C', k, '*');
      for j:=1 to n do begin
        write(f, m[j, k]);
        if (j<>n) then write(f, '-');
      end;
      writeln(f);
  end;
  var
    i, c : Integer;
begin
  rewrite(f) ;
  for i := 1 to n do begin
    c := 1;
    {ordre croissant}
    while (c<n) and (m[i, c]<=m[i, c+1]) do
      c := c+1;
    if (c=n) then begin
      ecrireln(f, i);
    end;
    {ordre decroissant}
    c := 1;
    while (c<n) and (m[i, c]>=m[i, c+1]) do 
      c := c+1;
    if (c=n) then begin
      ecrireln(f, i);
    end;
    c:= 1;
    while (c<n) and (m[c, i]>=m[c+1, i]) do 
      c := c+1;
    if (c=n) then begin
      ecrirecol(f, i);
    end;
    c := 1;
    while (c<n) and (m[c, i] <= m[c+1, i]) do 
      c := c+1;
    if (c=n) then begin
      ecrirecol(f, i);
    end;
  end;
end;

BEGIN
  assign(f, 'ft.txt');
  remplir(m, n);
  affic(m, n);
  Trait(n, m, f);
  close(f);
END.
