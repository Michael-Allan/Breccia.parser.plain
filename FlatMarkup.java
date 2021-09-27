package Breccia.parser.plain;

import Breccia.parser.Markup;
import java.nio.CharBuffer;
import java.util.List;


/** Non-composite markup; markup that has no parsed components.
  */
public abstract class FlatMarkup extends Markup_ {


    public FlatMarkup( CharBuffer b ) { super( b ); }



    public FlatMarkup( Fractum_<?> f ) { super( f ); }



    /** Makes an instance of flat markup with the generic tag name ‘Markup’.
      *
      *     @see #tagName()
      */
    public static FlatMarkup make( BrecciaCursor cursor ) { return make( cursor, "Markup" ); }
      // Neither `Markup_` nor `FlatMarkup` could implement a default `tagName` (e.g. ‘Markup’) without
      // inadvertently overriding the default implementations of the `Breccia.parser` interfaces.



    /** Makes an instance of flat markup.
      *
      *     @see #tagName()
      */
    public static FlatMarkup make( final BrecciaCursor cursor, final String tagName ) {
        return new FlatMarkup( cursor.buffer ) {

            public @Override int column() { return cursor.bufferColumn( text.start() ); }

            public @Override int lineNumber() { return cursor.bufferLineNumber( text.start() ); }

            public @Override String tagName() { return tagName; }};}



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Returns an empty list: this markup has no parsed components,
      * only unparsed, {@linkplain #text() flat text}.
      */
    public final @Override List<Markup> components() { return List.of(); }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
