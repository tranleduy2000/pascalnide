
type
  tab = array ['A'..'Z'] of Byte;
var
  s : string;

procedure CountSort(var s : string);
var
  t : tab;
  i : char;
  j : Byte;
  k : String;
begin
 for i := 'A' to 'Z' do begin 
    t[i] := 0;
 end; 
  
 for j := 1 to length(s) do begin
   t[Upcase(s[j])] += 1;
 end;

  {accumlate the array}
 for i := 'B' to 'Z' do begin 
    t[i] += t[chr(ord(i)-1)];
 end; 

 for j := 1 to length(s) do begin
    t[Upcase(s[j])] -= 1;
    k[t[Upcase(s[j])]] := s[j];
 end;
 s := k;
end;

procedure afficher(t : string; n : Integer);
var
  i : Integer;
begin
  for i:=1 to n do begin
    Write(t[i], ' ');
  end;
  Writeln;
end;

BEGIN
  s := 'BCDADDALL';
  CountSort(s);
  Writeln(s);
END.

