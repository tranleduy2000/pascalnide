Program change_background;
begin
  TextColor(15);
  WriteLn('This is written in with the default background color');
  delay(1000);
    TextBackground(green);
  WriteLn('This is written in with a Green background');
  delay(1000);
    TextBackground(brown);
  WriteLn('This is written in with a Brown background');
  delay(1000);
    TextBackground(black);
  WriteLn('Back with a black background');
  readln;
end.
