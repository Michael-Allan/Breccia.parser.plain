package Breccia.parser.plain;

import Breccia.parser.Markup;
import Java.DelimitableCharSequence;
import java.nio.CharBuffer;

import static Breccia.parser.plain.Language.impliesNewline;
import static Java.CharBuffers.newDelimitableCharSequence;


/** {@inheritDoc}  <p>Warning: while the `{@linkplain Breccia.parser.DataReflector DataReflector}`
  * annotations of the `{@linkplain Breccia.parser Breccia.parser}` parent types are not repeated here,
  * nevertheless they apply in depth.  Instances of this class are, or may be, mere data reflectors.
  * The same applies to each component that is not of a primitive or otherwise naturally immutable type;
  * and so on, through all subcomponents.</p>
  *
  *     @see Breccia.parser.DataReflector
  */
abstract class Markup_ implements Markup {


    Markup_( final CharBuffer buffer ) { text = newDelimitableCharSequence( buffer ); }



    Markup_( final Fractum_ f ) { this( f.cursor.buffer ); }



    final DelimitableCharSequence text;



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override CharSequence text() { return text; }



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
        return b.toString(); }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
