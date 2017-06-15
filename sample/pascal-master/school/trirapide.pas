
program triRapide;
Uses crt;
type
  tab = array [1..10] of Integer;
var
  t : tab;
  n : Word;
procedure Permuter(var a, b : Integer);
var
  tmp : Integer;
begin
  tmp := a;
  a   := b;
  b   := tmp ;
end;

function Partioner(var t : tab;
  debut, fin : Word): Word;
var
  Index : Word;
  Pivot : Integer;
  i     : Word;
begin
  Index := debut;
  Pivot := t[fin];
  for i :=debut to fin-1 do begin
    if (t[i]<=Pivot) then begin
      Permuter(t[i], t[index]);
      Inc(Index);
    end;
  end;
  Permuter(t[Index], t[fin]);
  Partioner := Index;
end;

procedure saisie(var t : tab;var  n : Word);
var
  i : Integer;
begin
  readln(n);
  for i := 1 to n do
  begin
    readln(t[i]); 
  end;
end;
procedure Tri(var t : tab; debut, fin : Word);
var
  Index : Word;
begin
  if (fin>debut) then begin
    Index := Partioner(t, debut, fin);
    Tri(t, debut, Index-1);
    Tri(t, Index+1, fin);
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
  saisie(t, n )  ;
  Tri(t, 1, n);
  afficher(t, n);
END.
