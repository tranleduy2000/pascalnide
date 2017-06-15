var
  som : Integer;
function Trait(s : string; var som : Integer) : Boolean;
var
  ch : string;
  x, e, i, somme : Integer;
  facteur : Integer;
begin
  while (length(s) mod 4 <> 0) do   begin
    s := '0' +s;
  end;
  ch := '';
  facteur := 1;
  somme := 0;
  for i := 1 to length(s) do begin
    ch := ch + s[i]   ;
    if (length(ch)=4) then begin
      Val(ch, x, e);
      somme := somme + (facteur * x) ;
      facteur := -facteur; 
      ch := '';
    end;
  end;
  som := somme;
  Trait := somme mod 137 = 0;
end;

BEGIN
  if Trait('2510792736157732104', som) then begin
    Writeln('la somme est ', som, ' est divisble par 137');
  end
  else Writeln('la somme est ', som, ' n''est pas divisible 137');
END.
