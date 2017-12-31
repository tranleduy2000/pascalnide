uses
  crt, aDisplay;

{$MODE ANDROID}
procedure processClick(v : TGestureEvent);
begin
end;

procedure processLongClick(v : TGestureEvent);
begin

end;

procedure processDoubleClick(v : TGestureEvent);
begin

end;


begin
  aDisplay.onClick(processClick());
  aDisplay.onLongClick(processLongClick());
  aDisplay.onDoubleClick(processDoubleClick());
end.