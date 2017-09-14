program exText;
var
  filename, data : string;
  myfile : text;

begin
  writeln('Enter the file name: ');
  readln(filename);

  assign(myfile, filename);
  rewrite(myfile);

  writeln(myfile, 'Note to Students: ');
  writeln(myfile, 'For details information on Pascal Programming');
  writeln(myfile, 'Contact: Tutorials Point');
  writeln('Completed writing');

  close(myfile);
end.