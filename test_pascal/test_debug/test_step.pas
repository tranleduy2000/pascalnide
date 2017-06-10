procedure b();
var
  h : LongInt = 1;
begin
  h := 2;
end;

procedure  a();
var
  ahh : LongInt = 1;
begin
  b(); //info
  b(); //over
  b(); //info
end;

begin
  a();
end.