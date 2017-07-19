var
   i : integer;
begin
   for i in [1..20, 40..50] + [100..102]do
   begin
	write(i, ' ');
   end;
end.
