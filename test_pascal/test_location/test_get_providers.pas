uses
  aLocation;

var
  list_provider : java_util_List;
  i : Integer;
begin
  //Returns available providers on the phone
  list_provider := locationProviders();

  for i := 0 to list_provider.size() - 1 do
    writeln(list_provider.get(i));
end.