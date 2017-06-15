PROGRAM haiku;
(*
 *    Haiku - A Musical Instrument, by Mark Oates.
 *
 *    Allegro example version by Peter Wang.
 *
 *    Pascal translation by Guillermo "Ñuño" Martínez.
 *
 *    It demonstrates use of the audio functions, and other things besides.
 *)

(* This version leaves out some things from Mark's original version:
 * the nice title sequence, text labels and mouse cursors.
 *)

USES
   Common,
   Allegro5,
   al5audio, al5acodec, al5image,
   sysutils;


CONST
   PI    = ALLEGRO_PI;
   TWOPI = ALLEGRO_PI * 2.0;

   TYPE_EARTH = 0;
   TYPE_WIND  = 1;
   TYPE_WATER = 2;
   TYPE_FIRE  = 3;
   NUM_TYPES  = 4;
   TYPE_NONE  = NUM_TYPES;

   IMG_EARTH        = TYPE_EARTH;
   IMG_WIND         = TYPE_WIND;
   IMG_WATER        = TYPE_WATER;
   IMG_FIRE         = TYPE_FIRE;
   IMG_BLACK        = 4;
   IMG_DROPSHADOW   = 5;
   IMG_GLOW         = 6;
   IMG_GLOW_OVERLAY = 7;
   IMG_AIR_EFFECT   = 8;
   IMG_WATER_DROPS  = 9;
   IMG_FLAME        = 10;
   IMG_MAIN_FLAME   = 11;
   IMG_MAX          = 12;

   MAX_ANIMS = 10;

TYPE
   TInterp = (
      INTERP_LINEAR,
      INTERP_FAST,
      INTERP_DOUBLE_FAST,
      INTERP_SLOW,
      INTERP_DOUBLE_SLOW,
      INTERP_SLOW_IN_OUT,
      INTERP_BOUNCE
   );

   PAnim = ^TAnim;
   TAnim = RECORD
      lVal: PSINGLE;   { NIL if unused. }
      StartVal,
      EndVal:     SINGLE;
      Func: TInterp;
      StartTime, EndTime: SINGLE;
   END;

   PSprite = ^TSprite;
   TSprite = RECORD
      Image: LONGWORD; { IMG_ }
      X, ScaleX, AlignX: SINGLE;
      Y, ScaleY, AlignY: SINGLE;
      Angle: SINGLE;
      R, G, B: SINGLE;
      Opacity: SINGLE;
      Anims: ARRAY [0..MAX_ANIMS - 1] OF TAnim; { Keep it simple. }
   END;

   PToken = ^TToken;
   TToken = RECORD
      TheType: LONGWORD; { TYPE_ }
      X, Y: SINGLE;
      Pitch: LONGINT; { [0, NUM_PITCH] }
      Bot, Top: TSprite;
   END;

   PFlair = ^TFlair;
   TFlair = RECORD
     EndTime: SINGLE;
     Sprite: TSprite;
     Next: PFlair;
   END;



(****************************************************************************
 * Globals                                                                  *
 ****************************************************************************)

CONST
   NUM_PITCH   = 8;
   TOKENS_X    = 16;
   TOKENS_Y    = NUM_PITCH;
   NUM_TOKENS  = TOKENS_X * TOKENS_Y;

VAR
   Display: ALLEGRO_DISPLAYptr;
   RefreshTimer, PlaybackTimer: ALLEGRO_TIMERptr;

   Images: ARRAY [0..IMG_MAX - 1] OF ALLEGRO_BITMAPptr;
   ElementSamples: ARRAY [0..NUM_TYPES - 1] OF ARRAY [0..NUM_PITCH - 1] OF ALLEGRO_SAMPLEptr;
   SelectSample: ALLEGRO_SAMPLEptr;

   Tokens: ARRAY [0..NUM_TOKENS - 1] OF TToken;
   Buttons: ARRAY [0..NUM_TYPES - 1] OF TToken;
   Glow, GlowOverlay: TSprite;
   GlowColor: ARRAY [0..NUM_TYPES] OF ALLEGRO_COLOR;
   Flairs: PFlair = NIL;
   HoverToken: PToken = NIL;
   SelectedButton: PToken = NIL;
   PlaybackColumn: LONGINT = 0;

CONST
   ScreenW = 1024;
   ScreenH = 600;
   GameBoardX = 100.0;
   TokenSize = 64;
   TokenScale = 0.8;
   ButtonSize = 64;
   ButtonUnselScale = 0.8;
   ButtonSelScale = 1.1;
   DropshadowUnselScale = 0.6;
   DropshadowSelScale = 0.9;
   RefreshRate = 60.0;
   PlaybackPeriod = 2.7333;

   HAIKU_DATA = 'data/haiku/';



(****************************************************************************
 * Init                                                                     *
 ****************************************************************************)

   PROCEDURE LoadImages;
   VAR
      Ndx: INTEGER;
   BEGIN
      Images[IMG_EARTH]       := al_load_bitmap (HAIKU_DATA+'earth4.png');
      Images[IMG_WIND]        := al_load_bitmap (HAIKU_DATA+'wind3.png');
      Images[IMG_WATER]       := al_load_bitmap (HAIKU_DATA+'water.png');
      Images[IMG_FIRE]        := al_load_bitmap (HAIKU_DATA+'fire.png');
      Images[IMG_BLACK]       := al_load_bitmap (HAIKU_DATA+'black_bead_opaque_A.png');
      Images[IMG_DROPSHADOW]  := al_load_bitmap (HAIKU_DATA+'dropshadow.png');
      Images[IMG_AIR_EFFECT]  := al_load_bitmap (HAIKU_DATA+'air_effect.png');
      Images[IMG_WATER_DROPS] := al_load_bitmap (HAIKU_DATA+'water_droplets.png');
      Images[IMG_FLAME]       := al_load_bitmap (HAIKU_DATA+'flame2.png');
      Images[IMG_MAIN_FLAME]  := al_load_bitmap (HAIKU_DATA+'main_flame2.png');
      Images[IMG_GLOW]        := al_load_bitmap (HAIKU_DATA+'healthy_glow.png');
      Images[IMG_GLOW_OVERLAY]:= al_load_bitmap (HAIKU_DATA+'overlay_pretty.png');

      FOR Ndx := LOW (Images) TO HIGH (Images) DO
         IF Images[Ndx] = NIL THEN
            AbortExample ('Error loading image.');
   END;



   PROCEDURE LoadSamples;
   CONST
      Base: ARRAY [0..NUM_TYPES - 1] OF STRING = (
         'earth', 'air', 'water', 'fire'
      );
   VAR
      Name: STRING;
      t, p: LONGINT;
   BEGIN
      FOR t := 0 TO NUM_TYPES - 1 DO
      BEGIN
         FOR p := 0 TO NUM_PITCH - 1 DO
         BEGIN
            Name := HAIKU_DATA + Base[t] + '_' + IntToStr (p) + '.ogg';
            ElementSamples[t][p] := al_load_sample (Name);
            IF ElementSamples[t][p] = NIL THEN
               AbortExample ('Error loading ' + Name + '.');
         END;
      END;
      SelectSample := al_load_sample (HAIKU_DATA+'select.ogg');
      IF SelectSample = NIL THEN
         AbortExample ('Error loading select.ogg.');
   END;



   PROCEDURE InitSprite (VAR Spr: TSprite; Image: INTEGER; x, y, Scale, Opacity: SINGLE);
   VAR
      Ndx: INTEGER;
   BEGIN
      Spr.Image := Image;
      Spr.X := x;
      Spr.Y := y;
      Spr.ScaleX := Scale;
      Spr.ScaleY := Scale;
      Spr.AlignX := 0.5;
      Spr.AlignY := 0.5;
      Spr.Angle := 0.0;
      Spr.R := 1.0;
      Spr.G := 1.0;
      Spr.B := 1.0;
      Spr.Opacity := Opacity;
      FOR Ndx := LOW (Spr.Anims) TO HIGH (Spr.Anims) DO
         Spr.Anims[Ndx].lval := NIL;
   END;



   PROCEDURE InitTokens;
   CONST
      TokenW = TokenSize * TokenScale;
      TokenX = GameBoardX + TokenW / 2;
      TokenY = 80;
   VAR
      Ndx, tx, ty: INTEGER;
      px, py: SINGLE;
   BEGIN
      FOR Ndx := LOW (Tokens) TO HIGH (Tokens) DO
      BEGIN
         tx := Ndx MOD TOKENS_X;
         ty := Ndx DIV TOKENS_X;
         px := TokenX + tx * TokenW;
         py := TokenY + ty * TokenW;

         Tokens[Ndx].TheType := TYPE_NONE;
         Tokens[Ndx].X := px;
         Tokens[Ndx].Y := py;
         Tokens[Ndx].Pitch := NUM_PITCH - 1 - ty;
         InitSprite (Tokens[Ndx].Bot, IMG_BLACK, px, py, TokenScale, 0.4);
         InitSprite (Tokens[Ndx].Top, IMG_BLACK, px, py, TokenScale, 0.0);
      END;
   END;



   PROCEDURE InitButtons;
   CONST
      Dist: ARRAY [0..NUM_TYPES - 1] OF SINGLE = (-1.5, -0.5, 0.5, 1.5);
   VAR
      Ndx: INTEGER;
      X, Y: SINGLE;
   BEGIN
      Y := ScreenH - 80;
      FOR Ndx := LOW (Buttons) TO HIGH (Buttons) DO
      BEGIN
         X := ScreenW DIV 2 + 150 * Dist[Ndx];
         Buttons[Ndx].TheType := Ndx;
         Buttons[Ndx].X := X;
         Buttons[Ndx].Y := Y;
         InitSprite (Buttons[Ndx].Bot, IMG_DROPSHADOW, X, Y, DropshadowUnselScale, 0.4);
         Buttons[Ndx].Bot.AlignY := 0.0;
         InitSprite (Buttons[Ndx].Top, Ndx, X, Y, ButtonUnselScale, 1.0);
      END;
   END;



   PROCEDURE InitGlow;
   BEGIN
     InitSprite (Glow, IMG_GLOW, ScreenW DIV 2, ScreenH, 1.0, 1.0);
     Glow.AlignY := 1.0;
     Glow.R := 0.0;
     Glow.g := 0.0;
     Glow.b := 0.0;

     InitSprite (GlowOverlay, IMG_GLOW_OVERLAY, 0.0, 0.0, 1.0, 1.0);
     GlowOverlay.AlignX := 0.0;
     GlowOverlay.AlignY := 0.0;
     GlowOverlay.R := 0.0;
     GlowOverlay.g := 0.0;
     GlowOverlay.b := 0.0;

     GlowColor[TYPE_EARTH] := al_map_rgb ($6B, $8E, $23); { olivedrab }
     GlowColor[TYPE_WIND]  := al_map_rgb ($AD, $D8, $E6); { lightblue }
     GlowColor[TYPE_WATER] := al_map_rgb ($41, $69, $E1); { royalblue }
     GlowColor[TYPE_FIRE]  := al_map_rgb ($FF, $00, $00); { red }
   END;



(****************************************************************************
 * Flairs                                                                   *
 ****************************************************************************)

   FUNCTION CreateFlair (Image: INTEGER; X, Y, EndTime: SINGLE): PSprite;
   VAR
      FL: PFlair;
   BEGIN
      GetMem (FL, SizeOf (TFlair));
      InitSprite (FL^.Sprite, Image, X, Y, 1.0, 1.0);
      FL^.EndTime := EndTime;
      FL^.Next := Flairs;
      Flairs := FL;
      CreateFlair := @(FL^.Sprite);
   END;



   PROCEDURE FreeOldFlairs (Now: SINGLE);
   VAR
      Prev, Fl, Next: PFlair;
   BEGIN
      Prev := NIL;
      Fl := Flairs;
      WHILE Fl <> NIL DO
      BEGIN
         Next := Fl^.Next;
         IF Fl^.EndTime > Now THEN
            Prev := Fl
         ELSE BEGIN
            IF Prev <> NIL THEN
               Prev^.Next := Next
            ELSE
               Flairs := Next;
            FreeMem (Fl, SizeOf (TFlair));
         END;
         Fl := Next;
      END;
   END;



   PROCEDURE FreeAllFlairs;
   VAR
      Next: PFlair;
   BEGIN
     WHILE Flairs <> NIL DO
     BEGIN
        Next := Flairs^.Next;
        FreeMem (Flairs, SizeOf (TFlair));
        Flairs := Next;
     END;
   END;



(****************************************************************************
 * Animations                                                               *
 ****************************************************************************)

   FUNCTION GetNextAnim (CONST Spr: TSprite): PAnim;
   VAR
      DummyAnim: TAnim;
      Ndx: LONGWORD;
   BEGIN
      FOR Ndx := LOW (Spr.Anims) TO HIGH (Spr.Anims) DO
         IF Spr.Anims[Ndx].lval = NIL THEN
         BEGIN
            GetNextAnim := @(Spr.Anims[Ndx]);
            EXIT;
         END;
      GetNextAnim := @DummyAnim;
   END;



   PROCEDURE FixConflictingAnims (VAR Grp: TSprite; lVal: PSINGLE; StartTime, StartVal: SINGLE);
   VAR
      Ndx: LONGWORD;
      Anim: PAnim;
   BEGIN
      FOR Ndx := LOW (Grp.Anims) TO HIGH (Grp.Anims) DO
      BEGIN
         Anim := @(Grp.Anims[Ndx]);
         IF Anim^.lVal = lVal THEN
         BEGIN
         (* If an old animation would overlap with the new one, truncate it
          * and make it converge to the new animation's starting value. *)
            IF Anim^.EndTime > StartTime THEN
            BEGIN
               Anim^.EndTime := StartTime;
               Anim^.EndVal := StartVal;
            END;
         (* Cancel any old animations which are scheduled to start after the
          * new one, or which have been reduced to nothing. *)
            IF (Anim^.StartTime >= StartTime)
            OR (Anim^.StartTime >= Anim^.EndTime) THEN
               Grp.Anims[Ndx].lVal := NIL;
         END;
      END;
   END;



   PROCEDURE AnimFull (VAR Spr: TSprite; lVal: PSINGLE; StartVal, EndVal: SINGLE; Func: TInterp; Delay, Duration: SINGLE);
   VAR
      StartTime: SINGLE;
      Anim: PAnim;
   BEGIN
     StartTime := al_get_time + Delay;
     FixConflictingAnims (Spr, lVal, StartTime, StartVal);
     Anim := GetNextAnim (Spr);

     Anim^.lVal := lVal;
     Anim^.StartVal := StartVal;
     Anim^.EndVal := EndVal;
     Anim^.Func := Func;
     Anim^.StartTime := StartTime;
     Anim^.EndTime := StartTime + Duration;
   END;



   PROCEDURE Anim (VAR Spr: TSprite; lVal: PSINGLE; StartVal, EndVal: SINGLE; Func: TInterp; Duration: SINGLE);
   BEGIN
      AnimFull (Spr, lVal, StartVal, EndVal, Func, 0, Duration);
   END;



   PROCEDURE AnimTo (VAR Spr: TSprite; lVal: PSINGLE; EndVal: SINGLE; Func: TInterp; Duration: SINGLE);
   BEGIN
     AnimFull (Spr, lVal, lVal^, EndVal, Func, 0, Duration);
   END;



   PROCEDURE AnimDelta (VAR Spr: TSprite; lVal: PSINGLE; Delta: SINGLE; Func: TInterp; Duration: SINGLE);
   BEGIN
     AnimFull (Spr, lVal, lVal^, lVal^ + Delta, Func, 0, Duration);
   END;



   PROCEDURE AnimTint (VAR Spr: TSprite; CONST Color: ALLEGRO_COLOR; Func: TInterp; Duration: SINGLE);
   VAR
      R, G, B: SINGLE;
   BEGIN
     al_unmap_rgb_f (Color, R, G, B);

     AnimTo (Spr, @(Spr.r), R, Func, Duration);
     AnimTo (Spr, @(Spr.g), G, Func, Duration);
     AnimTo (Spr, @(Spr.b), B, Func, Duration);
   END;



   FUNCTION Interpolate (Func: TInterp; t: SINGLE): SINGLE;
   VAR
      b, c, d: SINGLE;
   BEGIN
      CASE Func OF
         INTERP_LINEAR:
            Interpolate := t;
         INTERP_FAST:
            Interpolate := -t * (t - 2);
         INTERP_DOUBLE_FAST:
         BEGIN
            t := t - 1;
            Interpolate := t * t * t + 1;
         END;
         INTERP_SLOW:
            Interpolate := t * t;
         INTERP_DOUBLE_SLOW:
            Interpolate := t * t * t;
         INTERP_SLOW_IN_OUT:
         BEGIN
         { Quadratic easing in/out - acceleration until halfway, then deceleration. }
            b := 0; { TODO: Why are these values in variables? }
            c := 1;
            d := 1;
            t := t / (d / 2);
            IF t < 1 THEN
               Interpolate := c / 2 * t * t + b
            ELSE BEGIN
               t := t - 1;
               Interpolate := (-c) / 2 * (t * (t - 2) - 1) + b
            END;
         END;
         INTERP_BOUNCE:
         BEGIN { TODO: Next comment may explay the previous TODO (WTF?) }
         { BOUNCE EASING: exponentially decaying parabolic bounce
           t: current time, b: beginning value, c: change in position, d: duration
           bounce easing out }
            IF t < (1 / 2.75) THEN
               Interpolate := 7.5625 * t * t
            ELSE IF t < (2 / 2.75) THEN
            BEGIN
               t := t - (1.5 / 2.75);
               Interpolate := 7.5625 * t * t + 0.75
            END
            ELSE IF t < (2.5 / 2.75) THEN
            BEGIN
               t := t - (2.5 / 2.75);
               Interpolate := 7.5625 * t * t + 0.9375
            END
            ELSE BEGIN
               t := t - (2.625 / 2.75);
               Interpolate := 7.5625 * t * t + 0.984375
            END;
         END;
         ELSE
            Interpolate := 0.0
      END;
   END;



   PROCEDURE UpdateAnim (VAR Anim: TAnim; Now: SINGLE);
   VAR
      dt, t, Range: SINGLE;
   BEGIN
      IF Anim.lVal = NIL THEN
         EXIT;
      IF Now < Anim.StartTime THEN
         EXIT;
      dt := Now - Anim.StartTime;
      t := dt / (Anim. EndTime - Anim.StartTime);
      IF t >= 1.0 THEN
      BEGIN
      { Animation has run to completion }
         Anim.lVal^ := Anim.EndVal;
         Anim.lVal := NIL;
         EXIT;
      END;
      Range := Anim.EndVal - Anim.StartVal;
      Anim.lVal^ := Anim.StartVal + Interpolate (Anim.Func, t) * Range;
   END;



   PROCEDURE UpdateSpriteAnims (VAR Spr: TSprite; Now: SINGLE);
   VAR
      Ndx: INTEGER;
   BEGIN
      FOR Ndx := LOW (Spr.Anims) TO HIGH (Spr.Anims) DO
         UpdateAnim (Spr.Anims[Ndx], Now);
   END;


   PROCEDURE UpdateTokenAnims (VAR Token: TToken; Now: SINGLE);
   BEGIN
      UpdateSpriteAnims (Token.Bot, Now);
      UpdateSpriteAnims (Token.Top, Now);
   END;



   PROCEDURE UpdateAnims (Now: SINGLE);
   VAR
      fl: PFlair;
      Ndx: INTEGER;
   BEGIN
      FOR Ndx := LOW (Tokens) TO HIGH (Tokens) DO
         UpdateTokenAnims (Tokens[Ndx], Now);
      FOR Ndx := LOW (Buttons) TO HIGH (Buttons) DO
         UpdateTokenAnims (Buttons[Ndx], Now);
      UpdateSpriteAnims (Glow, Now);
      UpdateSpriteAnims (GlowOverlay, Now);
      fl := Flairs;
      WHILE fl <> NIL DO
      BEGIN
         UpdateSpriteAnims (fl^.Sprite, Now);
         fl := fl^.Next;
      END;
   END;



(****************************************************************************
 * Drawing                                                                  *
 ****************************************************************************)

   PROCEDURE DrawSprite (CONST Spr: TSprite);
   VAR
      Bmp: ALLEGRO_BITMAPptr;
      Tint: ALLEGRO_COLOR;
      cx, cy: SINGLE;
   BEGIN
      Bmp := Images[Spr.Image];
      cx := Spr.AlignX * al_get_bitmap_width (Bmp);
      cy := Spr.AlignY * al_get_bitmap_height (Bmp);
      Tint := al_map_rgba_f (Spr.r, Spr.g, Spr.b, Spr.Opacity);
      al_draw_tinted_scaled_rotated_bitmap (
         Bmp, Tint, cx, cy,
         Spr.x, Spr.y, Spr.ScaleX, Spr.ScaleY, Spr.Angle, 0
      );
   END;



   PROCEDURE DrawToken (CONST Token: TToken);
   BEGIN
      DrawSprite (Token.Bot);
      DrawSprite (Token.Top);
   END;



   PROCEDURE DrawScreen;
   VAR
      fl: PFlair;
      Ndx: INTEGER;
   BEGIN
      al_clear_to_color (al_map_rgb (0, 0, 0));
      al_set_blender (ALLEGRO_ADD, ALLEGRO_ALPHA, ALLEGRO_ONE);
      DrawSprite (Glow);
      DrawSprite (GlowOverlay);
      FOR Ndx := LOW (Tokens) TO HIGH (Tokens) DO
         DrawToken (Tokens[Ndx]);
      al_set_blender (ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_INVERSE_ALPHA);
      FOR Ndx := LOW (Buttons) TO HIGH (Buttons) DO
         DrawToken (Buttons[Ndx]);
      al_set_blender (ALLEGRO_ADD, ALLEGRO_ALPHA, ALLEGRO_ONE);
      fl := Flairs;
      WHILE fl <> NIL DO
      BEGIN
         DrawSprite (fl^.Sprite);
         fl := fl^.Next;
      END;
      al_flip_display;
   END;



(****************************************************************************
 * Playback                                                                 *
 ****************************************************************************)

   PROCEDURE SpawnWindEffects (x, y: SINGLE);
   VAR
      Now: SINGLE;
      Spr: PSprite;
   BEGIN
      Now := al_get_time;
      Spr := CreateFlair (IMG_AIR_EFFECT, x, y, Now + 1);
      Anim (Spr^, @(Spr^.ScaleX), 0.9, 1.3, INTERP_FAST, 1);
      Anim (Spr^, @(Spr^.ScaleY), 0.9, 1.3, INTERP_FAST, 1);
      Anim (Spr^, @(Spr^.Opacity), 1, 0, INTERP_FAST, 1);
      Spr := CreateFlair (IMG_AIR_EFFECT, x, y, Now + 1.2);
      Anim (Spr^, @(Spr^.Opacity), 1, 0, INTERP_LINEAR, 1.2);
      Anim (Spr^, @(Spr^.ScaleX), 1.1, 1.5, INTERP_FAST, 1.2);
      Anim (Spr^, @(Spr^.ScaleY), 1.1, 0.5, INTERP_FAST, 1.2);
      AnimDelta (Spr^, @(spr^.x), 10, INTERP_FAST, 1.2);
   END;



   PROCEDURE SpawnFireEffects (x, y: SINGLE);
   VAR
      Now: SINGLE;
      Spr: PSprite;
      i: INTEGER;
   BEGIN
      Now := al_get_time;
      Spr := CreateFlair (IMG_MAIN_FLAME, x, y, Now + 0.8);
      Spr^.AlignY := 0.75;
      AnimFull (Spr^, @(Spr^.ScaleX),  0.2, 1.3, INTERP_BOUNCE, 0.0, 0.4);
      AnimFull (Spr^, @(Spr^.ScaleY),  0.2, 1.3, INTERP_BOUNCE, 0.0, 0.4);
      AnimFull (Spr^, @(Spr^.ScaleX),  1.3, 1.4, INTERP_BOUNCE, 0.4, 0.5);
      AnimFull (Spr^, @(Spr^.ScaleY),  1.3, 1.4, INTERP_BOUNCE, 0.4, 0.5);
      AnimFull (Spr^, @(Spr^.Opacity), 1.0, 0.0, INTERP_FAST, 0.3, 0.5);
      FOR i := 0 TO 2 DO
      BEGIN
         Spr := CreateFlair (IMG_FLAME, x, y, Now + 0.7);
         Spr^.AlignX := 1.3;
         Spr^.Angle := TWOPI / 3 * i;
         AnimDelta (Spr^, @(Spr^.Angle), -PI, INTERP_DOUBLE_FAST, 0.7);
         Anim (Spr^, @(Spr^.Opacity), 1.0, 0.0, INTERP_SLOW, 0.7);
         Anim (Spr^, @(Spr^.ScaleX),  0.2, 1.0, INTERP_FAST, 0.7);
         Anim (Spr^, @(Spr^.ScaleY),  0.2, 1.0, INTERP_FAST, 0.7);
      END;
   END;



   PROCEDURE SpawnWaterEffects (x, y: SINGLE);

      FUNCTION RandomSign: SINGLE;
      BEGIN
         IF Random (2) < 1 THEN
            RandomSign := -1
         ELSE
            RandomSign := 1
      END;

      FUNCTION RandomFloat (Min, Max: SINGLE): SINGLE;
      BEGIN
         RandomFloat := Random * (Max - Min) + Min;
      END;

   VAR
      MaxDuration: SINGLE;

      FUNCTION MRand (Min, Max: SINGLE): SINGLE;
      BEGIN
         MRand := RandomFloat (Min, Max) * MaxDuration;
      END;

   VAR
      Now: SINGLE;
      Spr: PSprite;
      i: INTEGER;
   BEGIN
      Now := al_get_time;
      MaxDuration := 1.0; { TODO: Why is this value a variable? }
      Spr := CreateFlair (IMG_WATER, x, y, Now + MaxDuration);
      Anim (Spr^, @(Spr^.ScaleX), 1.0, 2.0, INTERP_FAST, 0.5);
      Anim (Spr^, @(Spr^.ScaleY), 1.0, 2.0, INTERP_FAST, 0.5);
      Anim (Spr^, @(Spr^.Opacity), 0.5, 0.0, INTERP_FAST, 0.5);
      FOR i := 0 TO 8 DO
      BEGIN
         Spr := CreateFlair (IMG_WATER_DROPS, x, y, Now + MaxDuration);
         Spr^.ScaleX := RandomFloat (0.3, 1.2) * RandomSign;
         Spr^.ScaleY := RandomFloat (0.3, 1.2) * RandomSign;
         Spr^.Angle := RandomFloat (0, TWOPI);
         Spr^.r := RandomFloat (0, 0.6);
         Spr^.g := RandomFloat (0.4, 0.6);
         Spr^.b := 1;
         IF i = 0 THEN
            AnimTo (Spr^, @(Spr^.Opacity), 0, INTERP_LINEAR, MaxDuration)
         ELSE
            AnimTo (Spr^, @(Spr^.Opacity), 0, INTERP_DOUBLE_SLOW, MRand (0.7, 1));
         AnimTo (Spr^, @(Spr^.ScaleX), RandomFloat (0.8, 3), INTERP_FAST, MRand (0.7, 1));
         AnimTo (Spr^, @(Spr^.ScaleY), RandomFloat (0.8, 3), INTERP_FAST, MRand (0.7, 1));
         AnimTo (Spr^, @(Spr^.X), MRand (0, 20) * RandomSign, INTERP_FAST, MRand (0.7, 1));
         AnimTo (Spr^, @(Spr^.Y), MRand (0, 20) * RandomSign, INTERP_FAST, MRand (0.7, 1));
      END;
   END;



   PROCEDURE PlayElement (TheType, Pitch: INTEGER; Volume, Pan: SINGLE);
   BEGIN
      al_play_sample (ElementSamples[TheType][Pitch], Volume, Pan, 1.0, ALLEGRO_PLAYMODE_ONCE, NIL);
   END;



   PROCEDURE ActivateToken (Token: TToken);
   CONST
      sc = TokenScale;
   VAR
      Spr: PSprite;
   BEGIN
      Spr := @(Token.Top);
      CASE Token.TheType OF
      TYPE_EARTH:
         BEGIN
            PlayElement (TYPE_EARTH, Token.Pitch, 0.8, 0.0);
            Anim (Spr^, @(Spr^.ScaleX), Spr^.ScaleX + 0.4, Spr^.ScaleX, INTERP_FAST, 0.3);
            Anim (Spr^, @(Spr^.ScaleY), Spr^.ScaleY + 0.4, Spr^.ScaleY, INTERP_FAST, 0.3);
         END;
      TYPE_WIND:
         BEGIN
            PlayElement (TYPE_WIND, Token.Pitch, 0.8, 0.0);
            AnimFull (Spr^, @(Spr^.ScaleX), sc * 1.0, sc * 0.8, INTERP_SLOW_IN_OUT, 0.0, 0.5);
            AnimFull (Spr^, @(Spr^.ScaleX), sc * 0.8, sc * 1.0, INTERP_SLOW_IN_OUT, 0.5, 0.8);
            AnimFull (Spr^, @(Spr^.ScaleY), sc * 1.0, sc * 0.8, INTERP_SLOW_IN_OUT, 0.0, 0.5);
            AnimFull (Spr^, @(Spr^.ScaleY), sc * 0.8, sc * 1.0, INTERP_SLOW_IN_OUT, 0.5, 0.8);
            SpawnWindEffects (Spr^.x, Spr^.y);
         END;
      TYPE_WATER:
         BEGIN
            PlayElement (TYPE_WATER, Token.Pitch, 0.7, 0.5);
            AnimFull (Spr^, @(Spr^.ScaleX), sc * 1.3, sc * 0.8, INTERP_BOUNCE, 0.0, 0.5);
            AnimFull (Spr^, @(Spr^.ScaleX), sc * 0.8, sc * 1.0, INTERP_BOUNCE, 0.5, 0.5);
            AnimFull (Spr^, @(Spr^.ScaleY), sc * 0.8, sc * 1.3, INTERP_BOUNCE, 0.0, 0.5);
            AnimFull (Spr^, @(Spr^.ScaleY), sc * 1.3, sc * 1.0, INTERP_BOUNCE, 0.5, 0.5);
            SpawnWaterEffects (Spr^.x, Spr^.y);
         END;
      TYPE_FIRE:
         BEGIN
            PlayElement (TYPE_FIRE, Token.Pitch, 0.8, 0.0);
            Anim (Spr^, @(Spr^.ScaleX), sc * 1.3, sc, INTERP_SLOW_IN_OUT, 1.0);
            Anim (Spr^, @(Spr^.ScaleY), sc * 1.3, sc, INTERP_SLOW_IN_OUT, 1.0);
            SpawnFireEffects (Spr^.x, Spr^.y);
         END;
      END;
   END;



   PROCEDURE UpdatePlayback;
   VAR
      y: INTEGER;
   BEGIN
      FOR Y := 0 TO (TOKENS_Y - 1) DO
         ActivateToken (Tokens[Y * TOKENS_X + PlaybackColumn]);
      INC (PlaybackColumn);
      IF PlaybackColumn >= TOKENS_X THEN
         PlaybackColumn := 0;
   END;

(****************************************************************************
 * Control                                                                  *
 ****************************************************************************)

   FUNCTION IsTouched (Token: TToken; Size, x, y: SINGLE): BOOLEAN;
   VAR
      Half: SINGLE;
   BEGIN
      Half := Size / 2;
      IsTouched := (Token.x - Half <= x) AND (x < Token.x + Half)
               AND (Token.y - Half <= y) AND (y < Token.y + Half)
   END;



   FUNCTION GetTouchedToken (x, y: SINGLE): PToken;
   VAR
      Ndx: INTEGER;
   BEGIN
      GetTouchedToken := NIL;
      FOR Ndx := LOW (Tokens) TO HIGH (Tokens) DO
         IF IsTouched (Tokens[Ndx], TokenSize, x, y) THEN
         BEGIN
            GetTouchedToken := @Tokens[Ndx];
            EXIT;
         END;
   END;



   FUNCTION GetTouchedButton (x, y: SINGLE): PToken;
   VAR
      Ndx: INTEGER;
   BEGIN
      GetTouchedButton := NIL;
      FOR Ndx := LOW (Buttons) TO HIGH (Buttons) DO
         IF IsTouched (Buttons[Ndx], ButtonSize, x, y) THEN
         BEGIN
            GetTouchedButton := @Buttons[Ndx];
            EXIT;
         END;
   END;



   PROCEDURE SelectToken (Token: PToken);
   VAR
      Spr: PSprite;
   BEGIN
      IF (Token^.TheType = TYPE_NONE) AND (SelectedButton <> NIL) THEN
      BEGIN
         Spr := @(Token^.Top);
         Spr^.Image := SelectedButton^.TheType;
         AnimTo (Spr^, @(Spr^.Opacity), 1, INTERP_FAST, 0.15);
         Token^.TheType := SelectedButton^.TheType;
      END;
   END;



   PROCEDURE UnselectToken (Token: PToken);
   VAR
      Spr: PSprite;
   BEGIN
      IF Token^.TheType <> TYPE_NONE THEN
      BEGIN
         Spr := @(Token^.Top);
         AnimFull (Spr^, @(Spr^.Opacity), Spr^.Opacity, 0, INTERP_SLOW, 0.15, 0.15);
         Token^.TheType := TYPE_NONE;
      END;
   END;



   PROCEDURE UnselectAllTokens;
   VAR
      Ndx: INTEGER;
   BEGIN
      FOR Ndx := LOW (Tokens) TO HIGH (Tokens) DO
         UnselectToken (@Tokens[Ndx]);
   END;



   PROCEDURE ChangeHealthyGlow (TheType: INTEGER; x: SINGLE);
   BEGIN
      AnimTint (Glow, GlowColor[TheType], INTERP_SLOW_IN_OUT, 3.0);
      AnimTo (Glow, @Glow.x, x, INTERP_SLOW_IN_OUT, 3.0);

      AnimTint (GlowOverlay, GlowColor[TheType], INTERP_SLOW_IN_OUT, 4.0);
      AnimTo (GlowOverlay, @GlowOverlay.Opacity, 1.0, INTERP_SLOW_IN_OUT, 4.0);
   END;



   PROCEDURE SelectButton (Button: PToken);
   VAR
      Spr: PSprite;
   BEGIN
      IF Button = SelectedButton THEN
         EXIT;
      IF SelectedButton <> NIL THEN
      BEGIN
         Spr := @(SelectedButton^.Top);
         AnimTo (Spr^, @(Spr^.ScaleX), ButtonUnselScale, INTERP_SLOW, 0.3);
         AnimTo (Spr^, @(Spr^.ScaleY), ButtonUnselScale, INTERP_SLOW, 0.3);
         AnimTo (Spr^, @(Spr^.Opacity), 0.5, INTERP_DOUBLE_SLOW, 0.2);

         Spr := @(SelectedButton^.Bot);
         AnimTo (Spr^, @(Spr^.ScaleX), DropshadowUnselScale, INTERP_SLOW, 0.3);
         AnimTo (Spr^, @(Spr^.ScaleY), DropshadowUnselScale, INTERP_SLOW, 0.3);
      END;
      SelectedButton := Button;
      Spr := @(Button^.Top);
      AnimTo (Spr^, @(Spr^.ScaleX), ButtonSelScale, INTERP_FAST, 0.3);
      AnimTo (Spr^, @(Spr^.ScaleY), ButtonSelScale, INTERP_FAST, 0.3);
      AnimTo (Spr^, @(Spr^.Opacity), 1.0, INTERP_FAST, 0.3);
      Spr := @(Button^.Bot);
      AnimTo (Spr^, @(Spr^.ScaleX), DropshadowSelScale, INTERP_FAST, 0.3);
      AnimTo (Spr^, @(Spr^.ScaleY), DropshadowSelScale, INTERP_FAST, 0.3);
      ChangeHealthyGlow (Button^.TheType, Button^.x);
      al_play_sample (SelectSample, 1.0, 0.0, 1.0, ALLEGRO_PLAYMODE_ONCE, NIL);
   END;



   PROCEDURE onMouseDown (x, y: SINGLE; mButton: INTEGER);
   VAR
      Token, Button: PToken;
   BEGIN
      IF mButton = 1 THEN
      BEGIN
         Token := GetTouchedToken (x, y);
         IF Token <> NIL THEN
            SelectToken (Token)
         ELSE BEGIN
            Button := GetTouchedButton (x, y);
            IF Button <> NIL THEN
               SelectButton (Button);
         END;
      END
      ELSE IF mButton = 2 THEN
      BEGIN
         Token := GetTouchedToken (x, y);
         IF Token <> NIL THEN
            UnselectToken (Token);
      END;
   END;



   PROCEDURE onMouseAxes (x, y: SINGLE);
   VAR
      Token: PToken;
      Spr: PSprite;
   BEGIN
      Token := GetTouchedToken (x, y);
      IF Token = HoverToken THEN
         EXIT;
      IF HoverToken <> NIL THEN
      BEGIN
         Spr := @(HoverToken^.Bot);
         AnimTo (Spr^, @(Spr^.Opacity), 0.4, INTERP_DOUBLE_SLOW, 0.2);
      END;
      HoverToken := Token;
      IF HoverToken <> NIL THEN
      BEGIN
         Spr := @(HoverToken^.Bot);
         AnimTo (Spr^, @(Spr^.Opacity), 0.7, INTERP_FAST, 0.2);
      END;
   END;



   PROCEDURE MainLoop (Queue: ALLEGRO_EVENT_QUEUEptr);
   VAR
      Event: ALLEGRO_EVENT;
      EndLoop, Redraw: BOOLEAN;
      Now: SINGLE;
   BEGIN
      EndLoop := FALSE;
      Redraw := TRUE;
      REPEAT
         IF Redraw AND al_is_event_queue_empty (Queue) THEN
         BEGIN
            Now := al_get_time;
            FreeOldFlairs (Now);
            UpdateAnims (Now);
            DrawScreen;
            Redraw := FALSE;
         END;

         al_wait_for_event (Queue, Event);

         IF Event.timer.source = RefreshTimer THEN
            Redraw := true
         ELSE IF Event.timer.source = PlaybackTimer THEN
            UpdatePlayback
         ELSE IF Event._type = ALLEGRO_EVENT_MOUSE_AXES THEN
            onMouseAxes (Event.mouse.x, Event.mouse.y)
         ELSE IF Event._type = ALLEGRO_EVENT_MOUSE_BUTTON_DOWN THEN
            onMouseDown (event.mouse.x, event.mouse.y, event.mouse.button)
         ELSE IF Event._type = ALLEGRO_EVENT_DISPLAY_CLOSE THEN
            EndLoop := TRUE
         ELSE IF Event._type = ALLEGRO_EVENT_KEY_DOWN THEN
         BEGIN
            IF Event.keyboard.keycode = ALLEGRO_KEY_ESCAPE THEN
               EndLoop := TRUE;
            IF Event.keyboard.keycode = ALLEGRO_KEY_C THEN
               UnselectAllTokens;
         END;
      UNTIL EndLoop;
   END;

VAR
   Queue: ALLEGRO_EVENT_QUEUEptr;
BEGIN
   IF NOT al_init THEN
      AbortExample ('Error initialising Allegro.');
   IF NOT al_install_audio OR NOT al_reserve_samples (128) THEN
      AbortExample ('Error initialising audio.');
   al_init_acodec_addon();
   al_init_image_addon();

   al_set_new_bitmap_flags (ALLEGRO_MIN_LINEAR OR ALLEGRO_MAG_LINEAR);

   Display := al_create_display (ScreenW, ScreenH);
   IF Display = NIL THEN
      AbortExample ('Error creating display.');
   al_set_window_title (Display, 'Haiku - A Musical Instrument');

   LoadImages;
   LoadSamples;

   InitTokens;
   InitButtons;
   InitGlow;
   SelectButton (@(Buttons[TYPE_EARTH]));

   al_install_keyboard;
   al_install_mouse;

   RefreshTimer := al_create_timer (1.0 / RefreshRate);
   PlaybackTimer := al_create_timer (PlaybackPeriod / TOKENS_X);

   Queue := al_create_event_queue;
   al_register_event_source (Queue, al_get_display_event_source (Display));
   al_register_event_source (Queue, al_get_keyboard_event_source);
   al_register_event_source (Queue, al_get_mouse_event_source);
   al_register_event_source (Queue, al_get_timer_event_source (RefreshTimer));
   al_register_event_source (Queue, al_get_timer_event_source (PlaybackTimer));

   al_start_timer (RefreshTimer);
   al_start_timer (PlaybackTimer);

   MainLoop (Queue);

   FreeAllFlairs;
END.

/* vim: set sts=3 sw=3 et: */
