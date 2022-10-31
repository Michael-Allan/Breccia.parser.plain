package Breccia.parser.plain;

import Breccia.parser.Granum;
import java.util.AbstractList;
import java.util.RandomAccess;


/** A granum list of redefinable boundaries based on a backing array.
  * It supports the `set` operation directly.  The `add` and `remove` operations
  * it supports only indirectly through redefinition of the terminal boundaries `start` and `end`.
  */
final class DelimitableGranumList extends AbstractList<Granum> implements RandomAccess {


    /** Makes a delimitable granum list that initially includes all the given elements.
      *
      *     @param elements The content of the backing array.
      */
    DelimitableGranumList( final Granum... elements ) {
        back = elements;
        end = back.length; }



    /** The exclusive end boundary of this list in the backing array.
      */
    int end() { return end; }



    /** Sets the exclusive end boundary.
      *
      *     @throws AssertionError If assertions are enabled and `e` is less than `start`
      *       or greater than the length of the backing array.
      */
    void end( int e ) {
        assert start <= e && e <= back.length;
        end = e; }



    /** The length of the backing array, the maximum delimitable size of this list.
      */
    int sizeLimit() { return back.length; }



    /** The inclusive start boundary of this list in the backing array.
      */
    int start() { return start; }



    /** Sets the inclusive start boundary.
      *
      *     @throws AssertionError If assertions are enabled and `s` is less than zero
      *       or greater than `end`.
      */
    void start( int s ) {
        assert 0 <= s && s <= end;
        start = s; }



   // ━━━  C o l l e c t i o n  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override int size() { return end - start; }



   // ━━━  L i s t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override Granum get( final int index ) {
        if( index >= 0 ) {
            final int i = start + index;
            if( i < end ) return back[i]; }
        throw new IndexOutOfBoundsException( index ); }



    public @Override Granum set( final int index, final Granum element ) {
        if( index >= 0 ) {
            final int i = start + index;
            if( i < end ) {
                final Granum elementWas = back[i];
                back[i] = element;
                return elementWas; }}
        throw new IndexOutOfBoundsException( index ); }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    /** The backing array of elements, a delimitable section of which determines this list.
      */
    private final Granum[] back;



    private int end;



    private int start; }


                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
