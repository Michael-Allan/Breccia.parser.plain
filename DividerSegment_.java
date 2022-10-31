package Breccia.parser.plain;

import Breccia.parser.AdjunctSlow;
import Breccia.parser.DividerSegment;
import Breccia.parser.Granum;
import java.util.List;


final class DividerSegment_ extends Granum_ implements DividerSegment {


    DividerSegment_( final BrecciaCursor cursor ) {
        super( cursor );
        this.cursor = cursor;
        components = new CoalescentArrayList( cursor.spooler );
        perfectIndent = new PerfectIndent( cursor, /*container*/this ); }



    final CoalescentGranumList components;



    /** The offset from the start of the present segment to its first non-space character
      * (a divider drawing character).  Always this is a multiple of four (including zero).
      */
    int indentWidth;



    final PerfectIndent perfectIndent;



   // ━━━  D i v i d e r   S e g m e n t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override Granum perfectIndent() { return perfectIndent; }



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override List<Granum> components() {
        assert components.isFlush();
        return components; }



    public final @Override @AdjunctSlow int lineNumber() {
        return cursor.bufferLineNumber( text.start() ); }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final BrecciaCursor cursor; }



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
