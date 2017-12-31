{Please install plugin BarCode before run this sample}
uses barCode;

var
  result : string;
begin
  writeln('Starting scan bar code....');
  result := scanBarCode;
  writeln('Scanned :' + result);
end.