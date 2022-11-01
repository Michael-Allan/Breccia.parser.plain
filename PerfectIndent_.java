package Breccia.parser.plain;

import Breccia.parser.AdjunctSlow;
import Breccia.parser.PerfectIndent;


final class PerfectIndent_ extends FlatGranum implements PerfectIndent {


    PerfectIndent_( BrecciaCursor cursor, Granum_ container ) {
        super( cursor );
        this.container = container; }



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override @AdjunctSlow int lineNumber() { return container.lineNumber(); }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final Granum_ container; }



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
