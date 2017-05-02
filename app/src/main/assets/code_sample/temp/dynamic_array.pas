type
    AI = array   of   Integer ;
var
    a : AI ; i : Integer ;
BEGIN
    SetLength(a , 10) ;
    for i  :=  Low(a) to High(a) do a [ i ] := i;
    for i  :=  Low(a) to High(a) do
        Write('a[' , i , ']' , '=' , a [ i ], ', ') ;
    WriteLn;
    SetLength(a , 5) ;
    for i  :=  Low(a) to High(a) do
        Write('a[' , i , ']' , '=' , a [ i ], ', ') ;
    WriteLn;
    SetLength(a , 10) ;
    for i  :=  Low(a) to High(a) do
        Write('a[' , i , ']' , '=' , a [ i ], ', ') ;
    WriteLn;
END.