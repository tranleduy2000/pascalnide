COMPILER = fpc
DEBUGGER = gdb
OUTPUT = bac_2008_10H 
FLAGS = -gl -o$(OUTPUT) -Mtp
#INPUT = < input.txt
SOURCE = bac_2008_10H.pas
all: $(SOURCE)
	$(COMPILER) $(FLAGS) $(SOURCE)
run: all $(OUTPUT)
	./$(OUTPUT) $(INPUT)
debug: all
	$(DEBUGGER) ./$(OUTPUT)
