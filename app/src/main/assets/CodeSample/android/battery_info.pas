uses aBattery, crt;
var
    status, health, level: Integer;
    plugType: Integer;
    tech: string;
    vol: Integer;
    temp: Integer;

procedure printStatus(i: Integer);
begin
    {Returns the most recently received battery status data:
    1 - unknown;
    2 - charging;
    3 - discharging;
    4 - not charging;
    5 - full;}
    write('Battery status: ');
    case i of
    1: writeln('unknown');
    2: writeln('charging');
    3: writeln('discharging');
    4: writeln('not charging');
    5: writeln('full');
    end;
end;

procedure printHealth(i: integer);
begin
    {Returns the most recently received battery health data:
    1 - unknown;
    2 - good;
    3 - overheat;
    4 - dead;
    5 - over voltage;
    6 - unspecified failure;}
    case i of
    1: writeln('unknown');
    2: writeln('good');
    3: writeln('overheat');
    4: writeln('not dead');
    5: writeln('over voltage');
    6: writeln('unspecified failure');
    end;
end;

procedure printPlugType(i: Integer);
begin
    {Returns the most recently received plug type data:
    -1 - unknown
    0 - unplugged;
    1 - power items is an AC charger
    2 - power items is a USB port
    }
    case i of
    -1: writeln('unknown');
    0: writeln('unplugged');
    1: writeln('power source is an AC charger');
    2: writeln('power source is a USB port');
    end;
end;

procedure printLevel(i: Integer);
begin
    writeln('battery level (percentage): ', i, ' %');
end;

procedure printTechnology(tech: string);
begin
    writeln('Battery technology data: ', tech);
end;

procedure printVoltage(vol: Integer);
begin
    writeln('Voltage: ', vol);
end;

procedure printTemp(temp: Integer);
begin
    writeln('Temperature: ', vol);
end;

begin
    {Start monitor}
    batteryStartMonitoring;
    {Wait 1 second for collection info}
    delay(1000);

    status := batteryGetStatus;
    printStatus(status);

    health := batteryGetHealth;
    printHealth(health);

    level := batteryGetLevel;
    printLevel(level);

    plugType := batteryGetPlugType;
    printPlugType(plugType);

    tech := batteryGetTechnology;
    printTechnology(tech);

    temp := batteryGetTemperature;
    printTemp(temp);

    vol := batteryGetVoltage;
    printVoltage(vol);

    {stop}
    batteryStopMonitoring;
    readln;
end.