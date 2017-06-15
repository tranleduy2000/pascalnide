
program Distances;
Uses crt;

type
  tab = array ['A'..'J'] of array ['x'.. 'y'] of Integer;
  mat = array ['A'..'J'] of array ['A'..'J'] of Real;
var
  t : tab;
  n : Byte;
  m : mat;
  Point : Char;
procedure Saisie(var t : tab;
  var n : byte);
var
  j, i : char;
begin
  repeat
    Writeln('N ? ');
    readln(n);
  until (n in [3..10]);

  for i:='A' to chr(64+n) do begin
    for j:='x' to 'y' do begin
      writeln(j, ' de ', i, ' ? ');
      readln(t[i][j]);
    end;
  end;
end;

procedure Traitement(t :tab; n : Byte; var m : mat);
  function Pow2(x : Integer) : Real;
  begin
    Pow2 := x*x;
  end;
var
  i, j : char;
  dis  : Real;
begin
  for i:='A' to chr(64+n) do begin
    for j:='A' to i do begin
      if (j=i) then m[i][j] := 0
      else begin
        dis := sqrt(Pow2(t[i]['x'] - t[j]['x']) + Pow2(t[i]['y']-
          t[j]['y']));
        m[i][j] := dis;
        m[j][i] := dis;
      end;
    end;
  end;
end;

procedure Affichage( m : mat; n : byte );
var
  i, j : char;
begin
  for i:='A' to chr(64+n) do begin
    for j:='A' to chr(64+n) do begin
      Write(m[i][j]:0:1, ' ');
    end;
    Writeln;
  end;
end;

function Chercher( n : byte; m : mat; P : char ): Char;
var
  PremierVue : char;
  Distance   : Real;
  i       : char;
begin
  PremierVue := 'A';
  Distance := m[P]['A'];
  for i:='B' to chr(64+n) do begin
    if (i <> P) and (m[P][i]<Distance) then begin
      PremierVue := i;
      Distance  := m[P][i];
    end;
  end;
  Chercher := PremierVue;
end;

BEGIN
  Saisie(t, n);
  Traitement(t, n, m);
  Affichage(m, n);
  Writeln('Chercher un point ?');
  readln(Point);
  Writeln(Chercher(n, m, Point));
END.
