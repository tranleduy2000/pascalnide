var
  f : text;
  i : Integer;
begin
  //open file for ouput
  assign(f, 'file.out');
  rewrite(f);

  //write data
  for i := 1 to 1000 do
  begin
    write(f, random(i), ' ');
    writeln(f);
  end;

  //close file
  close(f);
end.