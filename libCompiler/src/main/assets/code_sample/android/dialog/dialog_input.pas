uses
    aDialog;
begin
    title := 'This is title';
    hint := 'Please enter text';

    {Show dialog, wait for user input data ... and return the text}
    result := dialogGetDate(title,hint, '');

    writeln('result = ', result);

end.


