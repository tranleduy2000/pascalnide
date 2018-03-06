program exAppendfile;
var
  myfile : text;
  info : string;

begin
  assign(myfile, 'contact.txt');
  append(myfile);

  writeln(myfile, 'Contact Details');
  writeln(myfile, 'webmaster@tutorialspoint.com');
  close(myfile);

end.