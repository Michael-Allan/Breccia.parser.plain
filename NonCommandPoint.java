package Breccia.parser.plain;

import Breccia.parser.Markup;
import java.util.List;


abstract class NonCommandPoint extends Point_<BrecciaCursor> {


    NonCommandPoint( BrecciaCursor cursor ) {
        super( cursor );
        components = new DelimitableMarkupList( perfectIndent, bullet, descriptorWhenPresent ); }



    /** A component list of 2 to 3 elements, depending on whether a descriptor is present.
      */
    final DelimitableMarkupList components;



    Descriptor descriptor;



    final Descriptor descriptorWhenPresent = new Descriptor() {

        public @Override List<Markup> components() { return components; }};



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override List<Markup> components() { return components; }



   // ━━━  P o i n t  ━━━  C o m m a n d   P o i n t _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override Descriptor descriptor() { return descriptor; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
