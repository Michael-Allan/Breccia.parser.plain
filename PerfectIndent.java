package Breccia.parser.plain;

import Breccia.parser.Markup;
import java.nio.CharBuffer;


final class PerfectIndent extends FlatMarkup {


    PerfectIndent( BrecciaCursor cursor, Markup_ container ) {
        super( cursor );
        this.container = container; }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override int column() { return 0; }



    public @Override int lineNumber() { return container.lineNumber(); }



    public @Override String tagName() { return "PerfectIndent"; }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final Markup_ container; }



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
