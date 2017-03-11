program test_io;
var a: integer;
	n: string;
begin
	write('enter your filePath: ');
	readln(n);
	write('your filePath is ', n);
	
	write('enter your age: ');
	readln(a);
	write('your age is ', a);

	writeln;
	//print multi variable to console
	writeln('filePath: ', n, ' age: ', a);
end.