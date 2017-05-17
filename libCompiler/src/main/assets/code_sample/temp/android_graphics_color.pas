{See document in https://developer.android.com/reference/android/graphics/Color.html}
var
    c: android_graphics_Color;
        {android.graphics.Color}
    color: integer;
begin
    color := 1001;
    writeln('red = ', c.red(color));
    writeln('green = ', c.green(color));
    writeln('blue = ', c.blue(color));
    readln;
end.
