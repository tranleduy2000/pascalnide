uses Graph;

var
    Gd, Gm : Integer;
    P      : Pointer;
    Size   : Word;

begin
    Gd := Detect;
    InitGraph(Gd, Gm, 'X:\BP');
    if GraphResult <> grOk then
        Halt(1);
    SetFillStyle(XHatchFill, Cyan);
    Bar(0, 0, GetMaxX, GetMaxY);
    Size := ImageSize(10, 20, 30, 40);
    GetMem(P, Size);
    GetImage(10, 20, 30, 40, P^);
    ReadLn;
    ClearDevice;
    PutImage(100, 100, P^, NormalPut);
    ReadLn;
    CloseGraph;
end.
{http://pascal.net.ru/GetImage}