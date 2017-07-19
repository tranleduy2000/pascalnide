program checkCase;
var
    grade: char;
begin
    grade := 'A';

    case (grade) of
    'A' : writeln('Excellent!' );
    'B', 'C': writeln('Well done' );
    'D' : writeln('You passed' );
    'F' : writeln('Better try again' );
    end;

    writeln('Your grade is ', grade );
end.