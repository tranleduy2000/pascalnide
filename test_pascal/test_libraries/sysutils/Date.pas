Program Example1;

{ This program demonstrates the Date function }

uses sysutils;

Var
  YY, MM, DD : Word;

Begin
  Writeln ('Date : ', Date);
  DeCodeDate (Date, YY, MM, DD);
  writeln(yy, ' ', mm, ' ', DD);
  Writeln (format ('Date is (DD/MM/YY): %d/%d/%d ', [dd, mm, yy]));
End.