# Overview
Provides classes that manage various media interfaces in audio and video.
The Media APIs are used to play and, in some cases, record media files. This includes audio (e.g., play MP3s or other music files, ringtones, game sound effects, or DTMF tones) and video (e.g., play a video streamed over the web or from local storage).

# How to import

``uses aMedia;``

# Example

# Documentation

## assignMedia

**Declaration**

``procedure assignMedia(url: string; key: string)``

**Description** Open a media file

**Arguments**
* ``url`` url of media file, you can use **insert media link** feature in menu of application. It supports several different media sources such as:

    1. Local resources
    2. Internal URIs, such as one you might obtain from a Content Resolver
    3. External URLs (streaming)
* ``key`` the key for identifying resource

## closeMedia

**Declaration**

``procedure closeMedia(key: string)``

**Description** Close file media

**Argument**

* ``key`` the key for identifying resource

**Exception**
Media not assign before

## isMediaPlaying

**Declaration**

``function isMediaPlaying(key: string): boolean;``

**Description**
Checks whether the MediaPlayer is playing.

**Argument**

* ``key`` the key for identifying resource

**Retrun**
``true`` if currently playing, ``false`` otherwise

**Exception**
Media not assign before

## mediaSeekTo

**Declaration**
``procedure mediaSeekTo(msec: long; key: string);``

**Description**
Moves the media to specified time position by considering the given mode.

**Argument**

* ``msec`` the offset in milliseconds from the start to seek to
* ``key`` the key for identifying resource

**Exception**
Media not assign before

## pauseMedia

**Declaration**
``procedure pauseMedia(key: string);``

**Description**
Pauses playback. Call playMedia() to resume.

**Argument**

* ``key`` the key for identifying resource

**Exception**
Media not assign before

## playMedia

**Declaration**
``procedure playMedia(key: string);``

**Description**
Starts or resumes playback. If playback had previously been paused, playback will continue from where it was paused. If playback had been stopped, or never started before, playback will start at the beginning.

**Argument**

* ``key`` the key for identifying resource

**Exception**
Media not assign before


## setMediaLoop

**Declaration**
``procedure setMediaLoop(enable: boolean; key: string);``

**Description** Sets the player to be looping or non-looping.

**Argument**

* ``enable`` whether to loop or not
* ``key`` the key for identifying resource

**Exception**
Media not assign before

## getMediaCurrentPosition

**Declaration**

``function getMediaCurrentPosition(key: string): integer;``

**Description** Gets the current playback position.

**Argument**

* ``key`` the key for identifying resource

**Exception**
Media not assign before

## getDuration

**Declaration**

``function getDuration(key: string): integer;``

**Description** Gets the duration of the file.

**Argument**

* ``key`` the key for identifying resource

**Exception**
Media not assign before

## getDuration

**Declaration**

``function getDuration(key: string): integer;``

**Description** Gets the duration of the file.

**Argument**

* ``key`` the key for identifying resource

**Exception**
Media not assign before

## isMediaLooping
**Declaration**

``function isMediaLooping(key: string): boolean;``

**Description** Checks whether the MediaPlayer is looping or non-looping.

**Argument**

* ``key`` the key for identifying resource

**Exception**
Media not assign before

## getMediaUrl
**Declaration**

``function getMediaUrl(key: string): string;``

**Description** Return url of media

**Argument**

* ``key`` the key for identifying resource

**Exception**
Media not assign before



