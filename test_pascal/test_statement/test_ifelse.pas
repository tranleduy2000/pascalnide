program test_ifelse;
var
        { local variable declaration }
    a : integer;

begin
    a := 100;
    (* check the boolean condition *)
    if( a < 20 ) then
            (* if condition is true then print the following *)
        writeln('a is less than 20' )

    else
            (* if condition is false then print the following *)
        writeln('a is not less than 20' );
    writeln('value of a is: ', a);
end.