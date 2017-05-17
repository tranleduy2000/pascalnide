uses
    aDialog;
var
    result: org_json_JSONObject;
    minute: Integer;
    hour: Integer;
begin
    //hh/mm 24h
    result := dialogGetTime(10, 20, true);

    hour := result.getInt('hour');
    minute := result.getInt('minute');

    writeln('time = ', hour, ':' , minute);
end.


