program ifelse_ifelseChecking;
var
        { local variable declaration }
    a : integer;

begin
    a := 100;
    (* check the boolean condition *)
    if (a = 10)  then
            (* if condition is true then print the following *)
        writeln('Value of a is 10' )

    else if ( a = 20 ) then
            (* if else if condition is true *)
        writeln('Value of a is 20' )

    else if( a = 30 ) then
            (* if else if condition is true  *)
        writeln('Value of a is 30' )

    else
            (* if none of the conditions is true *)
        writeln('None of the values is matching' );
    writeln('Exact value of a is: ', a );
end.