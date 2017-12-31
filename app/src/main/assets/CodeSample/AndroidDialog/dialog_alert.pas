Uses
  aDialog;

Var
  title, hint, msg : string;
Begin
  title := 'Hello';
  msg := 'This is Pascal NIDE';

  DialogAlert(title, msg, true); //<==

  Readln;
End.