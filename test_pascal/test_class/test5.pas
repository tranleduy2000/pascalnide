{$mode objfpc} // directive to be used for defining classes
{$m+}         // directive to be used for using constructor

program exClass;
type
   Rectangle = class
   private
      length, width : integer;
   
   public
      procedure create(l, w : integer);
      
      procedure setlength(l : integer);
      
      function getlength() : integer;
      
      procedure setwidth(w : integer);
      
      function getwidth() : integer;
      
      procedure draw;
   
   end;
var
   r1 : Rectangle;

procedure Rectangle.create(l, w : integer);
begin
   length := l;
   width := w;
end;

procedure Rectangle.setlength(l : integer);
begin
   length := l;
end;

procedure Rectangle.setwidth(w : integer);
begin
   width := w;
end;

function Rectangle.getlength() : integer;
begin
   getlength := length;
end;

function Rectangle.getwidth() : integer;
begin
   getwidth := width;
end;

procedure Rectangle.draw;
var
   i, j : integer;
begin
   for i:= 1 to length do
   begin
      for j:= 1 to width do
         write(' * ');
      writeln;
   end;
end;

begin
   r1 := Rectangle.create(3, 7);
   
   writeln(' Draw Rectangle: ', r1.getlength(), ' by ', r1.getwidth());
   r1.draw;
   r1.setlength(4);
   r1.setwidth(6);
   
   writeln(' Draw Rectangle: ', r1.getlength(), ' by ', r1.getwidth());
   r1.draw;
end.