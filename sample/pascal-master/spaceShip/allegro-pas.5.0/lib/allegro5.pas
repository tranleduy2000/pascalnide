UNIT Allegro5;
(* Wrapper of the Allegro 5 core library. *)

{$include allegro.cfg}

INTERFACE

  USES
    al5base;

(* The code is distributed in sections.  Each section wraps with a header file (approx.). *)

(******************************************************************************
 * base.h
 *      Defines basic stuff needed by pretty much everything else.
 *
 *      By Shawn Hargreaves.
 **********)

  CONST
  (* Major version of Allegro. *)
    ALLEGRO_VERSION      =   5;
  (* Minor version of Allegro. *)
    ALLEGRO_SUB_VERSION  =   0;
  (* Revision number of Allegro. *)
    ALLEGRO_WIP_VERSION  =   6;
  (* Not sure we need it, but since ALLEGRO_VERSION_STR contains it:
     0 = SVN
     1 = first release
     2... = hotfixes?

     Note x.y.z (= x.y.z.0) has release number 1, and x.y.z.1 has release
     number 2, just to confuse you.
  *)
    ALLEGRO_RELEASE_NUMBER = 1;
  (* Packs version number in a simple AL_INT number. *)
    ALLEGRO_VERSION_INT  = (
	   (ALLEGRO_VERSION SHL 24)
	OR (ALLEGRO_SUB_VERSION SHL 16)
	OR (ALLEGRO_WIP_VERSION SHL  8)
	OR  ALLEGRO_RELEASE_NUMBER
    );
  (* Allegro PI. *)
    ALLEGRO_PI = 3.14159265358979323846;


  TYPE
  (* Description of user main function for al_run_main. *)
    ALLEGRO_USER_MAIN = FUNCTION (argc: AL_INT; argv: AL_POINTER): AL_INT; CDECL;


  (* Returns the (compiled) version of the Allegro library, packed into a single integer as groups of 8 bits.

     You can use code like this to extract the version number:
@longcode(#
  VAR
    Version: AL_INT;
    Major, Minor, Revision, Release: INTEGER;
  BEGIN
    Version := al_get_allegro_version;
    Major    :=  Version SHL 24;
    Minor    := (Version SHL 16) AND 255;
    Revision := (Version SHL  8) AND 255;
    Release  :=  Version         AND 255;
  END;
#)
    The release number is 0 for an unofficial version and 1 or greater for an official release. For example "5.0.2[1]” would be the (first) official 5.0.2 release while “5.0.2[0]” would be a compile of a version from the “5.0.2” branch before the official release.
 *)
  FUNCTION al_get_allegro_version: AL_UINT32; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* This function is useful in cases where you don’t have a @code(main) function but want to run Allegro (mostly useful in a wrapper library).  Under Windows and Linux this is no problem because you simply can call @link(al_install_system).  But some other system (like OSX) don’t allow calling @code(al_install_system) in the main thread.  @code(al_run_main) will know what to do in that case.  The passed @code(argc) and @code(argv) will simply be passed on to @code(user_main) and the return value of @code(user_main) will be returned.
 *)
  FUNCTION al_run_main (argc: AL_INT; argv: AL_POINTER; user_main: ALLEGRO_USER_MAIN): AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* This function can be used to create a packed 32 bit integer from 8 bit characters, on both 32 and 64 bit machines.  These can be used for various things, like custom datafile objects or system IDs. Example:

VAR
  OSTYPE_LINUX: LONGINT;
BEGIN
  OSTYPE_LINUX := AL_ID('TUX ');
END;
 *)
  FUNCTION AL_ID (str: SHORTSTRING): AL_INT;



(******************************************************************************
 * config.h
 ************)

{ TODO:
  At the moment I'll not include this header.  Object Pascal defines the
  TStrings class that implements a similar functionality.  Also both FCL and
  VCL defines classes that allows to manage INI files too.

  Actually I'll add this unit only if it's necessary because Allegro does need
  any special configuration file for internal use.
 }



(******************************************************************************
 * error.h
 *     Error handling.
 ***********)

(* Some Allegro functions will set an error number as well as returning an error code.  Call this function to retrieve the last error number set for the calling thread.
 *)
  FUNCTION al_get_errno: AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Set the error number for the calling thread. *)
  PROCEDURE al_set_errno (errnum: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;



(******************************************************************************
 * system.h *
 ************)

  TYPE
    ALLEGRO_SYSTEMptr = AL_POINTER;

  (* Pointer to @link(ALLEGRO_EVENT). *)
    ALLEGRO_EVENT_SOURCEptr = ^ALLEGRO_EVENT_SOURCE;
  (* An event source is any object which can generate events.  For example, an @link(ALLEGRO_DISPLAY) can generate events, and you can get the @code(ALLEGRO_EVENT_SOURCE) pointer from an @code(ALLEGRO_DISPLAY) with @link(al_get_display_event_source).

     You may create your own “user” event sources that emit custom events.
     @seealso(ALLEGRO_EVENT) @seealso(al_init_user_event_source) @seealso(al_emit_user_event)
   *)
    ALLEGRO_EVENT_SOURCE = RECORD
      __pad : ARRAY [0..31] OF AL_INT;
    END;


(* Like @link(al_install_system), but automatically passes in the version and uses the @code(atexit) function visible in the current compilation unit.
 *)
  FUNCTION al_init: AL_BOOL;

(* Initialize the Allegro system.  No other Allegro functions can be called before this (with one or two exceptions).
   @param(version Should always be set to @link(ALLEGRO_VERSION_INT).)
   @param(atexit_ptr If non-@nil, and if hasn’t been done already, @code(al_uninstall_system) will be registered as an atexit function.)
   @returns(@true if Allegro was successfully initialized by this function call @(or already was initialized previously@), @false if Allegro cannot be used.)
   @seealso(al_init)
 *)
  FUNCTION al_install_system (version: AL_INT; atexit_ptr: AL_POINTER): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Closes down the Allegro system.

   In most cases you don't need to call this, because it's called by the @code(FINALIZATION) section.
   @seealso(al_init) @seealso(al_install_system)
 *)
  PROCEDURE al_uninstall_system; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* @returns(@true if Allegro is initialized, otherwise returns @false.) *)
  FUNCTION al_is_system_installed: AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;



{ Modern Pascal compilers (i.e. Free Pascal) has functions and methods to get
  the path of system directories and the application name, so Allegro's
  functions for this aren't included.
}

(* This function allows the user to stop the system screensaver from starting up if @true is passed, or resets the system back to the default state (the state at program start) if @false is passed.
   @returns(@true if the state was set successfully, otherwise @false.)
 *)
  FUNCTION al_inhibit_screensaver (inhibit: AL_BOOL): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;



(******************************************************************************
 * color.h *
 ***********)

  TYPE
  (* An @code(ALLEGRO_COLOR) structure describes a color in a device independant way.  Use @link(al_map_rgb) et al. and @link(al_unmap_rgb) et al. to translate from and to various color representations.
   *)
    ALLEGRO_COLOR = RECORD
      r, g, b, a: AL_FLOAT;
    END;



(******************************************************************************
 * bitmap.h *
 ************)

  TYPE
  (* Abstract type representing a bitmap (2D image). *)
    ALLEGRO_BITMAPptr = AL_POINTER;

  (* Pixel formats.  Each pixel format specifies the exact size and bit layout of a pixel in memory.  Components are specified from high bits to low bits, so for example a fully opaque red pixel in ARGB_8888 format is 0xFFFF0000.

    @bold(Note:)
    The pixel format is independent of endianness.  That is, in the above example you can  always get the red component with

    @code(@(pixel AND $00ff0000@) SHR 16)

    But you can not rely on this code:

    @code(@(PBYTE @(pixel + 2@)@)^)

    It will return the red component on little endian systems, but the green component on big endian systems.

    Also note that Allegro’s naming is different from OpenGL naming here, where a format of @code(GL_RGBA8) merely defines the component order and the
    exact layout including endianness treatment is specified separately.  Usually @code(GL_RGBA8) will correspond to @code(ALLEGRO_PIXEL_ABGR_8888) though on little endian systems, so care must be taken (note the reversal of RGBA <-> ABGR).

    The only exception to this @code(ALLEGRO_PIXEL_FORMAT_ABGR_8888_LE) which will always have the components as 4 bytes corresponding to red, green, blue and alpha, in this order, independent of the endianness.
@unorderedlist(
  @item(ALLEGRO_PIXEL_FORMAT_ANY - Let the driver choose a format. This is the default format at
program start.)
  @item(ALLEGRO_PIXEL_FORMAT_ANY_NO_ALPHA - Let the driver choose a format without alpha.)
  @item(ALLEGRO_PIXEL_FORMAT_ANY_WITH_ALPHA - Let the driver choose a format with alpha.)
  @item(ALLEGRO_PIXEL_FORMAT_ANY_15_NO_ALPHA - Let the driver choose a 15 bit format without alpha.)
  @item(ALLEGRO_PIXEL_FORMAT_ANY_16_NO_ALPHA - Let the driver choose a 16 bit format without alpha.)
  @item(ALLEGRO_PIXEL_FORMAT_ANY_16_WITH_ALPHA - Let the driver choose a 16 bit format with alpha.)
  @item(ALLEGRO_PIXEL_FORMAT_ANY_24_NO_ALPHA - Let the driver choose a 24 bit format without alpha.)
  @item(ALLEGRO_PIXEL_FORMAT_ANY_32_NO_ALPHA - Let the driver choose a 32 bit format without alpha.)
  @item(ALLEGRO_PIXEL_FORMAT_ANY_32_WITH_ALPHA - Let the driver choose a 32 bit format with alpha.)
  @item(ALLEGRO_PIXEL_FORMAT_ARGB_8888 - 32 bit)
  @item(ALLEGRO_PIXEL_FORMAT_RGBA_8888 - 32 bit)
  @item(ALLEGRO_PIXEL_FORMAT_ARGB_4444 - 16 bit)
  @item(ALLEGRO_PIXEL_FORMAT_RGB_888 - 24 bit)
  @item(ALLEGRO_PIXEL_FORMAT_RGB_565 - 16 bit)
  @item(ALLEGRO_PIXEL_FORMAT_RGB_555 - 15 bit)
  @item(ALLEGRO_PIXEL_FORMAT_RGBA_5551 - 16 bit)
  @item(ALLEGRO_PIXEL_FORMAT_ARGB_1555 - 16 bit)
  @item(ALLEGRO_PIXEL_FORMAT_ABGR_8888 - 32 bit)
  @item(ALLEGRO_PIXEL_FORMAT_XBGR_8888 - 32 bit)
  @item(ALLEGRO_PIXEL_FORMAT_BGR_888 - 24 bit)
  @item(ALLEGRO_PIXEL_FORMAT_BGR_565 - 16 bit)
  @item(ALLEGRO_PIXEL_FORMAT_BGR_555 - 15 bit)
  @item(ALLEGRO_PIXEL_FORMAT_RGBX_8888 - 32 bit)
  @item(ALLEGRO_PIXEL_FORMAT_XRGB_8888 - 32 bit)
  @item(ALLEGRO_PIXEL_FORMAT_ABGR_F32 - 128 bit)
  @item(ALLEGRO_PIXEL_FORMAT_ABGR_8888_LE - Like the version without _LE, but the component order is guaranteed to be red, green, blue, alpha. This only makes a difference on big endian systems, on little endian it is just an alias.)
  @item(ALLEGRO_PIXEL_FORMAT_RGBA_4444 - 16bit)
)
    @seealso(al_set_new_bitmap_format) @seealso(al_get_bitmap_format)
   *)
    ALLEGRO_PIXEL_FORMAT = (
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_ANY := 0,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_ANY_NO_ALPHA,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_ANY_WITH_ALPHA,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_ANY_15_NO_ALPHA,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_ANY_16_NO_ALPHA,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_ANY_16_WITH_ALPHA,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_ANY_24_NO_ALPHA,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_ANY_32_NO_ALPHA,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_ANY_32_WITH_ALPHA,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_ARGB_8888,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_RGBA_8888,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_ARGB_4444,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_RGB_888,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_RGB_565,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_RGB_555,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_RGBA_5551,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_ARGB_1555,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_ABGR_8888,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_XBGR_8888,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_BGR_888,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_BGR_565,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_BGR_555,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_RGBX_8888,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_XRGB_8888,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_ABGR_F32,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_ABGR_8888_LE,
    { @exclude }
      ALLEGRO_PIXEL_FORMAT_RGBA_4444,
    { @exclude }
      ALLEGRO_NUM_PIXEL_FORMATS
    );

  CONST
  (* Bitmap flags.  Documented at al_set_new_bitmap_flags. *)
  { @exclude }
    ALLEGRO_MEMORY_BITMAP            = $0001;
  { @exclude }
    ALLEGRO_KEEP_BITMAP_FORMAT       = $0002;
  { @exclude }
    ALLEGRO_FORCE_LOCKING            = $0004;
  { @exclude }
    ALLEGRO_NO_PRESERVE_TEXTURE      = $0008;
  { @exclude }
    ALLEGRO_ALPHA_TEST               = $0010;
  { @exclude }
    _ALLEGRO_INTERNAL_OPENGL         = $0020;
  { @exclude }
    ALLEGRO_MIN_LINEAR               = $0040;
  { @exclude }
    ALLEGRO_MAG_LINEAR               = $0080;
  { @exclude }
    ALLEGRO_MIPMAP                   = $0100;
  { @exclude }
    ALLEGRO_NO_PREMULTIPLIED_ALPHA   = $0200;
  { @exclude }
    ALLEGRO_VIDEO_BITMAP             = $0400;



  (* Flags for the blitting functions.  Documented at al_draw_bitmap. *)
  { @exclude }
    ALLEGRO_FLIP_HORIZONTAL = $00001;
  { @exclude }
    ALLEGRO_FLIP_VERTICAL   = $00002;



  (* Locking flags. Documented at al_lock_bitmap. *)
  { @exclude }
    ALLEGRO_LOCK_READWRITE  = 0;
  { @exclude }
    ALLEGRO_LOCK_READONLY   = 1;
  { @exclude }
    ALLEGRO_LOCK_WRITEONLY  = 2;

  TYPE
  (* Blending modes.  Documented at al_set_blender. *)
    ALLEGRO_BLEND_MODE = (
    { @exclude }
      ALLEGRO_ZERO := 0,
    { @exclude }
      ALLEGRO_ONE := 1,
    { @exclude }
      ALLEGRO_ALPHA := 2,
    { @exclude }
      ALLEGRO_INVERSE_ALPHA := 3
    );



  (* Blending modes.  Documented at al_set_blender. *)
    ALLEGRO_BLEND_OPERATIONS = (
    { @exclude }
      ALLEGRO_ADD := 0,
    { @exclude }
      ALLEGRO_SRC_MINUS_DEST := 1,
    { @exclude }
      ALLEGRO_DEST_MINUS_SRC := 2,
    { @exclude }
      ALLEGRO_NUM_BLEND_OPERATIONS
    );


  (* Pointer to @link(ALLEGRO_LOCKED_REGION). *)
    ALLEGRO_LOCKED_REGIONptr = ^ALLEGRO_LOCKED_REGION;
  (* Users who wish to manually edit or read from a bitmap are required to lock it first.  The @code(ALLEGRO_LOCKED_REGION) structure represents the locked region of the bitmap.  This call will work with any bitmap, including memory bitmaps.
    @seealso(al_lock_bitmap) @seealso(al_lock_bitmap_region) @seealso(al_unlock_bitmap) @seealso(ALLEGRO_PIXEL_FORMAT)
   *)
    ALLEGRO_LOCKED_REGION = RECORD
    (* Points to the leftmost pixel of the first row (row 0) of the locked region. *)
      data: AL_VOIDptr;
    (* Indicates the pixel format of the data. *)
      format,
    (* Gives the size in bytes of a single row (also known as the stride).  The pitch may be greater than @code(width * pixel_size) due to padding; this is not uncommon.  It is also not uncommon for the pitch to be negative (the bitmap may be upside down). *)
      pitch,
    (* Number of bytes used to represent a single pixel. *)
      pixel_size: AL_INT;
    END;



(* Sets the pixel format for newly created bitmaps.  The default format is @code(ALLEGRO_PIXEL_FORMAT_ANY) and means the display driver will choose the best format.
  @seealso(ALLEGRO_PIXEL_FORMAT) @seealso(al_get_new_bitmap_format)
  @seealso(al_get_bitmap_format)
 *)
  PROCEDURE al_set_new_bitmap_format (format: ALLEGRO_PIXEL_FORMAT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Sets the flags to use for newly created bitmaps. Valid flags are:
   @unorderedlist(
     @item(@bold(ALLEGRO_VIDEO_BITMAP) Creates a bitmap that resides in the video card memory. These types of bitmaps receive the greatest benefit from hardware acceleration. @link(al_set_new_bitmap_flags) will implicitly set this flag unless @code(ALLEGRO_MEMORY_BITMAP) is present.)
     @item(@bold(ALLEGRO_MEMORY_BITMAP) Create a bitmap residing in system memory. Operations on, and with, memory bitmaps will not be hardware accelerated. However, direct pixel access can be relatively quick compared to video bitmaps, which depend on the display driver in use. @italic(Note: Allegro’s software rendering routines are currently very unoptimised.))
     @item(@bold(ALLEGRO_KEEP_BITMAP_FORMAT) Only used when loading bitmaps from disk files, forces the resulting ALLEGRO_BITMAP to use the same format as the file. @italic(This is not yet honoured.))
     @item(@bold(ALLEGRO_FORCE_LOCKING) When drawing to a bitmap with this flag set, always use pixel locking and draw to it using Allegro’s software drawing primitives. This should never be used if you plan to draw to the bitmap using Allegro’s graphics primitives as it would cause severe performance penalties. However if you know that the bitmap will only ever be accessed by locking it, no unneeded FBOs will be created for it in the OpenGL drivers.)
     @item(@bold(ALLEGRO_NO_PRESERVE_TEXTURE) Normally, every effort is taken to preserve the contents of bitmaps, since Direct3D may forget them. This can take extra processing time. If you know it doesn’t matter if a bitmap keeps its pixel data, for example its a temporary buffer, use this flag to tell Allegro not to attempt to preserve its contents. This can increase performance of your game or application, but there is a catch. See ALLEGRO_EVENT_DISPLAY_LOST for further information.)
     @item(@bold(ALLEGRO_ALPHA_TEST) This is a driver hint only. It tells the graphics driver to do alpha testing instead of alpha blending on bitmaps created with this flag. Alpha testing is usually faster and preferred if your bitmaps have only one level of alpha @(0@). This flag is currently not widely implemented @(i.e., only for memory bitmaps@).)
     @item(@bold(ALLEGRO_MIN_LINEAR) When drawing a scaled down version of the bitmap, use linear filtering. This usually looks better. You can also combine it with the MIPMAP flag for even better quality.)
     @item(@bold(ALLEGRO_MAG_LINEAR) When drawing a magnified version of a bitmap, use linear filtering. This will cause the picture to get blurry instead of creating a big rectangle for each pixel. It depends on how you want things to look like whether you want to use this or not.)
     @item(@bold(ALLEGRO_MIPMAP) This can only be used for bitmaps whose width and height is a power of two. In that case, it will generate mipmaps and use them when drawing scaled down versions. For example if the bitmap is 64x64, then extra bitmaps of sizes 32x32, 16x16, 8x8, 4x4, 2x2 and 1x1 will be created always containing a scaled down version of the original.)
     @item(@bold(ALLEGRO_NO_PREMULTIPLIED_ALPHA) By default, Allegro pre-multiplies the alpha channel of an image with the images color data when it loads it. Typically that would look something like this:
@longcode(#
  r := get_float_byte ();
  g := get_float_byte ();
  b := get_float_byte ();
  a := get_float_byte ();

  r := r * a;
  g := g * a;
  b := b * a;

  set_image_pixel (x, y, r, g, b, a);
#)
The reason for this can be seen in the Allegro example ex_premulalpha, ie, using pre-multiplied alpha gives more accurate color results in some cases. To use alpha blending with images loaded with pre-multiplied alpha, you would use the default blending mode, which is set with @code(al_set_blender @(ALLEGRO_ADD, ALLEGRO_ONE, ALLEGRO_INVERSE_ALPHA@)) to set the correct blender. This has some caveats. First, as mentioned above, drawing such an image can result in less accurate color blending @(when drawing an image with linear filtering on, the edges will be darker than they should be@). Second, the behaviour is somewhat confusing, which is explained in the example below.
@longcode(#
// Load and create bitmaps with an alpha channel
  al_set_new_bitmap_format (ALLEGRO_PIXEL_FORMAT_ANY_32_WITH_ALPHA);
// Load some bitmap with alpha in it
  bmp = al_load_bitmap ('some_alpha_bitmap.png');
// We will draw to this buffer and then draw this buffer to the screen
  tmp_buffer = al_create_bitmap (SCREEN_W, SCREEN_H);
// Set the buffer as the target and clear it
  al_set_target_bitmap (tmp_buffer);
  al_clear_to_color (al_map_rgba_f (0, 0, 0, 1));
// Draw the bitmap to the temporary buffer
  al_draw_bitmap (bmp, 0, 0, 0);
// Finally, draw the buffer to the screen
// The output will look incorrect (may take close inspection
// depending on the bitmap -- it may also be very obvious)
  al_set_target_bitmap (al_get_backbuffer (display));
  al_draw_bitmap (tmp_buffer, 0, 0, 0);
#)
     )
   )
   To explain further, if you have a pixel with 0.5 alpha, and you’re using (ALLEGRO_ADD, ALLEGRO_ALPHA, ALLEGRO_INVERSE_ALPHA) for blending, the formula is:
@longcode(#
  a := da * dst + sa * src
#)
Expands to:
@longcode(#
  result_a := dst_a * (1-0.5) + 0.5 * 0.5;
#)
   So if you draw the image to the temporary buffer, it is blended once resulting in 0.75 alpha, then drawn again to the screen, blended in the same way, resulting in a pixel has 0.1875 as an alpha value.
   @seealso(al_get_new_bitmap_flags) @seealso(al_get_bitmap_flags)
  *)
  PROCEDURE al_set_new_bitmap_flags (flags: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Returns the format used for newly created bitmaps.
   @seealso(ALLEGRO_PIXEL_FORMAT) @seealso(al_set_new_bitmap_format)
 *)
  FUNCTION al_get_new_bitmap_format: ALLEGRO_PIXEL_FORMAT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Returns the flags used for newly created bitmaps.
   @seealso(al_set_new_bitmap_flags)
 *)
  FUNCTION al_get_new_bitmap_flags: AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* A convenience function which does the same as @longcode(#
  al_set_new_bitmap_flags (al_get_new_bitmap_flags | flag);
#)
   @seealso(al_set_new_bitmap_flags) @seealso(al_get_new_bitmap_flags) @seealso(al_get_bitmap_flags)
 *)
  PROCEDURE al_add_new_bitmap_flag (flag: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;



(* Returns the width of a bitmap in pixels. *)
  FUNCTION al_get_bitmap_width (bitmap: ALLEGRO_BITMAPptr): AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Returns the height of a bitmap in pixels. *)
  FUNCTION al_get_bitmap_height (bitmap: ALLEGRO_BITMAPptr): AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Returns the pixel format of a bitmap.
   @seealso(ALLEGRO_PIXEL_FORMAT) @seealso(al_set_new_bitmap_flags)
 *)
  FUNCTION al_get_bitmap_format (bitmap: ALLEGRO_BITMAPptr): AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Return the flags user to create the bitmap.
   @seealso(al_set_new_bitmap_flags)
 *)
  FUNCTION al_get_bitmap_flags (bitmap: ALLEGRO_BITMAPptr): AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;



(* Creates a new bitmap using the bitmap format and flags for the current thread. Blitting between bitmaps of differing formats, or blitting between memory bitmaps and display bitmaps may be slow.

   Unless you set the ALLEGRO_MEMORY_BITMAP flag, the bitmap is created for the current display.  Blitting to another display may be slow.
If a display bitmap is created, there may be limitations on the allowed dimensions. For example a DirectX or OpenGL backend usually has a maximum allowed texture size - so if bitmap creation fails for very large dimensions, you may want to re-try with a smaller bitmap. Some platforms also dictate a minimum texture size, which is relevant if you plan to use this bitmap with the primitives addon. If you try to create a bitmap smaller than this, this call will not fail but the returned bitmap will be a section of a larger bitmap with the minimum size. This minimum size is 16 by 16.

   Some platforms do not directly support display bitmaps whose dimensions are not powers of two. Allegro handles this by creating a larger bitmap that has dimensions that are powers of two and then returning a section of that bitmap with the dimensions you requested. This can be relevant if you plan to use this bitmap with the primitives addon but shouldn’t be an issue otherwise.
   @seealso(al_set_new_bitmap_format) @seealso(al_set_new_bitmap_flags) @seealso(al_clone_bitmap) @seealso(al_create_sub_bitmap)
 *)
  FUNCTION al_create_bitmap (w, h: AL_INT): ALLEGRO_BITMAPptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Destroys the given bitmap, freeing all resources used by it. Does nothing if given the null pointer. *)
  PROCEDURE al_destroy_bitmap (Bitmap: ALLEGRO_BITMAPptr); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;



(* Blitting *)

(* Draws an unscaled, unrotated bitmap at the given position to the current target bitmap.  flags can be a combination of:
@unorderedlist(
  @item(ALLEGRO_FLIP_HORIZONTAL - flip the bitmap about the y-axis)
  @item(ALLEGRO_FLIP_VERTICAL - flip the bitmap about the x-axis)
)
   Note: The current target bitmap must be a different bitmap. Drawing a bitmap to itself (or to a sub-bitmap of itself) or drawing a sub-bitmap to its parent (or another sub-bitmap of its parent) are not currently supported. To copy part of a bitmap into the same bitmap simply use a temporary bitmap instead.

   Note: The backbuffer (or a sub-bitmap thereof) can not be transformed, blended or tinted. If you need to draw the backbuffer draw it to a temporary bitmap first with no active transformation (except translation). Blending and tinting settings/parameters will be ignored. This does not apply when drawing into a memory bitmap.
   @seealso(al_set_target_bitmap) @seealso(al_draw_bitmap_region) @seealso(al_draw_scaled_bitmap) @seealso(al_draw_rotated_bitmap) @seealso(al_draw_scaled_rotated_bitmap)
 *)
  PROCEDURE al_draw_bitmap (bitmap: ALLEGRO_BITMAPptr; dx, dy: AL_FLOAT; flags: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Draws a region of the given bitmap to the target bitmap.
   @param(sx source x)
   @param(sy source y)
   @param(sw source width @(width of region to blit@))
   @param(sh source height @(height of region to blit@))
   @param(dx destination x)
   @param(dy destination y)
   @param(flags same as for al_draw_bitmap)
   @seealso(al_draw_bitmap) @seealso(al_draw_scaled_bitmap) @seealso(al_draw_rotated_bitmap) @seealso(al_draw_scaled_rotated_bitmap)
  *)
  PROCEDURE al_draw_bitmap_region (bitmap: ALLEGRO_BITMAPptr; sx, sy, sw, sh, dx, dy: AL_FLOAT; flags: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Draws a scaled version of the given bitmap to the target bitmap.
   @param(sx source x)
   @param(sy source y)
   @param(sw source width)
   @param(sh source height)
   @param(dx destination x)
   @param(dy destination y)
   @param(dw destination width)
   @param(dh destination height)
   @param(flags same as for al_draw_bitmap)
   @seealso(al_draw_bitmap) @seealso(al_draw_bitmap_region) @seealso(al_draw_rotated_bitmap) @seealso(al_draw_scaled_rotated_bitmap)
 *)
  PROCEDURE al_draw_scaled_bitmap (bitmap: ALLEGRO_BITMAPptr; sx, sy, sw, sh, dx, dy, dw, dh: AL_FLOAT; flags: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Draws a rotated version of the given bitmap to the target bitmap.
   Example:
@longcode(#
VAR
  w, h: SINGLE;
BEGIN
  w := al_get_bitmap_width (bitmap);
  h := al_get_bitmap_height (bitmap);
  al_draw_rotated_bitmap (bitmap, w / 2, h / 2, x, y, ALLEGRO_PI / 2, 0);
#)
   The above code draws the bitmap centered on x/y and rotates it 90° clockwise.
   @param(cx center x @(relative to the left of bitmap@))
   @param(cy center y @(relative to the top or bitmap@))
   @param(dx destination x)
   @param(dy destination y)
   @param(angle angle in radians by which to rotate clockwise)
   @param(flags same as for al_draw_bitmap)
   @seealso(al_draw_bitmap) @seealso(al_draw_bitmap_region) @seealso(al_draw_scaled_bitmap) @seealso(al_draw_scaled_rotated_bitmap)
 *)
  PROCEDURE al_draw_rotated_bitmap (bitmap: ALLEGRO_BITMAPptr; cx, cy, dx, dy, angle: AL_FLOAT; flags: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Like @link(al_draw_rotated_bitmap), but can also scale the bitmap.

   The point at cx/cy in the bitmap will be drawn at dx/dy and the bitmap is rotated and scaled around this point.
   @param(cx center x @(relative to the left of bitmap@))
   @param(cy center y @(relative to the top or bitmap@))
   @param(dx destination x)
   @param(dy destination y)
   @param(xscale how much to scale on the x-axis @(e.g. 2 for twice the size@))
   @param(yscale how much to scale on the y-axis)
   @param(angle angle in radians by which to rotate clockwise)
   @param(flags same as for al_draw_bitmap)
   @seealso(al_draw_bitmap) @seealso(al_draw_bitmap_region) @seealso(al_draw_scaled_bitmap)
 *)
  PROCEDURE al_draw_scaled_rotated_bitmap (bitmap: ALLEGRO_BITMAPptr; cx, cy, dx, dy, xscale, yscale, angle: AL_FLOAT; flags: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;



(* Tinted blitting *)
  PROCEDURE al_draw_tinted_bitmap (bitmap: ALLEGRO_BITMAPptr; tint: ALLEGRO_COLOR; dx, dy: AL_FLOAT; flags: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_draw_tinted_bitmap_region (bitmap: ALLEGRO_BITMAPptr; tint: ALLEGRO_COLOR; sx, sy, sw, sh, dx, dy: AL_FLOAT; flags: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_draw_tinted_scaled_bitmap (bitmap: ALLEGRO_BITMAPptr; tint: ALLEGRO_COLOR; sx, sy, sw, sh, dx, dy, dw, dh: AL_FLOAT; flags: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Like @link(al_draw_rotated_bitmap) but multiplies all colors in the bitmap with the given color.
  @seealso(al_draw_tinted_bitmap)
 *)
  PROCEDURE al_draw_tinted_rotated_bitmap (bitmap: ALLEGRO_BITMAPptr; tint: ALLEGRO_COLOR; cx, cy, dx, dy, angle: AL_FLOAT; flags: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_draw_tinted_scaled_rotated_bitmap (bitmap: ALLEGRO_BITMAPptr; tint: ALLEGRO_COLOR; cx, cy, dx, dy, xscale, yscale, angle: AL_FLOAT; flags: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_draw_tinted_scaled_rotated_bitmap_region (bitmap: ALLEGRO_BITMAPptr; sx, sy, sw, sh: AL_FLOAT; tint: ALLEGRO_COLOR; cx, cy, dx, dy, xscale, yscale, angle: AL_FLOAT; flags: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;



(* Locking *)
  FUNCTION al_lock_bitmap (bitmap: ALLEGRO_BITMAPptr; format: ALLEGRO_PIXEL_FORMAT; flags: AL_INT): ALLEGRO_LOCKED_REGIONptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_lock_bitmap_region (bitmap: ALLEGRO_BITMAPptr; x, y, width, height: AL_INT; format: ALLEGRO_PIXEL_FORMAT; flags: AL_INT): ALLEGRO_LOCKED_REGIONptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_unlock_bitmap (bitmap: ALLEGRO_BITMAPptr); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;



  PROCEDURE al_put_pixel (x, y: AL_INT; color: ALLEGRO_COLOR); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_put_blended_pixel (x, y: AL_INT; color: ALLEGRO_COLOR); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_pixel (bitmap: ALLEGRO_BITMAPptr; x, y: AL_INT): ALLEGRO_COLOR; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_pixel_size (format: ALLEGRO_PIXEL_FORMAT): AL_INT;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Pixel mapping *)
  FUNCTION al_map_rgb (r, g, b: AL_UCHAR): ALLEGRO_COLOR; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_map_rgba (r, g, b, a: AL_UCHAR): ALLEGRO_COLOR; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_map_rgb_f (r, g, b: AL_FLOAT): ALLEGRO_COLOR; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_map_rgba_f (r, g, b, a: AL_FLOAT): ALLEGRO_COLOR; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Pixel unmapping *)
  PROCEDURE al_unmap_rgb (color: ALLEGRO_COLOR; VAR r, g, b: AL_UCHAR); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_unmap_rgba (color: ALLEGRO_COLOR; VAR r, g, b, a: AL_UCHAR); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_unmap_rgb_f (color: ALLEGRO_COLOR; VAR r, g, b: AL_FLOAT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_unmap_rgba_f (color: ALLEGRO_COLOR; VAR r, g, b, a: AL_FLOAT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_pixel_format_bits (format: AL_INT): AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Masking *)
  PROCEDURE al_convert_mask_to_alpha (bitmap: ALLEGRO_BITMAPptr; mask_color: ALLEGRO_COLOR); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Clipping *)
  PROCEDURE al_set_clipping_rectangle (x, y, width, height: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_restore_clipping_rectangle; CDECL
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_get_clipping_rectangle (VAR x, y, w, h: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Sub bitmaps *)
  FUNCTION al_create_sub_bitmap (parent: ALLEGRO_BITMAPptr; x, y, w, h: AL_INT): ALLEGRO_BITMAPptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_is_sub_bitmap (bitmap: ALLEGRO_BITMAPptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_parent_bitmap (bitmap: ALLEGRO_BITMAPptr): ALLEGRO_BITMAPptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Miscellaneous *)
  FUNCTION al_clone_bitmap (bitmap: ALLEGRO_BITMAPptr): ALLEGRO_BITMAPptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_is_bitmap_locked (bitmap: ALLEGRO_BITMAPptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Blending *)
  PROCEDURE al_set_blender (op: ALLEGRO_BLEND_OPERATIONS; source, dest: ALLEGRO_BLEND_MODE); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_get_blender (VAR op: ALLEGRO_BLEND_OPERATIONS; VAR source, dest: ALLEGRO_BLEND_MODE); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_set_separate_blender (op: ALLEGRO_BLEND_OPERATIONS; source, dest: ALLEGRO_BLEND_MODE;
				      alpha_op: ALLEGRO_BLEND_OPERATIONS; alpha_source, alpha_dest: ALLEGRO_BLEND_MODE); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_get_separate_blender (VAR op: ALLEGRO_BLEND_OPERATIONS; VAR source, dest: ALLEGRO_BLEND_MODE;
				     VAR alpha_op: ALLEGRO_BLEND_OPERATIONS; VAR alpha_source, alpha_dest: ALLEGRO_BLEND_MODE); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

  PROCEDURE _al_put_pixel (bitmap: ALLEGRO_BITMAPptr; x,y: AL_INT; color: ALLEGRO_COLOR); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;



(******************************************************************************
 * file.h
 ***************)

{ TODO:
  Actually, this header is needed by Allegro to define new loaders and savers,
  but at the moment I'll not add it. }



(******************************************************************************
 * bitmap_io.h *
 ***************)

{ TODO: Some functions need the file.h definitions. }

  FUNCTION al_load_bitmap (CONST filename: STRING): ALLEGRO_BITMAPptr; INLINE;
  FUNCTION al_save_bitmap (CONST filename: STRING; bitmap: ALLEGRO_BITMAPptr): AL_BOOL; INLINE;



(******************************************************************************
 * display.h *
 *************)


  CONST
    ALLEGRO_DEFAULT_DISPLAY_ADAPTER = -1;
  (* Possible bit combinations for the flags parameter of al_set_new_display_flags. *)
    ALLEGRO_DEFAULT                     = 0 SHL 0;
    ALLEGRO_WINDOWED                    = 1 SHL 0;
    ALLEGRO_FULLSCREEN                  = 1 SHL 1;
    ALLEGRO_OPENGL                      = 1 SHL 2;
    ALLEGRO_DIRECT3D_INTERNAL           = 1 SHL 3;
    ALLEGRO_RESIZABLE                   = 1 SHL 4;
    ALLEGRO_NOFRAME                     = 1 SHL 5;
    ALLEGRO_GENERATE_EXPOSE_EVENTS      = 1 SHL 6;
    ALLEGRO_OPENGL_3_0                  = 1 SHL 7;
    ALLEGRO_OPENGL_FORWARD_COMPATIBLE   = 1 SHL 8;
    ALLEGRO_FULLSCREEN_WINDOW           = 1 SHL 9;
    ALLEGRO_MINIMIZED                   = 1 SHL 10;

  TYPE
  (* Possible parameters for al_set_display_option.
   * Make sure to update ALLEGRO_EXTRA_DISPLAY_SETTINGS if you modify
   * anything here.
   *)
    ALLEGRO_DISPLAY_OPTIONS = (
      ALLEGRO_RED_SIZE,
      ALLEGRO_GREEN_SIZE,
      ALLEGRO_BLUE_SIZE,
      ALLEGRO_ALPHA_SIZE,
      ALLEGRO_RED_SHIFT,
      ALLEGRO_GREEN_SHIFT,
      ALLEGRO_BLUE_SHIFT,
      ALLEGRO_ALPHA_SHIFT,
      ALLEGRO_ACC_RED_SIZE,
      ALLEGRO_ACC_GREEN_SIZE,
      ALLEGRO_ACC_BLUE_SIZE,
      ALLEGRO_ACC_ALPHA_SIZE,
      ALLEGRO_STEREO,
      ALLEGRO_AUX_BUFFERS,
      ALLEGRO_COLOR_SIZE,
      ALLEGRO_DEPTH_SIZE,
      ALLEGRO_STENCIL_SIZE,
      ALLEGRO_SAMPLE_BUFFERS,
      ALLEGRO_SAMPLES,
      ALLEGRO_RENDER_METHOD,
      ALLEGRO_FLOAT_COLOR,
      ALLEGRO_FLOAT_DEPTH,
      ALLEGRO_SINGLE_BUFFER,
      ALLEGRO_SWAP_METHOD,
      ALLEGRO_COMPATIBLE_DISPLAY,
      ALLEGRO_UPDATE_DISPLAY_REGION,
      ALLEGRO_VSYNC,
      ALLEGRO_MAX_BITMAP_SIZE,
      ALLEGRO_SUPPORT_NPOT_BITMAP,
      ALLEGRO_CAN_DRAW_INTO_BITMAP,
      ALLEGRO_SUPPORT_SEPARATE_ALPHA,
      ALLEGRO_DISPLAY_OPTIONS_COUNT
    );

  CONST
    ALLEGRO_DONTCARE = 0;
    ALLEGRO_REQUIRE = 1;
    ALLEGRO_SUGGEST = 2;

  TYPE
    ALLEGRO_DISPLAY_ORIENTATION = (
      ALLEGRO_DISPLAY_ORIENTATION_0_DEGREES,
      ALLEGRO_DISPLAY_ORIENTATION_90_DEGREES,
      ALLEGRO_DISPLAY_ORIENTATION_180_DEGREES,
      ALLEGRO_DISPLAY_ORIENTATION_270_DEGREES,
      ALLEGRO_DISPLAY_ORIENTATION_FACE_UP,
      ALLEGRO_DISPLAY_ORIENTATION_FACE_DOWN
    );



    ALLEGRO_DISPLAYptr = AL_POINTER;



    ALLEGRO_DISPLAY_MODEptr = ^ALLEGRO_DISPLAY_MODE;
    ALLEGRO_DISPLAY_MODE = RECORD
      width, height, format, refresh_rate: AL_INT;
    END;



    ALLEGRO_MONITOR_INFO = RECORD
      x1, y1, x2, y2: AL_INT;
    END;

  PROCEDURE al_set_new_display_refresh_rate (refresh_rate: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_set_new_display_flags (flags: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_new_display_refresh_rate: AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_new_display_flags: AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;



  FUNCTION al_get_display_width (display: ALLEGRO_DISPLAYptr): AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_display_height (display: ALLEGRO_DISPLAYptr): AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_display_format (display: ALLEGRO_DISPLAYptr): AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_display_refresh_rate (display: ALLEGRO_DISPLAYptr): AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_display_flags (display: ALLEGRO_DISPLAYptr): AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_toggle_display_flag (display: ALLEGRO_DISPLAYptr; flag: AL_INT; onoff: AL_BOOL): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;



  FUNCTION al_create_display (w, h: AL_INT): ALLEGRO_DISPLAYptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_destroy_display (display: ALLEGRO_DISPLAYptr); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_current_display: ALLEGRO_DISPLAYptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_set_target_bitmap (Bitmap: ALLEGRO_BITMAPptr); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_set_target_backbuffer (display: ALLEGRO_DISPLAYptr); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_backbuffer (display: ALLEGRO_DISPLAYptr): ALLEGRO_BITMAPptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_target_bitmap: ALLEGRO_BITMAPptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

  FUNCTION al_acknowledge_resize (display: ALLEGRO_DISPLAYptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_resize_display (display: ALLEGRO_DISPLAYptr; width, height: AL_INT): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_flip_display; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_update_display_region (x, y, Width, height: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_is_compatible_bitmap (bitmap: ALLEGRO_BITMAPptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

  FUNCTION al_get_num_display_modes: AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_display_mode (index: AL_INT; mode: ALLEGRO_DISPLAY_MODEptr): ALLEGRO_DISPLAY_MODEptr;
    EXTERNAL ALLEGRO_LIB_NAME;

  FUNCTION al_wait_for_vsync: AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

  FUNCTION al_get_display_event_source (display: ALLEGRO_DISPLAYptr): ALLEGRO_EVENT_SOURCEptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Primitives *)
  PROCEDURE al_clear_to_color (color: ALLEGRO_COLOR); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_draw_pixel (x, y: AL_FLOAT; color: ALLEGRO_COLOR); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

  PROCEDURE al_set_display_icon (display: ALLEGRO_DISPLAYptr; icon: ALLEGRO_BITMAPptr); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Stuff for multihead/window management *)
  FUNCTION al_get_num_video_adapters: AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_monitor_info (adapter: AL_INT; VAR info: ALLEGRO_MONITOR_INFO): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_new_display_adapter: AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_set_new_display_adapter (adapter: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_set_new_window_position (x, y: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_get_new_window_position (VAR x, y: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_set_window_position (display: ALLEGRO_DISPLAYptr; x, y: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_get_window_position (display: ALLEGRO_DISPLAYptr; VAR x, y: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

  PROCEDURE al_set_window_title (display: ALLEGRO_DISPLAYptr; CONST title: STRING); INLINE;

  PROCEDURE al_set_new_display_option (option: ALLEGRO_DISPLAY_OPTIONS; value, importance: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_new_display_option (option: ALLEGRO_DISPLAY_OPTIONS; VAR importance: AL_INT): AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_reset_new_display_options; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_display_option (display: ALLEGRO_DISPLAYptr; option: ALLEGRO_DISPLAY_OPTIONS): AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Deferred drawing *)
  PROCEDURE al_hold_bitmap_drawing (hold: AL_BOOL); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_is_bitmap_drawing_held: AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;



(******************************************************************************
 * keycodes.h *
 **************)

{$include keycodes.inc}



(******************************************************************************
 * keyboard.h *
 **************)

  TYPE
    ALLEGRO_KEYBOARDptr = AL_POINTER;


    ALLEGRO_KEYBOARD_STATEptr = ^ALLEGRO_KEYBOARD_STATE;
    ALLEGRO_KEYBOARD_STATE = RECORD
      display: ALLEGRO_DISPLAYptr;
    { @exclude internal }
      __key_down__internal__: ARRAY [0..((ALLEGRO_KEY_MAX * 31) DIV 32) - 1] OF AL_UINT;
    END;

  FUNCTION al_is_keyboard_installed: AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_install_keyboard: AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_uninstall_keyboard; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

  FUNCTION al_set_keyboard_leds (leds: AL_INT): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

  FUNCTION al_keycode_to_name (keycode: AL_INT): AL_STRptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

  PROCEDURE al_get_keyboard_state (VAR ret_state: ALLEGRO_KEYBOARD_STATE); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_key_down (VAR state: ALLEGRO_KEYBOARD_STATE; keycode: AL_INT): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

  FUNCTION al_get_keyboard_event_source: ALLEGRO_EVENT_SOURCEptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;



(******************************************************************************
 * joystick.h *
 **************)

  CONST
(* internal values *)
    _AL_MAX_JOYSTICK_AXES    = 3;
    _AL_MAX_JOYSTICK_STICKS  = 8;
    _AL_MAX_JOYSTICK_BUTTONS = 32;

  TYPE
    ALLEGRO_JOYSTICKptr = AL_POINTER;



    ALLEGRO_JOYSTICK_STATE = RECORD
      stick: ARRAY [0.._AL_MAX_JOYSTICK_STICKS - 1] OF RECORD
	axis: ARRAY [0.._AL_MAX_JOYSTICK_AXES - 1] OF AL_FLOAT; { -1.0 to 1.0 }
      END;
      button: ARRAY [0.._AL_MAX_JOYSTICK_BUTTONS - 1] OF AL_INT; { 0 to 32767 }
    END;



    ALLEGRO_JOYFLAGS = (
      ALLEGRO_JOYFLAG_DIGITAL  := $01,
      ALLEGRO_JOYFLAG_ANALOGUE := $02
    );

  FUNCTION al_install_joystick: AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_uninstall_joystick; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_is_joystick_installed: AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_reconfigure_joysticks: AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

  FUNCTION al_get_num_joysticks: AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_joystick (joyn: AL_INT): ALLEGRO_JOYSTICKptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_release_joystick (j: ALLEGRO_JOYSTICKptr); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_joystick_active (j: ALLEGRO_JOYSTICKptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_joystick_name (j: ALLEGRO_JOYSTICKptr): AL_STRptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

  FUNCTION al_get_joystick_num_sticks (j: ALLEGRO_JOYSTICKptr): AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_joystick_stick_flags (j: ALLEGRO_JOYSTICKptr; stick: AL_INT): AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_joystick_stick_name (j: ALLEGRO_JOYSTICKptr; stick: AL_INT): AL_STRptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

  FUNCTION al_get_joystick_num_axes (j: ALLEGRO_JOYSTICKptr; stick: AL_INT): AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_joystick_axis_name (j: ALLEGRO_JOYSTICKptr; stick, axis: AL_INT): AL_STRptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

  FUNCTION al_get_joystick_num_buttons (j: ALLEGRO_JOYSTICKptr): AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_joystick_button_name (j: ALLEGRO_JOYSTICKptr; buttonn: AL_INT): AL_STRptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

  PROCEDURE al_get_joystick_state (j: ALLEGRO_JOYSTICKptr; VAR ret_state: ALLEGRO_JOYSTICK_STATE); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

  FUNCTION al_get_joystick_event_source: ALLEGRO_EVENT_SOURCEptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;



(******************************************************************************
 * mouse.h *
 ***********)

  CONST
  (* Allow up to four extra axes for future expansion. *)
    ALLEGRO_MOUSE_MAX_EXTRA_AXES = 4;

  TYPE
    ALLEGRO_MOUSEptr = AL_POINTER;

  (* Type: ALLEGRO_MOUSE_STATE *)
    ALLEGRO_MOUSE_STATEptr = ^ALLEGRO_MOUSE_STATE;
    ALLEGRO_MOUSE_STATE = RECORD
    (* (x, y) Primary mouse position
     * (z) Mouse wheel position (1D 'wheel'), or,
     * (w, z) Mouse wheel position (2D 'ball')
     * display - the display the mouse is on (coordinates are relative to this)
     * pressure - the pressure appleid to the mouse (for stylus/tablet)
     *)
      x, y, z, w: AL_INT;
      more_axes: ARRAY [0..(ALLEGRO_MOUSE_MAX_EXTRA_AXES - 1)] OF AL_INT;
      buttons: AL_INT;
      pressure: AL_FLOAT;
      display: ALLEGRO_DISPLAYptr;
    END;


  (* Mouse cursors *)
    ALLEGRO_MOUSE_CURSORptr = AL_POINTER;

    ALLEGRO_SYSTEM_MOUSE_CURSOR = (
      ALLEGRO_SYSTEM_MOUSE_CURSOR_NONE        :=  0,
      ALLEGRO_SYSTEM_MOUSE_CURSOR_DEFAULT     :=  1,
      ALLEGRO_SYSTEM_MOUSE_CURSOR_ARROW       :=  2,
      ALLEGRO_SYSTEM_MOUSE_CURSOR_BUSY        :=  3,
      ALLEGRO_SYSTEM_MOUSE_CURSOR_QUESTION    :=  4,
      ALLEGRO_SYSTEM_MOUSE_CURSOR_EDIT        :=  5,
      ALLEGRO_SYSTEM_MOUSE_CURSOR_MOVE        :=  6,
      ALLEGRO_SYSTEM_MOUSE_CURSOR_RESIZE_N    :=  7,
      ALLEGRO_SYSTEM_MOUSE_CURSOR_RESIZE_W    :=  8,
      ALLEGRO_SYSTEM_MOUSE_CURSOR_RESIZE_S    :=  9,
      ALLEGRO_SYSTEM_MOUSE_CURSOR_RESIZE_E    := 10,
      ALLEGRO_SYSTEM_MOUSE_CURSOR_RESIZE_NW   := 11,
      ALLEGRO_SYSTEM_MOUSE_CURSOR_RESIZE_SW   := 12,
      ALLEGRO_SYSTEM_MOUSE_CURSOR_RESIZE_SE   := 13,
      ALLEGRO_SYSTEM_MOUSE_CURSOR_RESIZE_NE   := 14,
      ALLEGRO_SYSTEM_MOUSE_CURSOR_PROGRESS    := 15,
      ALLEGRO_SYSTEM_MOUSE_CURSOR_PRECISION   := 16,
      ALLEGRO_SYSTEM_MOUSE_CURSOR_LINK        := 17,
      ALLEGRO_SYSTEM_MOUSE_CURSOR_ALT_SELECT  := 18,
      ALLEGRO_SYSTEM_MOUSE_CURSOR_UNAVAILABLE := 19,
      ALLEGRO_NUM_SYSTEM_MOUSE_CURSORS
    );

  FUNCTION al_is_mouse_installed: AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_install_mouse: AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_uninstall_mouse; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_mouse_num_buttons: AL_UINT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_mouse_num_axes: AL_UINT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_set_mouse_xy (display: ALLEGRO_DISPLAYptr; x, y: AL_INT): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_set_mouse_z (z: AL_INT): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_set_mouse_w (w: AL_INT): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_set_mouse_axis (axis, value: AL_INT): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_get_mouse_state (ret_state: ALLEGRO_MOUSE_STATEptr); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_mouse_button_down (CONST state: ALLEGRO_MOUSE_STATEptr; button: AL_INT): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_mouse_state_axis (CONST state: ALLEGRO_MOUSE_STATEptr; axis: AL_INT): AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

  FUNCTION al_get_mouse_event_source: ALLEGRO_EVENT_SOURCEptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(*
 * Cursors:
 *
 * This will probably become part of the display API.  It provides for
 * hardware cursors only; software cursors may or may not be provided
 * for later (it would need significant cooperation from the display
 * API).
 *)
  FUNCTION al_create_mouse_cursor (sprite: ALLEGRO_BITMAPptr; xfocus, yfocus: AL_INT): ALLEGRO_MOUSE_CURSORptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_destroy_mouse_cursor (cursor: ALLEGRO_MOUSE_CURSORptr);
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_set_mouse_cursor (display: ALLEGRO_DISPLAYptr; cursor: ALLEGRO_MOUSE_CURSORptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_set_system_mouse_cursor (display: ALLEGRO_DISPLAYptr; cursor_id: ALLEGRO_SYSTEM_MOUSE_CURSOR): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_show_mouse_cursor (display: ALLEGRO_DISPLAYptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_hide_mouse_cursor (display: ALLEGRO_DISPLAYptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_mouse_cursor_position (VAR ret_x, ret_y: AL_INT): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_grab_mouse (display: ALLEGRO_DISPLAYptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_ungrab_mouse: AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;



(******************************************************************************
 * timer.h *
 ***********)

(* Converts microseconds to seconds. *)
  FUNCTION ALLEGRO_USECS_TO_SECS (x: AL_DOUBLE): AL_DOUBLE; INLINE;

(* Converts milliseconds to seconds. *)
  FUNCTION ALLEGRO_MSECS_TO_SECS (x: AL_DOUBLE): AL_DOUBLE; INLINE;

(* Converts beats per second to seconds. *)
  FUNCTION ALLEGRO_BPS_TO_SECS (x: AL_DOUBLE): AL_DOUBLE; INLINE;

(* Converts beats per minute to seconds. *)
  FUNCTION ALLEGRO_BPM_TO_SECS (x: AL_DOUBLE): AL_DOUBLE; INLINE;

  TYPE
    ALLEGRO_TIMERptr = POINTER;

  FUNCTION al_create_timer (speed_secs: AL_DOUBLE): ALLEGRO_TIMERptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_destroy_timer (timer: ALLEGRO_TIMERptr); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_start_timer (timer: ALLEGRO_TIMERptr); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_stop_timer (timer: ALLEGRO_TIMERptr); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_timer_started (CONST timer: ALLEGRO_TIMERptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_timer_speed (CONST timer: ALLEGRO_TIMERptr): AL_DOUBLE; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_set_timer_speed (timer: ALLEGRO_TIMERptr; speed_secs: AL_DOUBLE); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_timer_count (CONST timer: ALLEGRO_TIMERptr): AL_INT64; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_set_timer_count (timer: ALLEGRO_TIMERptr; count: AL_INT64); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_add_timer_count (timer: ALLEGRO_TIMERptr; diff: AL_INT64); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_timer_event_source (timer: ALLEGRO_TIMERptr): ALLEGRO_EVENT_SOURCEptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;



(******************************************************************************
 * altime.h *
 ************)

  TYPE
    ALLEGRO_TIMEOUTptr = ^ALLEGRO_TIMEOUT;
  (* This is an abstract data type representing a timer object. *)
    ALLEGRO_TIMEOUT = RECORD
      __pad1__, __pad2__: AL_INT64;
    END;

  FUNCTION al_get_time: AL_DOUBLE; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_rest (seconds: AL_DOUBLE); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* TODO: Change to VAR? *)
  PROCEDURE al_init_timeout (timeout: ALLEGRO_TIMEOUTptr; seconds: AL_DOUBLE); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;



(******************************************************************************
 * events.h *
 ************)

  TYPE
    ALLEGRO_EVENT_TYPE = AL_UINT;

  CONST
    ALLEGRO_EVENT_JOYSTICK_AXIS          = 1;
    ALLEGRO_EVENT_JOYSTICK_BUTTON_DOWN   = 2;
    ALLEGRO_EVENT_JOYSTICK_BUTTON_UP     = 3;
    ALLEGRO_EVENT_JOYSTICK_CONFIGURATION = 4;

    ALLEGRO_EVENT_KEY_DOWN               = 10;
    ALLEGRO_EVENT_KEY_CHAR               = 11;
    ALLEGRO_EVENT_KEY_UP                 = 12;

    ALLEGRO_EVENT_MOUSE_AXES             = 20;
    ALLEGRO_EVENT_MOUSE_BUTTON_DOWN      = 21;
    ALLEGRO_EVENT_MOUSE_BUTTON_UP        = 22;
    ALLEGRO_EVENT_MOUSE_ENTER_DISPLAY    = 23;
    ALLEGRO_EVENT_MOUSE_LEAVE_DISPLAY    = 24;
    ALLEGRO_EVENT_MOUSE_WARPED           = 25;

    ALLEGRO_EVENT_TIMER                  = 30;

    ALLEGRO_EVENT_DISPLAY_EXPOSE         = 40;
    ALLEGRO_EVENT_DISPLAY_RESIZE         = 41;
    ALLEGRO_EVENT_DISPLAY_CLOSE          = 42;
    ALLEGRO_EVENT_DISPLAY_LOST           = 43;
    ALLEGRO_EVENT_DISPLAY_FOUND          = 44;
    ALLEGRO_EVENT_DISPLAY_SWITCH_IN      = 45;
    ALLEGRO_EVENT_DISPLAY_SWITCH_OUT     = 46;
    ALLEGRO_EVENT_DISPLAY_ORIENTATION    = 47;

(*    1 <= n < 512  - builtin events
 *  512 <= n < 1024 - reserved user events (for addons)
 * 1024 <= n        - unreserved user events
 *)
  FUNCTION ALLEGRO_EVENT_TYPE_IS_USER (t: ALLEGRO_EVENT_TYPE): AL_BOOL; INLINE;

(*
 * Event structures
 *
 * All event types have the following fields in common.
 *
 *  _type     -- the type of event this is
 *  timestamp -- when this event was generated
 *  source    -- which event source generated this event
 *
 * For people writing event sources: The common fields must be at the
 * very start of each event structure.
 *)
  TYPE
    ALLEGRO_ANY_EVENT = RECORD
      _type : ALLEGRO_EVENT_TYPE;
      source : ALLEGRO_EVENT_SOURCEptr;
      timestamp : AL_DOUBLE;
    END;

    ALLEGRO_DISPLAY_EVENT = RECORD
      _type : ALLEGRO_EVENT_TYPE;
      source : ALLEGRO_DISPLAYptr;
      timestamp : AL_DOUBLE;
      x : AL_INT;
      y : AL_INT;
      width : AL_INT;
      height : AL_INT;
      orientation : AL_INT;
    END;

    ALLEGRO_JOYSTICK_EVENT = RECORD
      _type : ALLEGRO_EVENT_TYPE;
      source : ALLEGRO_JOYSTICKptr;
      timestamp : AL_DOUBLE;
      id : ALLEGRO_JOYSTICKptr;
      stick : AL_INT;
      axis : AL_INT;
      pos : AL_FLOAT;
      button : AL_INT;
    END;

    ALLEGRO_KEYBOARD_EVENT = RECORD
      _type : ALLEGRO_EVENT_TYPE;
      source : ALLEGRO_KEYBOARDptr;
      timestamp : AL_DOUBLE;
      display : ALLEGRO_DISPLAYptr;
      keycode : AL_INT;
      unichar : AL_INT;
      modifiers : AL_UINT;
      _repeat : AL_BOOL;
    END;

    ALLEGRO_MOUSE_EVENT = RECORD
      _type : ALLEGRO_EVENT_TYPE;
      source : ALLEGRO_MOUSEptr;
      timestamp : AL_DOUBLE;
      display : ALLEGRO_DISPLAYptr;
      x, y, z, w : AL_INT;
      dx, dy, dz, dw : AL_INT;
      button : AL_UINT;
      pressure : AL_FLOAT;
    END;

    ALLEGRO_TIMER_EVENT = RECORD
      _type : ALLEGRO_EVENT_TYPE;
      source : ALLEGRO_TIMERptr;
      timestamp : AL_DOUBLE;
      count : AL_INT64;
      error : AL_DOUBLE;
    END;

    ALLEGRO_USER_EVENT_DESCRIPTORptr = POINTER;

    ALLEGRO_USER_EVENTptr = ^ALLEGRO_USER_EVENT;
    ALLEGRO_USER_EVENT = RECORD
      _type : ALLEGRO_EVENT_TYPE;
      source : ALLEGRO_EVENT_SOURCEptr;
      timestamp : AL_DOUBLE;
      __internal__descr : ALLEGRO_USER_EVENT_DESCRIPTORptr;
      data1 : AL_POINTER;
      data2 : AL_POINTER;
      data3 : AL_POINTER;
      data4 : AL_POINTER;
    END;

    ALLEGRO_EVENTptr = ^ALLEGRO_EVENT;

    ALLEGRO_EVENT = RECORD
      case LONGINT OF
   (* This must be the same as the first field of _AL_EVENT_HEADER.  *)
	0 : ( _type : ALLEGRO_EVENT_TYPE );
   (* `any' is to allow the user to access the other fields which are
    * common to all event types, without using some specific type
    * structure.
    *)
	1 : ( any : ALLEGRO_ANY_EVENT );
	2 : ( display : ALLEGRO_DISPLAY_EVENT );
	3 : ( joystick : ALLEGRO_JOYSTICK_EVENT );
	4 : ( keyboard : ALLEGRO_KEYBOARD_EVENT );
	5 : ( mouse : ALLEGRO_MOUSE_EVENT );
	6 : ( timer : ALLEGRO_TIMER_EVENT );
	7 : ( user : ALLEGRO_USER_EVENT );
      END;

      ALLEGRO_EVENT_DTOR_PROC = PROCEDURE (evt: ALLEGRO_USER_EVENTptr); CDECL;

(* Event sources *)
  PROCEDURE al_init_user_event_source (source: ALLEGRO_EVENT_SOURCEptr); CDECL; { TODO: Use VAR parameter? }
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_destroy_user_event_source (source: ALLEGRO_EVENT_SOURCEptr); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* The second argument is ALLEGRO_EVENT instead of ALLEGRO_USER_EVENT
 * to prevent users passing a pointer to a too-short structure.
 *)
  FUNCTION al_emit_user_event (source: ALLEGRO_EVENT_SOURCEptr; Event: ALLEGRO_EVENTptr; dtor: ALLEGRO_EVENT_DTOR_PROC): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_unref_user_event (event: ALLEGRO_USER_EVENTptr); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_set_event_source_data (source: ALLEGRO_EVENT_SOURCEptr; data: AL_POINTER); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_event_source_data (CONST source: ALLEGRO_EVENT_SOURCEptr): AL_POINTER; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

  TYPE
    ALLEGRO_EVENT_QUEUEptr = AL_POINTER;

  FUNCTION al_create_event_queue: ALLEGRO_EVENT_QUEUEptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_destroy_event_queue (queue: ALLEGRO_EVENT_QUEUEptr); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_register_event_source (queue: ALLEGRO_EVENT_QUEUEptr; source: ALLEGRO_EVENT_SOURCEptr); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_unregister_event_source (queue: ALLEGRO_EVENT_QUEUEptr; source: ALLEGRO_EVENT_SOURCEptr); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_is_event_queue_empty (queue: ALLEGRO_EVENT_QUEUEptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_next_event (queue: ALLEGRO_EVENT_QUEUEptr; OUT event: ALLEGRO_EVENT): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_peek_next_event (queue: ALLEGRO_EVENT_QUEUEptr; OUT event: ALLEGRO_EVENT): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_drop_next_event (queue: ALLEGRO_EVENT_QUEUEptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_flush_event_queue (queue: ALLEGRO_EVENT_QUEUEptr); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_wait_for_event (queue: ALLEGRO_EVENT_QUEUEptr; OUT event: ALLEGRO_EVENT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_wait_for_event_timed (queue: ALLEGRO_EVENT_QUEUEptr; OUT event: ALLEGRO_EVENT; secs: AL_FLOAT): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_wait_for_event_until (queue: ALLEGRO_EVENT_QUEUEptr; OUT event: ALLEGRO_EVENT; timeout: ALLEGRO_TIMEOUTptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;



(******************************************************************************
 * transformations.h *
 *********************)

  TYPE
    ALLEGRO_TRANSFORMptr = ^ALLEGRO_TRANSFORM;
    ALLEGRO_TRANSFORM = RECORD
      m: ARRAY [0..3] OF ARRAY [0..3] OF AL_FLOAT;
    END;

(* Transformations*)
  PROCEDURE al_use_transform (VAR trans: ALLEGRO_TRANSFORM); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_copy_transform (OUT dest: ALLEGRO_TRANSFORM; VAR src: ALLEGRO_TRANSFORM); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_identity_transform (OUT trans: ALLEGRO_TRANSFORM); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_build_transform (OUT trans: ALLEGRO_TRANSFORM; x, y, sx, sy, theta: AL_FLOAT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_translate_transform (VAR trans: ALLEGRO_TRANSFORM; x, y: AL_FLOAT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_rotate_transform (VAR trans: ALLEGRO_TRANSFORM; theta: AL_FLOAT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_scale_transform (VAR trans: ALLEGRO_TRANSFORM; sx, sy: AL_FLOAT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_transform_coordinates (VAR trans: ALLEGRO_TRANSFORM; VAR x, y: AL_FLOAT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_compose_transform (VAR trans, other: ALLEGRO_TRANSFORM); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_get_current_transform: ALLEGRO_TRANSFORMptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_invert_transform (VAR trans: ALLEGRO_TRANSFORM); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_check_inverse (VAR trans: ALLEGRO_TRANSFORM; tol: AL_FLOAT): AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;



(******************************************************************************
 * utf8.h *
 **********)

  {TODO: Documentation says it's not needed as it's used internally.
	Only basic functionality is implemented for convenience.

	Use of WIDESTRING and UTFSTRING is recommendable. }

  TYPE
    _al_tagbstring = RECORD
      mlen, slen: AL_INT;
      data: AL_VOIDptr;
    END;



    ALLEGRO_USTRptr = ^ALLEGRO_USTR;
    ALLEGRO_USTR = _al_tagbstring;



    ALLEGRO_USTR_INFOptr = ^ALLEGRO_USTR_INFO;
    ALLEGRO_USTR_INFO = _al_tagbstring;

(* Creating strings *)
  FUNCTION al_ustr_new (CONST s: STRING): ALLEGRO_USTRptr; INLINE;
  FUNCTION al_ustr_new_from_buffer (CONST s: AL_STRptr; size: AL_SIZE_T): ALLEGRO_USTRptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_ustr_free (us: ALLEGRO_USTRptr); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_cstr (CONST us: ALLEGRO_USTRptr): AL_STRptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_ustr_to_buffer (CONST us: ALLEGRO_USTRptr; buffer: AL_STRptr; size: AL_INT); CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_cstr_dup (CONST us: ALLEGRO_USTRptr): AL_STRptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_ustr_dup (CONST us: ALLEGRO_USTRptr): ALLEGRO_USTRptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_ustr_dup_substr (CONST us: ALLEGRO_USTRptr; start_pos, end_pos: AL_INT): ALLEGRO_USTRptr; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;

(* Assign *)
  FUNCTION al_ustr_assign (us1: ALLEGRO_USTRptr; CONST us2: ALLEGRO_USTRptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_ustr_assign_cstr (us1: ALLEGRO_USTRptr; CONST s: STRING): AL_BOOL; INLINE;

(* Compare *)
  FUNCTION al_ustr_equal (CONST us1, us2: ALLEGRO_USTRptr): AL_BOOL; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_ustr_compare (CONST u, v: ALLEGRO_USTRptr): AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;
  FUNCTION al_ustr_ncompare (CONST u, v: ALLEGRO_USTRptr): AL_INT; CDECL;
    EXTERNAL ALLEGRO_LIB_NAME;



(******************************************************************************
 * tls.h
 *      Thread local storage routines.
 *********)

  TYPE
    ALLEGRO_STATE_FLAGS = (
      ALLEGRO_STATE_NEW_DISPLAY_PARAMETERS := $0001,
      ALLEGRO_STATE_NEW_BITMAP_PARAMETERS  := $0002,
      ALLEGRO_STATE_DISPLAY                := $0004,
      ALLEGRO_STATE_TARGET_BITMAP          := $0008,
      ALLEGRO_STATE_BITMAP                 := $000A, {<ALLEGRO_STATE_TARGET_BITMAP + ALLEGRO_STATE_NEW_BITMAP_PARAMETERS, }
      ALLEGRO_STATE_BLENDER                := $0010,
      ALLEGRO_STATE_NEW_FILE_INTERFACE     := $0020,
      ALLEGRO_STATE_TRANSFORM              := $0040,

      ALLEGRO_STATE_ALL                    := $FFFF
    );



    ALLEGRO_STATE = RECORD
    { Internally, a thread_local_state structure is placed here. }
      _tls: ARRAY [0..1023] OF AL_CHAR;
    END;

  PROCEDURE al_store_state (VAR state: ALLEGRO_STATE; flags: ALLEGRO_STATE_FLAGS);
    EXTERNAL ALLEGRO_LIB_NAME;
  PROCEDURE al_restore_state (VAR state: ALLEGRO_STATE);
    EXTERNAL ALLEGRO_LIB_NAME;



IMPLEMENTATION

(******************************************************************************
 * base.h *
 **********)

  FUNCTION AL_ID (str: SHORTSTRING): AL_INT;
  BEGIN
    AL_ID := (ORD (str[1]) SHL 24) OR (ORD (str[2]) SHL 16)
	     OR (ORD (str[3]) SHL  8) OR  ORD (str[4]);
  END;



(******************************************************************************
 * system.h *
 ************)

  FUNCTION al_init: AL_BOOL;
  BEGIN
    al_init := al_install_system (ALLEGRO_VERSION_INT, NIL);
  END;

(******************************************************************************
 * bitmap_io.h *
 ***************)

  FUNCTION _al_load_bitmap_ (CONST filename: AL_STRptr): ALLEGRO_BITMAPptr; CDECL;
  EXTERNAL ALLEGRO_LIB_NAME NAME 'al_load_bitmap';

  FUNCTION al_load_bitmap (CONST filename: STRING): ALLEGRO_BITMAPptr;
  BEGIN
    al_load_bitmap := _al_load_bitmap_ (AL_STRptr (Filename));
  END;



  FUNCTION _al_save_bitmap_ (CONST filename: AL_STRptr; bitmap: ALLEGRO_BITMAPptr): AL_BOOL; CDECL;
  EXTERNAL ALLEGRO_LIB_NAME NAME 'al_save_bitmap';

  FUNCTION al_save_bitmap (CONST filename: STRING; bitmap: ALLEGRO_BITMAPptr): AL_BOOL;
  BEGIN
    al_save_bitmap := _al_save_bitmap_ (AL_STRptr (Filename), bitmap);
  END;



(******************************************************************************
 * display.h *
 *************)

  PROCEDURE _al_set_window_title_ (display: ALLEGRO_DISPLAYptr; CONST title: AL_STRptr); CDECL;
  EXTERNAL ALLEGRO_LIB_NAME NAME 'al_set_window_title';

  PROCEDURE al_set_window_title (display: ALLEGRO_DISPLAYptr; CONST title: STRING);
  BEGIN
    _al_set_window_title_ (display, AL_STRptr (Title));
  END;



(******************************************************************************
 * timer.h *
 ***********)

  FUNCTION ALLEGRO_USECS_TO_SECS (x: AL_DOUBLE): AL_DOUBLE;
  BEGIN
    ALLEGRO_USECS_TO_SECS := x / 1000000
  END;

  FUNCTION ALLEGRO_MSECS_TO_SECS (x: AL_DOUBLE): AL_DOUBLE;
  BEGIN
    ALLEGRO_MSECS_TO_SECS := x / 1000
  END;

  FUNCTION ALLEGRO_BPS_TO_SECS (x: AL_DOUBLE): AL_DOUBLE;
  BEGIN
    ALLEGRO_BPS_TO_SECS := 1 / x
  END;

  FUNCTION ALLEGRO_BPM_TO_SECS (x: AL_DOUBLE): AL_DOUBLE;
  BEGIN
    ALLEGRO_BPM_TO_SECS := 60 / x
  END;



(******************************************************************************
 * events.h *
 ************)

  FUNCTION ALLEGRO_EVENT_TYPE_IS_USER (t: ALLEGRO_EVENT_TYPE): AL_BOOL;
  BEGIN
    ALLEGRO_EVENT_TYPE_IS_USER := t >= 512;
  END;



(******************************************************************************
 * utf8.h *
 **********)

  FUNCTION _al_ustr_new_ (CONST s: AL_STRptr): ALLEGRO_USTRptr; CDECL;
  EXTERNAL ALLEGRO_LIB_NAME NAME 'al_ustr_new';

  FUNCTION al_ustr_new (CONST s: STRING): ALLEGRO_USTRptr;
  BEGIN
    al_ustr_new := _al_ustr_new_ (AL_STRptr (s));
  END;



  FUNCTION _al_ustr_assign_cstr_ (us1: ALLEGRO_USTRptr; CONST s: AL_STRptr): AL_BOOL; CDECL;
  EXTERNAL ALLEGRO_LIB_NAME NAME 'al_ustr_assign_cstr';

  FUNCTION al_ustr_assign_cstr (us1: ALLEGRO_USTRptr; CONST s: STRING): AL_BOOL;
  BEGIN
    al_ustr_assign_cstr := _al_ustr_assign_cstr_ (us1, AL_STRptr (s));
  END;

INITIALIZATION
{ Delphi forces an INITIALIZATION section if FINALIZATION is used. }
  ;
{ Suggested by FPC mailing list user. }

{ $if defined(cpui386) or defined(cpux86_64)}
{ SetExceptionMask(GetExceptionMask + [exZeroDivide, exInvalidOp]); }
{ $ENDIF}

FINALIZATION
{ Ensures that we call it, as Pascal hasn't an "atexit" function. }
  al_uninstall_system;
END.
