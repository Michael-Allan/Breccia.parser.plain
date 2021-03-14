package Breccia.parser.plain;

import Breccia.parser.Markup;

import static Breccia.parser.plain.Language.impliesNewline;


public abstract class Markup_ implements Markup {


    protected Markup_() {}



   // ━━━  O b j e c t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override String toString() {
        final var b = new StringBuilder();
        final int column = column();
        b.append( getClass().getSimpleName() );
        b.append( ':' );
        b.append( lineNumber() );
        if( column != 0 ) {
            b.append( ':' );
            b.append( column() ); }
        b.append( ':' ).append( ' ' ).append( '{' );
        b.append( text() );
        for( int c = b.length() - 1, cBreak = 0;; --c ) { // Escape any trailing sequence of line breaks
            final char ch = b.charAt( c );               // for sake of readability.
            if( ch == '\n' ) b.setCharAt( cBreak = c, 'n' );
            else if( ch == '\r' ) b.setCharAt( cBreak = c, 'r' );
            else {
                assert !impliesNewline( ch );
                if( cBreak != 0 ) b.insert( cBreak, '\\' ); // One backslash for the whole sequence.
                break; }}
        b.append( '}' );
        return b.toString(); }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** The default implementation returns ‘Markup’.
      */
    public @Override String tagName() { return "Markup"; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
