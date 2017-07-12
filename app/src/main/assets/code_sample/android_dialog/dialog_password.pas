Uses
    aDialog;
Var
    title, hint, result: string;
Begin
    title := 'What is your password?';
    hint := 'enter here';

    result := dialogGetPassword(title, hint);

    Writeln('Your password is', result);
    Readln;
End.