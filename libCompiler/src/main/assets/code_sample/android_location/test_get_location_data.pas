uses
    aLocation, crt;
var
    i: Integer;
    data: java_util_Map;
begin
    startLocating(1000, 1);

    //wait for 5 second
    for i := 1 to 60 do
    begin
        delay(1000);
        data := readLocation();
        writeln(data);
        clearDataLocation();
    end;

    //stop collect data
    stopLocating();
end.