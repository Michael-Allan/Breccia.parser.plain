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



    final Descriptor descriptorWhenPresent = new Descriptor();



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override List<Markup> components() { return components; }



   // ━━━  P o i n t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override Descriptor descriptor() { return descriptor; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class Descriptor extends Point_<BrecciaCursor>.Descriptor {


        /** Late composition control flag.  Cleared on committing this non-command point
          * through the `reifyNonCommandPoint` method of the cursor.  Set on late composition
          * of this descriptor, which is triggered by a call for its `components`.
          *
          *     @see BrecciaCursor#reifyNonCommandPoint()
          *     @see #components()
          */
        boolean isComposed; /* Justification of late parsing and composition of the descriptor: Free in
          form, the unparsed components contribute nothing essential to the point.  Use cases may exist,
          therefore, which demand no further parsing and benefit from the time saved by omitting it. */



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public final @Override List<Markup> components() throws MalformedMarkup {
            if( !isComposed ) {
                cursor.composeDescriptor( NonCommandPoint.this );
                isComposed = true; }
            assert components.isFlush();
            return components; }}}


                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
