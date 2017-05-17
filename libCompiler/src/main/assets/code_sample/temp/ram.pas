var
    runtime: java_lang_Runtime;
    freeMemory, totalMemory, maxMemory: LongInt;
begin
    New(runtime);
    runtime := runtime.getRuntime();

    freeMemory := runtime.freeMemory() / 1024;
    totalMemory := runtime.totalMemory() / 1024;
    maxMemory := runtime.maxMemory() / 1024;

    writeln('freeMemory ' + freeMemory);
    writeln('totalMemory ' + totalMemory);
    writeln('maxMemory ' + maxMemory);

end.