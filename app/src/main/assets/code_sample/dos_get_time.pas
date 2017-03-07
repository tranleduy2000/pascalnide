program test_time;
var
  a,b,c,d : integer;

	function print(w:integer):string;
	var
	  s : string;
	begin
	  Str(w,s);
	  if w<10 then
	   result :='0'+s
	  else
	   result := s;
	end;

begin
  GetTime(a,b,c,d);
  WriteLn('Current time');
  WriteLn(print(a),':',print(b),':',print(c));
end.
