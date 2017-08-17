var
  fi, fo : text;
  data : string;

begin
  //write data to file 'file.inp'
  Assign(fo, 'file.out');
  Rewrite(fo);
  Writeln(fo, 'test data');
  Close(fo);

  Assign(fi, 'file.inp');
  Reset(fi);
  ReadLn(fi, data);
  WriteLn(data);
  close(fi);
end.