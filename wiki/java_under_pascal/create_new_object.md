# How to initialize a new object

## Declare

```pascal
Var
    <VariableName>: <ClassName>;
```

Inside

* ``VariableName`` is the name of the object
* ``ClassName`` is the name of the class, it is important to declare the correct name of the class, replace the sign. By the _ sign when accessing the package class

Example

```pascal
Var
    m: java_lang_Math;
    list: java_util_ArrayList;
    c: android_graphics_Color;
begin
    ...
end.
```

## Initialization

We will use the ``new`` function to initialize an ``Object``

**NOTE: it is not possible to initialize a ``final class``.**

Syntax:

```pascal
    New(object);
```
Or

```pascal
    New(object, list_parameters);
```

Param ``list_parameters`` is the list of parameters to initialize object

Example

```pascal

Var
    List: java_util_ArrayList;
Begin
    New(list);
End.
```

Or

```pascal
Var
    mSocket: java_net_Socket;
    ipAddress: string;
    port: integer;
Begin
    ipAddress: = '192.168.1.1';
    port: = 80;
    {Socket mSocket = new Socket(ipAddress, port)}
    New(mSocket, ipAddress, port);
End
```

## Calling an method

Very simple, it's like ``java`` language

For example, we need to call the ``isConnected`` method of the ``Socket`` class

```pascal
Var
    mSocket: java_net_Socket;
    ipAddress: string;
    port: integer;
Begin
    Ipaddress: = '192.168.1.1';
    port: = 80;
    New(mSocket, ipAddress, port);

    Writeln(mSocket.isConnected()); //<==
End
```

## Create a custom class

Currently the app is not supported