package Breccia.parser.plain;

import Breccia.parser.Markup;
import java.util.List;


final class Pattern extends Markup_ {


    Pattern( final BrecciaCursor cursor ) {
        super( cursor );
        this.cursor = cursor;
        components = new CoalescentArrayList( cursor.spooler ); }



    final CoalescentMarkupList components;



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override int column() { return cursor.bufferColumn( text.start() ); }



    public @Override List<Markup> components() {
        assert components.isFlush();
        return components; }



    public @Override int lineNumber() { return cursor.bufferLineNumber( text.start() ); }



    public final @Override String tagName() { return "Pattern"; }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final BrecciaCursor cursor; }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
