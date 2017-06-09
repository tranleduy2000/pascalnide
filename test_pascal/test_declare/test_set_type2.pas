program setColors;
type
    color = (red, blue, yellow, green, white, black, orange);
    colors = set of color;

procedure displayColors(c : colors);
const
    names : array [color] of String[7]
    = ('red', 'blue', 'yellow', 'green', 'white', 'black', 'orange');
var
    cl : color;
    s : String;

begin
    s := ' ';
    for cl:=red to orange do
        if cl in c then
        begin
            if (s <> ' ') then s := s + ' , ';
            s := s + names[cl];
        end;
    writeln('[',s,']');
end;

var
    c : colors;

begin

end.