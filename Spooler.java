package Breccia.parser.plain;

import java.util.ArrayList;


/** A dispensary of reusable resources of various types.
  */
abstract class Spooler {


    Spooler() {}



    final void initialize( final ArrayList<Spool<?>> spools ) {
        this.spools = spools.toArray( spoolArrayType ); } /* Bypassing the list interface
          in favour of a bare array, because speed of iteration matters here. */



    /** Rewinds all spools, making all resources ready for redispensing and reuse.
      * Do not call this method if a previously dispensed resource remains in use.
      *
      *     @see Spool#rewind()
      */
    final void rewind() { for( Spool<?> s: spools ) s.rewind(); }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private static final Spool<?>[] spoolArrayType = new Spool<?>[0];



    private Spool<?>[] spools; } // Do not modify after `initialize`.



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
