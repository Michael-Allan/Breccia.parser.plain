package Breccia.parser.plain;

import Breccia.parser.Markup;
import java.nio.CharBuffer;
import java.util.List;


/** Composite markup; markup that has parsed components.
  *
  *     @see FlatMarkup
  */
public abstract class DeepMarkup extends Markup_ {


    protected DeepMarkup( CharBuffer b ) { super( b ); }



    protected DeepMarkup( Fractum_ f ) { super( f ); }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override List<Markup> components() { throw new UnsupportedOperationException(); }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
