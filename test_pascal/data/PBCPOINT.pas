const
    maxx = 1002;
type
    Tnode = ^PNode;
    PNode = record
        x, y: integer;
        next: Tnode;
    end;

var
    front, rear: tnode;
    InQueue: array[-maxx..maxx, -maxx..maxx] of Boolean;
    freeR, freeC: array[-maxx..maxx] of Boolean;
    maxR, maxC, minR, minC: array[-maxx..maxx] of integer;

procedure push(x, y: integer);
var
    P: Tnode;
begin
    new(P);
    P^.x := x;
    P^.y := y;
    P^.next := nil;
    if front = nil then front := P else rear^.next := P;
    rear := P;
end;

function pop: tnode;
begin
    pop := front;
    front := front^.next;
end;

procedure process;
var
    x, y, i: integer;
    res: LongInt;
    point : Tnode;
begin
    res := 0;
    repeat
        point := pop;
        x := point^.x;
        y := point^.y;
        Dispose(point);
        inc(res);

        if (freeR[x]) then
        begin
            freeR[x] := false;
            minR[x] := y;
            maxR[x] := y;
        end
        else
        begin
            if y < minR[x] then
            begin
                for i := y + 1 to minR[x] - 1 do
                    if InQueue[x, i] then begin
                        push(x, i);
                        InQueue[x, i] := false;
                    end;
                minR[x] := y;
            end;

            if y > maxR[x] then
            begin
                for i := maxR[x] + 1 to y - 1 do
                    if InQueue[x, i] then
                    begin
                        push(x, i);
                        InQueue[x, i] := false;
                    end;
                maxR[x] := y;
            end;
        end;

        if (freeC[y])then
        begin
            freeC[y] := false;
            minC[y] := x;
            minC[y] := x;
        end
        else
        begin
            if x < minC[y] then
            begin
                for i := x + 1 to minC[y] - 1 do
                    if InQueue[i, y] then
                    begin
                        InQueue[i, y] := false;
                        push(i, y);
                    end;
                minC[y] := x;
            end;

            if x > maxC[y] then
            begin
                for i := maxC[y] + 1 to x - 1 do
                    if InQueue[i, y] then
                    begin
                        InQueue[i, y] := false;
                        push(i, y);
                    end;
                maxC[y] := x;
            end;
        end;
    until front = nil;
    writeln(res);
end;

procedure readf;
var
    f: text;
    x, y: integer;
    n, i: longint;
begin
    Assign(f, 'file.inp');
    Reset(f);

    Read(f, n);
    FillChar(freeC, SizeOf(freeC), true);
    freeR := freeC;
    FillChar(InQueue, SizeOf(InQueue), true);
    front := nil;

    for i := 1 to n do
    begin
        read(f, x, y);
        if InQueue[x, y] then
        begin
            push(x,y);
            InQueue[x, y] := false;
        end;
    end;
    close(f);
end;

begin
    readf;
    process();
end.