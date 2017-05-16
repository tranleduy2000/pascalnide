uses
    aSensor, crt;
var
    x, y, z: real;
begin
    clrscr;
    writeln('Press any key to stop program');
    {1 = All,
    2 = Accelerometer,
    3 = Magnetometer,
    4 = Light,
    5 = Pressure,
    6 = Gravity,
    7 = Humidity,
    8 = Temperature}

    {Start sensor monitor, Accelerometer}
    startSensor(2);
    while not keypressed do
    begin
        {Get x, y, z value}
        x := getXAccelerometer;
        y := getYAccelerometer;
        z := getZAccelerometer;

        {Print to console}
        writeln('  x = ', x:3:3);
        writeln('  y = ', y:3:3);
        writeln('  z = ', z:3:3);
        {Clear screen}
        delay(100);
        clrscr;
    end;
    stopSensor;
end.

{For more information of Accelerometer sensor,
 see wiki https://en.wikipedia.org/wiki/Accelerometer
or search with keyword "Acceleromeer sensor"
If you want to improve this sample, please send code to me
Tranleduy1233@gmail.com}