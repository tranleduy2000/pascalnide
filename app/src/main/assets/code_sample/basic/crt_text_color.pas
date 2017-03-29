Program crt_text_color;
begin
  WriteLn('This is written in the default color');
  delay(1000);
    TextColor(red);
  WriteLn('This is written in Red');
  delay(1000);
    TextColor(white);
  WriteLn('This is written in White');
  delay(1000);
    TextColor(lightBlue);
  WriteLn('This is written in Light Blue');
  readln;
end.

{if you want to improve this code, please send code to me
tranleduy1233@gmail.com}