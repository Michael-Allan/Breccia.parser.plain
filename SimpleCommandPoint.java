package Breccia.parser.plain;

import Breccia.parser.Markup;
import Breccia.parser.TagName;
import java.util.List;


/** A command point wherein the command comprises a single term.
  *
  *     @param <C> The type of cursor.
  */
public abstract class SimpleCommandPoint<C extends BrecciaCursor> extends CommandPoint_<C> {


    /** @see Descriptor#command
      * @see makeCommand
      */
    protected SimpleCommandPoint( final C cursor ) {
        super( cursor );
        initialize( descriptor.command.text ); }



   // ━━━  P o i n t  ━━━  C o m m a n d   P o i n t _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override Descriptor descriptor() { return descriptor; }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    final Descriptor descriptor = new Descriptor();



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    protected @Override void commit() {
        super.commit();
        descriptor.isComposed = false; } // Pending demand.



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class Descriptor extends Point_<C>.Descriptor {


        final FlatMarkup command = FlatMarkup.make( cursor, "Command");



        /** Late composition control flag.  Cleared on committing this command point
          * through its `commit` method.  Set on late composition of this descriptor,
          * which is triggered by a call for its `components`.
          *
          *     @see SimpleCommandPoint#commit()
          *     @see #components()
          */
        boolean isComposed; /* Justification of late composition: the components of the descriptor
          contribute nothing further to the command point that is essential, beyond what is already
          present at commit time.  The only addition is the content of a postgap, if one is present.
          Use cases will exist, therefore, which demand no composition. */



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public final @Override List<Markup> components() throws MalformedMarkup {
            if( !isComposed ) {
                cursor.composeDescriptor( SimpleCommandPoint.this );
                isComposed = true; }
            assert components.isFlush();
            return components; }}}


                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
