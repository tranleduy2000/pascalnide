var
  s : string;
  i : Integer;
begin
  s := 'Hello';
  for i  := 1 to Length(s) do s[i] := Upcase(s[i]);
  writeln(s);
end.