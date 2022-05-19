package Breccia.parser.plain;

import Breccia.parser.CommandPoint;
import Breccia.parser.Markup;
import Java.DelimitableCharSequence;
import java.util.*;

import static Java.CharBuffers.newDelimitableCharSequence;


/** @param <C> The type of cursor.
  */
public abstract class CommandPoint_<C extends BrecciaCursor> extends Point_<C> implements CommandPoint {


    /** Makes a command point with a compound command, one that may comprise more than the keyword.
      */
    CommandPoint_( final C cursor ) { this( cursor, false ); }



    /** @param hasSimpleCommand Whether the command always comprises the keyword alone.
      *   If true, then never add markup to its component list; rather leave it empty.
      */
    CommandPoint_( final C cursor, final boolean hasSimpleCommand ) {
        super( cursor );
        components = List.of( perfectIndent, bullet, descriptor );
        keyword = hasSimpleCommand? command.text : newDelimitableCharSequence(cursor.buffer); }



   // ━━━  C o m m a n d   P o i n t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override AppendageClause appendageClause() throws MalformedMarkup {
        ensureComposition();
        return appendageClause; }



    public final @Override Set<Modifier> modifierSet() { return modifierSet; }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override List<Markup> components() { return components; }



   // ━━━  P o i n t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override Descriptor descriptor() { return descriptor; }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    AppendageClause appendageClause;



    final AppendageClause_ appendageClauseWhenPresent = new AppendageClause_();



    final Command command = new Command();



    private final List<Markup> components;;



    final Descriptor descriptor = new Descriptor();



    /** @see BrecciaCursor#commandPointKeywords
      */
    final DelimitableCharSequence keyword;



    final Set<Modifier> modifierSet = EnumSet.noneOf( Modifier.class );



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    protected @Override void commit() {
        super.commit();
        cursor.commandPoint( this ); }



   // ━━━  P o i n t _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Late composition.  Do not call this method directly.  It is called by `ensureComposition` alone,
      * which in turn is called by the access methods of the parts of this command point that allow
      * for late composition, namely `appendageClause`, `descriptor.components` and any others
      * specified by subclasses, q.v.
      *
      *     @see #ensureComposition()
      *     @see #appendageClause()
      *     @see Descriptor#components()
      */
    abstract @Override void compose() throws MalformedMarkup;



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class Appendage extends Markup_ {


        Appendage() { super( cursor ); }



        final CoalescentMarkupList components = new CoalescentArrayList( cursor.spooler );



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override int column() { return cursor.bufferColumn( text.start() ); }



        public @Override List<Markup> components() {
            assert components.isFlush();
            return components; }



        public @Override int lineNumber() { return cursor.bufferLineNumber( text.start() ); }



        public @Override String tagName() { return "Appendage"; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class AppendageClause_ extends Markup_ implements AppendageClause {


        AppendageClause_() {
            super( cursor );
            components = List.of( delimiter, appendage ); }



        final Appendage appendage = new Appendage();



        private final List<Markup> components;



        final FlatMarkup delimiter = FlatMarkup.make( cursor, "Delimiter");



       // ━━━  A p p e n d a g e   C l a u s e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override Markup appendage() { return appendage; }



        public @Override Markup delimiter() { return delimiter; }



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override int column() { return cursor.bufferColumn( text.start() ); }



        public @Override List<Markup> components() { return components; }



        public @Override int lineNumber() { return cursor.bufferLineNumber( text.start() ); }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class Command extends Markup_ {


        Command() { super( cursor ); }



        final CoalescentMarkupList components = new CoalescentArrayList( cursor.spooler );



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override int column() { // The command is collinear with the bullet.
          return bullet.column() + cursor.bufferColumnarSpan(bullet.text.start(),text.start()); }



        public @Override List<Markup> components() {
            assert components.isFlush();
            return components; }



        public @Override int lineNumber() { return CommandPoint_.this.lineNumber(); }
          // Always the command begins on the first line of the command point.



        public @Override String tagName() { return "Command"; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    protected abstract class End_ extends Point_<C>.End_ implements CommandPoint.End {


        protected @Override void commit() {
            super.commit();
            cursor.commandPointEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
