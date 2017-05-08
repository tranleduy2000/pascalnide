uses aTone;
var
    phone: string;
    time: integer;
begin
    writeln('Enter your phone number (or 1234567890): ');
    readln(phone);

    time := 1000; {1000 milisecond}

    generateTones(phone, time);

    readln;
end.
