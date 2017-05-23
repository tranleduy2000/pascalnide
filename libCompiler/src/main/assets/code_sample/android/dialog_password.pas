Uses
    aDialog;
Var
    title, hint, result: string;
function a: integer;

function b(): integer ;
begin
    c();
end;
begin
end;
Begin
    title := 'What is your password?';
    hint := 'enter here';

    result := dialogGetPassword(title, hint);

    Writeln('Your password is', result);
    Readln;
End.