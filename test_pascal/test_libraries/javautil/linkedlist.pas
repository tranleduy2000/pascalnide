uses JavaData;

var
   ll : JLinkedList;
begin
   // add elements to the linked list
   ll.add('F');
   ll.add('B');
   ll.add('D');
   ll.add('E');
   ll.add('C');
   ll.addLast('Z');
   ll.addFirst('A');
   ll.add(1, 'A2');
   writeln('Original contents of ll: ' + ll);
   
   // remove elements from the linked list
   ll.remove('F');
   ll.remove(2);
   writeln('Contents of ll after deletion: ' + ll);
   
   // remove first and last elements
   ll.removeFirst();
   ll.removeLast();
   writeln('ll after deleting first and last: ' + ll);

end.

{See https://www.tutorialspoint.com/java/java_linkedlist_class.htm}