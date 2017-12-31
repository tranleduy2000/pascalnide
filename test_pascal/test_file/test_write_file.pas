program DataFiles;
type
  StudentRecord = Record
    s_name : String;
    s_addr : String;
    s_batchcode : String;
  end;

var
  Student : StudentRecord;
  f : text;

begin
  Assign(f, 'students.dat');
  Rewrite(f);
  Student.s_name := 'John Smith';
  Student.s_addr := 'United States of America';
  Student.s_batchcode := 'Computer Science';
  Write(f, Student);
  Close(f);
end.