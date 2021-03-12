package Breccia.parser.plain;

import Breccia.parser.DataReflector;
import Breccia.parser.Markup;
import java.util.List;


/** Markup that is anonymous in the Breccia language definition.
  */
public abstract @DataReflector class GenericMarkup extends Markup_ {


    public GenericMarkup() {}



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override int column() { throw new UnsupportedOperationException(); }



    public @Override List<Markup> components() { throw new UnsupportedOperationException(); }



    public @Override int lineNumber() { throw new UnsupportedOperationException(); }



    /** The default implementation returns ‘GenericMarkup’.
      */
    public @Override String tagName() { return "GenericMarkup"; }



    public @Override CharSequence text() { throw new UnsupportedOperationException(); }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
