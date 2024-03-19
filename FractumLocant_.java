package Breccia.parser.plain;

import Breccia.parser.*;
import java.util.ArrayList;
import java.util.List;


final class FractumLocant_ extends Granum_ implements FractumLocant {


    FractumLocant_( final BrecciaCursor cursor ) {
        super( cursor );
        this.cursor = cursor;
        components = new GranalArrayList( cursor.spooler );
        fileLocantWhenPresent = new FileLocant_( cursor ); }



    final GranalArrayList components;



    FileLocant fileLocant;



    final FileLocant_ fileLocantWhenPresent;



    List<PatternMatcher_> patternMatchers;



    final List<PatternMatcher_> patternMatchersWhenPresent = new ArrayList<>();



   // ━━━  F r a c t u m   I n d i c a n t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override FileLocant fileLocant() { return fileLocant; }



    public @Override List<? extends PatternMatcher> patternMatchers() { return patternMatchers; }



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override @AdjunctSlow int column() { return cursor.bufferColumn( text.start() ); }



    public @Override List<Granum> components() {
        assert components.isFlush();
        return components; }



    public final @Override @AdjunctSlow int lineNumber() {
        return cursor.bufferLineNumber( text.start() ); }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final BrecciaCursor cursor; }



                                             // Copyright © 2021-2022, 2024  Michael Allan.  Licence MIT.
