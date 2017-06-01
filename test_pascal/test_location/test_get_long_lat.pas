{declare library aLocation}
uses
    aLocation, crt;

{declare type java}
type
    map = java_util_Map;{https://docs.oracle.com/javase/7/docs/api/java/util/Map.html}
    list = java_util_List; {https://docs.oracle.com/javase/7/docs/api/java/util/List.html}
    obj = java_lang_Object;
    loc = android_location_Location;
        {https://developer.android.com/reference/android/location/Location.html}
var
    i: Integer;
    data: map; {Location data will be saved here}

{This method will be print data to console,
include latitude, longitude,....}
procedure printData(data: map);
var
    providers: list;
    i: Integer;
    name: obj; //the name of provider
    info: loc; //store data of location
begin
    {Each provider has a set of criteria under
    which it may be used; for example, some providers
    require GPS hardware and visibility to a number
    of satellites; others require the use of the
    cellular radio, or access to a specific carrier's
    network, or to the internet.}

    {Returns available providers on the phone}
    providers := locationProviders();

    for i := 0 to providers.size() - 1 do
    begin
        name := providers.get(i); //get name of provider in position i

        if (data.get(name) <> null) then //if the data of provider is exist
        begin
            cast(info, data.get(name));

            //print long,lat to console
            writeln('getLatitude = ', info.getLatitude());
            writeln('getLongitude = ', info.getLongitude());
        end;
    end;
    writeln;
end;


begin
    {Starts collecting location data.
    The first parameter is minimum time between updates in milliseconds
    The second parameter is minimum distance between updates in meters}
    startLocating(100, 10);

    //wait for 60 second
    for i := 1 to 60 do
    begin
        delay(1000); //wait for collect info
        data := readLocation(); //read data
        printData(data); //print data to console
    end;

    //stop collect data
    stopLocating();
end.