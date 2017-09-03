Uses
    aDialog;
Var
    title, hint, result: string;
Begin
    title := 'What''s your name?';
    hint := 'Enter here';

    result := dialogGetInput(title, hint, ''); //<==

    Writeln('hello' + result);
    Readln;
End.