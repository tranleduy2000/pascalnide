program test_exit;

procedure func;
begin
    WriteLn('After exit');
    exit;
    WriteLn('Before exit');
end;

begin
    func;
end.

{if you want to improve this code, please send code to me
tranleduy1233@gmail.com}
