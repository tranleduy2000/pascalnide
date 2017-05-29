program test_set_assign_value;
type
    color = (red, blue, yellow, green, white, black, orange);
    colors = set of color;
var
    c : colors;

begin
    c := [red, blue, yellow, green, white, black, orange];
end.