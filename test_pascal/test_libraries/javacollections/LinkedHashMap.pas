uses JavaCollections;

var
   // Create a linked hash map
   listHashMap : JLinkedHashMap;
   entrySet : JEntrySet;
   i : JIterator;
   balance : Double;
begin
   // Put elements to the map
   listHashMap.put('Zara', (3434.34));
   listHashMap.put('Mahnaz', (123.22));
   listHashMap.put('Ayan', (1378.00));
   listHashMap.put('Daisy', (99.22));
   listHashMap.put('Qadir', (-19.08));
   
   // Get a set of the entries
   entrySet = listHashMap.entrySet();
   
   // Get an iterator
   i = entrySet.iterator();
   
   // Display elements
   while(i.hasNext()) do
   begin
      me = (Map.Entry)i.next();
      write(me.getKey() + ': ');
      writeln(me.getValue());
   end;
   writeln();
   
   // Deposit 1000 into Zara's account
   cast(balance, listHashMap.get('Zara'));
   listHashMap.put('Zara', (balance + 1000));
   
   writeln(listHashMap.get('Zara'));
end.

{See https://www.tutorialspoint.com/java/java_linkedlist_class.htm}