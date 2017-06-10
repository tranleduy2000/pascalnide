uses aMedia, crt;
var
    key: string;
begin
    key := 'myMusic';

    {open a media file, replace url of your media}
    assignMedia('path to file media', key);

    playMedia(key);

    {pause 10 second}
    delay(10000);

    closeMedia(key);
end.
