package Breccia.parser.plain;

import java.util.ArrayList;
import java.util.function.Supplier;


/** A dispensary of reusable resources of various types.
  */
final class ResourceSpooler {


    ResourceSpooler( final BrecciaCursor cursor ) {
        final ArrayList<Spool<?>> spools = new ArrayList<>();
        spools.add( commentAppender = new Spool<>( () -> new CommentAppender_( cursor )));
        spools.add( flatMarkup = new Spool<>( () -> FlatMarkup.make( cursor )));
        this.spools = spools.toArray( spoolTypeArray ); } /* Bypassing the list interface
          in favour of a bare array, because speed of iteration matters here. */



    /** Spool of comment appenders.
      */
    final Spool<CommentAppender_> commentAppender;



    /** Spool of flat-markup instances, each with a generic tag name of ‘Markup’.
      *
      *     @see Breccia.parser.Markup#tagName()
      */
    final Spool<FlatMarkup> flatMarkup;



    /** Rewinds all spools, making all resources ready for reuse.
      * Do not call this method if a previously dispensed resource remains in use.
      *
      *     @see Spool#rewind()
      */
    void rewind() { for( Spool<?> s: spools ) s.rewind(); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** A dispenser of reusable resources of type `R`.
      */
    final class Spool<R> {


        /** @see #source
          */
        Spool( Supplier<R> source ) { this.source = source; }



        private int r = 0; // Next to unwind.



        private ArrayList<R> resources = new ArrayList<>();



        /** Effectively winds all previously dispensed resources back onto this spool,
          * ready for redispensing and reuse.
          *
          *     @see ResourceSpooler#rewind()
          */
        void rewind() { r = 0; }



        /** Where to get new instances of the resource.
          */
        private final Supplier<R> source;



        /** Dispenses a single instance of the resource.
          */
        R unwind() {
            final R res;
            if( r < resources.size() ) res = resources.get( r );
            else resources.add( res = source.get() );
            ++r;
            return res; }}



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final Spool<?>[] spools;



    private final Spool<?>[] spoolTypeArray = new Spool<?>[0]; }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
