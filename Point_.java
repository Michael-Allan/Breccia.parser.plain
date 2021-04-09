package Breccia.parser.plain;

import Breccia.parser.Markup;
import Breccia.parser.Point;


/** @param <C> The type of cursor.
  */
abstract class Point_<C extends BrecciaCursor> extends BodyFractum_<C> implements Point {


    Point_( C cursor ) { super( cursor ); }



    final FlatMarkup bullet = new FlatMarkup( this ) {

        public @Override int column() { return cursor.fractumIndentWidth; }

        public @Override int lineNumber() { return Point_.this.lineNumber(); }

        public @Override String tagName() { return "Bullet"; }};



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    @Override void commit() {
        super.commit();
        cursor.point( this ); }



   // ━━━  P o i n t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override Markup bullet() { return bullet; }



    public abstract @Override Descriptor descriptor();



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    abstract class Descriptor extends Markup_ {


        protected Descriptor() { super( Point_.this ); }



        final CoalescentMarkupList components = new CoalescentArrayList( cursor.spooler );



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override int column() {
          return bullet.column() + cursor.bufferColumnarSpan(bullet.text.start(),text.start()); }



        public @Override int lineNumber() { return Point_.this.lineNumber(); } /* Always the descriptor
          begins on the first line of the point, if only because it includes that line’s terminator. */



        public @Override String tagName() { return "Descriptor"; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    abstract class End_ extends BodyFractum_<C>.End_ implements Point.End {


        @Override void commit() {
            super.commit();
            cursor.pointEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
