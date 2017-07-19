package com.duy.pascal.interperter.tokenizer;

import java.io.Reader;

/**
 * inner class used to store info for nested
 * input streams
 */
public final class ZzFlexStreamInfo {
    Reader zzReader;
    int zzEndRead;
    int zzStartRead;
    int zzCurrentPos;
    int zzMarkedPos;
    int yyline;
    int yycolumn;
    char[] zzBuffer;
    boolean zzAtEOF;
    boolean zzEOFDone;

    /**
     * sets all values stored in this class
     */
    ZzFlexStreamInfo(Reader zzReader, int zzEndRead, int zzStartRead,
                     int zzCurrentPos, int zzMarkedPos,
                     char[] zzBuffer, boolean zzAtEOF, int yyline, int yycolumn) {
        this.zzReader = zzReader;
        this.zzEndRead = zzEndRead;
        this.zzStartRead = zzStartRead;
        this.zzCurrentPos = zzCurrentPos;
        this.zzMarkedPos = zzMarkedPos;
        this.zzBuffer = zzBuffer;
        this.zzAtEOF = zzAtEOF;
        this.zzEOFDone = zzEOFDone;
        this.yyline = yyline;
        this.yycolumn = yycolumn;
    }
}
