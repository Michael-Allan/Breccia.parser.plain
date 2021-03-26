package Breccia.parser.plain;

import Breccia.parser.Markup;
import java.nio.CharBuffer;
import java.util.List;


/** Composite markup; markup that has parsed components.
  *
  *     @param <L> The type of component list.
  *     @see FlatMarkup
  */
abstract class DeepMarkup<L extends List<Markup>> extends Markup_ {

    // No constructor is exposed — no allowance for implementing composites as subclasses
    // of `DeepMarkup` — as that would entail the needless complication and clutter
    // of passing constructor and type parameters up through the class hierarchy.


    private DeepMarkup( CharBuffer b, L components ) {
        super( b );
        this.components = components; }



    final L components;



    /** Makes an instance of deep markup with the generic tag name ‘Markup’.
      *
      *     @see #components()
      *     @see #tagName()
      */
    static <L extends List<Markup>> DeepMarkup<L> make( BrecciaCursor cursor, L components ) {
        return make( cursor, components, "Markup" ); }
      // Base class `Markup_` could not implement a default `tagName` (e.g. ‘Markup’) without
      // inadvertently overriding the default implementations of the `Breccia.parser` interfaces.



    /** Makes an instance of deep markup.
      *
      *     @see #components()
      *     @see #tagName()
      */
    static <L extends List<Markup>> DeepMarkup<L> make( final BrecciaCursor cursor,
          final L components, final String tagName ) {
        return new DeepMarkup<>( cursor.buffer, components ) {

            public @Override int column() { return cursor.bufferColumn( text.start() ); }

            public @Override int lineNumber() { return cursor.bufferLineNumber( text.start() ); }

            public @Override String tagName() { return tagName; }};}



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override L components() { return components; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
