uses JavaCollections;

var
  list : JLinkedList;
begin
  // add elements to the linked list
  list.add('F');
  list.add('B');
  list.add('D');
  list.add('E');
  list.add('C');
  list.addLast('Z');
  list.addFirst('A');
  list.add(1, 'A2');
  writeln('Original contents of list: ' + list);

  // remove elements from the linked list
  list.remove('F');
  list.remove(2);
  writeln('Contents of list after deletion: ' + list);

  // remove first and last elements
  list.removeFirst();
  list.removeLast();
  writeln('list after deleting first and last: ' + list);

end.

{See https://www.tutorialspoint.com/java/java_linkedlist_class.htm}