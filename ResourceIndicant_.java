package Breccia.parser.plain;

import Breccia.parser.ResourceIndicant;
import Breccia.parser.Markup;
import java.util.List;


final class ResourceIndicant_ extends Markup_ implements ResourceIndicant {


    ResourceIndicant_( final BrecciaCursor cursor ) {
        super( cursor.buffer );
        this.cursor = cursor;
        components = new CoalescentArrayList( cursor.spooler );
        reference = FlatMarkup.make( cursor ); }



    final CoalescentMarkupList components;



    boolean isFractal;



    final FlatMarkup reference;



   // ━━━  F r a c t u m   I n d i c a n t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override boolean isFractal() { return isFractal; }



    public @Override Markup reference() { return reference; }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override final int column() { return cursor.bufferColumn( text.start() ); }



    public @Override List<Markup> components() {
        assert components.isFlush();
        return components; }



    public @Override final int lineNumber() { return cursor.bufferLineNumber( text.start() ); }



    public @Override final String tagName() { return "ResourceIndicant"; }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final BrecciaCursor cursor; }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
