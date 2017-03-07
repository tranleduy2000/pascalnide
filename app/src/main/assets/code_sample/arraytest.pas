program recordtest;

type  point = record x:integer; y:integer; end;
var
	simplearray: array[1..3] of integer;
	complexstaticarray: array[1..3,1..3] of integer;
	alternativedefinition: array[1..3] of array[1..3] of integer;
	arrayofstrings: array[-3..-1] of string;
	pointarray: array[6..9] of point;
	i,j: integer;
	p:point;
begin
 for i:=2 to 3 do begin
   simplearray[i]:=i*i;
 end;
 for i:=1 to 3 do 
 	writeln(simplearray[i]);
 for i:=2 to 3 do 
 	for j:=1 to 2 do 
 		complexstaticarray[i][j]:=i*j;
 writeln('complex array:');
 for i:=1 to 3 do for j:=1 to 3 do 
 	writeln(i,',',j,'=',complexstaticarray[i][j]);
 alternativedefinition[2]:=complexstaticarray[2];
 writeln('alternative array:');
 for i:=1 to 3 do for j:=1 to 3 do 
 	writeln(i,',',j,'=',alternativedefinition[i][j]);
 arrayofstrings[-2]:='hello there';
 writeln(arrayofstrings[-3],arrayofstrings[-2]);
 
 p.x:=5;
 p.y:=10;
 pointarray[6]:=p;
 pointarray[7]:=p;
 pointarray[7].x:=9;
 p.y:=18;
 for i:=6 to 9 do
 	writeln(pointarray[i].x,',',pointarray[i].y);
 writeln(p.x,',',p.y);
end. 