Program crt_text_color;
begin
  WriteLn('This is written in the default color');
  delay(1000);
  TextColor(4);
  WriteLn('This is written in Red');
  delay(1000);
  TextColor(15);
  WriteLn('This is written in White');
  delay(1000);
  TextColor(9);
  WriteLn('This is written in Light Blue');
end.
