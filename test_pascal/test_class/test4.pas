{$mode objfpc} // directive to be used for defining classes
{$m+}         // directive to be used for using constructor

program exClass;
type
   Rectangle = class
   private
      zrandom : integer;
   
   public
      function setRandom() : integer;
      
      function getRandom() : integer;
   
   end;
var
   r1, r2 : Rectangle;

function Rectangle.setRandom() : integer;
begin
   zrandom := random(100);
end;

function Rectangle.getRandom() : integer;
begin
   getRandom := zrandom;
end;

begin
   r1 = Rectangle.create;
   r1.zrandom := 3;
   r2.zrandom := 4;
   writeln(' Draw Rectangle:1 ', ' by ', r1.zrandom);
   writeln(' Draw Rectangle: 2', ' by ', r2.zrandom);
end.