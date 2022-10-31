package Breccia.parser.plain;

import Breccia.parser.Granum;
import java.util.List;


abstract class NonCommandPoint extends Point_<BrecciaCursor> {


    NonCommandPoint( BrecciaCursor cursor ) {
        super( cursor );
        components = new DelimitableGranumList( perfectIndent, bullet, descriptorWhenPresent ); }



    /** A component list of 2 or 3 elements, depending on whether a descriptor is present.
      */
    final DelimitableGranumList components;



    Descriptor descriptor;



    final Descriptor descriptorWhenPresent = new Descriptor();



   // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override List<Granum> components() { return components; }



   // ━━━  P o i n t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override Descriptor descriptor() { return descriptor; }



   // ━━━  P o i n t _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    final @Override void compose() throws MalformedText {
        cursor.composeDescriptor( NonCommandPoint.this ); }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
