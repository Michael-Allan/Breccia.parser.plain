package Breccia.parser.plain;

import Breccia.parser.PatternMatcher;
import Breccia.parser.Markup;
import java.util.ArrayList;
import java.util.List;


final class PatternMatcher_ extends Markup_ implements PatternMatcher {


    PatternMatcher_( final BrecciaCursor cursor ) {
        super( cursor.buffer );
        this.cursor = cursor;
        pattern = new Pattern( cursor ); }



    List<Markup> components = new ArrayList<>();



    final Pattern pattern;



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override int column() { return cursor.bufferColumn( text.start() ); }



    public @Override List<Markup> components() { return components; }



    public final @Override int lineNumber() { return cursor.bufferLineNumber( text.start() ); }



   // ━━━  P a t t e r n   M a t c h e r ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override Markup pattern() { return pattern; }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final BrecciaCursor cursor; }



                                                        // Copyright © 2022  Michael Allan.  Licence MIT.
