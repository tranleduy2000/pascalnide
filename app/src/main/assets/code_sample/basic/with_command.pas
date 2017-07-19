type
    student = record
        name : string;
        address : string;
        grade : integer;
    end;

var
    s : student;

function getInfo( s: student): string;
begin
    exit('name: ' + s.name + '; address: ' + s.address);
end;

begin
    with s do
    begin
        name := 'John';
        address := 'main street';
        grade := 20;
    end;

    writeln(getInfo(s));

    s.name := 'John';
    s.address := 'main street';
    s.grade := 20;

    writeln(getInfo(s));
end.
