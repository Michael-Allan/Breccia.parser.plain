package Breccia.parser.plain;

import java.util.ArrayList;
import java.util.function.Supplier;


/** A dispenser of reusable resources.
  *
  *     @param <R> The type of resource dispensed by this spool.
  */
final class Spool<R> {


    /** @see #source
      */
    Spool( Supplier<R> source ) { this.source = source; }



    /** Winds all previously dispensed resources back onto this spool, ready for redispensing and reuse.
      * Do not call this method if a previously dispensed resource remains in use.
      *
      *     @see Spooler#rewind()
      */
    void rewind() { r = 0; }



    /** Winds the last dispensed resource back onto this spool, ready for redispensing and reuse.
      *
      *     @param res The last dispensed resource.
      *     @throws IllegalArgumentException If `res` is not the last dispensed resource.
      */
    void rewind( final R res ) {
        --r;
        if( r < 0  ||  res != resources.get(r) )  throw new IllegalArgumentException(); }



    /** Dispenses a single instance of the resource.
      */
    R unwind() {
        final R res;
        if( r < resources.size() ) res = resources.get( r );
        else resources.add( res = source.get() );
        ++r;
        return res; }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private int r = 0; // Next to unwind.



    private ArrayList<R> resources = new ArrayList<>();



    /** Where to get new instances of the resource.
      */
    private final Supplier<R> source; }



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
