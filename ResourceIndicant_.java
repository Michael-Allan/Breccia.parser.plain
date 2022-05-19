package Breccia.parser.plain;

import Breccia.parser.ResourceIndicant;
import Breccia.parser.Markup;
import java.util.ArrayList;
import java.util.List;


final class ResourceIndicant_ extends Markup_ implements ResourceIndicant {


    ResourceIndicant_( final BrecciaCursor cursor ) {
        super( cursor );
        this.cursor = cursor;
        components = new CoalescentArrayList( cursor.spooler );
        reference = FlatMarkup.make( cursor, "Reference" ); }



    final CoalescentMarkupList components;



    final FlatMarkup reference;



    final List<String> qualifiers = new ArrayList<>(
      BrecciaCursor.resourceIndicantQualifiers_initialCapacity );



   // ━━━  F r a c t u m   I n d i c a n t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override Markup reference() { return reference; }



    public final @Override List<String> qualifiers() { return qualifiers; }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override int column() { return cursor.bufferColumn( text.start() ); }



    public @Override List<Markup> components() {
        assert components.isFlush();
        return components; }



    public final @Override int lineNumber() { return cursor.bufferLineNumber( text.start() ); }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final BrecciaCursor cursor; }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
