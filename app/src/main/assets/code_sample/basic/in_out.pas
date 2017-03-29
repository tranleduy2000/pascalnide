{How to use "Readln" and "Write" procedure}
program test_io;
var a: integer;
	n: string;
begin
	write('> Enter your name: ');
	readln(n);
	write('Your name is ', n);
	
	writeln('> Enter your age: ');
	readln(a);
	writeln('Your age is ', a);
	//print multi variable to console
	writeln('Name: ', n, '; Age: ', a);
	readln;
end.

{if you want to improve this code, please send code to me
tranleduy1233@gmail.com}