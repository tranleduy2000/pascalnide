program nested_ifelseChecking;
var
        { local variable declaration }
    a, b : integer;

begin
    a := 100;
    b := 200;

    (* check the boolean condition *)
    if (a = 100) then
            (* if condition is true then check the following *)
        if ( b = 200 ) then
                (* if nested if condition is true  then print the following *)
            writeln('Value of a is 100 and value of b is 200' )
        else if a = 2 then writeln;

    writeln('Exact value of a is: ', a );
    writeln('Exact value of b is: ', b );
end.