uses JavaCollections;

var
    // Create a linked hash map
  linkedHashMap : JLinkedHashMap;
  entrySet : JSet;
  i : JIterator;
  balance : Double;
  me : JEntry;
begin
  // Put elements to the map
  linkedHashMap.put('Zara', (3434.34));
  linkedHashMap.put('Mahnaz', (123.22));
  linkedHashMap.put('Ayan', (1378.00));
  linkedHashMap.put('Daisy', (99.22));
  linkedHashMap.put('Qadir', (-19.08));

  // Get a set of the entries
  entrySet := linkedHashMap.entrySet();

  // Get an iterator
  i := entrySet.iterator();

  // Display elements
  while(i.hasNext()) do
  begin
    me := i.next();
    write(me.getKey() + ': ');
    writeln(me.getValue());
  end;
  writeln();

  // Deposit 1000 into Zara's account
  cast(balance, linkedHashMap.get('Zara'));
  linkedHashMap.put('Zara', (balance + 1000));

  writeln(linkedHashMap.get('Zara'));
end.

{See https://www.tutorialspoint.com/java/java_linkedlist_class.htm}