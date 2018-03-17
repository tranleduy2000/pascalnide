var
  arr : array['a'..'z'] of string;
  c : Char;
begin
  arr['a'] := 'Hello';
  for c := 'a' to 'z' do Write(arr[c]);
end.