var
  s1 : string[10] = 'asdasdasdhasd';
  s2 : string[1] = 'sadasd';
begin
  s1 := s2;
  writeln(s1);
  s1 := 'sadasdasdasdasd';
  writeln(s1);
  s2 := s1;
  writeln(s2);
end.
