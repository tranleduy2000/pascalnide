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
   r1.setRandom();
   r2.setRandom();
   writeln(' Draw Rectangle:1 ', r1.getRandom());
   writeln(' Draw Rectangle:2 ', r2.getRandom());
end.