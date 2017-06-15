PROGRAM test;
(*
  So far, this demonstrates if it links and works.
 *)

  USES
    Allegro5, al5gl,
  {$IFDEF FPC}
    GL;
  {$ELSE}
    OpenGL;
  {$ENDIF}



  PROCEDURE DrawScene;
  BEGIN
  { Clear the RGB buffer and the depth buffer. }
    glClear (GL_COLOR_BUFFER_BIT OR GL_DEPTH_BUFFER_BIT);

  { Set the modelview matrix to be the identity matrix. }
    glLoadIdentity;
  { Translate and rotate the object. }
    glTranslatef (-2.5, 0.0, 0.0);
    glRotatef (-30, 1.0, 0.0, 0.0);
    glRotatef (30, 0.0, 1.0, 0.0);
    glRotatef (30, 0.0, 0.0, 1.0);

    glColor3f (1.0, 0.0, 1.0);

    glLoadIdentity;
    glTranslatef (2.5, 0.0, 0.0);
    glRotatef (45, 1.0, 0.0, 0.0);
    glRotatef (45, 0.0, 1.0, 0.0);
    glRotatef (45, 0.0, 0.0, 1.0);

    glColor3f (0.0, 1.0, 0.0);

    glDisable (GL_TEXTURE_2D);
  { Draw the sides of the cube. }
    glBegin (GL_QUAD_STRIP);
      glVertex3d ( 3,  3, -3);
      glVertex3d ( 3, -3, -3);
      glVertex3d (-3,  3, -3);
      glVertex3d (-3, -3, -3);
      glVertex3d (-3,  3,  3);
      glVertex3d (-3, -3,  3);
      glVertex3d ( 3,  3,  3);
      glVertex3d ( 3, -3,  3);
      glVertex3d ( 3,  3, -3);
      glVertex3d ( 3, -3, -3);
    glEnd;

    glColor3f (0.0, 0.0, 1.0);

  { Draw the top of the cube. }
    glBegin (GL_QUADS);
      glVertex3d (-3, -3, -3);
      glVertex3d ( 3, -3, -3);
      glVertex3d ( 3, -3,  3);
      glVertex3d (-3, -3,  3);
    glEnd;
  END;

VAR
  Display: ALLEGRO_DISPLAYptr;
BEGIN
  IF al_init THEN
  BEGIN
    WriteLn ('Allegro 5 installed and initialised. :)');
    al_set_new_display_flags (ALLEGRO_OPENGL);
    al_set_new_display_option (ALLEGRO_DEPTH_SIZE, 16, ALLEGRO_SUGGEST);
    Display := al_create_display (640, 480);
    IF Display <> NIL THEN
    BEGIN
      WriteLn ('OpenGL display created.');
      DrawScene;
      al_flip_display;
    END
    ELSE
      WriteLn ('Could not create display.');
  END
  ELSE
    WriteLn ('Allegro 5 didn''t work! :(')
END.
