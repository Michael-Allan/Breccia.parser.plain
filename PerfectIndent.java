package Breccia.parser.plain;

import Breccia.parser.AdjunctSlow;
import Breccia.parser.Granum;
import java.nio.CharBuffer;


final class PerfectIndent extends FlatGranum {


    PerfectIndent( BrecciaCursor cursor, Granum_ container ) {
        super( cursor );
        this.container = container; }



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Returns zero, always the indent leads the line.
      */
    public @Override int column() { return 0; }



    public @Override @AdjunctSlow int lineNumber() { return container.lineNumber(); }



    public @Override String tagName() { return "PerfectIndent"; }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final Granum_ container; }



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
