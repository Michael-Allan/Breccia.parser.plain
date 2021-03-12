package Breccia.parser.plain;

import Breccia.parser.BodyFractum;


public abstract class BodyFractum_ extends Fractum_ implements BodyFractum {


    protected BodyFractum_( BrecciaCursor cursor ) { super( cursor ); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    public static abstract class End_ extends Fractum_.End_ implements BodyFractum.End {


        protected End_() {}}



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    /** Sets within the cursor the corresponding end state.
      *
      *     @see BrecciaCursor
      */
    protected abstract void commitEnd(); }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
