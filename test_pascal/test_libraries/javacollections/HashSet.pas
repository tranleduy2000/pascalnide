uses JavaCollections;

var
   // create a hash set
   hs : JHashSet;
begin
   // add elements to the hash set
   hs.add('B');
   hs.add('A');
   hs.add('D');
   hs.add('E');
   hs.add('C');
   hs.add('F');
   writeln(hs);
end.
{https://www.tutorialspoint.com/java/java_hashset_class.htm}