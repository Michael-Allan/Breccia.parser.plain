package Breccia.parser.plain;

import Breccia.parser.Markup;
import java.util.AbstractList;


/** A markup list of redefinable boundaries based on a backing sequence.  The list is immutable as such,
  * providing for modification only through its movable boundaries.
  */
final class DelimitableMarkupList extends AbstractList<Markup> {


    /** Makes a delimitable markup list that initially includes all the given elements.
      *
      *     @param elements The content of the backing sequence.
      */
    DelimitableMarkupList( final Markup... elements ) {
        back = elements;
        end = back.length; }



    /** The exclusive end boundary of this list in the backing sequence.
      */
    int end() { return end; }



    /** Sets the exclusive end boundary.
      *
      *     @throws AssertionError If assertions are enabled and `e` is less than `start`
      *       or greater than the length of the backing sequence.
      */
    void end( int e ) {
        assert start <= e && e <= back.length;
        end = e; }



    /** The length of the backing sequence, the maximum delimitable size of this list.
      */
    int sizeLimit() { return back.length; }



    /** The inclusive start boundary of this list in the backing sequence.
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


    public @Override Markup get( final int index ) {
        if( index >= 0 ) {
            final int i = start + index;
            if( i < end ) return back[i]; }
        throw new IndexOutOfBoundsException( index ); }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    /** The backing sequence of elements, a delimitable section of which determines this list.
      */
    private final Markup[] back;



    private int end;



    private int start; }


                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
