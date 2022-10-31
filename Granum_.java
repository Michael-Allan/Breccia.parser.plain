package Breccia.parser.plain;

import Breccia.parser.AdjunctSlow;
import Breccia.parser.Granum;
import Java.CharacterPointer;
import Java.DelimitableCharSequence;

import static Breccia.parser.plain.Language.impliesNewline;
import static Java.CharBuffers.newDelimitableCharSequence;
import static java.util.Arrays.copyOf;


/** {@inheritDoc} <p>Warning: while the `{@linkplain Breccia.parser.DataReflector DataReflector}`
  * annotations of the `{@linkplain Breccia.parser Breccia.parser}` parent types are not repeated here,
  * nevertheless they apply in depth.  Instances of this class are, or may be, mere data reflectors.
  * The same applies to each component that is not of a primitive or otherwise naturally immutable type;
  * and so on, through all subcomponents.</p>
  *
  *     @see Breccia.parser.DataReflector
  */
abstract class Granum_ implements Granum {


    Granum_( final BrecciaCursor cursor ) {
        this.cursor = cursor;
        text = newDelimitableCharSequence( cursor.buffer ); }



    Granum_( final Fractum_<?> f ) { this( f.cursor ); }



    final DelimitableCharSequence text;



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override @AdjunctSlow CharacterPointer characterPointer( final int c ) {
        return cursor.characterPointer( text.start() + c ); }



    public final @Override CharSequence text() { return text; }



    public final @Override int xunc() { return cursor.xunc + text.start(); }



    public final @Override @AdjunctSlow int[] xuncFractalDescent() {
        final int hN = cursor.hierarchy.size();
        int[] xuncs = new int[hN];
        if( hN > 0 ) {
            int h = 0, x = 0;
            do {
                final var hierarch = cursor.hierarchy.get( h++ );
                if( hierarch != null ) xuncs[x++] = hierarch.xunc(); }
                while( h < hN );
            final int xN = x; // The number of xuncs copied from `hierarchy` above.
            if( xN < hN ) { // Then a null hierarch was encountered and `xuncs` is too long.
                xuncs = copyOf( xuncs, xN ); }} // Therefore truncate it to the correct length.
        return xuncs; }



   // ━━━  O b j e c t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override String toString() {
        final var b = new StringBuilder();
        final int column = column();
        final String className = getClass().getSimpleName();
        b.append( className.isEmpty()? tagName() : className );
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



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final BrecciaCursor cursor; }



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
