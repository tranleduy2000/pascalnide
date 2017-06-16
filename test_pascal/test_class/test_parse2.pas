{$mode objfpc} // directive to be used for defining classes
{$m+}         // directive to be used for using constructor

program exClass;
type
   Rectangle = class
   private
      length, width : integer;
   
   public
      constructor create(l, w : integer);
      
      function getlength(x, y, z : integer) : integer;
      
      function getwidth() : integer;
   
   end;
var
   r1 : Rectangle;

constructor Rectangle.create(l, w : integer);
begin
   length := l;
   width := w;
end;

function Rectangle.getlength(x, y, z : Integer) : integer;
begin
   getlength := length;
end;

function Rectangle.getwidth() : integer;
begin
   getwidth := width;
end;


begin
   writeln(' Draw Rectangle: ', r1.getlength(1, 2, 3), ' by ', r1.getwidth());
end.