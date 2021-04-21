package Breccia.parser.plain;

import Breccia.parser.*;
import java.util.*;


final class Division_ extends BodyFractum_<BrecciaCursor> implements Division {


    Division_( BrecciaCursor cursor ) { super( cursor ); }



    final DividerSegmentList components = new DividerSegmentList();



    Division_ endSet() {
        end = new End_();
        return this; }



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    @Override void commit() {
        super.commit();
        cursor.division( this ); }



   // ━━━  M a r k u p  ━━━  D i v i s i o n  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override List<? extends DividerSegment> components() { return components; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class DividerSegmentList extends AbstractList<DividerSegment_> implements RandomAccess {


        DividerSegment_ add() {
            final DividerSegment_ segment;
            if( size < back.size() ) segment = back.get( size );
            else back.add( segment = new DividerSegment_( cursor ));
            ++size;
            assert size == back.size();
            return segment; }



        private final ArrayList<DividerSegment_> back = new ArrayList<>( /*initial capacity*/4 );



        private int size;



       // ━━━  C o l l e c t i o n  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override void clear() { size = 0; }



        public @Override int size() { return size; }



       // ━━━  L i s t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override DividerSegment_ get( final int index ) {
            if( index < size ) return back.get( index );
            throw new IndexOutOfBoundsException( index ); }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class End_ extends BodyFractum_<BrecciaCursor>.End_ implements Division.End {


        @Override void commit() {
            super.commit();
            cursor.divisionEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
