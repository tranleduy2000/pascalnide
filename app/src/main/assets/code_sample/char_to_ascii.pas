program test_ord_chr;
var c: char;
	asc: integer;
begin
	asc := ord('a');
	writeln('a in ascii is ', asc);
	c := chr(87);
	writeln('ascii 87 is character ', c);
	readln;
end.