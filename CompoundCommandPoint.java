package Breccia.parser.plain;

import Breccia.parser.Markup;
import java.util.List;


/** A command point wherein the command may comprise multiple terms.
  */
abstract class CompoundCommandPoint extends CommandPoint_<BrecciaCursor> {


    CompoundCommandPoint( BrecciaCursor cursor ) {
        super( cursor );
        initialize(); }



    final Descriptor descriptor = new Descriptor() {

        public @Override List<Markup> components() { return components; }};



   // ━━━  P o i n t  ━━━  C o m m a n d   P o i n t _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override Descriptor descriptor() { return descriptor; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
