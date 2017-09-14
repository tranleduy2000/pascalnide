Type
  TWeekDay = (monday, tuesday, wednesday, thursday,
               friday, saturday, sunday);

Var
  Week : set of TWeekDay
    = [monday, tuesday, wednesday, thursday, friday];
  d : TWeekday;

begin
  for d in Week do
    writeln(d);
end.