package Breccia.parser.plain;

import Breccia.parser.Markup;
import Breccia.parser.Point;
import java.util.ArrayList;
import java.util.List;


public abstract class Point_ extends BodyFractum_ implements Point {


    protected Point_( BrecciaCursor cursor, End_ end ) {
        super( cursor, end );
        components.add( perfectIndent );
        components.add( bullet );
        components.add( descriptorWhenPresent ); // Absent at most once per file.
        assert components.size() == componentsMax; }



    public @Override void commit() {
        super.commit();
        cursor.point( this ); }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override List<Markup> components() { return components; }



   // ━━━  P o i n t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override Bullet bullet() { return bullet; }



    public @Override Descriptor descriptor() { return descriptor; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    public static abstract class End_ extends BodyFractum_.End_ implements Point.End {


        protected End_( BrecciaCursor cursor ) { super( cursor ); }



        public @Override void commit() {
            super.commit();
            cursor.pointEnd( this ); }}



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    protected final Bullet_ bullet = new Bullet_();



    final ArrayList<Markup> components = new ArrayList<>( /*initial capacity*/componentsMax );



    protected static final int componentsMax = 3;



    protected Descriptor descriptor;



    protected final Descriptor_ descriptorWhenPresent = new Descriptor_();



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    protected final class Bullet_ extends FlatMarkup implements Bullet {


        Bullet_() { super( Point_.this ); }



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override int column() { return cursor.fractumIndentWidth; }



        public @Override int lineNumber() { return Point_.this.lineNumber(); }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    protected final class Descriptor_ extends FlatMarkup/*TEST*/ implements Descriptor {


        Descriptor_() { super( Point_.this ); }



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override int column() {
          return bullet.column() + cursor.bufferColumnarSpan(bullet.text.start(),text.start()); }



        public @Override int lineNumber() { return Point_.this.lineNumber(); }}} /* Always the descriptor
          begins on the first line of the point, if only because it comprises that line’s terminator. */



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
