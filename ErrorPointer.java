package Breccia.parser.plain;

import static Java.Characters.isJavaOrUnicodeWhitespace;
import static java.lang.Character.charCount;


/** Indicant of where within markup a (precisely located) parse error was detected.
  */
public final class ErrorPointer {


    /** @see MalformedMarkup#lineNumber
      * @see #line
      * @see #column
      */
    ErrorPointer( int lineNumber, String line, int column ) {
        this.lineNumber = lineNumber;
        this.line = line;
        this.column = column; }



    /** The columnar offset at which the malformed markup was detected.
      *
      *     @see Markup#column()
      */
    public final int column;



    /** A copy of the source line containing the malformed markup, extending as far
      * as the parse buffer allows, up to and including any terminal newline.
      */
    public final String line;



    /** @see MalformedMarkup#lineNumber
      */
    public final int lineNumber;



    /** Returns a sanitized copy of the source line concatenated to a second line
      * comprising a column marker.
      */
    public String markedLine() {
        final StringBuilder b = new StringBuilder();
        for( int ch, c = 0, cN = line.length(); c < cN; c += charCount(ch) ) { // Sanitize:
            ch = line.codePointAt( c ); // By full code point, not to miss the problematic ones.
            b.appendCodePoint( isJavaOrUnicodeWhitespace(ch)? ' ' : ch ); }
        b.append( '\n' );
        for( int c = column; c > 0; --c ) b.append( ' ' );
        b.append( '^' );
        return b.toString(); }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
