package Breccia.parser.plain;

import Breccia.parser.Markup;
import Breccia.parser.PlainCommandPoint;
import java.util.List;


final class PlainCommandPoint_ extends CommandPoint_<BrecciaCursor> implements PlainCommandPoint {


    PlainCommandPoint_( BrecciaCursor cursor ) {
        super( cursor );
        initialize(); }



    final Descriptor descriptor = new Descriptor();



    PlainCommandPoint_ endSet() {
        end = new End_();
        return this; }



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    protected @Override void commit() {
        super.commit();
        descriptor.isComposed = false; // Pending demand.
        cursor.plainCommandPoint( this ); }



   // ━━━  P o i n t  ━━━  C o m m a n d   P o i n t _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override Descriptor descriptor() { return descriptor; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class Descriptor extends Point_<BrecciaCursor>.Descriptor {


        /** Late composition control flag.  Cleared on committing this plain command point
          * through its `commit` method.  Set on late composition of this descriptor,
          * which is triggered by a call for its `components`.
          *
          *     @see PlainCommandPoint#commit()
          *     @see #components()
          */
        boolean isComposed; /* Justification of late parsing and composition of the full descriptor,
          viz. beyond what was parsed at commit time: Use cases exist which care nothing for
          plain command points but their reification, and these may benefit from the time saved
          by leaving unparsed those components which contributes nothing to reification. */



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public final @Override List<Markup> components() throws MalformedMarkup {
            if( !isComposed ) {
                cursor.composeDescriptor( PlainCommandPoint_.this );
                isComposed = true; }
            assert components.isFlush();
            return components; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class End_ extends CommandPoint_<BrecciaCursor>.End_ implements PlainCommandPoint.End {


        protected @Override void commit() {
            super.commit();
            cursor.plainCommandPointEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
