package org.toxsoft.skf.journals.e4.uiparts;

import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.skf.journals.e4.uiparts.engine.ILibClassInfoesTreeModel;
import org.toxsoft.uskat.core.api.sysdescr.ISkClassInfo;
import org.toxsoft.uskat.core.api.sysdescr.dto.IDtoEventInfo;
import org.toxsoft.uskat.core.connection.ISkConnection;
import org.toxsoft.uskat.core.gui.conn.ISkConnectionSupplier;

/**
 * Модель классов, используемых в фильтре событий, по умолчанию - список всех классов, у которых есть события.
 *
 * @author max
 * @author dima
 */
public class LibDefaultEventsFilterClassListModel
    implements ILibClassInfoesTreeModel<IDtoEventInfo> {

  private ITsGuiContext context;

  private IStridablesList<ISkClassInfo> classes;

  @Override
  public void init( ITsGuiContext aContext ) {
    context = aContext;
    classes = listNeededClasses();
  }

  @Override
  public IStridablesList<ISkClassInfo> getRootClasses() {
    return classes;
  }

  @Override
  public IStridablesList<ISkClassInfo> getChildren( ISkClassInfo aParentClass ) {
    return IStridablesList.EMPTY;
  }

  @Override
  public IStridablesList<IDtoEventInfo> getParamsInfo( ISkClassInfo aClass ) {
    return aClass.events().list();
  }

  protected IStridablesList<ISkClassInfo> listNeededClasses() {
    IStridablesListEdit<ISkClassInfo> eventClasses = new StridablesList<>();

    ISkConnection connection = context.get( ISkConnectionSupplier.class ).defConn();

    for( ISkClassInfo classInfo : connection.coreApi().sysdescr().listClasses() ) {
      if( classInfo.events().list().size() > 0 ) {
        eventClasses.add( classInfo );
      }
    }
    return eventClasses;
  }
}
