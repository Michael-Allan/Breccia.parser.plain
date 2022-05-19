package Breccia.parser.plain;

import Breccia.parser.Markup;
import Breccia.parser.Point;
import java.util.List;


/** @param <C> The type of cursor.
  */
abstract class Point_<C extends BrecciaCursor> extends BodyFractum_<C> implements Point {


    Point_( C cursor ) { super( cursor ); }



    final FlatMarkup bullet = new FlatMarkup( this ) {

        public @Override int column() { return cursor.fractumIndentWidth; }

        public @Override int lineNumber() { return Point_.this.lineNumber(); }

        public @Override String tagName() { return "Bullet"; }};



    /** Late composition.  Do not call this method directly.  It is called by `ensureComposition` alone,
      * which in turn is called by the access methods of any parts of this point that allow for
      * late composition, namely `descriptor.components` (if a descriptor is present)
      * and any others specified by subclasses, q.v.
      *
      *     @see #ensureComposition()
      *     @see Descriptor#components()
      */
    abstract void compose() throws MalformedMarkup;



    /** Assurance of composition.  This method is called by the access methods of any parts of this point
      * that allow for efficient parsing through late composition, viz. subsequent to `commit`.
      * See `compose` for the specific access methods.
      *
      *     @see #commit()
      *     @see #compose()
      */
    final void ensureComposition() throws MalformedMarkup {
        if( !isComposed ) {
            compose();
            isComposed = true; }}



    /** Control flag for late composition.  This flag is cleared by this point’s `commit` method,
      * set by its `ensureComposition` method.
      *
      *     @see #commit()
      *     @see #ensureComposition()
      */
    boolean isComposed;



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    @Override void commit() {
        super.commit();
        isComposed = false; // Pending demand.
        cursor.point( this ); }



   // ━━━  P o i n t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override Markup bullet() { return bullet; }



    public abstract @Override Descriptor descriptor();



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class Descriptor extends Markup_ {


        Descriptor() { super( cursor ); }



        final CoalescentMarkupList components = new CoalescentArrayList( cursor.spooler );



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public final @Override List<Markup> components() throws MalformedMarkup {
            ensureComposition();
            assert components.isFlush();
            return components; }



        public final @Override int column() { // The descriptor is collinear with the bullet.
          return bullet.column() + cursor.bufferColumnarSpan(bullet.text.start(),text.start()); }



        public final @Override int lineNumber() { return Point_.this.lineNumber(); } /* The descriptor
          begins on the first line of the point, if only because it includes that line’s terminator. */



        public final @Override String tagName() { return "Descriptor"; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    abstract class End_ extends BodyFractum_<C>.End_ implements Point.End {


        @Override void commit() {
            super.commit();
            cursor.pointEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
