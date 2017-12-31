var
  f : text;
  a : Integer;
begin
  Assign(f, 'file_test1.inp');
  Reset(f);
  read(f, a);
  writeln(a);
  Close(f);
end.