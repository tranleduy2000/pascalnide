program repeatUntilLoop;
var
  a : integer;

begin
  a := 10;
  (* repeat until loop execution *)
  repeat
    writeln('value of a: ', a);
    a := a + 1
  until a = 20;
end.