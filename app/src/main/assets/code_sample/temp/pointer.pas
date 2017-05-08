program PointerArray;
var
    i : Longint;
    p : ^Longint;
    pp : array[0..100] of Longint;
begin
    for i := 0 to 100 do pp[i] := i; { Fill array }
    p := @pp[0];                     { Let p point to pp }
    for i := 0 to 100 do
        if p[i] <> pp[i] then
            WriteLn ('Ohoh, problem !')
end.