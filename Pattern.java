package Breccia.parser.plain;

import Breccia.parser.AdjunctSlow;
import Breccia.parser.Granum;
import java.util.List;


final class Pattern extends Granum_ {


    Pattern( final BrecciaCursor cursor ) {
        super( cursor );
        this.cursor = cursor;
        components = new GranalArrayList( cursor.spooler ); }



    final CoalescentGranalList components;



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override @AdjunctSlow int column() { return cursor.bufferColumn( text.start() ); }



    public @Override List<Granum> components() {
        assert components.isFlush();
        return components; }



    public @Override @AdjunctSlow int lineNumber() { return cursor.bufferLineNumber( text.start() ); }



    public final @Override String tagName() { return "Pattern"; }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final BrecciaCursor cursor; }



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
