begin
    writeln('a' < 'b');
    writeln('a' < 'ba');
    writeln('b' < 'ab');
    writeln(('a' < 'b') and ('b' < 'a'));
    writeln('a' <= 'b');
    writeln('c' >= 'ba');
    writeln('abc' <> 'bca');
    readln;
end.
