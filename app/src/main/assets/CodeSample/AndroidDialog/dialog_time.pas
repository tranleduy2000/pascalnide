Uses
  aDialog;

Var
  result : org_json_JSONObject;
  Minute : Integer;
  Hour : Integer;
Begin
  //  hh/mm 24h
  result := dialogGetTime(10, 20, true);

  Hour := result.getInt('hour');
  Minute := result.getInt('minute');

  Writeln('time =', hour, ':', minute);
End.