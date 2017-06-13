program checkCase;
var
    grade: char;

begin
    grade := 'F';
    case (grade) of
    'A' : writeln('Excellent!' );
    'B', 'C': writeln('Well done' );
    'D' : writeln('You passed' );

    else
        writeln('You really did not study right!' );
    end;

    writeln('Your grade is ', grade );
end.