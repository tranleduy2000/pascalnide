program nested_function;

function fibonacci(n:integer) :integer;
var tmp:integer;

    function calculate(i: integer): integer;
    begin
        if (i = 1) then
            result := 1
        else
            result := calculate(i-1) + calculate(i-2);
    end;

begin
	result:=calculate(n);
end;

begin
  writeln('fibonacci 4th = ', fibonacci(4));
end.
