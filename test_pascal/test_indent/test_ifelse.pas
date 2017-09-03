program test_ifelse;
begin
  a := 100;
  if(a < 20) then
    writeln('a is less than 20')
  else
    writeln('a is not less than 20');
  writeln('value of a is: ', a);
end.