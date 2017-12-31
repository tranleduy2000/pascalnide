var
  fi, fo : text;
  data : string;

begin
  //write data to file 'file.inp'
  Assign(fo, 'file.inp');
  Rewrite(fo);
  Writeln(fo, 'test data');
  Close(fo);

  //read data from file 'file.inp'
  Assign(fi, 'file.inp');
  Reset(fi);
  ReadLn(fi, data);
  close(fi);

  //write to screen
  WriteLn(data);
end.