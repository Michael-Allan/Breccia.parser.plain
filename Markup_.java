package Breccia.parser.plain;

import Breccia.parser.Markup;

import static Breccia.parser.plain.Language.impliesNewline;


public abstract class Markup_ implements Markup {


    protected Markup_() {}



    /** @see Object#toString()
      */
    public static String toString( final Markup m ) {
        final StringBuilder b = new StringBuilder();
        final int column = m.column();
        b.append( m.tagName() );
        b.append( ':' );
        b.append( m.lineNumber() );
        if( column != 0 ) {
            b.append( ':' );
            b.append( m.column() ); }
        b.append( ':' ).append( ' ' ).append( '{' );
        b.append( m.text() );
        for( int c = b.length() - 1, cBreak = 0;; --c ) { // Escape any trailing sequence of line breaks
            final char ch = b.charAt( c );               // for sake of readability.
            if( ch == '\n' ) b.setCharAt( cBreak = c, 'n' );
            else if( ch == '\r' ) b.setCharAt( cBreak = c, 'r' );
            else {
                assert !impliesNewline( ch );
                if( cBreak != 0 ) b.insert( cBreak, '\\' ); // One backslash for the whole sequence.
                break; }}
        b.append( '}' );
        return b.toString(); }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
