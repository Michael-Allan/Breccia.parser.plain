package Breccia.parser.plain;

import Breccia.parser.FractumIndicant;
import Breccia.parser.Markup;
import Breccia.parser.ResourceIndicant;
import java.util.ArrayList;
import java.util.List;


final class FractumIndicant_ extends Markup_ implements FractumIndicant {


    FractumIndicant_( final BrecciaCursor cursor ) {
        super( cursor.buffer );
        this.cursor = cursor;
        components = new CoalescentArrayList( cursor.spooler );
        resourceIndicantWhenPresent = new ResourceIndicant_( cursor ); }



    final CoalescentArrayList components;



    List<? extends Markup> patterns;



    final List<Pattern> patternsWhenPresent = new ArrayList<>();



    ResourceIndicant resourceIndicant;



    final ResourceIndicant_ resourceIndicantWhenPresent;



   // ━━━  F r a c t u m   I n d i c a n t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override List<? extends Markup> patterns() { return patterns; }



    public @Override ResourceIndicant resourceIndicant() { return resourceIndicant; }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override final int column() { return cursor.bufferColumn( text.start() ); }



    public @Override List<Markup> components() {
        assert components.isFlush();
        return components; }



    public @Override final int lineNumber() { return cursor.bufferLineNumber( text.start() ); }



    public @Override final String tagName() { return "FractumIndicant"; }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final BrecciaCursor cursor; }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
