Program Example11;
{ Program to demonstrate the Copy function. }
var
   arr : array[0..4] of integer = (1, 2, 3, 4, 5);
   dyn : array of Integer;
begin
   dyn := copy(arr, 0, 3);
   WriteLn(dyn);
end.