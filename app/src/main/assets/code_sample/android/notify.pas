{Declare library}
uses aNotify;
begin
    writeln('Look! Status bar has a new notify');
    notify('This is a title', 'This is a message');
    readln;
end.