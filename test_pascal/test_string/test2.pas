program exString;
var
  greetings : string;
  name : string;
  organisation : string[10];
  message : string;

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