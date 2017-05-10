var
    list: java.util.ArrayList;
    i: integer;
begin
    list := newInstance(list);
    for i := 1 to 10 do
        list.add(i);
    for i := 1 to 10 do
        writeln(list.get(i));

    ReadLn();
end.
