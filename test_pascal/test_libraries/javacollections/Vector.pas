{https://www.tutorialspoint.com/java/java_vector_class.htm}
uses JavaCollections;

var
   v : JVector;
   vEnum : JEnumeration;
begin
   // initial size is 3, increment is 2
   new(v, 3, 2);
   
   writeln('Initial size : ' + v.size());
   writeln('Initial capacity : ' + v.capacity());
   
   v.addElement((1));
   v.addElement((2));
   v.addElement((3));
   v.addElement((4));
   writeln('Capacity after four additions : ' + v.capacity());
   
   v.addElement((5.45));
   writeln('Current capacity : ' + v.capacity());
   
   v.addElement((6.08));
   v.addElement((7));
   writeln('Current capacity : ' + v.capacity());
   
   v.addElement((9.4));
   v.addElement((10));
   writeln('Current capacity : ' + v.capacity());
   
   v.addElement((11));
   v.addElement((12));
   writeln('First element : ' + v.firstElement());
   writeln('Last element : ' + v.lastElement());
   
   if(v.contains((3))) then
      writeln('Vector contains 3.');
   
   // enumerate the elements in the vector.
   vEnum := v.elements();
   writeln('Elements in vector : ');
   
   while(vEnum.hasMoreElements()) do
      write(vEnum.nextElement() + ' ');
   writeln();
end.
