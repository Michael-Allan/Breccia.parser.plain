package Breccia.parser.plain;

import Breccia.parser.Markup;
import java.util.List;


abstract class NonCommandPoint extends Point_<BrecciaCursor> {


    NonCommandPoint( BrecciaCursor cursor ) {
        super( cursor );
        components = new DelimitableMarkupList( perfectIndent, bullet, descriptorWhenPresent ); }



    /** A component list of 2 or 3 elements, depending on whether a descriptor is present.
      */
    final DelimitableMarkupList components;



    Descriptor descriptor;



    final Descriptor descriptorWhenPresent = new Descriptor();



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override List<Markup> components() { return components; }



   // ━━━  P o i n t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override Descriptor descriptor() { return descriptor; }



   // ━━━  P o i n t _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    final @Override void compose() throws MalformedMarkup {
        cursor.composeDescriptor( NonCommandPoint.this ); }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
