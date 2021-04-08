package Breccia.parser.plain;

import Breccia.parser.Markup;
import java.util.List;


abstract class NonCommandPoint extends Point_<BrecciaCursor> {


    protected NonCommandPoint( BrecciaCursor cursor ) {
        super( cursor );
        components = new DelimitableMarkupList( perfectIndent, bullet, descriptorWhenPresent ); }



    /** A component list of 2 to 3 elements, depending on whether a descriptor is present.
      */
    final DelimitableMarkupList components;



    Markup descriptor;



    final Descriptor descriptorWhenPresent = new Descriptor();



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override List<Markup> components() { return components; }



   // ━━━  P o i n t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override Markup descriptor() { return descriptor; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
