program ball1;

uses Allegro5, Al5primitives;
const
  FPS = 120;
  Gravity = 9.8;
  SurfaceWeight = 60000000;
  accx = 12;
type
  Ball = record
    xp, yp, vx, vy, mass : real;
  end;
var
  Display  : ALLEGRO_DISPLAYptr;
  timer    : ALLEGRO_TIMERptr;
  event_queue : ALLEGRO_EVENT_QUEUEptr;
  ev   : ALLEGRO_EVENT;
  redraw : Boolean;
  B : Ball;
  oldVy, dt, oldFps, newFps : Real;
  i : integer;
BEGIN
  B.vx := -20; B.vy := 0; B.xp:= 100; B.yp := 10;
  i:=0;
  B.mass := 60000;
  al_init();
  al_init_primitives_addon(); 
  Display := al_create_display(700, 500);
  timer := al_create_timer(1/FPS);
  event_queue := al_create_event_queue();
  al_register_event_source(event_queue, al_get_display_event_source(Display));
  al_register_event_source(event_queue, al_get_timer_event_source(timer));
  al_start_timer(timer);
  dt := al_get_timer_speed(timer) * 10;

  oldFps := al_get_timer_count(timer);
  while True do begin
    al_wait_for_event(event_queue, ev); 
    Inc(i);
    Writeln((al_get_timer_count(timer)));
    if ev._type = ALLEGRO_EVENT_DISPLAY_CLOSE then Break
    else if ev._type = ALLEGRO_EVENT_TIMER then begin

      redraw := True;
      (* The Juicy part the physics*)
      oldVy := B.vy;
      B.vy += Gravity * dt;
      B.vx += accx * dt;
      (* I use verlet algorithm*)
      B.xp += B.vx * dt +0.5*accx*(dt*dt);
      B.yp += B.vy * dt +0.5*Gravity*(dt*dt);
      if B.yp+10>=500 then begin
        B.vy := ((abs(B.vy)*(B.mass-SurfaceWeight)) / (B.mass+SurfaceWeight));
        B.vy := round(B.vy);
      end
      else if (B.xp+10>=700) or (B.xp<=10) then begin
        b.Vx *= -1;
        B.vx := round(B.vx);
      end;

      if redraw and al_is_event_queue_empty(event_queue) then begin
        al_clear_to_color(al_map_rgb(0, 0, 0));
        al_draw_filled_circle(B.xp, B.yp, 10, al_map_rgb(255, 255, 0));
        al_flip_display();
        redraw := False;
      end;

    end;
  end;
  al_destroy_event_queue(event_queue);
  al_destroy_timer(timer);
  al_destroy_display(Display);
END.
