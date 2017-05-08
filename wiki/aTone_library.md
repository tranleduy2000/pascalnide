# Overview
This library provides API to play DTMF tones (ITU-T Recommendation Q.23)

# Function and procedure

### generateTones

``procedure generateTones(number: string; duration: integer);``

**Description** Generate DTMF tones for the given phone number.

**Arguments**

* ``number``
* ``duration`` The tone duration in milliseconds.


### generateSound

``procedure generateSound(frequency: integer; duration: integer);``

**Description** Generate and play a sound with frequency in duration (milliseconds)

**Arguments**

* ``frequency`` frequency of sound
* ``duration`` The tone duration in milliseconds.


