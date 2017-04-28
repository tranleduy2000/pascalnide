var
    fi, fo: text ;
    x, s: string;
begin
    assign(fi, 'vao.txt');
    assign(fo, 'ra.txt');
    reset(fi);
    rewrite(fo);
    while not eof(fi) do
    begin
        readln(f, s);
        x := s;
        writeln(g, x);
    end;
    close(fi);
    Close(fo);
end
.