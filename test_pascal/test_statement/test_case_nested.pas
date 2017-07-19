program checknestedCase;
var
    a, b: integer;
begin
    a := 100;
    b := 200;

    case (a) of
    100: begin
        writeln('This is part of outer statement' );
        case (b) of
        200: writeln('This is part of inner statement' );
        end;
    end;
    end;

    writeln('Exact value of a is: ', a );
    writeln('Exact value of b is: ', b );
end.