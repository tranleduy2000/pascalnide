uses aTone;
var
    phone: string;
    delayTime: integer;
begin
    writeln('Enter your phone number (or 1234567890): ');
    readln(phone);

    delayTime := 1000; {100 milisecond}

    generateTones(phone,delayTime );

    readln;
end.
