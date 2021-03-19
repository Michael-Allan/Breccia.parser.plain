package Breccia.parser.plain;

import Breccia.parser.Empty;


final class Empty_ implements Empty {


    Empty_( BrecciaCursor cursor ) { this.cursor = cursor; }



    void commit() { cursor.empty( this ); }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private final BrecciaCursor cursor; }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
