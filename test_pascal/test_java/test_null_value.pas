var
  obj : java_lang_Object;
begin
  New(obj);
  while obj <> null do
  begin
    WriteLn('not null');
    obj := null;
  end;
end.