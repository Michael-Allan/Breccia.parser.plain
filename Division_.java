package Breccia.parser.plain;

import Breccia.parser.*;
import java.util.*;


final class Division_ extends BodyFractum_<BrecciaCursor> implements Division {


    Division_( BrecciaCursor cursor ) { super( cursor ); }



    /** Late composition control flag.  Cleared on committing this division through its `commit` method.
      * Set on late composition of its divider segments, which is triggered by a call to `components`.
      *
      *     @see #commit()
      *     @see #components()
      */
    boolean areSegmentsComposed; /* Justification of late parsing and composition, viz. beyond
      what was parsed and composed at commit time: Use cases exist which care nothing for divisions
      but their reification, and these may benefit from the time saved by leaving unparsed the content
      of the divider segments which contributes nothing to that reification.
          Instead triggering late composition on `components.get` and so forth (even later) would break
      the `List` API, as late composition may throw parse errors which it makes no allowance for. */



    final DividerSegmentList components = new DividerSegmentList();



    Division_ endSet() {
        end = new End_();
        return this; }



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    @Override void commit() {
        super.commit();
        areSegmentsComposed = false; // Pending demand.
        cursor.division( this ); }



   // ━━━  G r a n u m  ━━━  D i v i s i o n  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override List<? extends DividerSegment> components() {
        if( !areSegmentsComposed ) {
            cursor.composeDividerSegments();
            areSegmentsComposed = true; }
        return components; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class DividerSegmentList extends AbstractList<DividerSegment_> implements RandomAccess {


        DividerSegment_ add() {
            final DividerSegment_ segment;
            if( size < back.size() ) segment = back.get( size );
            else {
                assert size == back.size();
                back.add( segment = new DividerSegment_( cursor )); }
            ++size;
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
