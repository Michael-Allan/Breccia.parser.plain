package Breccia.parser.plain;

import Breccia.parser.Markup;
import java.util.List;


/** Markup that is non-composite, having no parsed components.
  *
  *     @see DeepMarkup
  */
public abstract class FlatMarkup extends Markup_ {


    protected FlatMarkup( Fractum_ f ) { super( f ); }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Returns an empty list: this markup has no parsed components, only {@linkplain #text() flat text}.
      */
    public final @Override List<Markup> components() { return List.of(); }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
