Var
  A : TComponent;

begin
  A := TComponent.Create;
  A.Name := 'MyComponent';
  A.Free;
  Writeln('A is still assigned: ', Assigned(A));
end.