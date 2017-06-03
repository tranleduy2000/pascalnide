program DataFiles;
type
    StudentRecord = Record
        s_name: String;
        s_addr: String;
        s_batchcode: String;
    end;

var
    Student: StudentRecord;
    f: text;

begin
    assign(f, 'students.dat');
    reset(f);
    while not eof(f) do
    begin
        read(f,Student);
        writeln('Name: ',Student.s_name);
        writeln('Address: ',Student.s_addr);
        writeln('Batch Code: ', Student.s_batchcode);
    end;
    close(f);
end.