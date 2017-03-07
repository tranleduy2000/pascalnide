Program change_background;
begin
  TextColor(15);
  WriteLn('This is written in with the default background color');
  delay(1000);
  TextBackground(2);
  WriteLn('This is written in with a Green background');
  delay(1000);
  TextBackground(6);
  WriteLn('This is written in with a Brown background');
  delay(1000);
  TextBackground(0);
  WriteLn('Back with a black background');
end.
