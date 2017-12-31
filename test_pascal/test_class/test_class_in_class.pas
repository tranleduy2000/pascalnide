program exClass;
type
  Rectangle = class
  private
  type
    aclass = class
    public
      function get() : Integer;

    end;
  public
    simple : aclass;
  end;
var
  r1 : Rectangle;

function Rectangle.aclass.get() : Integer;
begin
  exit(1);
end;

begin
  r1 := Rectangle.create;
  r1.simple := Rectangle.aclass.create;
  writeln(r1.simple.get());
end.
