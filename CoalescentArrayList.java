package Breccia.parser.plain;

import Breccia.parser.Granum;
import Java.DelimitableCharSequence;
import Java.RangedArrayList;


final class CoalescentArrayList extends RangedArrayList<Granum> implements CoalescentGranumList {


    CoalescentArrayList( FractalSpooler spooler ) { this.spooler = spooler; }



   // ━━━  C o a l e s c e n t   G r a n u m   L i s t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override void appendFlat( final int start, final int end ) {
        assert start < end;
        if( size() == flatSize ) {

          // Coalesce, appending by deferred coalescence
          // ────────
            assert start == flatEnd;
            flatEnd = end; } // Ensures flushing is enabled unless (caller error) `end == flatFlush`.
        else {

          // Compose, appending by the addition of a new component
          // ───────
            if( flatFlush != flatEnd ) flatText.delimit( flatStart, flatEnd );
              // Flushes any pending coalescene of a previous component.
            final FlatGranum flatGranum = spooler.flatGranum.unwind();
            add( flatGranum );
            flatText = flatGranum.text;
            flatStart = flatFlush = start; // Enables flushing unless (caller error) `start == end`.
            flatEnd = end;
            flatSize = size(); }} // Enables coalescence.



    public @Override void flush() {
        if( flatFlush == flatEnd ) return; // Already flush;
        flatText.delimit( flatStart, flatEnd );
        flatFlush = flatEnd; } // Disables a redundant reflush.



    public @Override boolean isFlush() { return flatFlush == flatEnd; }



   // ━━━  C o l l e c t i o n  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override void clear() {
        super.clear();
        flatSize = -1; // Disables coalescence.
        flatFlush = flatEnd = 0; } // Disables flushing.



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private int flatEnd; // To set as `flatText.end` on the next flush.



    private int flatFlush; // The value of `flatEnd` at last `flush`.



    private int flatSize = -1; /* Either the list size after `appendFlat` last added a component,
      or an impossible size whereby coalescence is disabled. */



    private int flatStart; // To set as `flatText.start` on the next flush.



    private DelimitableCharSequence flatText; // The text of the last component added by `appendFlat`.



    private final FractalSpooler spooler; }


                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
