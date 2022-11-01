package Breccia.parser.plain;

import Breccia.parser.AdjunctSlow;
import Breccia.parser.Granum;
import Breccia.parser.PerfectIndent;
import Breccia.parser.Point;
import java.util.List;


/** @param <C> The type of cursor.
  */
abstract class Point_<C extends BrecciaCursor> extends BodyFractum_<C> implements Point {


    Point_( final C cursor ) {
        super( cursor );
        perfectIndent = new PerfectIndent_( cursor, /*container*/this ); }



    final FlatGranum bullet = new FlatGranum( this ) {

        public @Override @AdjunctSlow int column() { return cursor.fractumIndentWidth; }

        public @Override @AdjunctSlow int lineNumber() { return Point_.this.lineNumber(); }

        public @Override String tagName() { return "Bullet"; }};



    /** Late composition.  Do not call this method directly.  It is called by `ensureComposition` alone,
      * which in turn is called by the access methods of any parts of this point that allow for
      * late composition, namely `descriptor.components` (if a descriptor is present)
      * and any others specified by subclasses, q.v.
      *
      *     @see #ensureComposition()
      *     @see Descriptor#components()
      */
    abstract void compose() throws MalformedText;



    /** Assurance of composition.  This method is called by the access methods of any parts of this point
      * that allow for efficient parsing through late composition, viz. subsequent to `commit`.
      * See `compose` for the specific access methods.
      *
      *     @see #commit()
      *     @see #compose()
      */
    final void ensureComposition() throws MalformedText {
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



    final PerfectIndent_ perfectIndent;



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    @Override void commit() {
        super.commit();
        isComposed = false; // Pending demand.
        cursor.point( this ); }



   // ━━━  P o i n t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override Granum bullet() { return bullet; }



    public abstract @Override Descriptor descriptor();



    public final @Override PerfectIndent perfectIndent() { return perfectIndent; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class Descriptor extends Granum_ {


        Descriptor() { super( cursor ); }



        final CoalescentGranalList components = new GranalArrayList( cursor.spooler );



       // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public final @Override List<Granum> components() throws MalformedText {
            ensureComposition();
            assert components.isFlush();
            return components; }



        public final @Override @AdjunctSlow int column() { // The descriptor is collinear with the bullet.
          return bullet.column() + cursor.bufferClusterCount(bullet.text.start(),text.start()); }



        public final @Override @AdjunctSlow int lineNumber() {
            return Point_.this.lineNumber(); } /* The descriptor begins on the first line of the point,
              if only because it includes that line’s terminator. */



        public final @Override String tagName() { return "Descriptor"; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    abstract class End_ extends BodyFractum_<C>.End_ implements Point.End {


        @Override void commit() {
            super.commit();
            cursor.pointEnd( this ); }}}



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
