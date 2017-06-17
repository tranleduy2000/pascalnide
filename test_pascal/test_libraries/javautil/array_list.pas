uses JavaData;

var
   a1 : JArrayList;
begin
   writeln('Initial size of al : ' + al.size());
   
   // add elements to the array list
   al.add('C');
   al.add('A');
   al.add('E');
   al.add('B');
   al.add('D');
   al.add('F');
   al.add(1, 'A2');
   writeln('Size of al after additions : ' + al.size());
   
   // display the array list
   writeln('Contents of al : ' + al);
   
   // Remove elements from the array list
   al.remove('F');
   al.remove(2);
   writeln('Size of al after deletions : ' + al.size());
   writeln('Contents of al : ' + al);
end.
{https://www.tutorialspoint.com/java/java_arraylist_class.htm}