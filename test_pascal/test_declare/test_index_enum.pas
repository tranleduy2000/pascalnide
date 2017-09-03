program setColors;
type
  color = (red, blue, yellow, green, white, black, orange);

  colors = set of color;
var
  names : array [color] of String[7] = ('red', 'blue', 'yellow', 'green', 'white', 'black', 'orange');
begin
  names[blue] := 'read';
end.