{see test_get_location_data.pas, test_get_providers.pas}
uses
  aLocation, crt;

type
  map = java_util_Map;

  list = java_util_List;
var
  i : Integer;
  data : map;

procedure printData(data : map);
var
  providers : list;
  i : Integer;
  name : java_lang_Object;
begin
  //Returns available providers on the phone
  providers := locationProviders();
  for i := 0 to providers.size() - 1 do
  begin
    name := providers.get(i);
    writeln(providers, ' = ', data.get(name));
  end;
end;


begin
  startLocating(1000, 1);

  //wait for 60 second
  for i := 1 to 60 do
  begin
    delay(1000);
    data := readLocation();
    printData(data);
    clearDataLocation();
  end;

  //stop collect data
  stopLocating();
end.