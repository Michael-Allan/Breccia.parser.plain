package Breccia.parser.plain;

import java.util.ArrayList;


/** A spooler of per-source resources.  Use them in one markup source, then rewind them for the next.
  *
  *     @see BrecciaCursor#markupSource(java.io.Reader)
  */
final class SourceSpooler extends Spooler {


    SourceSpooler( final BrecciaCursor c ) {
        final ArrayList<Spool<?>> ss = new ArrayList<>();
        ss.add( hierarch = new Spool<>( () -> c.new Hierarch() ));
        initialize( ss ); }



    /** Spool of hierarchs.
      */
    final Spool<BrecciaCursor.Hierarch> hierarch; }



                                                        // Copyright © 2022  Michael Allan.  Licence MIT.
