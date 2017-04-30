program Example72;

{ Program to demonstrate the upcase function. }

var
    c: char;

begin
    for c:='a' to 'z' do
        write(upcase(c));
    Writeln;
    { This doesn't work in TP, but it does in Free Pascal }
    Writeln(upcase('abcdefghijklmnopqrstuvwxyz'));
end.