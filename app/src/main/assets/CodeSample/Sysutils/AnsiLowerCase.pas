Program Example53;

{ This program demonstrates the AnsiLowerCase function }

Uses sysutils;

Procedure Testit (S : String);

begin
    Writeln (S,' -> ',AnsiLowerCase(S))
end;

Begin
    Testit('AN UPPERCASE STRING');
    Testit('Some mixed STring');
    Testit('a lowercase string');
End.
{http://www.freepascal.org/docs-html-3.0.0/rtl/sysutils/ansilowercase.html}