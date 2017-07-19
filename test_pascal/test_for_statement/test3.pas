var
    a: array[0..2] of Integer = (1, 2, 2);
    i: Integer;
begin
    for i := a[1] to a[2] + 1 do WriteLn(i);
end.