program Greetings;
const
    message = ' Welcome to the world of Pascal ';

type
    name = string;
var
    firstname, surname: name;

begin
    writeln('Please enter your first name: ');
    readln(firstname);

    writeln('Please enter your surname: ');
    readln(surname);

    writeln;
    writeln(message, ' ', firstname, ' ', surname);
end.