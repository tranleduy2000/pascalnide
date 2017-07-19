uses JavaCollections;

var
   list : JArrayList;
begin
   writeln('Initial size of list : ' + list.size());
   
   // add elements to the array list
   list.add('C');
   list.add('A');
   list.add('E');
   list.add('B');
   list.add('D');
   list.add('F');
   list.add(1, 'A2');
   writeln('Size of list after additions : ' + list.size());
   
   // display the array list
   writeln('Contents of list : ' + list);
   
   // Remove elements from the array list
   list.remove('F');
   list.remove(2);
   writeln('Size of list after deletions : ' + list.size());
   writeln('Contents of list : ' + list);
end.
{https://www.tutorialspoint.com/java/java_arraylist_class.htm}