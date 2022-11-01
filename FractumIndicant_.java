package Breccia.parser.plain;

import Breccia.parser.*;
import java.util.ArrayList;
import java.util.List;


final class FractumIndicant_ extends Granum_ implements FractumIndicant {


    FractumIndicant_( final BrecciaCursor cursor ) {
        super( cursor );
        this.cursor = cursor;
        components = new GranalArrayList( cursor.spooler );
        resourceIndicantWhenPresent = new ResourceIndicant_( cursor ); }



    final GranalArrayList components;



    List<PatternMatcher_> patternMatchers;



    final List<PatternMatcher_> patternMatchersWhenPresent = new ArrayList<>();



    ResourceIndicant resourceIndicant;



    final ResourceIndicant_ resourceIndicantWhenPresent;



   // ━━━  F r a c t u m   I n d i c a n t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override List<? extends PatternMatcher> patternMatchers() { return patternMatchers; }



    public @Override ResourceIndicant resourceIndicant() { return resourceIndicant; }



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override @AdjunctSlow int column() { return cursor.bufferColumn( text.start() ); }



    public @Override List<Granum> components() {
        assert components.isFlush();
        return components; }



    public final @Override @AdjunctSlow int lineNumber() {
        return cursor.bufferLineNumber( text.start() ); }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final BrecciaCursor cursor; }



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
