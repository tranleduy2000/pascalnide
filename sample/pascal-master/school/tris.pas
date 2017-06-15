program tris;
Uses crt;
type
  tab = array of Integer;
var
  n : Integer;
  t : tab;

procedure Saisie(var t : tab; var n : Integer);
var
  i : Integer;
begin
  readln(n);
  SetLength(t, n);
  for i := 0 to n-1 do begin
    t[i] := random(400);
  end;
end;

procedure TriFusion(var t : tab; debut, fin : Integer);

  procedure Fusion(k, m : tab; var t : tab; n, c : Integer);
  var
    i, j, l : Integer;
  begin
    j := 1;
    i := 1;
    l :=1;
    while (j<=n) and (i<=c) do  begin
      if (k[j]<m[i]) then begin
        t[l] := k[j];
        Inc(j);
      end
      else begin
        t[l] := m[i];
        Inc(i);
      end;
      Inc(l);
    end;

    while (j<=n) do begin
      t[l] := k[j];
      Inc(l);
      Inc(j);
    end;

    while (i<=c) do begin
      t[l] := m[i];
      Inc(l);
      Inc(i);
    end;
  end;

var
  milieu, i : Integer;
  k, m : tab;
begin
  if (fin-debut>0) then  begin
    milieu := (fin + debut) div 2;
    SetLength(k, milieu);
    for i := debut to milieu do begin
      k[i] := t[i];
    end;
    SetLength(m, fin-milieu);
    for i := milieu to fin do begin
      m[i-milieu]  := t[i];
    end;
    TriFusion(K, debut, milieu-1);
    TriFusion(m, debut, fin-milieu);
    Fusion(K, M, T, 0, fin-milieu);
  end;
end;

procedure TriRapide(var t: tab; d, f: Integer);
begin
  if (n>0) then begin
    Position := Trier(t, d, f);
  end; 
end;

procedure affic(t : tab; n : Integer);
var
  i : Integer;
begin
  for i := 1 to n do begin
    write(t[i], ' ')
  end;
  writeln;
end;

BEGIN
  clRscr;
  Saisie(t, n)    ;
  affic(t, n);
  TriFusion(t, 0, n-1);
  affic(t, n);
END.

