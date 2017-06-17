{https://www.tutorialspoint.com/java/java_hashmap_class.htm}
uses JavaData;

var
   // Create a hash map
   hs : JHashMap;
begin
   
   
   // Put elements to the map
   hm.put('Zara', (3434.34));
   hm.put('Mahnaz', (123.22));
   hm.put('Ayan', (1378.00));
   hm.put('Daisy', (99.22));
   hm.put('Qadir', (-19.08));
   
   // Get a set of the entries
   Set set = hm.entrySet();
   
   // Get an iterator
   Iterator i = set.iterator();
   // Display elements
   while(i.hasNext()) do
   begin
      Map.Entry me = (Map.Entry)i.next();
      write(me.getKey() + " : ");
      writeln(me.getValue());
   end;
   writeln();
   
   // Deposit 1000 into Zara's account
   double balance = ((Double)hm.get('Zara')).doubleValue();
   hm.put('Zara', (balance + 1000));
   writeln('Zara's new balance : ' + hm.get('Zara'));
end.
