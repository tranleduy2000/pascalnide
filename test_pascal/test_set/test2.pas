program setColors;
type
    color = (red, blue, yellow, green, white, black, orange);
    colors = set of color;
var
    c : colors;

begin
    c := [red, blue] + [yellow, green];
//    c := [red, blue, yellow, green, white, black, orange] - [green, white];
//
//    c := [red, blue, yellow, green, white, black, orange] * [green, white];
//
//    c := [red, blue, yellow, green] >< [yellow, green, white, black];
end.