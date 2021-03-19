package Breccia.parser.plain;

import Breccia.parser.Halt;


final class Halt_ implements Halt {


    Halt_( BrecciaCursor cursor ) { this.cursor = cursor; }



    void commit() { cursor.halt( this ); }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final BrecciaCursor cursor; }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
