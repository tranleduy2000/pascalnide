program exAppendfile;
var
  myfile : text;
  info : string;

begin
  assign(myfile, 'contact.txt');
  append(myfile);

  writeln('Contact Details');
  writeln('webmaster@tutorialspoint.com');
  close(myfile);

end.