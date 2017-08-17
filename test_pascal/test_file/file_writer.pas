var
  f : text;
  i : Integer;
begin
  //open file for ouput
  assign(f, 'file.out');
  rewrite(f);

  //write data
  for i := 1 to 10 do
  begin
    write(f, i, ' ');
    writeln(f);
  end;

  //close file
  close(f);
end.