package Breccia.parser.plain;

import Breccia.parser.DividerSegment;
import Breccia.parser.Markup;
import java.util.List;


final class DividerSegment_ extends Markup_ implements DividerSegment {


    DividerSegment_( final BrecciaCursor cursor ) {
        super( cursor.buffer );
        this.cursor = cursor;
        components = new CoalescentArrayList( cursor.spooler );
        perfectIndent = new PerfectIndent( cursor.buffer, /*container*/this ); }



    final CoalescentMarkupList components;



    /** The offset from the start of the present segment to its first non-space character
      * (a divider drawing character).  Always this is a multiple of four (including zero).
      */
    int indentWidth;



    final PerfectIndent perfectIndent;



   // ━━━  D i v i d e r   S e g m e n t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override Markup perfectIndent() { return perfectIndent; }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override List<Markup> components() {
        assert components.isFlush();
        return components; }



    public final @Override int lineNumber() { return cursor.bufferLineNumber( text.start() ); }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final BrecciaCursor cursor; }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
