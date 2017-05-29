program exString;
var
    greetings: string;
    name: packed array [1..10] of char;
    organisation: string[10];
    message: pchar;

begin
    greetings := 'Hello ';
    message := 'Good Day!';

    writeln('Please Enter your Name');
    readln(name);

    writeln('Please Enter the name of your Organisation');
    readln(organisation);

    writeln(greetings, name, ' from ', organisation);
    writeln(message);
end.