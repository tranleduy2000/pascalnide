Program Graphika12;
Uses Graph;

Procedure Graphinterface;
Var
    gd, gm, error: Integer;
    s: String;
Begin
    gd := detect;
    s := ' ';
    Initgraph(gd,gm,s);
    error := GraphResult;
    if error <> GrOk then
    begin
        writeln(GraphErrorMsg(Error));
        Halt(error)
    end
end;

begin
    Graphinterface;
    setcolor (5);
    setlinestyle(2,0,3);
    setfillstyle (6,7);
    bar3d (100,100,300,500,50,topoff);
    readln;
    closegraph;
end.


