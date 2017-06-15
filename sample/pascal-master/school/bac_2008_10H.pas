program Bac08;
Uses crt;
var
  f, f1 : text;

procedure Saisie(var f : text);
var
  s : string;
  i, n : Integer;
begin
  Rewrite(f) ;
  Readln(n);
  for i := 1 to n do begin
    readln(s) ;
    writeln(f, s);
  end;
end;

procedure Traitement(var f, f1 : text);

  function ch_Cor(s : string):string;
  begin
    while (Pos('  ', s)<>0)   do begin
      Delete(s, Pos('  ', s), 1);
    end;
    if (s[1]=' ') then Delete(s, 1, 1);
    if (s[length(s)]=' ') then Delete(s, length(s), 1);
    if (s[length(s)]<>'.') then s := s+'.';
    ch_Cor := s;
  end;

var
  s, chaine_corrigee : String;
begin
  reset(f) ;
  Rewrite(f1);
  while (not(EOF(f))) do begin
    readln(f, s);
    chaine_corrigee := ch_Cor(s);
    Writeln(chaine_corrigee);
    writeln(f1, chaine_corrigee);
  end;
end;

BEGIN

  assign(f, 'phrases.txt');
  assign(f1, 'phr_cor.txt');
  Saisie(f);
  Traitement(f, f1);
  close(f);
  close(f1);
END.
