package Breccia.parser.plain;

import Breccia.parser.AdjunctSlow;
import Breccia.parser.ResourceIndicant;
import Breccia.parser.Granum;
import java.util.ArrayList;
import java.util.List;


final class ResourceIndicant_ extends Granum_ implements ResourceIndicant {


    ResourceIndicant_( final BrecciaCursor cursor ) {
        super( cursor );
        this.cursor = cursor;
        components = new CoalescentArrayList( cursor.spooler );
        reference = FlatGranum.make( cursor, "Reference" ); }



    final CoalescentGranumList components;



    final FlatGranum reference;



    final List<String> qualifiers = new ArrayList<>(
      BrecciaCursor.resourceIndicantQualifiers_initialCapacity );



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override @AdjunctSlow int column() { return cursor.bufferColumn( text.start() ); }



    public @Override List<Granum> components() {
        assert components.isFlush();
        return components; }



    public final @Override @AdjunctSlow int lineNumber() {
        return cursor.bufferLineNumber( text.start() ); }



   // ━━━  R e s o u r c e   I n d i c a n t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override Granum reference() { return reference; }



    public final @Override List<String> qualifiers() { return qualifiers; }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final BrecciaCursor cursor; }



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
