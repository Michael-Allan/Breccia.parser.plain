package Breccia.parser.plain;

import Breccia.parser.FileFractum;
import Breccia.parser.Markup;
import java.util.List;


final class FileFractum_ extends Fractum_<BrecciaCursor> implements FileFractum {


    FileFractum_( BrecciaCursor cursor ) {
        super( cursor );
        componentsWhenPresent = List.of( descriptorWhenPresent ); }



    List<Markup> components;



    static final List<Markup> componentsWhenAbsent = List.of();



    final List<Markup> componentsWhenPresent;



    FileDescriptor descriptor;



    final FileDescriptor descriptorWhenPresent = new FileDescriptor();



    FileFractum_ endSet() {
        end = new End_();
        return this; }



    /** Late composition control flag.  Cleared on committing this fractum as the present parse state,
      * which is done through the `readyFileFractum` method of the cursor.  Set on late composition,
      * which is triggered either by a call to `components` or the reifying `asFileFractum` getter.
      *
      *     @see BrecciaCursor#readyFileFractum()
      *     @see #components()
      *     @see BrecciaCursor#asFileFractum()
      */
    boolean isComposed;



   // ━━━  F i l e   F r a c t u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override Markup descriptor() { return descriptor; }



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    @Override void commit() {
        super.commit();
        cursor.fileFractum( this ); }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override List<Markup> components() {
        if( !isComposed ) {
            cursor.composeFileFractum();
            isComposed = true; }
        return components; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class End_ extends Fractum_<BrecciaCursor>.End_ implements FileFractum.End {


        @Override void commit() {
            super.commit();
            cursor.fileFractumEnd( this ); }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class FileDescriptor extends Markup_ {


        FileDescriptor() { super( FileFractum_.this.text ); }



        final CoalescentMarkupList components = new CoalescentArrayList( cursor.spooler );



        /** Late composition control flag.  Cleared on composition of the WHAT?, which is done through
          * the `composeFileFractum` method of the cursor.  Set on late composition of this descriptor,
          * which is triggered by a call for its `components`.
          *
          *     @see BrecciaCursor#composeFileFractum()
          *     @see #components()
          */
        boolean isComposed; /* Justification of late composition: justified only by... WHAT? */



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override int column() { return FileFractum_.this.column(); }



        public final @Override List<Markup> components() {
            if( !isComposed ) {
                cursor.composeFileDescriptor();
                isComposed = true; }
            assert components.isFlush();
            return components; }



        public @Override int lineNumber() { return FileFractum_.this.lineNumber(); }



        public @Override String tagName() { return "FileDescriptor"; }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
