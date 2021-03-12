package Breccia.parser.plain;

import Breccia.parser.DataReflector;
import Breccia.parser.Markup;
import java.util.List;


/** Markup that is non-composite, having no parsed components.
  */
public @DataReflector class FlatMarkup extends Markup_ {


    /** @see #tagName()
      */
    public FlatMarkup( String tagName ) { this.tagName = tagName; }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override int column() { throw new UnsupportedOperationException(); }



    /** Returns an empty list: this markup has no parsed components, only {@linkplain #text() flat text}.
      */
    public @Override List<Markup> components() { return List.of(); }



    public @Override int lineNumber() { throw new UnsupportedOperationException(); }



    public final @Override String tagName() { return tagName; }



    public @Override CharSequence text() { throw new UnsupportedOperationException(); }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final String tagName; }


                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
