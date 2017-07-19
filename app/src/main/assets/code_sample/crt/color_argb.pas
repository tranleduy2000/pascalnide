Program color_argb;
Uses Crt;

var
   generator : android_graphics_Color;
   a, color : integer;

Begin
   for a := 0 to 255 do
   begin
      color := generator.argb(a, 0, 0, 255);
      textBackground(color); //ARGB color
      write(' ');
   end;
   for a := 0 to 255 do
   begin
      color := generator.argb(a, 0, 255, 0);
      textBackground(color); //ARGB color
      write(' ');
   end;
   for a := 0 to 255 do
   begin
      color := generator.argb(a, 255, 0, 0);
      textBackground(color); //ARGB color
      write(' ');
   end;
   ReadLn;
End.
