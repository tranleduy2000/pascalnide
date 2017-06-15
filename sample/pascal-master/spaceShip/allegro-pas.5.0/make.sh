#!/bin/bash
# FLAGS='-O3 -Xs- -XX'
# FLAGS='-dDEBUGMODE -g'
FLAGS='-g'
DIRS='-Fulib -FUobj -FEexamples'
fpc $FLAGS $DIRS examples/test.pas
fpc $FLAGS $DIRS examples/ex_audio_simple.pas
fpc $FLAGS $DIRS examples/ex_blit.pas
fpc $FLAGS $DIRS examples/ex_font.pas
fpc $FLAGS $DIRS examples/ex_gldepth.pas
fpc $FLAGS $DIRS examples/ex_haiku.pas
fpc $FLAGS $DIRS examples/ex_lines.pas
fpc $FLAGS $DIRS examples/ex_prim.pas
fpc $FLAGS $DIRS examples/ex_rotate.pas
fpc $FLAGS $DIRS examples/ex_scale.pas
fpc $FLAGS $DIRS examples/ex_transform.pas
fpc $FLAGS $DIRS examples/ex_warp_mouse.pas

