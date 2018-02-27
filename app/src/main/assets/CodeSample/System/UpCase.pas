program UpCase;

{ Program to demonstrate the upcase function. }

var
  c : char;

begin
  for c:='a' to 'z' do
    write(upcase(c));
  Writeln;
end.