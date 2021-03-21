package Breccia.parser.plain;

import Breccia.parser.Markup;
import Breccia.parser.Point;
import java.util.ArrayList;
import java.util.List;


/** @param <C> The type of cursor.
  */
abstract class Point_<C extends BrecciaCursor> extends BodyFractum_<C> implements Point {


    Point_( C cursor ) {
        super( cursor );
        components.add( perfectIndent );
        components.add( bullet );
        components.add( descriptorWhenPresent ); // Absent at most once per file.
        assert components.size() == componentsMax; }



    final Bullet_ bullet = new Bullet_();



    final List<Markup> components = new ArrayList<>( /*initial capacity*/componentsMax );



    static final int componentsMax = 3;



    Descriptor descriptor;



    final Descriptor_ descriptorWhenPresent = new Descriptor_();



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    @Override void commit() {
        super.commit();
        cursor.point( this ); }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override List<Markup> components() { return components; }



   // ━━━  P o i n t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override Bullet bullet() { return bullet; }



    public final @Override Descriptor descriptor() { return descriptor; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class Bullet_ extends FlatMarkup implements Bullet {


        Bullet_() { super( Point_.this ); }



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override int column() { return cursor.fractumIndentWidth; }



        public @Override int lineNumber() { return Point_.this.lineNumber(); }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class Descriptor_ extends FlatMarkup/*TEST*/ implements Descriptor {


        Descriptor_() { super( Point_.this ); }



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override int column() {
          return bullet.column() + cursor.bufferColumnarSpan(bullet.text.start(),text.start()); }



        public @Override int lineNumber() { return Point_.this.lineNumber(); }} /* Always the descriptor
          begins on the first line of the point, if only because it includes that line’s terminator. */



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    abstract class End_ extends BodyFractum_<C>.End_ implements Point.End {


        @Override void commit() {
            super.commit();
            cursor.pointEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
