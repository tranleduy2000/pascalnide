program exBoolean;
var
    v_exit: boolean;

    choice: char;
begin
    writeln('Do you want to continue? ');
    writeln('Enter Y/y for yes, and N/n for no');
    readln(choice);

    if(choice = 'n') or (choice = 'N') then
        v_exit := true
    else
        v_exit := false;

    if (v_exit) then
        writeln('Good Bye!')
    else
        writeln('Please Continue');

    readln;
end.