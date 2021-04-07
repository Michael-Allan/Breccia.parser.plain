package Breccia.parser.plain;

import Breccia.parser.Markup;
import java.util.ArrayList;
import java.util.List;


abstract class NonCommandPoint extends Point_<BrecciaCursor> {


    protected NonCommandPoint( BrecciaCursor cursor ) {
        super( cursor );
        components.add( perfectIndent );
        components.add( bullet );
        components.add( descriptorWhenPresent ); // Absent at most once per file.
        assert components.size() == componentsMax; }



    final List<Markup> components = new ArrayList<>( /*initial capacity*/componentsMax );



    static final int componentsMax = 3;



    Markup descriptor;



    final Descriptor descriptorWhenPresent = new Descriptor();



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override List<Markup> components() { return components; }



   // ━━━  P o i n t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override Markup descriptor() { return descriptor; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
