
program TriFusion;

uses crt;
type
  tab = array [1..10] of Integer;
var
  t : Tab;
  n : Integer;
procedure saisie(var t : tab;var  n : Integer);
var
  i : Integer;
begin
  readln(n);
  for i := 1 to n do
  begin
    readln(t[i]); 
  end;
end;

procedure fusion(a, b : tab;
  m, n : Integer; var t : tab);
var
  j, k, i : Integer;
begin
  i := 1;
  j := 1;
  k := 1;
  while (j<=n) and (i<=m) do begin
    if (a[i] <= b[j]) then begin
      t[k] := a[i];
      Inc(i);
    end
    else begin
      t[k] := b[j];
      Inc(j)
    end;
    Inc(k);
  end;

  while (i<=m) do begin
    t[k] := a[i];
    Inc(k);
    Inc(i);
  end;

  while (j<=n) do begin
    t[k] := b[j];
    Inc(k);
    Inc(j);
  end;
end;

procedure tri(var t : tab; n : Integer);
var
  d, g, i
  : Integer;
  l, m : Tab;
begin
  if (n>=2) then begin
    d :=  n div 2;
    g := n -d;
    for i:=1 to d do begin
      l[i] := t[i];
    end;
    for i:=1 to g do begin
      m[i] := t[d+i];
    end;

    tri(l, d);
    tri(m, g);
    fusion(l, m, d, g, t);
  end;
end;

procedure afficher(t : tab; n : Integer);
var
  i : Integer;
begin
  for i:=1 to n do begin
    Write(t[i], ' ');
  end;
  Writeln;
end;

BEGIN
  saisie(t, n);
  tri(t, n);
  afficher(t, n);
END.

