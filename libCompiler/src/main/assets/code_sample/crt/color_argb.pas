Program color_argb;
Uses Crt;

var
  generator : android_graphics_Color;
  a, color : integer;

Begin
  for a := 0 to 25 do
  begin
    color := generator.argb(a, 0, 0, 255);
    textBackground(color); //ARGB color
    write(' ');
  end;
  ReadLn;
End.
