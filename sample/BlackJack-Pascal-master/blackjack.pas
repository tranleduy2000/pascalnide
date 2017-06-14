Program blackjack;
(* Herrero Arnanz, Javier *)
(* Tejero de Pablos, Miguel Angel *)
(* Enero 2010 - Programacion I - UVa *)

(*Programa que permite al usuario jugar una serie de partidas de una versión simplificada del BlackJack*)

USES
  crt;

(*Libreria para usar los subprogramas delay, clrscr y readkey*)

CONST
  MaxCartas = 52;(*Numero de cartas de la baraja*)

TYPE
  Tvalor = 0..11;

    (*Representa los valores de cada carta*)
    (*El cero es para inicializar las bazas*)

  Ttam_baraja = 1..MaxCartas;

(*Representa el tamaño de una baraja*)

  Tcartas = Record
    carta : String[4];
    valor : Tvalor;
  end;

(*Representa cada una de las cartas de la baraja*)

  Tbaraja = ARRAY[Ttam_baraja] of Tcartas;

(*Representa la baraja*)

  Tbaza = ARRAY[1..11] of Tcartas;(*Representa las cartas del jugador y el casino*)
    (*Como mucho se necesitaran 11 cartas, pues con 11 se obtiene una puntacion igual o mayor de 21*)




(*FUNCIONES Y PROCEDIMIENTOS*)

Procedure crea_baraja (Var baraja : Tbaraja);
(*Se crea una baraja con las cartas agrupadas por palos*)
(*Sera necesario barajarla antes de comenzar a jugar*)
(*Los ases se inicializan con valor 11*)
Var
  i : Integer;
(*Indice para recorrer toda la baraja*)
  card : Integer;
(*Representa la carta que se va ha crear en cada momento*)
  cadena : String[2];(*Variable auxiliar para el paso de entero a cadena*)
Begin
  card := 1; (*Comenzamos con un 'as'*)
  For i:=1 to MaxCartas do
  Begin
    If (card > 13) then card := 1; (*Cuando hemos completado un palo,pasamos al siguiente*)
    Case card of
      1 :
      Begin
        baraja[i].carta := '[A]';
        baraja[i].valor := 11;
      end;
      2..10 :
      Begin
        str(card, cadena); (*Pasamos a cadena la carta para poder guardarla en la baraja*)
        baraja[i].carta := '[' + cadena + ']';
        baraja[i].valor := card;
      end;
      11 :
      Begin
        baraja[i].carta := '[J]';
        baraja[i].valor := 10;
      end;
      12 :
      Begin
        baraja[i].carta := '[Q]';
        baraja[i].valor := 10;
      end;
      13 :
      Begin
        baraja[i].carta := '[K]';
        baraja[i].valor := 10;
      end;

    end;

    card := (card + 1);
  end;
end;

(*crea_baraja*)





Procedure barajar (Var baraja : Tbaraja);
(*Se barajan las cartas de la baraja pasada como argumento*)
Var
  i : Integer;
(*Indice para recorrer toda la baraja*)
  carta_aux : Tcartas;
(*Variable auxiliar para poder hacer el intercambio de cartas*)
  posic : Integer;(*Posicion a la que se mueve la carta*)

Begin
  (*Recorremos la baraja cambiando aleatoriamente la posicion de las cartas*)
  (*Para ello intercambiamos la carta en la que nos encotramos por otra carta situada*)
  (*en una poscicion obtenida aleatoriamente*)
  For i:=1 to MaxCartas do
  Begin
    posic := Random(52) + 1; (*Calculamos la posicion aleatoriamente*)

    (*Realizamos el intercambio usando para ello la variable auxiliar*)
    carta_aux.carta := baraja[posic].carta;
    carta_aux.valor := baraja[posic].valor;
    baraja[posic].carta := baraja[i].carta;
    baraja[posic].valor := baraja[i].valor;
    baraja[i].carta := carta_aux.carta;
    baraja[i].valor := carta_aux.valor;
  end;

end;

(*barajar*)





Procedure imprime_reglas (Var opc : Char);
(*Imprime por pantalla las reglas e instrucciones del juego*)
(*Al final se le pide al usuario que elija entre jugar o salir, la opcion eligida*)
(*se almacena en 'opc' y se le pasa al programa principal para que se conozca la eleccion*)
Begin
  Clrscr;
  Writeln('');
  Writeln('                         ------------------------');
  Writeln('                         |REGLAS E INSTRUCCIONES|');
  Writeln('                         ------------------------');
  Writeln('');
  Writeln('- El BlackJack se juega con una baraja de 52 cartas, con 4 palos,');
  Writeln('  cada palo consta de un ás, 3 figuras (J,Q,K) y cartas numeradas del 2 al 10.');
  Writeln('  Las cartas numeradas tienen un valor igual a su número, las figuras cuentan ');
  Writeln('  como 10 y los ases como 1 o 11, según le convenga al jugador.');
  Writeln('');
  Writeln('- El propósito del juego es conseguir una serie de cartas cuyo valor total sea');
  Writeln('  el máximo posible pero sin superar el valor 21 (que se denomina BlackJack y ');
  Writeln('  es la mejor jugada).');
  Writeln('');
  Writeln('- El desarrollo de una partida del juego es el siguiente: ');
  Writeln('');
  Writeln('        1) APUESTA INICIAL: Siempre de 2 euros.');
  Writeln('        2) REPARTO INICIAL: Dos cartas al jugador y una al casino.');
  Writeln('        3) SERIE DEL JUGADOR: El jugador va pidiendo nuevas cartas hasta que ');
  Writeln('           se plante, obtenga un blackjack (21), o se pase (supere el valor 21).');
  Writeln('           Al pedir una nueva carta tiene la opción de doblar la apuesta.');
  Writeln('');
  Writeln('         Si el jugador ha obtenido un blackjack o se ha pasado se omite el paso ');
  Writeln('         siguiente (el casino no pide cartas).');
  Writeln('');
  Writeln('        4) SERIE DEL CASINO: Independientemente del valor obtenido por el jugador, ');
  Writeln('           el casino va pidiendo nuevas cartas hasta que sumen un valor >= a 17.');
  Writeln('        5) EVALUCACION DEL RESULTADO:');
  Writeln('            -Si el jugador tiene blackjack gana y el casino le paga 3/2 de la apuesta.');
  Writeln('            -Si el jugador se ha pasado pierde.');
  Writeln('            -Si el jugador no se ha pasado y el casino sí, el jugador gana.');
  Writeln('            -Si ambos no se han pasado, gana el que tenga una baza de valor mayor,');
  Writeln('             o bien el casino en caso de empate.');
  Writeln('');
  Writeln('');
  Write('¿ Desea [J]ugar o [S]alir ?: [J/S] ');
  Repeat
    opc := Readkey;
  until (opc = 'j') or (opc = 's');

end;

(*imprime_reglas*)





Procedure inicializar_baza (Var baza : Tbaza);
(*Inicializa a cero la baza que se le pase como argumento*)
(*Las cadenas que simbolizan las cartas, se inicializan con el caracter '0'*)
Var
  i : Integer;(*Indice*)
Begin
  For i:=1 to 11 do
  Begin
    baza[i].carta := '0';
    baza[i].valor := 0;
  end;
end;

(*inicializar_baza*)





Procedure balance_global (par, apu, bal : Integer);
(*Muestra por pantalla el numero de partida, apuesta, balance*)
Begin
  Writeln('Partida: ', par, '| Apuesta: ', apu, '| Balance: ', bal);
end;

(*balance_global*)





Procedure estado_partida (baza_cas, baza_jug : Integer; cart_cas, cart_jug : Tbaza; ret_cas, ret_jug : Boolean);
(*Muestra por pantalla las cartas del casino y el jugador*)
Var
  i : Integer;(*Indice*)
Begin
  Writeln('-----------------------------------------------------------');
  (*Se muestra la baza del casino*)
  Write('Casino    ');
  If (ret_cas) then Delay(1000);
  If (baza_cas < 10) then
    Write('( ', baza_cas, ')   ')
  else
    Write('(', baza_cas, ')   ');
  (*Se muestran las cartas del casino*)
  i := 1;
  While (i <= 11) do
  Begin
    If (cart_cas[i].carta <> '0') then
    Begin
      If (ret_cas) then Delay(1000); (*Entre que se muestra cada carta esperamos 1 segundos*)
      Write(cart_cas[i].carta, ' ');
    end
    else
    Begin
      i := 11; (*Cuando encontremos un cero ya no quedaran cartas por mostrar*)
    end;

    i := (i + 1);
  end;
  Writeln('');
  (*Se muestra la baza del jugador*)
  Write('Jugador   ');
  If (ret_jug) then Delay(1000);
  If (baza_jug < 10) then
    Write('( ', baza_jug, ')   ')
  else
    Write('(', baza_jug, ')   ');
  (*Se muestran las cartas del jugador del mismo modo que las del casino*)
  i := 1;
  While (i <= 11) do
  Begin
    If (cart_jug[i].carta <> '0') then
    Begin
      If (ret_jug) then Delay(1000); (*Entre que se muestra cada carta esperamos 1 segundos*)
      write(cart_jug[i].carta, ' ');
    end
    else
    Begin
      i := 11;
    end;

    i := (i + 1);
  end;
  Delay(1000);
  Writeln('');
  Writeln('------------------------------------------------------------');
end;

(*estado_partida*)





Procedure robar_carta (Var baraja, retiradas : Tbaraja; Var baza : Tbaza; Var card : Ttam_baraja);
(*Se roba una carta de la baraja y esta se incluye en la baza especificada*)
(*Ademas la carta robada se elimina de la baraja y pasa al monton de retiradas*)
Var
  pos_baza : 1..11;
(*Contiene el indice de la baza donde colocaremos la carta robada*)
  i : Integer;(*Indice*)
Begin
  pos_baza := 1;
  (*Buscamos una posicion de la baza donde no exista carta alguna*)
  While (baza[pos_baza].valor <> 0) do
  Begin
    pos_baza := (pos_baza + 1);
  end;
  (*Colocamos la carta en la baza*)
  baza[pos_baza].carta := baraja[card].carta;
  baza[pos_baza].valor := baraja[card].valor;
  (*Pasamos la carta al monton de retiradas*)
  retiradas[card].carta := baraja[card].carta;
  retiradas[card].valor := baraja[card].valor;
  (*Eliminamos la carta de la baraja*)
  baraja[card].carta := '0';
  baraja[card].valor := 0;
  (*Comprobamos si se ha agotado el monton de reparto*)
  If (card = 1) then
  Begin
    (*El monton de reparto toma las cartas del monton de retiradas y lo barajamos*)
    baraja := retiradas;
    barajar(baraja);
    (*Limpiamos el monton de retiradas*)
    For i:=1 to MaxCartas do
    Begin
      retiradas[i].carta := '0';
      retiradas[i].valor := 0;
    end;
    (*Actualizamos el apuntador de carta*)
    card := MaxCartas;
  end
  else
      (*Actualizamos el apuntador de carta para la sig. vez q se robe*)
    card := (card - 1);
end;

(*robar_carta*)





Function tiene_ases(c : Tbaza) : Boolean;
(*Devuelve true si encuentra ases con valor 11*)
Var
  i : Integer;(*Indice*)
Begin
  i := 1;
  While (i <= 11) do
  Begin
    If (c[i].valor <> 0) then
    Begin
      If (c[i].valor = 11) then
      Begin
        tiene_ases := True;
        i := 13;
      end
      else
      Begin
        i := (i + 1);
      end;
    end
    else
      i := 12;
  end;

  If (i = 12) then tiene_ases := False;
end;

(*tiene_ases*)





Procedure valor_ases(Var baz : Integer; Var cart : Tbaza);
(*Evalua la baza y cartas pasadas como parametros por variable y en caso de que la baza supere el valor 21*)
(*y se posean ases, estos pasan ha tener valor 1*)
Var
  i : Integer;
Begin
  If (baz > 21) and (tiene_ases(cart)) then
  Begin
    i := 1;
    While (i <= 11) do
    Begin
      If (cart[i].valor = 11) then
      Begin
        cart[i].valor := 1;
        baz := (baz - 10);
        i := 12;
      end
      else
      Begin
        i := (i + 1);
      end;
    end;
  end;
end;

(*valor_ases*)







(*VARIABLES DEL PROGRAMA PRINCPAL*)
VAR
  monton_reparto, monton_retiradas : Tbaraja;
    (*Monton de cartas pendientes de repartir / Monton de cartas ya repartidas*)
  cartas_jugador, cartas_casino : Tbaza;
    (*Almacena las cartas que va recibiendo el jugador y el casino*)
  partida, apuesta, balance : Integer;
(*Contador de partidas / Contador de apuestas / Balance de ganancias y perdidas del jugador*)
  opcion : Char;
(*Almacena las elecciones hechas por el jugador a lo largo de la partida *)
  apuntador_carta : Ttam_baraja;
(*Contiene el indice de la carta que queremos robar*)
  baza_casino, baza_jugador : Integer;
(*Contiene la baza en cada momento del casino y del jugador respectivamente*)
  serie_casino : Boolean;
(*Indica si se ejecuta o no la serie del casino*)
  ganada : Boolean;
    (*Indica si el jugador ganó la partida o no*)
  t : integer;
(*Indice*)
  enter : Char;
(*Almacena la tecla enter cuando la pulsa el jugador*)
  retardo_cas, retardo_jug : Boolean;(*Indica si activamos los retardos o no, al mostrar la baza y las cartas del casino o el jugador*)





(*CUERPO DEL PROGRAMA PRINCIPAL*)
BEGIN
  Randomize;
  Clrscr; (*Limpiamos la pantalla*)
  apuntador_carta := MaxCartas; (*Inicializamos el apuntador de carta con la carta de la cima del monton*)

  (*Impresion de la pantalla de inicio del juego*)
  Writeln('');
  Writeln('                     ---------------------------------------------');
  Writeln('                     | *** [21] ***    BLACKJACK    *** [21] *** |');
  Writeln('                     ---------------------------------------------');
  Writeln('');
  Writeln('');
  Writeln('                                 1- Reglas e instrucciones');
  Writeln('                                 2- Jugar');
  Writeln('                                 3- Salir');
  Writeln('');
  Writeln('');
  Write('Escoja una opcion: ');

  (*Leemos la opcion elegida y la ejecutamos*)
  Repeat
    opcion := Readkey;
  until (opcion = '1') or (opcion = '2') or (opcion = '3');


  If (opcion = '1') then imprime_reglas(opcion);

  (*Si recibimos un '2' o bien una 'j' (despues de haber imprimido las reglas) sabemos que*)
  (*debemos comenzar el juego, en caso contrario se termina la ejecucion del programa*)
  If (opcion = '2') or (opcion = 'j') then
  Begin
    (*Generamos el monton de reparto y barajamos las cartas*)
    crea_baraja(monton_reparto);
    barajar(monton_reparto);

    (*Inicializamos el contador de partida y balance*)
    partida := 1;
    balance := 0;


    (*BUCLE PRINCIPAL EN EL QUE CADA ITERACION REPRESENTA UNA PARTIDA JUGADA*)
    Repeat
      Clrscr;
        (*Inicializamos el contador de apuesta*)
      apuesta := 2;
        (*Repartimos dos cartas al jugador*)
      inicializar_baza(cartas_jugador);
      inicializar_baza(cartas_casino);
      For t:=1 to 2 do
        robar_carta(monton_reparto, monton_retiradas, cartas_jugador, apuntador_carta);

        (*Repartimos una carta al casino*)
      robar_carta(monton_reparto, monton_retiradas, cartas_casino, apuntador_carta);
      retardo_cas := True;
      retardo_jug := True; (*Activamos los retardos al mostrar las cartas para el reparto inicial*)

        (*SERIE DEL JUGADOR*)
      Repeat
          (*Actualizamos las bazas del casino y el jugador*)
        baza_casino := 0;
        baza_jugador := 0;
        For t:=1 to 11 do
        Begin
          baza_casino := (baza_casino + cartas_casino[t].valor);
          baza_jugador := (baza_jugador + cartas_jugador[t].valor);
        end;

          (*Comprobamos la baza para saber que valor deben tomar los ases(si los hay)*)
        valor_ases(baza_jugador, cartas_jugador);
          (*Mostramos el estado de la partida*)
        balance_global(partida, apuesta, balance);
        estado_partida(baza_casino, baza_jugador, cartas_casino, cartas_jugador, retardo_cas, retardo_jug);
          (*Despues de el reparto inicial solo aplicamos retardo al jugador*)
        retardo_cas := False;
        retardo_jug := True;
          (*Evaluamos la baza obtenida*)
        If (baza_jugador >= 21) then
        Begin
          If (baza_jugador = 21) then
          Begin
            Write('BLACKJACK! (Pulse ENTER)');
            Repeat
              enter := Readkey;
            until (enter = #13);
            Writeln('');
            opcion := 'p';
            serie_casino := False;
            ganada := True;
            (*Incrementamos el balance en 3/2 de la apuesta actual*)
            apuesta := ((apuesta div 2) * 3);
            balance := (balance + apuesta);
          end
          else
          Begin
            Write('SE PASO! (Pulse ENTER)');
            Repeat
              enter := Readkey;
            until (enter = #13);
            Writeln('');
            opcion := 'p';
            serie_casino := False;
            ganada := False;
            (*Decrementamos al balance la apuesta actual*)
            balance := (balance - apuesta);
          end;
        end
        else
        Begin
          (*Si no se paso ni consiguio blackjack le mostramos las opciones*)
          Write('[O]tra carta, [P]lantarse, [D]oblar: [O/P/D] ');
          Repeat
            opcion := Readkey;
          until (opcion = 'o') or (opcion = 'p') or (opcion = 'd');
          Case opcion of
            'o' :
            Begin
              robar_carta(monton_reparto, monton_retiradas, cartas_jugador, apuntador_carta);
              clrscr;
            end;
            'd' :
            Begin
              robar_carta(monton_reparto, monton_retiradas, cartas_jugador, apuntador_carta);
              apuesta := (apuesta * 2);
              Clrscr;
            end;
            'p' :
            Begin
              serie_casino := True;
            end;
          end;
        end;

      until (opcion = 'p'); (*Serie del jugador*)




        (*SERIE DEL CASINO*)
      While (serie_casino) do
      Begin
        Writeln('');
        (*El casino roba una carta*)
        robar_carta(monton_reparto, monton_retiradas, cartas_casino, apuntador_carta);

        (*Actualizamos la baza del casino*)
        baza_casino := 0;
        For t:=1 to 11 do
        Begin
          baza_casino := (baza_casino + cartas_casino[t].valor);
        end;

        (*En la serie del casino solo retardamos el reparto de cartas del casino*)
        retardo_cas := True;
        retardo_jug := False;
        valor_ases(baza_casino, cartas_casino);
        estado_partida(baza_casino, baza_jugador, cartas_casino, cartas_jugador, retardo_cas, retardo_jug);
        If (baza_casino >= 17) then
        Begin
          If (baza_casino <= 21) then
          Begin
            Writeln('Casino se planta');
            Delay(1500);
          end
          else
          Begin
            Writeln('Casino se paso');
            Delay(1500);
            ganada := True;
            (*Incrementamos el balance con la apuesta actual*)
            balance := (balance + apuesta);
          end;
          serie_casino := False;
        end
        else
        Begin
          Writeln('Casino pide otra carta');
          Delay(1500);
          serie_casino := True;
        end;
      end; (*Serie del casino*)



        (*RESULTADO FINAL DE LA PARTIDA*)
      If (baza_jugador > baza_casino) and (baza_jugador < 21) then
      Begin
        ganada := True;
        balance := (balance + apuesta);
      end;

      If (baza_casino > baza_jugador) and (baza_casino <= 21) then
      Begin
        ganada := False;
        balance := (balance - apuesta);
      end;
      If (baza_jugador = baza_casino) then
      Begin
        ganada := False;
        balance := (balance - apuesta);
      end;

      If ganada then
      Begin
        Writeln('');
        Write('GANASTE (+', apuesta, ')   ');
        Delay(1000);
      end
      else
      Begin
        Writeln('');
        Write('PERDISTE (-', apuesta, ')   ');
        Delay(1000);
      end;

        (*MENSAJE DE FINAL DE PARTIDA*)
      Write('Otra ronda? [S/N]');
      Repeat
        opcion := Readkey;
      until (opcion = 's') or (opcion = 'n');

      partida := (partida + 1); (*Actualizamos el contador de partida*)

    until (opcion = 'n'); (*partida*)


  end;

  Clrscr; (*Limpiamos la pantalla al salir del programa*)

END.(*Fin programa principal*)
