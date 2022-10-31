package Breccia.parser.plain;

import Breccia.parser.AdjunctSlow;
import Breccia.parser.Granum;
import java.util.List;


/** A non-composite granum, having no parsed components.
  */
public abstract class FlatGranum extends Granum_ {


    public FlatGranum( BrecciaCursor cursor ) { super( cursor ); }



    public FlatGranum( Fractum_<?> f ) { super( f.cursor ); }



    /** Makes a flat granum with the generic tag name ‘Granum’.
      *
      *     @see #tagName()
      */
    public static FlatGranum make( BrecciaCursor cursor ) { return make( cursor, "Granum" ); }
      // Neither `Granum_` nor `FlatGranum` could implement a default `tagName` (e.g. ‘Granum’) without
      // inadvertently overriding the default implementations of the `Breccia.parser` interfaces.



    /** Makes a flat granum with the given tag name.
      *
      *     @see #tagName()
      */
    public static FlatGranum make( final BrecciaCursor cursor, final String tagName ) {
        return new FlatGranum( cursor ) {

            public @Override @AdjunctSlow int column() { return cursor.bufferColumn( text.start() ); }

            public @Override @AdjunctSlow int lineNumber() {
                return cursor.bufferLineNumber( text.start() ); }

            public @Override String tagName() { return tagName; }}; }



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Returns an empty list: this granum has no parsed components,
      * only unparsed, {@linkplain #text() flat text}.
      */
    public final @Override List<Granum> components() { return List.of(); }}



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
