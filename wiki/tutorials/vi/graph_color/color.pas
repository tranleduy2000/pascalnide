Program rect;
Uses Crt;
var
  colorFactory : android_graphics_Color;
  r, g, b : integer;

Begin
  for r := 0 to 25 do
    for g := 0 to 25 do
      for b := 0 to 25 do
      begin
        textBackground(colorFactory.rgb(r * 10, g * 10, b * 10)); //RGB color
        write(' ');
      end;
  ReadLn;
End.
