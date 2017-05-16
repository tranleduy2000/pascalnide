uses
    aDialog;
var
    result: org_json_JSONObject;
    year, month, day: Integer;
begin
    //yyyy/mm/dd
    result := dialogGetDate(2017, 7, 12);

    year := result.getInt('year');
    month := result.getInt('month');
    day := result.getInt('day');

    writeln('date = ', day, '/' , month,'/', year);
end.


